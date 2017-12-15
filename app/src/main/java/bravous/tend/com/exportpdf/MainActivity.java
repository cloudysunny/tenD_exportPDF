package bravous.tend.com.exportpdf;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    Toolbar toolbar;
    ImageView imageView;
    Bundle bundle;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        imageView = (ImageView)findViewById(R.id.alarmBell);

        Button startBtn = (Button)findViewById(R.id.startUse);
        Button newNoteBtn = (Button)findViewById(R.id.newNotebook);
        Button newDiaryBtn = (Button)findViewById(R.id.newDiary);
        Button viewDiaryBtn = (Button)findViewById(R.id.viewDiary);
        Button pdfBtn = (Button)findViewById(R.id.toPDF);
        Button alarmBtn = (Button)findViewById(R.id.alramSetting);
        startBtn.setOnClickListener(this);
        newNoteBtn.setOnClickListener(this);
        newDiaryBtn.setOnClickListener(this);
        viewDiaryBtn.setOnClickListener(this);
        pdfBtn.setOnClickListener(this);
        alarmBtn.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.newNotebook)
            startActivity(new Intent(getApplicationContext(), CreateNewNotebook.class));
        else if(view.getId() == R.id.newDiary)
            startActivity(new Intent(getApplicationContext(), writeNewDiary.class));
        else if(view.getId() == R.id.viewDiary)
            startActivity(new Intent(getApplicationContext(), ViewDiaryActivity.class));
        else if(view.getId() == R.id.toPDF)
            startActivity(new Intent(getApplicationContext(), ExportSelectActivity.class));
        else if(view.getId() == R.id.alramSetting)
            startActivity(new Intent(getApplicationContext(), AlarmSetting.class));
        else if(view.getId() == R.id.startUse){
            UserSetting setting = new UserSetting(this);
            setting.put(setting.USER_EMAIL, "test@gmail.com");
            setting.put(setting.COMMENT_ALARM, true);
            setting.put(setting.REGULAR_ALARM, false);
            setting.put(setting.IS_CURRENT_NOTEBOOK, true);

            Toast.makeText(getApplicationContext(), "열다를 사용할 준비가 완료되었습니다.", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        bundle = intent.getExtras();

        if(bundle!=null){
            position = bundle.getInt("position");
            imageView.setImageResource(R.drawable.bellon);
            //  onResume();
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), ViewDiaryActivity.class);
                    intent.putExtra("position", position);
                    startActivity(intent);
                    imageView.setImageResource(R.drawable.belloff);

                }
            });
        }

    }
}
