package bravous.tend.com.exportpdf;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by cloud on 2017-12-10.
 */

public class BaseActivity extends AppCompatActivity {


        public Notebook getCurrentNotebook(){
            MyDBHelper myDBHelper = new MyDBHelper(this);
            SQLiteDatabase db = myDBHelper.getWritableDatabase();
            String sql = "SELECT * FROM " + myDBHelper.TABLE_NOTEBOOK
                    + " WHERE SELECT MAX(" + myDBHelper.KEY_ID + ") FROM TABLE";
            Cursor cursor = db.rawQuery(sql, null);

            Notebook notebook = new Notebook();
            notebook.setNotebook_name(cursor.getString(1));
            notebook.setNotebook_type(cursor.getShort(2));

            db.close();

            return notebook;
        }

        public int getNotebookType(String notebook_name){

            int notebook_type = 0;

            MyDBHelper myDBHelper = new MyDBHelper(this);
            SQLiteDatabase db = myDBHelper.getWritableDatabase();
            String sql = "SELECT " + myDBHelper.KEY_NOTEBOOK_TYPE + " FROM " + myDBHelper.TABLE_NOTEBOOK
                    + " WHERE "+myDBHelper.KEY_NOTEBOOK_NAME + " = '" + notebook_name + "'";
            Cursor cursor = db.rawQuery(sql, null);

            if(cursor.getCount() != 0) {
                cursor.moveToFirst();
                notebook_type = cursor.getInt(cursor.getColumnIndex(myDBHelper.KEY_NOTEBOOK_TYPE));
            }
            Log.i("getNotebookType() check", Integer.toString(notebook_type));

            db.close();

            return notebook_type;
        }


        public int getNotebookCoverPath(int ordinal){
            int coverPath = 0;
            if(ordinal==0){
                coverPath=NotebookType.LEAF.getCoverPath();
            }else if(ordinal == 1){
                coverPath= NotebookType.FLOWER.getCoverPath();
            }else if(ordinal == 2){
                coverPath= NotebookType.PATTERN.getCoverPath();
            }else if(ordinal == 3){
                coverPath= NotebookType.FRUIT.getCoverPath();
            }

            return coverPath;
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
            Cursor cursor = db.rawQuery("SELECT * FROM " + myDBHelper.TABLE_NOTEBOOK, null);

            while (cursor.moveToNext()){

                Notebook notebook = new Notebook();
                notebook.setNotebook_name(cursor.getString(cursor.getColumnIndex(myDBHelper.KEY_NOTEBOOK_NAME)));
                notebook.setNotebook_type(cursor.getInt(cursor.getColumnIndex(myDBHelper.KEY_NOTEBOOK_TYPE)));
                notebook.setPdfPath(cursor.getString(cursor.getColumnIndex(myDBHelper.KEY_TEXT_PATH)));

                if(cursor.getInt(cursor.getColumnIndex(myDBHelper.KEY_EXIST_PDF))==0)
                    notebook.setExist_pdf(false);
                else
                    notebook.setExist_pdf(true);

                list.add(notebook);
            }

            db.close();

            return list;
        }




}
