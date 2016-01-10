package echo.echo.echo;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Shreya Prabhu on 1/9/2016.
 */
public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi

                String name = getWifiName(context);
                Toast.makeText(context, activeNetwork.getTypeName() + name, Toast.LENGTH_SHORT).show();

                DatabaseHandler db = new DatabaseHandler(context);
                ArrayList<AlarmItems> allArray = db.getAllAlarmItems();
                for (AlarmItems curItem : allArray) {
                    if (curItem.isCheck() == 1 && curItem.getEvent().equals("WiFi Connected")) {
                        Log.d("Service Receiver", "wifi");
                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
                        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
                        mBuilder.setContentTitle(curItem.getRemind());
                        mBuilder.setContentText("Hey, you asked us to remind: " + curItem.getRemind());
                        mBuilder.setAutoCancel(true);
                        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        // notificationID allows you to update the notification later on.
                        mNotificationManager.notify(curItem.getId(), mBuilder.build());
                    }
                }
            }

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                Toast.makeText(context, activeNetwork.getTypeName(), Toast.LENGTH_SHORT).show();

            }
        }
    }

    //To get wifi name
    public String getWifiName(Context context)
    {

        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (manager.isWifiEnabled()) {
            WifiInfo wifiInfo = manager.getConnectionInfo();
            if (wifiInfo != null) {
                NetworkInfo.DetailedState state = WifiInfo.getDetailedStateOf(wifiInfo.getSupplicantState());
                if (state == NetworkInfo.DetailedState.CONNECTED || state == NetworkInfo.DetailedState.OBTAINING_IPADDR) {
                    return wifiInfo.getSSID();
                }
            }
        }return null;
    }
}
