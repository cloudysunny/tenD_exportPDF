package bravous.tend.com.exportpdf;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class AlarmSetting extends BaseActivity {

    boolean comment_alarm;
    boolean regular_alarm;

    Switch comment;
    Switch regular;

    UserSetting setting;

    long settingTime; //사용자가 선택한 시간의 long타입 값(timeSetting()에서 리턴한 값)
    String savedTime; //기존에 저장된 시간값

    TextView timeView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_setting);

        setting = new UserSetting(this);

        comment = (Switch)findViewById(R.id.switch1);
        regular = (Switch)findViewById(R.id.switch2);

        timeView = (TextView)findViewById(R.id.settingTimeView);


        //기존에 설정한 값을 불러와서 세팅(알람 초기값은 comment: true, regular: false, 알람시간(savedTime: "")
        savedTime = setting.getValue(setting.ALARM_TIME, "");
        if(savedTime!=null){
            timeView.setText(savedTime);
        }else{
            timeView.setText("");
        }

        comment.setChecked(setting.getValue(setting.COMMENT_ALARM, true));
        regular.setChecked(setting.getValue(setting.REGULAR_ALARM, false));

        comment.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                comment_alarm = b;
            }
        });

        regular.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(b == true){
                    timeSetting();
                }else if(b == false){
                    timeView.setText("");
                }

               regular_alarm = b;
            }
        });

        if(!comment.isActivated())
        comment_alarm = setting.getValue(setting.COMMENT_ALARM, true);

        if(!regular.isActivated())
            regular_alarm = setting.getValue(setting.REGULAR_ALARM, false);
    }

    //regular alarm 설정
    public void setAlarm(long time){

        long interval = 1000 * 60 * 60  * 24;
        long now =System.currentTimeMillis();

        if(time < now){
            time += interval;
        }

        AlarmManager manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, MyReceiver3.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,
                1000,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, time, AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    public void cancelAlarm(){
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, MyReceiver3.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1000, intent, PendingIntent.FLAG_NO_CREATE);
        alarmManager.cancel(pendingIntent);
        pendingIntent.cancel();
    }


    public long timeSetting(){

        new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {

                Log.i("time picker check", hour + " / " + minute);
                Calendar cal = new GregorianCalendar(Locale.KOREA);
                cal.setTimeInMillis(System.currentTimeMillis());
                cal.set(Calendar.HOUR_OF_DAY, hour);
                cal.set(Calendar.MINUTE, minute);
                cal.set(Calendar.SECOND, 00);
                settingTime =cal.getTimeInMillis();
                SimpleDateFormat format = new SimpleDateFormat("HH"+"시 "+"mm"+"분");
                String viewTime = format.format(settingTime);
                timeView.setText(viewTime);
            }
        }, 22, 00, true).show();

        return settingTime;
    }


    @Override
    protected void onPause() {
        super.onPause();

        if (comment_alarm != setting.getValue(setting.COMMENT_ALARM, true)) {
            setting.put(setting.COMMENT_ALARM, comment_alarm);

            if (comment_alarm == false) {
                //이미 설정된 코멘트 알람 cancel이 안돼!!
                NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                Intent intent = new Intent(this, MyReceiver.class);
                PendingIntent sender = PendingIntent.getBroadcast(this, 1010, intent, PendingIntent.FLAG_NO_CREATE);
                if (sender != null) {
                    manager.cancel(10);
                    sender.cancel();
               }
            }
        }

        //레귤러 알람 설정사항이 변경된 경우에만 설정값 저장
            if (regular_alarm != setting.getValue(setting.REGULAR_ALARM, true)) {
                setting.put(setting.REGULAR_ALARM, regular_alarm);

                if(regular_alarm==false)
                    cancelAlarm();
            }

            String timeViewValue = timeView.getText().toString();
            //설정값은 그대로지만 시간을 변경한 경우
            if(regular_alarm == true || !timeView.equals(setting.getValue(setting.ALARM_TIME, ""))) {
                setting.put(setting.ALARM_TIME, timeView.getText().toString());
                    setAlarm(settingTime);
                    Log.i("onPause() settingTime", Long.toString(settingTime));
                }
            }

}
