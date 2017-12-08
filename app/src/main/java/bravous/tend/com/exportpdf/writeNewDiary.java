package bravous.tend.com.exportpdf;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
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
import java.util.Date;

public class writeNewDiary extends AppCompatActivity {

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

                Diary diary = new Diary();
                String dateValue = dateview.getText().toString();
                diary.setNotebook_name(noteName);
                diary.setDate(dateValue);
                diary.setEmotion(emotion);
                diary.setImgPath(saveImageFile(imagePathFromGallery, timestamp+".png"));
                diary.setTextPath(saveTextFile(editText.getText().toString(), timestamp+".txt"));

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



    public String getCurrentNotebook(){

        String notebook_Name = null;

      MyDBHelper helper = new MyDBHelper(this);
      SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select " + helper.KEY_NOTEBOOK_NAME
                + " from " + helper.TABLE_NOTEBOOK
                +  " order by _id desc limit 1", null);
        if(cursor.getCount() != 0) {
            cursor.moveToFirst();
            notebook_Name = cursor.getString(cursor.getColumnIndex(helper.KEY_NOTEBOOK_NAME));
        }
        db.close();
     return notebook_Name;
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

    @Override
    protected void onStop() {
        super.onStop();
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }

    }
}
