package bravous.tend.com.exportpdf;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by cloud on 2017-12-06.
 */

public class MyDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "testDB15";

    public static final String TABLE_NOTEBOOK = "notebook_tb";
    public static final String TABLE_DIARY = "diary_tb";
    public static final String TABLE_USER_SETTING = "user_setting_tb";

    public static final String KEY_ID = "_id";
    public static final String KEY_NOTEBOOK_NAME = "notebook_name";
    public static final String KEY_COVER_PATH = "cover_path";

    public static final String KEY_DATE = "date";
    public final String KEY_EMOTION = "emotion";
    public static final String KEY_IMG_PATH = "img_path";
    public static final String KEY_TEXT_PATH = "text_path";
    public static final String KEY_COMMENT = "comment";
    public static final String KEY_COMMENT_TIME = "comment_time";

    public static final String KEY_NOTEBOOK_TYPE = "notebook_type";
    public static final String KEY_EXIST_PDF = "exist_pdf";
    public static final String KEY_PDF_PATH = "pdf_path";

    public static final String KEY_USER_EMAIL = "user_email";
    public static final String KEY_IS_CURRENT_NOTEBOOK = "current_notebook";
    public static final String KEY_USE_COMMENT_ALARM = "comment_alarm";
    public static final String KEY_USE_REGULAR_ALARM = "regular_alarm";

    public MyDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_DIARY =
                "CREATE TABLE " + TABLE_DIARY + " (" +
                        KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        KEY_NOTEBOOK_NAME + " not null, " +
                        KEY_DATE+ " not null, " +
                        KEY_EMOTION + " not null, " +
                        KEY_IMG_PATH + " not null, " +
                        KEY_TEXT_PATH + " not null, " +
                        KEY_COMMENT + " not null, " +
                        KEY_COMMENT_TIME + " not null);";

        String CREATE_TABLE_NOTEBOOK =
                "CREATE TABLE " + TABLE_NOTEBOOK + " (" +
                        KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        KEY_NOTEBOOK_NAME + " not null, " +
                        KEY_NOTEBOOK_TYPE + " INTEGER not null, " +
                        KEY_COVER_PATH + " not null, " +
                        //boolean : false=0, true=1
                        KEY_EXIST_PDF + " INTEGER not null, " +
                        KEY_PDF_PATH + ");";

        String CREATE_USER_SETTING =
                "CREATE TABLE " + TABLE_USER_SETTING + " (" +
                        KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        KEY_USER_EMAIL + " not null, " +
                        //boolean : false=0, true=1
                        KEY_IS_CURRENT_NOTEBOOK + " INTEGER not null, " +
                        KEY_USE_COMMENT_ALARM + " not null, " +
                        KEY_USE_REGULAR_ALARM + " INTEGER not null);";

        db.execSQL(CREATE_TABLE_DIARY);
        db.execSQL(CREATE_TABLE_NOTEBOOK);
        db.execSQL(CREATE_USER_SETTING);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_TABLE_DIARY =
                "DROP TABLE IF EXISTS " + TABLE_DIARY;
        String DROP_TABLE_NOTEBOOK =
                "DROP TABLE IF EXISTS " + TABLE_NOTEBOOK;
        String DROP_TABLE_USER_SETTING =
                "DROP TABLE IF EXISTS " + TABLE_USER_SETTING;
        db.execSQL(DROP_TABLE_DIARY);
        db.execSQL(DROP_TABLE_NOTEBOOK);
        db.execSQL(DROP_TABLE_USER_SETTING);

        onCreate(db);
    }



    public void createDiary(Diary diary) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NOTEBOOK_NAME, diary.getNotebook_name());
        values.put(KEY_DATE, diary.getDate());
        values.put(KEY_EMOTION, diary.getEmotion());
        values.put(KEY_IMG_PATH, diary.getImgPath());
        values.put(KEY_TEXT_PATH, diary.getTextPath());
        values.put(KEY_COMMENT, diary.getComment());
        values.put(KEY_COMMENT_TIME, diary.getCommentTime());

        db.insert(TABLE_DIARY, null, values);

        db.close();
    }

    public void createNotebook(Notebook notebook) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NOTEBOOK_NAME, notebook.getNotebook_name());
        values.put(KEY_NOTEBOOK_TYPE, notebook.getNotebook_type());
        values.put(KEY_COVER_PATH, notebook.getCoverPath());
        values.put(KEY_EXIST_PDF, notebook.getExist_pdf());
        values.put(KEY_PDF_PATH, notebook.getPdfPath());

        db.insert(TABLE_NOTEBOOK, null, values);

        db.close();
    }



}
