package bravous.tend.com.exportpdf;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class MyReceiver2 extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        int position = bundle.getInt("position");

        Intent mainActivityIntent = new Intent(context,MainActivity.class);
        mainActivityIntent.putExtra("position", position);
        mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent2 = PendingIntent.getActivity(context,
                1011,
                mainActivityIntent,
                PendingIntent.FLAG_ONE_SHOT);

        try {
            pendingIntent2.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }

    }


}
