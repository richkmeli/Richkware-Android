package it.richkmeli.richkware.system.device;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

public class PowerInfo {
    private Intent batteryStatus;

    public PowerInfo(Context context) {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        batteryStatus = context.registerReceiver(null, ifilter);
    }

    public String getBatteryLevel() {
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float batteryPct = level * 100 / (float) scale;

        return String.valueOf(Math.round(batteryPct * 100));
    }

    public boolean isCharging() {
        // Are we charging / charged?
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;
        return isCharging;
    }

    public int chargingMode() {
        int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);

        switch (chargePlug) {
            case BatteryManager.BATTERY_PLUGGED_AC:
                return 0;
            case BatteryManager.BATTERY_PLUGGED_USB:
                return 1;
            case BatteryManager.BATTERY_PLUGGED_WIRELESS:
                return 2;
            default:
                return -1;
        }
    }
}
