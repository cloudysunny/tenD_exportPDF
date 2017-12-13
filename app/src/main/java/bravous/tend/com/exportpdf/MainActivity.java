package bravous.tend.com.exportpdf;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button newNoteBtn = (Button)findViewById(R.id.newNotebook);
        Button newDiaryBtn = (Button)findViewById(R.id.newDiary);
        Button viewDiaryBtn = (Button)findViewById(R.id.viewDiary);
        Button pdfBtn = (Button)findViewById(R.id.toPDF);
        Button alarmBtn = (Button)findViewById(R.id.alramSetting);
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
    }



}
