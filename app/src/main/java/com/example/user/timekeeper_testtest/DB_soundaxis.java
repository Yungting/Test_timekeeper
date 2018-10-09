package com.example.user.timekeeper_testtest;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Timestamp;

public class DB_soundaxis extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "record.db";
    public static final int VERSION = 1;
    private static final String TABLE_NAME = "record";
    public static final String KEY_ID = "_id";

    public static final String DATETIME = "date_time";
    public static final String X_AXIS = "x_axis";
    public static final String Y_AXIS = "y_axis";
    public static final String Z_AXIS = "z_axis";
    public static final String SOUND_DB = "sound_db";
    public static final String STATE = "state";
    public static final String DATEALARM = "date_alarm";


    private SQLiteDatabase db;

    //private static SQLiteDatabase database;
    public DB_soundaxis(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                        KEY_ID + " INTEGER," +
                        DATETIME +" TIMESTAMP PRIMARY KEY," +
                        X_AXIS + " REAL , " +
                        Y_AXIS + " REAL , " +
                        Z_AXIS + " REAL , " +
                        SOUND_DB + " REAL," +
                        DATEALARM + " TEXT , "+
                        STATE+" BOOLEAN );";
        db.execSQL(CREATE_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }
    public Cursor select()
    {
        //SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("record",null,null,null,null,null,null);
        return cursor;
    }
    public Cursor select_update(){
        String select_state[] = {"0"};
        Cursor cursor = db.query("record",null,"state=?",select_state,null,null,null);
        //Cursor cursor = db.query("record",null,null,null,null,null,null);
        return cursor;
    }
    public void update_state_change(){
        ContentValues values = new ContentValues();
        values.put(STATE,1);
        db.update("record",values,STATE+"=0",null);
    }
    public long insert(String u_id, Timestamp Datetime, double x_axis, double y_axis, double z_axis, double sound_db, String date_alarm) {
        //SQLiteDatabase db = this.getWritableDatabase();
        // 建立準備新增資料的ContentValues物件
        ContentValues cv = new ContentValues();
        // 加入ContentValues物件包裝的新增資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
        cv.put(KEY_ID,u_id);
        cv.put(DATETIME, String.valueOf(Datetime));
        cv.put(X_AXIS, x_axis);
        cv.put(Y_AXIS, y_axis);
        cv.put(Z_AXIS, z_axis);
        cv.put(SOUND_DB, sound_db);
        cv.put(DATEALARM, date_alarm);
        cv.put(STATE, false);

        // 新增一筆資料並取得編號
        // 第一個參數是表格名稱
        // 第二個參數是沒有指定欄位值的預設值
        // 第三個參數是包裝新增資料的ContentValues物件
        long row = db.insert(TABLE_NAME, null, cv);
        return  row;
    }
    public void delete(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = "_id" + " = " + Integer.toString(id) ;
        db.delete("record", where, null);
    }

    //刪除Table全部資料
    public void deleteAll()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + "record" );
    }
}
