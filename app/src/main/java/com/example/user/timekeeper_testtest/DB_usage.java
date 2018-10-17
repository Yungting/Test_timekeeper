package com.example.user.timekeeper_testtest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DB_usage extends SQLiteOpenHelper {
    private SQLiteDatabase db;
    public DB_usage(Context context) {
        super(context, "usage.db", null, 1);
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE IF NOT EXISTS usage(id INTEGER, date TEXT PRIMARY KEY, period INTEGER, ifawake BOOLEAN, state BOOLEAN)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }
    public Cursor select()
    {
        //SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("usage",null,null,null,null,null,null);
        return cursor;
    }
    public Cursor select_this_usage(String Date){
        String select_column[] = {"state"};
        String select_Date[] = {Date};
        Cursor cursor = db.query("usage",null,"date=?",select_Date,null,null,null);
        //Cursor cursor = db.query("usage",null,null,null,null,null,null);
        return cursor;
    }
    public Cursor select_update(){
        String select_column[] = {"state"};
        String select_state[] = {"0"};
        Cursor cursor = db.query("usage",null,"state=?",select_state,null,null,null);
        //Cursor cursor = db.query("usage",null,null,null,null,null,null);
        return cursor;
    }
    public void update_state_change(){
        ContentValues values = new ContentValues();
        values.put("state",1);
        db.update("usage",values,"state=0",null);
    }
    public long insert(String u_id, String Date, int period) {
        //SQLiteDatabase db = this.getWritableDatabase();
        // 建立準備新增資料的ContentValues物件
        ContentValues cv = new ContentValues();
        // 加入ContentValues物件包裝的新增資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
        cv.put("id", u_id);
        cv.put("date", Date);
        cv.put("period", period);
        cv.put("state", false);

        // 新增一筆資料並取得編號
        // 第一個參數是表格名稱
        // 第二個參數是沒有指定欄位值的預設值
        // 第三個參數是包裝新增資料的ContentValues物件
        long row = db.insert("usage", null, cv);
        return  row;
    }

    public void ifawake(String u_id, String Date, boolean wake){
        Log.d("u_id",":"+u_id);
        Log.d("date",":"+Date);
        ContentValues cv = new ContentValues();
        cv.put("ifawake", wake);
        String sql = "UPDATE usage SET ifawake = ? WHERE id = ? AND date = ?";
        db.execSQL(sql, new Object[]{wake, u_id, Date});
    }

    public void delete(String u_id, String Date){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "DELETE FROM usage WHERE id = ? AND date = ?";
        db.execSQL(sql, new Object[]{u_id, Date});
    }

    public void deleteAll()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + "usage" );
    }
}
