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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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


    DatabaseReference db;

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

    ArrayList<Comment> list;

    public Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_new_diary);

        //파이어베이스 db에서 전체 코멘트 리스트 미리 불러오기
        list = new ArrayList<>();
        db = FirebaseDatabase.getInstance().getReference();
        db.child("comment").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("datasnanpshot check", Long.toString(dataSnapshot.getChildrenCount()));
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    list.add(snapshot.getValue(Comment.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("datasnanpshot check", databaseError.getMessage());
            }
        });




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

        Notebook notebook = getCurrentNotebook();
        Diary diary = writeNewDiary(notebook.getNotebook_name());

        int position = getAllDiary(notebook.getNotebook_name()).size();
        UserSetting setting = new UserSetting(this);
        //코멘트 알람 작업 수행 여부(알람설정을 꺼도 아이콘 알람표시는 항상 수행됨)
        if(setting.getValue(setting.COMMENT_ALARM, true)==true) {
            setAlarm(position, diary.getCommentTime());
        }
            setAlarmBell(position, diary.getCommentTime());

        MyDBHelper helper = new MyDBHelper(this);
  //      SQLiteDatabase db = helper.getWritableDatabase();
//        db.execSQL("DELETE FROM " + helper.TABLE_DIARY);
        helper.createDiary(diary);
    //    db.close();

    }


    private Diary writeNewDiary(final String noteName) throws IOException {

        HashSet<String> commentSet = getCommentDB(emotion);
        Log.i("commentSet size 2 ", Integer.toString(commentSet.size()));


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
                diary.setComment(getRandomComment(commentSet));
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

            Bitmap.createScaledBitmap(bitmap, 400, bitmap.getHeight() * 400 / bitmap.getWidth(), true)
            .compress(Bitmap.CompressFormat.PNG, 50, bos);

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


    public String getRandomComment(HashSet<String> commentSet){
        //기존 comment 리스트 가져와서 iterator 만들기
        ArrayList<Diary> diaryList = getAllDiary(getCurrentNotebook().getNotebook_name());
        ArrayList<String> commentList = new ArrayList<>();
        for(Diary diary : diaryList){
            commentList.add(diary.getComment());
        }
        Log.i("commentList size", Integer.toString(commentList.size()));

        commentSet.removeAll(commentList);
        Iterator<String> commentIterator = commentSet.iterator();
        String comment = commentIterator.next();

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
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1010, intent, PendingIntent.FLAG_ONE_SHOT);
        manager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
    }

    public void setAlarmBell(int position, long time){
        AlarmManager manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, MyReceiver2.class);
        intent.putExtra("position", position);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1011, intent, PendingIntent.FLAG_ONE_SHOT);
        manager.set(AlarmManager.RTC, time, pendingIntent);
    }

    HashSet<String> commentSet;

    public HashSet<String> getCommentDB(final String selectEmotion){

        commentSet = new HashSet<>();
        String str = null;

        for(Comment comment : list){
            if(comment.getEmotion_type().equals(selectEmotion)){
                commentSet.add(comment.getComment());
            }
        }


        return commentSet;
    }


}
