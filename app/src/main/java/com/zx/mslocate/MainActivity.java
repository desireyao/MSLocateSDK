package com.zx.mslocate;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;
import org.altbeacon.beacon.Region;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, BeaconConsumer {

    private static final String TAG = "MainActivity";

    public static final String IBEACON_FORMAT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24";
//    private BeaconManager beaconManager;

    private Button btn_test;

    private Button btn_beancon;

    private MSLocateSDKManager mSDK;
    private Region mRegion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // To detect proprietary beacons, you must add a line like below corresponding to your beacon
        // type.  Do a web search for "setBeaconLayout" to get the proper expression.
        btn_test = findViewById(R.id.btn_test);
        btn_test.setOnClickListener(this);

        btn_beancon = findViewById(R.id.btn_beancon);
        btn_beancon.setOnClickListener(this);

//        beaconManager = BeaconManager.getInstanceForApplication(this);
//        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(IBEACON_FORMAT));

//        SDKLogTool.LogE_DEBUG(TAG, " beaconManager.isBound = " + beaconManager.isBound(this));
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
        if (id == R.id.btn_beancon) {
            startBeacon();
        } else if (id == R.id.btn_test) {

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
                        }

                        @Override
                        public void onLocationChanged(MSLocationPosition location) {
                            SDKLogTool.showE(TAG, " onLocationChanged ---> location = " + location.toString());
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

    @Override
    public void onBeaconServiceConnect() {
//        startRanging();

    }

    //    /**
//     * 模拟 beacon 发送信号
//     */
    private void startBeacon() {
        Beacon beacon = new Beacon.Builder()
                .setId1("fda50693-b4e2-4fb1-afcf-c6eb07647822")
                .setId2("627")
                .setId3("401")
                .setManufacturer(0x0112)
                .setTxPower(-60)
                .setDataFields(Arrays.asList(new Long[]{0l}))
                .build();
        BeaconParser beaconParser = new BeaconParser()
                .setBeaconLayout(IBEACON_FORMAT);
        BeaconTransmitter beaconTransmitter = new BeaconTransmitter(getApplicationContext(), beaconParser);
        beaconTransmitter.startAdvertising(beacon);
    }

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
