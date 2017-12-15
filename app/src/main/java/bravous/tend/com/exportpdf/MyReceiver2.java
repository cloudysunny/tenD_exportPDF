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

//        Intent activityIntent = new Intent(context, writeNewDiary.class);
//        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(activityIntent);
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(context,
//                1000,
//                activityIntent,
//                PendingIntent.FLAG_ONE_SHOT);
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
//        builder.setContentTitle("열다")
//                .setContentText("하루를 정리할 시간입니다.")
//                .setTicker("열다-하루를 정리할 시간입니다.")
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentIntent(pendingIntent)
//                .setAutoCancel(true)
//                .setWhen(System.currentTimeMillis())
//                .setDefaults(Notification.DEFAULT_ALL);
//
//        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
//            builder.setCategory(Notification.CATEGORY_MESSAGE)
//                    .setPriority(Notification.PRIORITY_HIGH)
//                    .setVisibility(Notification.VISIBILITY_PUBLIC);
//        }
//
//        NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
//        manager.notify(11, builder.build());
//
//        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE );
//        PowerManager.WakeLock wakeLock = pm.newWakeLock( PowerManager.SCREEN_DIM_WAKE_LOCK
//                | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG" );
//        wakeLock.acquire(3000);
//

    }


}
