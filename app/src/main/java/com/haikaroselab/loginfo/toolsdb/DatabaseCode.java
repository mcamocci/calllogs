package com.haikaroselab.loginfo.toolsdb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.haikaroselab.loginfo.pojos.LogItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 1/6/17.
 */

public class DatabaseCode extends SQLiteOpenHelper {

    public static String DBNAME="DATABASE_NAME";
    public static int VERSION=1;
    private Context context;

    String CREATE_CALL_LOG_TABLE="CREATE TABLE CALL_LOGS (" +
            "ID INTEGER PRIMARY KEY,PHONE_NUMBER TEXT,RING_TIME TEXT,ACCEPTED_TIME TEXT,END_TIME TEXT," +
            "CALL_DURATION TEXT,STATUS TEXT)";

    public void insertData(LogItem item){

        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("PHONE_NUMBER",item.getPhoneNumber());
        values.put("RING_TIME",item.getTimeCalled());
        values.put("ACCEPTED_TIME",item.getTimeAccepted());
        values.put("END_TIME",item.getTimeEnded());
        values.put("CALL_DURATION",item.getCallDuration());
        values.put("STATUS",item.getStatus());

        database.insert("CALL_LOGS",null,values);
    }

    public DatabaseCode(Context context){
        super(context,DatabaseCode.DBNAME,null,DatabaseCode.VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        SQLiteDatabase database=db;
        db.execSQL(CREATE_CALL_LOG_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS CALL_LOGS");
        onCreate(db);
    }



    public void insertData(String number,String time,String acceptedTime,String EndedTime,String CallDuration){
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("","");
        database.insert("CALL_LOGS",null,values);
    }

    public List<LogItem> getAllLogs(){
        List<LogItem> list=new ArrayList<>();
        String sql="SELECT * FROM CALL_LOGS";

        SQLiteDatabase database=this.getWritableDatabase();
        Cursor cursor=database.rawQuery(sql,null);

        while (cursor.moveToNext()){
            LogItem item=new LogItem();
            item.setId(cursor.getInt(0));
            item.setPhoneNumber(cursor.getString(1));
            item.setTimeCalled(cursor.getString(2));
            item.setTimeAccepted(cursor.getString(3));
            item.setTimeEnded(cursor.getString(4));
            item.setCallDuration(cursor.getString(5));
            item.setStatus(cursor.getString(6));
            list.add(item);
        }

        return list;
    }

    public void getCallOfNumber(String number){

    }

    public boolean deleteAllLogs(){
        SQLiteDatabase database=this.getWritableDatabase();
        String sql="DELETE FROM CALL_LOGS";
        database.execSQL(sql);
        return true;
    }

    public boolean deleteAllLogsWhereId(int id){
        SQLiteDatabase database=this.getWritableDatabase();
        String sql="DELETE FROM CALL_LOGS where ID="+Integer.toString(id);
        database.execSQL(sql);
        return true;
    }



}
