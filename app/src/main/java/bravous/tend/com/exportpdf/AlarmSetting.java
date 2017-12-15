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
    String savedTime; //앱에 저장된 시간값

    TextView timeView;

    //timepicker에서 가져온 값을 담을 시, 분 변수
    int hour;
    int minute;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_setting);

        setting = new UserSetting(this);

        comment = (Switch)findViewById(R.id.switch1);
        regular = (Switch)findViewById(R.id.switch2);

        timeView = (TextView)findViewById(R.id.settingTimeView);

        savedTime = setting.getValue(setting.ALARM_TIME, "시간을 선택해주세요");
        if(savedTime!=null | !savedTime.equals("alarm off"))
            timeView.setText(savedTime);
        else if(savedTime.equals("alarm off"))
            timeView.setText("");

        comment.setChecked(setting.getValue(setting.COMMENT_ALARM, true));
        regular.setChecked(setting.getValue(setting.REGULAR_ALARM, true));
        Log.i("setChecked state", Boolean.toString(setting.getValue(setting.COMMENT_ALARM, true)));


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
                    settingTime = timeSetting();
                    SimpleDateFormat format = new SimpleDateFormat("HH"+"시 "+"mm"+"분");
                    String viewTime = format.format(settingTime);
                    timeView.setText(viewTime);
                    savedTime = viewTime;
                }else if(b == false){
                    timeView.setText("");
                    savedTime = "alarm off";
                }

               regular_alarm = b;
            }
        });

        if(!comment.isActivated())
        comment_alarm = setting.getValue(setting.COMMENT_ALARM, true);

        if(!regular.isActivated())
            regular_alarm = setting.getValue(setting.REGULAR_ALARM, true);
    }

    //regular alarm 설정
    public void setAlarm(long time){

        long interval = 1000 * 60 * 60  * 24;
        long now =System.currentTimeMillis();

        if(time < now){
            time += now;
        }

        AlarmManager manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, MyReceiver2.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1000, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, time, interval, pendingIntent);
    }

    public void cancelAlarm(){
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmSetting.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1000, intent, 0);
        alarmManager.cancel(pendingIntent);
    }


    public long timeSetting(){
        new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                hour = i;//timePicker.getCurrentHour(); // == i
                minute = i1; //timePicker.getCurrentMinute(); // == i1
            }
        }, 22, 00, true).show();

        Log.i("time picker check", hour + " / " + minute);
        Calendar cal = new GregorianCalendar(Locale.KOREA);
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, 00);
        long time =cal.getTimeInMillis();

        return time;
    }


    @Override
    protected void onPause() {
        super.onPause();

        if (comment_alarm != setting.getValue(setting.COMMENT_ALARM, true)) {
            setting.put(setting.COMMENT_ALARM, comment_alarm);

            if (comment_alarm == false) {
                //이미 설정된 코멘트 알람 cancel이 안돼!!
                NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

               // Intent intent = new Intent(this, MyReceiver.class);
               // PendingIntent sender = PendingIntent.getBroadcast(this, 1010, intent, PendingIntent.FLAG_NO_CREATE);
               // if (sender != null) {
                    manager.cancel(10);
                 //   sender.cancel();
               // }
            }
        }

            if (regular_alarm != setting.getValue(setting.REGULAR_ALARM, true)) {
                setting.put(setting.REGULAR_ALARM, regular_alarm);
                setting.put(setting.ALARM_TIME, savedTime);
                if(regular_alarm == true)
                    setAlarm(settingTime);
                else
                    cancelAlarm();
            }

        }


}
