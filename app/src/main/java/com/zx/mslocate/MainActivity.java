package com.zx.mslocate;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.beacool.widget.exception.AddViewException;
import com.beacool.widget.locate.widgets.image.LocViewLayout;
import com.beacool.widget.locate.widgets.image.LocViewManager;
import com.beacool.widget.locate.widgets.image.PositionView;
import com.beacool.widget.models.MapPositionData;
import com.beacool.widget.tools.BitmapTool;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.marslocate.listener.MSLocationListener;
import com.marslocate.listener.MSQueryAllNetworkListner;
import com.marslocate.log.SDKLogTool;
import com.marslocate.model.MSLocationMapInfo;
import com.marslocate.model.MSLocationPosition;
import com.marslocate.model.MSNetworkInfo;
import com.marslocate.network.enums.EnumStatus;
import com.marslocate.sdk.MSLocateSDKManager;
import com.marslocate.sdk.enums.EnumLocationStatus;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;
import org.altbeacon.beacon.Region;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private Button btn_test;
    private Button btn_startLocation;

    private MSLocateSDKManager mSDK;

    private LocViewLayout mLayoutLoc;
    private LocViewManager mLocViewManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_test = findViewById(R.id.btn_test);
        btn_test.setOnClickListener(this);

        btn_startLocation = findViewById(R.id.btn_startLocation);
        btn_startLocation.setOnClickListener(this);

//        beaconManager = BeaconManager.getInstanceForApplication(this);
//        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(IBEACON_FORMAT));

//        SDKLogTool.LogE_DEBUG(TAG, " beaconManager.isBound = " + beaconManager.isBound(this));

        mLayoutLoc = findViewById(R.id.layout_LocView);
        mLocViewManager = LocViewManager.getManager(this);
        mLocViewManager.init(mLayoutLoc);

        mSDK = MSLocateSDKManager.initSDK(this, "pre_q145g2o45426k8r479I0J44618A2kE52H4N0OU7Mfn86z55i84rqkByUpVD024u4Nl587t2M218CI0QD");
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mSDK.stopLocation();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_test) {
//            startBeacon();
        } else if (id == R.id.btn_startLocation) {
            mSDK.queryAllNetworkList(new MSQueryAllNetworkListner() {
                @Override
                public void onQueryAllNetworkList(EnumStatus status, List<MSNetworkInfo> networks) {
                    SDKLogTool.LogE_DEBUG(TAG, " onQueryAllNetworkList = " + status.name()
                            + " \n " + networks.toString());

                    if (networks.isEmpty()) {
                        return;
                    }

                    mSDK.startLocation(networks.get(0).getNetworkId(), new MSLocationListener() {
                        @Override
                        public void onMapChanged(MSLocationMapInfo mapInfo) {
                            SDKLogTool.showE(TAG, " onMapChanged --->  mapInfo = " + mapInfo.toString());
                            showMap(mapInfo.getMapUrl());
                        }

                        @Override
                        public void onLocationChanged(MSLocationPosition location) {
                            SDKLogTool.showE(TAG, " onLocationChanged ---> location = " + location.toString());
                            showPosition(location);
                        }

                        @Override
                        public void onLocationStatus(EnumLocationStatus status) {
                            SDKLogTool.showE(TAG, "onLocationStatus ---> " + status.name());
                        }
                    });
                }
            });
        }
    }

    private void showMap(String mapUrl) {
        Glide.with(this).load(mapUrl).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                SDKLogTool.showE(TAG, " width = " + bitmap.getWidth() + " height = " + bitmap.getHeight());
                // 显示地图
//                bitmap = BitmapTool.getBmpFromAsset(MainActivity.this, "icon/map.png");
                mLocViewManager.setMap(bitmap, bitmap.getWidth(), bitmap.getHeight(), 1f, true);
            }
        });
    }

    private void showPosition(MSLocationPosition location) {
        PositionView mUserPosition = new PositionView(MainActivity.this);
        mUserPosition.setId(View.generateViewId());
        mUserPosition.setmTime(SystemClock.uptimeMillis());
        MapPositionData data = new MapPositionData();
        data.setmCoordinateX(location.getPositionX());
        data.setmCoordinateY(location.getPositionY());
        mUserPosition.initView(true, "marsLocate", "",
                BitmapTool.getBmpFromAsset(MainActivity.this, "icon/icon_pos_1.png"),
                BitmapTool.getBmpFromAsset(MainActivity.this, "icon/icon_pos_2.png"),
                data, mUserClickListener);
        try {
            mLocViewManager.deletePosition("marsLocate");
            mLocViewManager.addPosition(mUserPosition);
        } catch (AddViewException e) {
            e.printStackTrace();
        }
    }


    private PositionView.OnUserViewClickListener mUserClickListener = new PositionView.OnUserViewClickListener() {
        @Override
        public void onClick(String name, int viewId) {
            SDKLogTool.showE(TAG, " onClick ------> name = " + name);
        }
    };


    //    /**
//     * 模拟 beacon 发送信号
//     */
//    private void startBeacon() {
//        Beacon beacon = new Beacon.Builder()
//                .setId1("fda50693-b4e2-4fb1-afcf-c6eb07647822")
//                .setId2("627")
//                .setId3("401")
//                .setManufacturer(0x0112)
//                .setTxPower(-60)
//                .setDataFields(Arrays.asList(new Long[]{0l}))
//                .build();
//        BeaconParser beaconParser = new BeaconParser()
//                .setBeaconLayout(IBEACON_FORMAT);
//        BeaconTransmitter beaconTransmitter = new BeaconTransmitter(getApplicationContext(), beaconParser);
//        beaconTransmitter.startAdvertising(beacon);
//    }

//    private void startRanging() {
//        beaconManager.addRangeNotifier(new RangeNotifier() {
//            @Override
//            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
//                Log.e(TAG, "beacons.size = " + beacons.size());
////                if (beacons.size() > 0) {
////                    Log.i(TAG, "The first beacon I see is about " + beacons.iterator().next().getDistance() + " meters away.");
////                }
//                for (Beacon beacon : beacons) {
//                    Log.e(TAG, " beacon = " + beacon.toString()
//                            + " \n rssi = " + beacon.getRssi()
//                            + " \t distance = " + beacon.getDistance()
//                            + " \t macaddress = " + beacon.getBluetoothAddress());
//                }
//            }
//        });
//
//        Identifier id1 = Identifier.parse("2F234454-CF6D-4A0F-ADF2-F4911BA9FFA6");
//        try {
////            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
//            mRegion = new Region("myRangingUniqueId", null, null, null);
//            beaconManager.startRangingBeaconsInRegion(mRegion);
//        } catch (RemoteException e) {
//        }
//    }

}
