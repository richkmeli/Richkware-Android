package it.richkmeli.richkware.permission;

import android.Manifest;
import android.app.AlertDialog;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import java.util.List;

import it.richkmeli.richkware.R;

import static android.provider.Settings.canDrawOverlays;

public class NotificationPermissionManager {
    private static final int PERMISSIONS_REQUEST_ACCESS_BOOT_COMPLETED = 1;
    private static final int PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION_COMPLETED = 2;

    Context context;
    FragmentActivity fragmentActivity;

    public NotificationPermissionManager(Context contextTmp) {
        context = contextTmp;
    }

    public NotificationPermissionManager(Context contextTmp, FragmentActivity fragmentActivityTmp) {
        context = contextTmp;
        fragmentActivity = fragmentActivityTmp;
    }


    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_BOOT_COMPLETED: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                } else {
                    // permission denied, boo! Disable the functionality that depends on this permission.
                    Toast.makeText(context, context.getResources().getString(R.string.permission_toast_boot_not_grated), Toast.LENGTH_LONG).show();
                }
                return;
            }
      /*      case PERMISSIONS_REQUEST_NOTIFICATION_COMPLETED: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                } else {
                    // permission denied, boo! Disable the functionality that depends on this permission.
                    Toast.makeText(context, "Notification Permission Not Granted !", Toast.LENGTH_LONG).show();
                }
                return;
            }*/
        }
    }

    public boolean checkAppUsagePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // access to the statistics of the device usage
            UsageStatsManager usm = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);

            long now = System.currentTimeMillis();
            //  applist contains all app statistics in that (now) moment
            List<UsageStats> appList = null;
            appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, now - 1000 * 1000, now);

            if (appList.isEmpty()) {
                return false;
            }
        }
        return true;
    }


    public void showAppUsageDialogPermission() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_permission, null);

        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setCanceledOnTouchOutside(false);

        TextView title = (TextView) view.findViewById(R.id.textViewPermission);
        title.setText(context.getString(R.string.permission_dialog_app_usage));

        ImageView icon = (ImageView) view.findViewById(R.id.iconPermission);
        icon.setImageResource(R.drawable.usage_permission);

        TextView yesButton = (TextView) view.findViewById(R.id.allow);
        TextView noButton = (TextView) view.findViewById(R.id.deny);

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
                alertDialog.dismiss();
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, context.getResources().getString(R.string.permission_dialog_app_usage), Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });

        alertDialog.setView(view);
        alertDialog.show();
        alertDialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);

    }

    public boolean checkOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (!canDrawOverlays(context)) {
                return false;
            }
        }
        return true;
    }

    public void showOverlayDialogPermission() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_permission, null);

        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setCanceledOnTouchOutside(false);

        TextView title = (TextView) view.findViewById(R.id.textViewPermission);
        title.setText(context.getResources().getString(R.string.permission_dialog_overdraw));

        ImageView icon = (ImageView) view.findViewById(R.id.iconPermission);
        icon.setImageResource(R.drawable.overdraw_permission);

        TextView yesButton = (TextView) view.findViewById(R.id.allow);
        TextView noButton = (TextView) view.findViewById(R.id.deny);

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION));
                alertDialog.dismiss();
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, context.getResources().getString(R.string.permission_dialog_overdraw), Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });

        alertDialog.setView(view);
        alertDialog.show();
        alertDialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);

    }

    public boolean checkLocationPermissions() {
        String permission = Manifest.permission.ACCESS_COARSE_LOCATION;
        int permissionCheck = ContextCompat.checkSelfPermission(context, permission);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            return true;
        }
    }

    public void dialogLocationPermissions() {
        String permission = Manifest.permission.ACCESS_COARSE_LOCATION;
        int permissionCheck = ContextCompat.checkSelfPermission(context, permission);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(fragmentActivity, permission)) {
                Toast.makeText(context, context.getResources().getString(R.string.permission_dialog_location), Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(fragmentActivity, new String[]{permission}, PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION_COMPLETED);
            } else {
                ActivityCompat.requestPermissions(fragmentActivity, new String[]{permission}, PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION_COMPLETED);
            }
        } else {
            // permissions already obtained
        }

    }


    public void checkForBootCompletedPermissions() {
        String permission = Manifest.permission.RECEIVE_BOOT_COMPLETED;
        int permissionCheck = ContextCompat.checkSelfPermission(context, permission);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(fragmentActivity, permission)) {
                Toast.makeText(context, context.getResources().getString(R.string.permission_dialog_boot), Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(fragmentActivity, new String[]{permission}, PERMISSIONS_REQUEST_ACCESS_BOOT_COMPLETED);
            } else {
                ActivityCompat.requestPermissions(fragmentActivity, new String[]{permission}, PERMISSIONS_REQUEST_ACCESS_BOOT_COMPLETED);
            }
        } else {
            // permissions already obtained
        }

    }


    public boolean checkNotificationPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String permission = Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE;
            int permissionCheck = context.checkSelfPermission(permission);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
/*
    public void showNotificationDialogPermission() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_permission, null);

        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setCanceledOnTouchOutside(false);


        TextView title = (TextView) view.findViewById(R.id.textViewPermission);
        title.setText(context.getResources().getString(R.string.permission_dialog_notification));

        ImageView icon = (ImageView) view.findViewById(R.id.iconPermission);
        icon.setImageResource(R.drawable.notification_permission);

        TextView yesButton = (TextView) view.findViewById(R.id.allow);
        TextView noButton = (TextView) view.findViewById(R.id.deny);

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
                alertDialog.dismiss();
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, context.getResources().getString(R.string.permission_dialog_notification), Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });

        alertDialog.setView(view);
        alertDialog.show();
        alertDialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);


    }
*/
}
