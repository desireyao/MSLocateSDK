package com.zx.mslocate;

import android.Manifest;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.RemoteException;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.marslocate.log.SDKLogTool;
import com.marslocate.sdk.MSLocateSDKManager;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Arrays;
import java.util.Collection;

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
        mSDK = MSLocateSDKManager.initSDK(this, "");
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

        mSDK.stop();
//        beaconManager.removeAllRangeNotifiers();
//        beaconManager.unbind(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_beancon) {
            startBeacon();
        } else if (id == R.id.btn_test) {
            mSDK.start();
//            beaconManager.bind(this);
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
