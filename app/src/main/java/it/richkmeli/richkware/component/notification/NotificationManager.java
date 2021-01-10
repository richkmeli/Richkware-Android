package it.richkmeli.richkware.component.notification;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import it.richkmeli.richkware.R;
import it.richkmeli.richkware.permission.PermissionManager;
import it.richkmeli.richkware.service.location.LocationService;
import it.richkmeli.richkware.util.Logger;

public class NotificationManager {

    public static void notify(Context context, NotificationType notificationType, String text) {
        PackageManager packageManager = context.getPackageManager();
        if (PermissionManager.checkOverlayPermission(context)) {
            Logger.info("NotificationManager, type: " + notificationType.name()+ ", text: " + text);
            switch (notificationType) {
                case TOAST_SHORT: {
                    Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                    break;
                }
                case TOAST_LONG: {
                    Toast.makeText(context, text, Toast.LENGTH_LONG).show();
                    break;
                }
                case DIALOG: {
                    View view = LayoutInflater.from(context).inflate(R.layout.dialog_notification, null);

                    TextView messageTextView = (TextView) view.findViewById(R.id.dg_text);
                    messageTextView.setText(text);

                    ImageView appImage = (ImageView) view.findViewById(R.id.dg_image);
                    try {
                        appImage.setImageDrawable(packageManager.getApplicationIcon(context.getPackageName()));
                    } catch (PackageManager.NameNotFoundException e) {
                        appImage.setImageResource(R.drawable.ic_stat_ad);
                    }

                    LinearLayout colorBar = (LinearLayout) view.findViewById(R.id.dg_colorbar);
                    colorBar.setBackgroundColor(Color.BLACK);

                    final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                    Button dialog_btn1 = (Button) view.findViewById(R.id.dg_btn1);

                    dialog_btn1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });

                    Button dialog_btn2 = (Button) view.findViewById(R.id.dg_btn2);

                    dialog_btn2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });

                    alertDialog.setView(view);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
                    }else {
                        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                    }
                    alertDialog.show();

                    break;

                }
                case ACTIVITY: {

                    Intent intent = new Intent(context, ActivityNotification.class);
                    intent.putExtra("text", text);

                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                    break;
                }
                default:
                    Logger.error("notification type not valid");
                    break;
            }
        }
    }
}
