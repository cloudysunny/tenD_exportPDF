package bravous.tend.com.exportpdf;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * Created by cloud on 2017-12-10.
 */

public class BaseActivity extends AppCompatActivity {


    //현재 노트북을 불러왔을 때 노트북 이름값을 반환하는 것에서 노트북 객체 자체를 반환하는 걸로 바꾸었어요.
    //그럼 이제 메인 화면에서 새로 만든 노트북의 이름(사용자 지정)과 표지를 띄울 때
    //getCurrentNotebook()으로 먼저 노트북 객체를 불러오고(Notebook notebook = getCurrentNotebook())
    //그 다음에 notebook.getNote_name()으로 노트북 이름을 불러옵니다.
    //노트북 이미지 id(R.drawable.id)는 notebook.getNote_type()으로 노트타입 상수를 먼저 받아온 다음에
    //NotebookType type = NotebookType.valueOf(notebook.getNote_type());
    //int id = type.getCoverpath()
    // 이렇게 불러올 수 있습니다.(CreateNewNotebook.java의 주석 참고)
    public Notebook getCurrentNotebook(){

        Notebook notebook = new Notebook();

        MyDBHelper myDBHelper = new MyDBHelper(this);
        SQLiteDatabase db = myDBHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from "
                + myDBHelper.TABLE_NOTEBOOK
                +  " order by _id desc limit 1", null);
        if(cursor.getCount() != 0) {
            cursor.moveToFirst();
            notebook.setNotebook_name(cursor.getString(cursor.getColumnIndex(myDBHelper.KEY_NOTEBOOK_NAME)));
            notebook.setNotebook_type(cursor.getString(cursor.getColumnIndex(myDBHelper.KEY_NOTEBOOK_TYPE)));
            notebook.setPdfPath(cursor.getString(cursor.getColumnIndex(myDBHelper.KEY_PDF_PATH)));
            notebook.setExist_pdf(cursor.getInt(cursor.getColumnIndex(myDBHelper.KEY_EXIST_PDF))>0);
        }
        db.close();

        return notebook;
    }


        public ArrayList<Diary> getAllDiary(String note_name) {

            ArrayList<Diary> list = new ArrayList<>();
            //일기 데이터 불러오기
            MyDBHelper myDBHelper = new MyDBHelper(this);
            SQLiteDatabase db = myDBHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM " + myDBHelper.TABLE_DIARY
                    + " WHERE " + myDBHelper.KEY_NOTEBOOK_NAME + " = '"+ note_name +"'", null);

            while (cursor.moveToNext()){

                Diary diary = new Diary();
                diary.setDate(cursor.getString(cursor.getColumnIndex(myDBHelper.KEY_DATE)));
                diary.setEmotion(cursor.getString(cursor.getColumnIndex(myDBHelper.KEY_EMOTION)));
                diary.setImgPath(cursor.getString(cursor.getColumnIndex(myDBHelper.KEY_IMG_PATH)));
                diary.setTextPath(cursor.getString(cursor.getColumnIndex(myDBHelper.KEY_TEXT_PATH)));
                diary.setComment(cursor.getString(cursor.getColumnIndex(myDBHelper.KEY_COMMENT)));
                diary.setCommentTime(cursor.getLong(cursor.getColumnIndex(myDBHelper.KEY_COMMENT_TIME)));


                list.add(diary);
            }

            db.close();

            return list;
        }

        public ArrayList<Notebook> getAllNotebook() {

            ArrayList<Notebook> list = new ArrayList<>();
            //일기 데이터 불러오기
            MyDBHelper myDBHelper = new MyDBHelper(this);
            SQLiteDatabase db = myDBHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM " + myDBHelper.TABLE_NOTEBOOK
                    + " ORDER BY " + myDBHelper.KEY_ID + " desc;", null);

            while (cursor.moveToNext()){

                Notebook notebook = new Notebook();
                notebook.setNotebook_name(cursor.getString(cursor.getColumnIndex(myDBHelper.KEY_NOTEBOOK_NAME)));
                notebook.setNotebook_type(cursor.getString(cursor.getColumnIndex(myDBHelper.KEY_NOTEBOOK_TYPE)));
                notebook.setPdfPath(cursor.getString(cursor.getColumnIndex(myDBHelper.KEY_PDF_PATH)));
                notebook.setExist_pdf(cursor.getInt(cursor.getColumnIndex(myDBHelper.KEY_EXIST_PDF))>0);

                list.add(notebook);
            }

            db.close();

            return list;
        }



}
