package com.marslocate.util;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;

/**
 * Created by yaoh on 2018/5/22.
 */

public class SDKUtil {

    /**
     * 判断蓝牙是否开启
     *
     * @return
     */
    @SuppressLint("MissingPermission")
    public static boolean isBLESwitchOn() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return bluetoothAdapter.isEnabled();
    }


}
