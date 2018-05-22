package com.marslocate.sdk;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.nfc.Tag;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.util.SparseArray;

import com.beacool.jni.BeacoolJniHelper;
import com.marslocate.beacon.BeaconInfo;
import com.marslocate.comparator.BeaconRSSIComparator;
import com.marslocate.listener.MSLocationListener;
import com.marslocate.log.SDKLogTool;
import com.marslocate.model.MSLocationMapInfo;
import com.marslocate.model.MSLocationPosition;
import com.marslocate.network.NetworkManager;
import com.marslocate.network.bean.GetMapInfo;
import com.marslocate.network.bean.GetNetworkAllBeacons;
import com.marslocate.network.callback.JsonObjectCallback;
import com.marslocate.network.enums.EnumStatus;
import com.marslocate.sdk.enums.EnumLocationStatus;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static android.content.Context.SENSOR_SERVICE;

/**
 * Created by yaoh on 2018/5/19.
 */

public class MSBeaconManager implements BeaconConsumer, RangeNotifier {

    private static final String TAG = "MSBeaconManager";

    private static final String IBEACON_FORMAT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24";

    private static final int ACC_UPDATE_INTERVAL = 20;

    private Context mContext;
    private BeaconManager beaconManager;
    private Region mRegion;

    private ScheduledExecutorService mExecutor;

    private float[] mAccVal, mMagVal, mRVal, mDirVal;
    private float mDirDegree, mDirDegree360;
    private Sensor mSensorAcc, mSensorMag;
    private SensorManager mSensorManager;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    /**
     * 进入定位算法的beacon
     */
    private ArrayMap<String, BeaconInfo> mListIntoAlgorithm = new ArrayMap<>();

    /**
     * 用来判断进入地图的beacon
     */
    private ArrayMap<String, BeaconInfo> mScannedBeacons = new ArrayMap<>();

    /**
     * 用于缓存选定网络下所有的beacon信息
     */
    private ArrayMap<String, BeaconInfo> mCacheBeacons = new ArrayMap<>();

    private String mNetworkId;  // 当前的网络
    private int mCurMapId = -1; // 当前的地图

    /**
     * 缓存当前网络所有的 beacon 信息
     */
    private boolean isProcessingLocationMapTask;

    /**
     * 回调地图信息 和 定位点的信息
     */
    private MSLocationListener mLocationListener;

    public MSBeaconManager(Context context) {
        mContext = context.getApplicationContext();

        /**
         *  加载 so
         */
        System.loadLibrary("PosAlgoJni");

        beaconManager = BeaconManager.getInstanceForApplication(mContext);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(IBEACON_FORMAT));

        mSensorManager = (SensorManager) mContext.getSystemService(SENSOR_SERVICE);
        mSensorAcc = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorMag = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        mAccVal = new float[3];
        mMagVal = new float[3];
        mRVal = new float[9];
        mDirVal = new float[3];
        mDirDegree = 0f;
        mDirDegree360 = 0f;
    }


    public void startLocation(String networkId, MSLocationListener locationListener) {
        SDKLogTool.LogE_DEBUG(TAG, " isBound ---> " + beaconManager.isBound(this));
        if (!beaconManager.isBound(this)) {
            mNetworkId = networkId;
            mLocationListener = locationListener;

            // 初始化
            BeacoolJniHelper.CYWBBPositionInit(0, 1, 0.f, 0.f, 1.f);

            // 获取这个网络下所有的 ibeacon 信息
            NetworkManager.get().getNetworkAllBeacons(mNetworkId, new JsonObjectCallback<GetNetworkAllBeacons>() {
                @Override
                public void onResponse(GetNetworkAllBeacons response) {
                    if (response.getResult() == 0 && response.getData().getDeviceList() != null) {
                        List<GetNetworkAllBeacons.DataBean.DeviceListBean> deviceListBeans
                                = response.getData().getDeviceList();
                        mCacheBeacons.clear();
                        if (!deviceListBeans.isEmpty()) {
                            for (GetNetworkAllBeacons.DataBean.DeviceListBean deviceBeacon : deviceListBeans) {
                                String uuid = deviceBeacon.getIbeaconInfo().getUuid().toLowerCase();
                                int major = deviceBeacon.getIbeaconInfo().getMajor();
                                int minor = deviceBeacon.getIbeaconInfo().getMinor();

                                BeaconInfo beaconInfo = new BeaconInfo();
                                beaconInfo.setMapId(deviceBeacon.getMapId());
                                beaconInfo.setUuid(uuid);
                                beaconInfo.setMajorID(major);
                                beaconInfo.setMinorID(minor);
                                beaconInfo.setCoordinateX(deviceBeacon.getCoordinateX());
                                beaconInfo.setCoordinateY(deviceBeacon.getCoordinateY());
                                beaconInfo.setMacAddress(deviceBeacon.getDeviceMacId());

                                mCacheBeacons.put(checkKey(uuid, major, minor, beaconInfo.getMacAddress()), beaconInfo);
                            }

                            SDKLogTool.LogE_DEBUG(TAG, " mCacheBeacons ---> \n" + mCacheBeacons.toString());

                            // 开始定位
                            beaconManager.bind(MSBeaconManager.this);

                            // 开启传感器的监听
                            startRegistSensor();
                            if (mExecutor != null) {
                                mExecutor.shutdown();
                            }
                            mExecutor = new ScheduledThreadPoolExecutor(1);
                            mExecutor.schedule(new SersorChangedTask(), 100, TimeUnit.MILLISECONDS);
                        } else {
                            mLocationListener.onLocationStatus(EnumLocationStatus.STATUS_EMPTY_BEACON);
                        }
                    } else {
                        mLocationListener.onLocationStatus(EnumLocationStatus.STATUS_DATA_ERROR);
                    }
                }

                @Override
                public void onError(EnumStatus errorMsg) {
                    SDKLogTool.showE(TAG, errorMsg.toString());
                    mLocationListener.onLocationStatus(EnumLocationStatus.STATUS_NETWORK_ERROR);
                }
            });
        }
    }

    public void unBindConsumer() {
        unRegistSensor();
        try {
            beaconManager.stopRangingBeaconsInRegion(mRegion);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        beaconManager.removeAllRangeNotifiers();
        beaconManager.unbind(this);

        // 停止所有任务
        if (mExecutor != null) {
            mExecutor.shutdown();
        }
    }

    @Override
    public void onBeaconServiceConnect() {
        SDKLogTool.LogE_DEBUG(TAG, "onBeaconServiceConnect--->");
        startRangingBeacons();
    }

    @Override
    public Context getApplicationContext() {
        SDKLogTool.LogE_DEBUG(TAG, "getApplicationContext--->");
        return mContext;
    }

    @Override
    public void unbindService(ServiceConnection serviceConnection) {
        SDKLogTool.LogE_DEBUG(TAG, "unbindService--->");
        mContext.unbindService(serviceConnection);
    }

    @Override
    public boolean bindService(Intent intent, ServiceConnection serviceConnection, int mode) {
        SDKLogTool.LogE_DEBUG(TAG, "bindService--->");
        return mContext.bindService(intent, serviceConnection, mode);
    }

    @Override
    public void didRangeBeaconsInRegion(final Collection<Beacon> beacons, Region region) {
        SDKLogTool.LogE_DEBUG(TAG, "didRangeBeaconsInRegion------------> beacons.size = " + beacons.size());

        for (Beacon beacon : beacons) {
            SDKLogTool.LogD(TAG, "ranging------> beacon = " + beacon.toString()
                    + " \n rssi = " + beacon.getRssi()
                    + " \t distance = " + beacon.getDistance()
                    + " \t macaddress = " + beacon.getBluetoothAddress());
        }

        // 说明获取到正确的地图信息 每次计算一次坐标
        if (mCurMapId != -1) {
            mExecutor.execute(new LocationChangedTask(beacons));
        }

        // 累计几次计算地图信息
        if (mCountRanging >= 5) {
            if (isProcessingLocationMapTask) {
                return;
            }
            mExecutor.execute(new LocationMapChangedTask());
        } else {
            mExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    processBeaconWithRSSI(beacons);
                }
            });
            mCountRanging++;
        }
    }


    private int mCountRanging = 0;

    /**
     * 开始搜索 beacon
     */
    private void startRangingBeacons() {
        beaconManager.addRangeNotifier(this);
        try {
//            Identifier id1 = Identifier.parse("2F234454-CF6D-4A0F-ADF2-F4911BA9FFA6");
//            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
            mRegion = new Region("marslocate", null, null, null);
            beaconManager.startRangingBeaconsInRegion(mRegion);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 累计 beacon 取rssi最强的3个 用于判断地图是否改变
     */
    private void processBeaconWithRSSI(Collection<Beacon> beacons) {
        for (Beacon beacon : beacons) {

            if (!mCacheBeacons.containsKey(checkKey(beacon))) {
                SDKLogTool.LogD(TAG, "processBeaconWithRSSI---> 过滤 : " + checkKey(beacon));
                continue;
            }

            if (!mScannedBeacons.containsKey(checkKey(beacon))) {
                BeaconInfo beaconInfo = new BeaconInfo();
                beaconInfo.setMajorID(beacon.getId2().toInt());
                beaconInfo.setMinorID(beacon.getId3().toInt());
                beaconInfo.setRssi(beacon.getRssi());
                beaconInfo.setAverageRSSI(beacon.getRssi());
                beaconInfo.setUuid(beacon.getId1().toString());
                beaconInfo.setMacAddress(beacon.getBluetoothAddress());
                BeaconInfo cacheBeacon = mCacheBeacons.get(checkKey(beacon));
                beaconInfo.setCoordinateX(cacheBeacon.getCoordinateX());
                beaconInfo.setCoordinateY(cacheBeacon.getCoordinateY());
                beaconInfo.setMapId(cacheBeacon.getMapId());

                mScannedBeacons.put(checkKey(beacon), beaconInfo);
            } else {
                BeaconInfo scanBeacon = mScannedBeacons.get(checkKey(beacon));
                scanBeacon.setRssi(beacon.getRssi());
                scanBeacon.processAverageRSSI(beacon.getRssi());
            }
        }
    }

    private SensorEventListener mSensorListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {
//            SDKLogTool.LogE_DEBUG(TAG, " onSensorChanged ---> " + event.toString());
            switch (event.sensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER: {
                    mAccVal = event.values;
                    break;
                }
                case Sensor.TYPE_MAGNETIC_FIELD: {
                    mMagVal = event.values;
                    break;
                }
                default:
                    break;
            }
            calculateOrientation();
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    /**
     * 计算手机的角度
     */
    private void calculateOrientation() {
        SensorManager.getRotationMatrix(mRVal, null, mAccVal, mMagVal);
        SensorManager.getOrientation(mRVal, mDirVal);
        mDirDegree = (float) Math.toDegrees(mDirVal[0]);

        if (mDirDegree >= -180 && mDirDegree < 0) {
            mDirDegree360 = 360f + mDirDegree;
        } else if (mDirDegree >= 0 && mDirDegree <= 180) {
            mDirDegree360 = mDirDegree;
        }

//        SDKLogTool.LogE_DEBUG(TAG, " mDirDegree360 ---> " + mDirDegree360);
    }

    /**
     * 开始监听手机的传感器
     */
    private void startRegistSensor() {
        mSensorManager.registerListener(mSensorListener, mSensorAcc, SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(mSensorListener, mSensorMag, SensorManager.SENSOR_DELAY_FASTEST);
    }

    private void unRegistSensor() {
        mSensorManager.unregisterListener(mSensorListener, mSensorAcc);
        mSensorManager.unregisterListener(mSensorListener, mSensorMag);

    }

    class SersorChangedTask implements Runnable {

        @Override
        public void run() {
//            SDKLogTool.LogE_DEBUG(TAG, " CYWAccUpdatePoll -------> " + mDirDegree360);
            BeacoolJniHelper.CYWAccUpdatePoll(mAccVal[0] / 10.f, mAccVal[1] / 10.f,
                    mAccVal[2] / 10.f);

            mExecutor.schedule(new SersorChangedTask(), ACC_UPDATE_INTERVAL, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * 监听位置改变的 task
     */
    class LocationChangedTask implements Runnable {

        private Collection<Beacon> mBeacons;

        public LocationChangedTask(Collection<Beacon> beacons) {
            mBeacons = beacons;
        }

        @Override
        public void run() {
            for (Beacon beacon : mBeacons) {
                if (mCacheBeacons.containsKey(checkKey(beacon))) {
                    BeaconInfo beaconInfo = mCacheBeacons.get(checkKey(beacon));
                    beaconInfo.setRssi(beacon.getRssi());
                    beaconInfo.setDistance(beacon.getDistance());
                    mListIntoAlgorithm.put(checkKey(beacon), beaconInfo);
                }
            }

            SDKLogTool.LogE_DEBUG(TAG, "CalcutePositionTask ---> "
                    + " \t thread.name = " + Thread.currentThread().getName()
                    + " \n mListIntoAlgorithm ------> " + mListIntoAlgorithm.toString());

            if (!mListIntoAlgorithm.isEmpty()) {
                calculatePosition();
            }

        }
    }

    /**
     * 监听地图改变的 task
     */
    class LocationMapChangedTask implements Runnable {
        @Override
        public void run() {
            isProcessingLocationMapTask = true;
            List<BeaconInfo> beaconInfoList = new ArrayList<>(mScannedBeacons.values());

            if (beaconInfoList.size() < 3) {
                mCountRanging = 0;
                isProcessingLocationMapTask = false;
                SDKLogTool.LogE_DEBUG(TAG, "LocationMapChanged ---> "
                        + "\t beaconInfoList.size() ---> " + beaconInfoList.size());
                return;
            }

            // 按照 rssi 从大到小排序
            Collections.sort(beaconInfoList, new BeaconRSSIComparator());
//          SDKLogTool.LogE_DEBUG(TAG, " after sort beaconInfoList ---> " + beaconInfoList.toString());
            SDKLogTool.LogE_DEBUG(TAG, "LocationMapChanged ---> "
                    + "\t thread.name ---> " + Thread.currentThread().getName()
                    + "\t beaconInfoList.size() ---> " + beaconInfoList.size()
                    + "\n after sort beaconInfoList ---> \n" + beaconInfoList.toString());

            // 取前3个beacon 计算地图信息
            beaconInfoList = beaconInfoList.subList(0, 3);
            BeaconInfo info0 = beaconInfoList.get(0);
            BeaconInfo info1 = beaconInfoList.get(1);
            BeaconInfo info2 = beaconInfoList.get(2);

            int mMapId = 0;
            if (info0.getMapId() == info1.getMapId() || info1.getMapId() == info2.getMapId()) {
                mMapId = info1.getMapId();
            } else if (info0.getMapId() == info2.getMapId()) {
                mMapId = info0.getMapId();
            }

            if (info0.getMapId() == info1.getMapId() || info1.getMapId() == info2.getMapId()) {
                // 说明搜到的信号最强的三个 beacon 都在同一张地图上
                SDKLogTool.LogE_DEBUG(TAG, "-----> mCurMapId = " + mCurMapId + " mMapId = " + mMapId);
                if (mCurMapId != mMapId) {
                    final int mapId = mMapId;
                    NetworkManager.get().getMapInfo(mNetworkId, mapId, new JsonObjectCallback<GetMapInfo>() {
                        @Override
                        public void onResponse(GetMapInfo response) {
                            // 回调地图信息
                            if (response.getResult() == 0) {
                                mLocationListener.onMapChanged(MSLocationMapInfo.parseData(response));
                                // 说明切换到新的地图
                                mCurMapId = mapId;

                                mScannedBeacons.clear();
                                mCountRanging = 0;
                                isProcessingLocationMapTask = false;
                            } else {
                                mLocationListener.onLocationStatus(EnumLocationStatus.STATUS_DATA_ERROR);
                            }
                        }

                        @Override
                        public void onError(EnumStatus errorMsg) {
                            mScannedBeacons.clear();
                            mCountRanging = 0;
                            isProcessingLocationMapTask = false;
                        }
                    });
                }
            }
        }
    }


    /**
     * 计算坐标点的位置
     */
    private void calculatePosition() {
        List<BeaconInfo> mListBeacon = new ArrayList<>(mListIntoAlgorithm.values());
        if (mListBeacon.size() < 3) {
            return;
        }

        Collections.sort(mListBeacon, new BeaconRSSIComparator());
        int len = mListBeacon.size() > 10 ? 10 : mListBeacon.size();

        short[] major = new short[len];
        short[] minor = new short[len];
        float[] rssi = new float[len];
        float[] distance = new float[len];
        float[] beaconpos_x = new float[len];
        float[] beaconpos_y = new float[len];
        int[] floor = new int[len];

        for (int i = 0; i < len; i++) {
            BeaconInfo item = mListBeacon.get(i);
            major[i] = (short) item.getMajorID();
            minor[i] = (short) item.getMinorID();
            rssi[i] = item.getRssi();
            distance[i] = (float) item.getDistance();
            beaconpos_x[i] = (float) item.getCoordinateX();
            beaconpos_y[i] = (float) item.getCoordinateY();
            floor[i] = 1; // 这里楼层先固定 1

            SDKLogTool.LogD(TAG, "item[" + i + "]"
                    + " major  = " + major[i]
                    + " minor = " + minor[i]
                    + " beaconpos_x = " + beaconpos_x[i]
                    + " beaconpos_y = " + beaconpos_y[i]
                    + " rssi = " + rssi[i]
                    + " distance = " + distance[i]
                    + " floor = " + floor[i]);
        }
        // 清空进入算法的 beacon
//        mListIntoAlgorithm.clear();

        int res = BeacoolJniHelper.CYWBeaconPositionPoll(0,
                len, major, minor, rssi, distance, beaconpos_x, beaconpos_y, floor, mDirDegree360);

        SDKLogTool.LogE_DEBUG(TAG, " CYWBeaconPositionPoll--->  res = " + res);

        if (res == 0) {
//            int curFloor = BeacoolJniHelper.CYWGetFloorOut(0);
            final float curX = BeacoolJniHelper.CYWGetPosXOut(0);
            final float curY = BeacoolJniHelper.CYWGetPosYOut(0);
            SDKLogTool.LogE_DEBUG(TAG, "doGetPosJni---> " + " curX = " + curX + " curY = " + curY);

            /**
             * 回调位置信息
             */
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mLocationListener.onLocationChanged(new MSLocationPosition(curX, curY));
                }
            });
        }
    }

    /**
     * 根据 uuid major minor macaddress 拼接 唯一的key
     *
     * @param uuid
     * @param major
     * @param minor
     * @param macaddress
     * @return
     */
    private String checkKey(String uuid, int major, int minor, String macaddress) {
        macaddress = macaddress.replace(":", "");
        StringBuffer buffer = new StringBuffer();
        buffer.append(uuid)
                .append("-")
                .append(String.valueOf(major))
                .append("-")
                .append(String.valueOf(minor));
//                .append("-")
//                .append(macaddress);
//        SDKLogTool.LogE_DEBUG(TAG, "checkKey --->" + buffer.toString());
        return buffer.toString();
    }

    private String checkKey(Beacon beacon) {
        String key = checkKey(beacon.getId1().toString(),
                beacon.getId2().toInt(),
                beacon.getId3().toInt(),
                beacon.getBluetoothAddress());
//        SDKLogTool.LogE_DEBUG(TAG, "checkKey --->" + buffer.toString());
        return key;
    }

}
