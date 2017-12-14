package bravous.tend.com.exportpdf;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;

public class AlarmSetting extends BaseActivity {

    MyDBHelper helper;
    SQLiteDatabase db;
    boolean comment_alarm;
    boolean regular_alarm;

    Switch switch1;
    Switch switch2;

    UserSetting setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_setting);

        setting = new UserSetting(this);

        switch1 = (Switch)findViewById(R.id.switch1);
        switch2 = (Switch)findViewById(R.id.switch2);

        switch1.setChecked(setting.getValue(setting.COMMENT_ALARM, true));
        switch2.setChecked(setting.getValue(setting.REGULAR_ALARM, true));
        Log.i("setChecked state", Boolean.toString(setting.getValue(setting.COMMENT_ALARM, true)));

       // helper = new MyDBHelper(this);
        //db = helper.getWritableDatabase();

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                comment_alarm = b;
            }
        });

        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
               regular_alarm = b;
            }
        });


    }

    @Override
    protected void onPause() {
        super.onPause();

        if(comment_alarm!=setting.getValue(setting.COMMENT_ALARM, true)){
            setting.put(setting.COMMENT_ALARM, comment_alarm);

        }

        if(regular_alarm!=setting.getValue(setting.REGULAR_ALARM, true)){
            setting.put(setting.REGULAR_ALARM, regular_alarm);
        }

    }


   //이미 설정된 코멘트 알람 cancel

}
