package bravous.tend.com.exportpdf;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;

public class MyReceiver3 extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent writeNewDiaryIntent = new Intent(context, writeNewDiary.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,
                1000,
                writeNewDiaryIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        Uri soundUri= RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_NOTIFICATION);

        builder.setContentTitle("열다")
                .setContentText("하루를 정리할 시간입니다.")
                .setTicker("열다-하루를 정리할 시간입니다.")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setSound(soundUri)
                .setVibrate(new long[]{0,3000});

        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            builder.setCategory(Notification.CATEGORY_MESSAGE)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setVisibility(Notification.VISIBILITY_PUBLIC);
        }

        NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(11, builder.build());

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE );
        PowerManager.WakeLock wakeLock = pm.newWakeLock( PowerManager.SCREEN_DIM_WAKE_LOCK
                | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG" );
        wakeLock.acquire(3000);
        wakeLock.release();

        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(1000);


    }
}
