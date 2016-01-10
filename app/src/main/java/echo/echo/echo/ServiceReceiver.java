package echo.echo.echo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

/**
 Broadcast receiver
 */
public class ServiceReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {

        MyPhoneStateListener phoneListener=new MyPhoneStateListener(context);
        TelephonyManager telephony = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        telephony.listen(phoneListener,PhoneStateListener.LISTEN_CALL_STATE);
      /*TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        telephony.listen(new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                super.onCallStateChanged(state, incomingNumber);

                Log.d("ServiceReceiver", "incomingNumber :" + incomingNumber);

            }
        }, PhoneStateListener.LISTEN_CALL_STATE);*/
    }
}
