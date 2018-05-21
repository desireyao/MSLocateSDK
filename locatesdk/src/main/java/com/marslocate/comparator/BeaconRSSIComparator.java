package com.marslocate.comparator;

import com.marslocate.beacon.BeaconInfo;

import java.util.Comparator;

/**
 * Created by yaoh on 2018/5/20.
 */

public class BeaconRSSIComparator implements Comparator<BeaconInfo> {

    @Override
    public int compare(BeaconInfo device1, BeaconInfo device2) {

        int rssi1 = device1.getAverageRSSI();
        int rssi2 = device2.getAverageRSSI();

        if (rssi1 > rssi2) {
            return -1;
        } else if (rssi1 < rssi2) {
            return 1;
        }
        return 0;
    }
}
