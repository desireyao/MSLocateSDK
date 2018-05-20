package com.marslocate.sdk;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.RemoteException;
import android.util.Log;

import com.marslocate.log.SDKLogTool;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;

/**
 * Created by yaoh on 2018/5/19.
 */

public class MSBeaconManager implements BeaconConsumer {

    private static final String TAG = "MSBeaconManager";

    public static final String IBEACON_FORMAT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24";

    private Context mContext;
    private BeaconManager beaconManager;

    public MSBeaconManager(Context context) {
        mContext = context.getApplicationContext();
        beaconManager = BeaconManager.getInstanceForApplication(mContext);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(IBEACON_FORMAT));
    }

    public void startBeaconService() {
        beaconManager.bind(this);

    }

    public void stop() {

        beaconManager.removeAllRangeNotifiers();
        beaconManager.unbind(this);
    }

    @Override
    public void onBeaconServiceConnect() {
        SDKLogTool.LogE_DEBUG(TAG, "onBeaconServiceConnect--->");
        startRanging();
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

    private void startRanging() {
        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                Log.e(TAG, "beacons.size = " + beacons.size());
//                if (beacons.size() > 0) {
//                    Log.i(TAG, "The first beacon I see is about " + beacons.iterator().next().getDistance() + " meters away.");
//                }
                for (Beacon beacon : beacons) {
                    Log.e(TAG, "ranging---> beacon = " + beacon.toString()
                            + " \n rssi = " + beacon.getRssi()
                            + " \t distance = " + beacon.getDistance()
                            + " \t macaddress = " + beacon.getBluetoothAddress());
                }
            }
        });

//        Identifier id1 = Identifier.parse("2F234454-CF6D-4A0F-ADF2-F4911BA9FFA6");
        try {
//            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
            Region mRegion = new Region("myRangingUniqueId", null, null, null);
            beaconManager.startRangingBeaconsInRegion(mRegion);
        } catch (RemoteException e) {
        }
    }

}
