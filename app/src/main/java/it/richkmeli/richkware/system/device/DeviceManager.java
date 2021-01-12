package it.richkmeli.richkware.system.device;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import it.richkmeli.richkware.receiver.DeviceAdmin;

public class DeviceManager {
    private DevicePolicyManager devicePolicyManager;
    //private DeviceAdminInfo deviceAdminInfo;
    private ComponentName policyAdmin;
    private Context context;
    private static final int REQ_ACTIVATE_DEVICE_ADMIN = 10;


    public DeviceManager(Context context) {
        this.devicePolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        this.policyAdmin = new ComponentName(context, DeviceAdmin.class);
        this.context = context;
    }

    private boolean isActiveAdmin() {
        return devicePolicyManager.isAdminActive(policyAdmin);
    }

    public void requestDeviceAdminPermission(Activity activity) {
        if (!isActiveAdmin()) {

            Intent activateDeviceAdminIntent =
                    new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);

            activateDeviceAdminIntent.putExtra(
                    DevicePolicyManager.EXTRA_DEVICE_ADMIN, policyAdmin);

            activateDeviceAdminIntent.putExtra(
                    DevicePolicyManager.EXTRA_ADD_EXPLANATION, "RICHKWARE");

            activity.startActivityForResult(activateDeviceAdminIntent, REQ_ACTIVATE_DEVICE_ADMIN);
        }
    }

    public void setDevicePassword() {
        devicePolicyManager.setPasswordQuality(policyAdmin, DevicePolicyManager.PASSWORD_QUALITY_ALPHANUMERIC);
        devicePolicyManager.setPasswordMinimumLength(policyAdmin, 8);

        Intent intent = new Intent(DevicePolicyManager.ACTION_SET_NEW_PASSWORD);
        context.startActivity(intent);
    }

    public void setDeviceLockTimeout(long timeMs) {
        devicePolicyManager.setMaximumTimeToLock(policyAdmin, timeMs);
    }

    public void lockDeviceNow() {
        devicePolicyManager.lockNow();
    }

    public void wipeData() {
        devicePolicyManager.wipeData(0);
    }

    public void enableCamera() {
        devicePolicyManager.setCameraDisabled(policyAdmin, true);
    }

    public void disableCamera() {
        devicePolicyManager.setCameraDisabled(policyAdmin, true);
    }

}
