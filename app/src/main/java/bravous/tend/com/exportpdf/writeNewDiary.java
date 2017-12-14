package bravous.tend.com.exportpdf;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;

public class writeNewDiary extends BaseActivity {


    //코멘트 테스트
    HashSet<String> happy;
    HashSet<String> excited;
    HashSet<String> normal;
    HashSet<String> melancholy;
    HashSet<String> angry;

    private static final int GALLERY_CODE = 10;


    TextView dateview;
    Spinner spinner;
    ImageView imageView;
    EditText editText;
    Button galleryBtn;
    Button okBtn;

    String emotion;
    String imagePathFromGallery;

    Notebook notebook;

    public Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_new_diary);

        //코멘트 테스트
        happy = new HashSet<>();
        happy.add("happy1");
        happy.add("happy2");
        happy.add("happy3");
        happy.add("happy4");
        happy.add("happy5");
        happy.add("happy6");
        happy.add("happy7");
        happy.add("happy8");
        happy.add("happy9");
        happy.add("happy10");
        excited = new HashSet<>();
        excited.add("excited1");
        excited.add("excited2");
        excited.add("excited3");
        excited.add("excited4");
        excited.add("excited5");
        excited.add("excited6");
        excited.add("excited7");
        excited.add("excited8");
        excited.add("excited9");
        excited.add("excited10");
        normal = new HashSet<>();
        normal.add("normal1");
        normal.add("normal2");
        normal.add("normal3");
        normal.add("normal4");
        normal.add("normal5");
        melancholy = new HashSet<>();
        melancholy.add("melancholy1");
        melancholy.add("melancholy2");
        melancholy.add("melancholy3");
        melancholy.add("melancholy4");
        melancholy.add("melancholy5");
        angry = new HashSet<>();
        angry.add("angry1");
        angry.add("angry2");
        angry.add("angry3");
        angry.add("angry4");
        angry.add("angry5");




        SimpleDateFormat sdf = new SimpleDateFormat("yyyy"+"년 "+"MM"+"월 "+"dd"+"일 ");
        Date date = new Date(System.currentTimeMillis());
        dateview = (TextView)findViewById(R.id.date);
        dateview.setText(sdf.format(date));

        spinner = (Spinner)findViewById(R.id.emotionSpinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String str = adapterView.getItemAtPosition(i).toString();
                if ( str != "" | str !="감정선택")
                    emotion = str;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        imageView = (ImageView)findViewById(R.id.pic);

        editText = (EditText)findViewById(R.id.text);

        //갤러리 불러오기
        galleryBtn = (Button)findViewById(R.id.gallery);
        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);

                startActivityForResult(intent,GALLERY_CODE);
            }
        });

        okBtn = (Button)findViewById(R.id.ok);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    submitDiary();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finish();
            }
        });

    }

    //갤러리에서 선택한 사진 뷰에 보여주기
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        imagePathFromGallery = getImagePathfromGallary(data.getData());
        File f = new File(imagePathFromGallery);
        imageView.setImageURI(Uri.fromFile(f));
    }

    //안드로이드 기기 갤러리에 저장된 이미지 경로 가져오기
    public String getImagePathfromGallary(Uri uri){

        String [] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this,uri,proj,null,null,null);

        Cursor cursor = cursorLoader.loadInBackground();
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        return cursor.getString(index);

    }


    private void submitDiary() throws IOException {

        Diary diary = writeNewDiary(getCurrentNotebook());

        UserSetting setting = new UserSetting(this);
        //코멘트 알람 작업 수행 여부
        if(setting.getValue(setting.COMMENT_ALARM, true)==true) {
            int position = getAllDiary(getCurrentNotebook()).size();
            setAlarm(position, diary.getCommentTime());
        }

        MyDBHelper helper = new MyDBHelper(this);
  //      SQLiteDatabase db = helper.getWritableDatabase();
//        db.execSQL("DELETE FROM " + helper.TABLE_DIARY);
        helper.createDiary(diary);
    //    db.close();

    }


    private Diary writeNewDiary(final String noteName) throws IOException {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Date date = new Date(System.currentTimeMillis());
        String timestamp = simpleDateFormat.format(date);

        //코멘트 보이는 시각 설정
        Calendar cal = new GregorianCalendar(Locale.KOREA);
        cal.setTimeInMillis(System.currentTimeMillis());
        //cal.add(Calendar.DAY_OF_YEAR, 1);
        //cal.set(Calendar.HOUR_OF_DAY, 22);
        cal.add(Calendar.MINUTE, +2);
        cal.set(Calendar.SECOND, 00);
        long time =cal.getTimeInMillis();

        Date date_test = cal.getTime();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date1 = format1.format(date_test);

        Log.i("time check", date1);

                Diary diary = new Diary();
                String dateValue = dateview.getText().toString();
                diary.setNotebook_name(noteName);
                diary.setDate(dateValue);
                diary.setEmotion(emotion);
                diary.setImgPath(saveImageFile(imagePathFromGallery, timestamp+".png"));
                diary.setTextPath(saveTextFile(editText.getText().toString(), timestamp+".txt"));
                diary.setComment(getRandomComment(emotion));
                diary.setCommentTime(time);
                return diary;

    }

    private String saveImageFile(String content, String filename) throws IOException {

        File file = new File(getFilesDir(), filename);
        FileOutputStream fos = new FileOutputStream(file);
        BufferedOutputStream bos = new BufferedOutputStream(fos);

        BitmapFactory.Options options = new BitmapFactory.Options();
        //이미지의 크기와 타입을 구함
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePathFromGallery, options);
        options.inSampleSize = calculateInSampleSize(options, 400, 400);
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(imagePathFromGallery, options);
        Bitmap resized = Bitmap.createScaledBitmap(bitmap, 400, bitmap.getHeight()*400/bitmap.getWidth(), true);
        resized.compress(Bitmap.CompressFormat.PNG, 50, bos);
        bos.flush();
        bos.close();
        fos.close();

        return getFilesDir()+"/"+filename;
    }


    //외부저장공간에 폴더를 생성해 텍스트 파일을 저장하고 저장경로값를 반환하는 메소드
    private String saveTextFile(String content, String filename) throws IOException {

        File file = new File(getFilesDir(), filename);

        Writer writer = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(writer);
        bw.write(content);
        writer.flush();
        bw.close();
        writer.close();

        return getFilesDir()+"/"+filename;
        }




    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight){
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if(height > reqHeight || width > reqWidth){
            final int halfHeight = height/2;
            final int halfWidth = width/2;
            while ((halfHeight/inSampleSize)>reqHeight || (halfWidth/inSampleSize)>reqWidth){
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }


    public String getRandomComment(String emotion){

        Iterator<String> iterator = null;
        String comment = null;

        //기존 comment 리스트 가져와서 iterator 만들기
        ArrayList<Diary> diaryList = getAllDiary(getCurrentNotebook());
        ArrayList<String> commentList = new ArrayList<>();
        for(Diary diary : diaryList){
            commentList.add(diary.getComment());
        }
        Log.i("commentList size", Integer.toString(commentList.size()));

        if(emotion.equals("기쁨")){
            happy.removeAll(commentList);
            iterator = happy.iterator();
            comment = iterator.next();
        }else if(emotion.equals("설렘")){
            excited.removeAll(commentList);
            iterator = excited.iterator();
            comment = iterator.next();
        }else if(emotion.equals("무념무상")){
            normal.removeAll(commentList);
            iterator = normal.iterator();
            comment = iterator.next();
        }else if(emotion.equals("우울")){
            melancholy.removeAll(commentList);
            iterator = melancholy.iterator();
            comment = iterator.next();
        }else if(emotion.equals("분노")){
            angry.removeAll(commentList);
            iterator = angry.iterator();
            comment = iterator.next();
        }
        return comment;
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }

    }


    public void setAlarm(int position, long time){
        AlarmManager manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, MyReceiver.class);
        intent.putExtra("position", position);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        manager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
    }

}
