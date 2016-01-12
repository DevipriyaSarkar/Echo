package echo.echo.echo;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v7.app.NotificationCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Shreya Prabhu on 1/9/2016.
 */

public class MyPhoneStateListener extends PhoneStateListener {

    Context ctx;

    public MyPhoneStateListener (Context ctx) {
        super();
        this.ctx=ctx;
    }
    public void onCallStateChanged(int state, String incomingNumber) {
        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE:
                Log.d("DEBUG", "IDLE");
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                Log.d("DEBUG", "OFFHOOK");
                break;
            case TelephonyManager.CALL_STATE_RINGING:
                Toast.makeText(ctx, "Ringing", Toast.LENGTH_LONG).show();
                Log.d("DEBUG", "RINGING");
                Log.d("ServiceReceiver", "Ringing! incomingNumber :" + incomingNumber);
                DatabaseHandler db = new DatabaseHandler(ctx);
                ArrayList<AlarmItems> allArray = db.getAllAlarmItems();
                for( AlarmItems curItem : allArray) {
                    Log.d("SERVICEREC",curItem.getKey());
                    if(curItem.isCheck()== 1 && curItem.getKey().equals(incomingNumber)) {
                        Log.d("Service Receiver", "insideif");
                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ctx);
                        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
                        mBuilder.setContentTitle(curItem.getRemind());
                        mBuilder.setContentText("Hey, you asked us to remind: " + curItem.getRemind());
                        mBuilder.setAutoCancel(true);
                        NotificationManager mNotificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
                        // notificationID allows you to update the notification later on.
                        mNotificationManager.notify(curItem.getId(), mBuilder.build());
                    }
                }
                break;
        }
    }
}
