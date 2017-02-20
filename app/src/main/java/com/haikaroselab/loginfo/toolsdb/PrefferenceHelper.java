package com.haikaroselab.loginfo.toolsdb;

import android.content.Context;
import android.content.SharedPreferences;

import com.haikaroselab.loginfo.pojos.LogItem;

/**
 * Created by root on 1/6/17.
 */

public class PrefferenceHelper {

    public static final String PHONE="phone";
    public static final String RINGTIME="ringing";
    public static final String ACCEPTEDTIME="accepted";
    public static final String ENDEDTIME="ended";
    public static final String CALL_DURATION="DURATION";

    public static final String LAST_STATE="status";



    public static LogItem getLogItem(Context context){

        SharedPreferences sharedPreferences=context.getSharedPreferences("SUPER_SETTINGS",0);

        LogItem item=new LogItem();
        item.setPhoneNumber(sharedPreferences.getString(PHONE,"none"));
        item.setTimeCalled(sharedPreferences.getString(RINGTIME,"none"));
        item.setTimeAccepted(sharedPreferences.getString(ACCEPTEDTIME,"none"));
        item.setTimeEnded(sharedPreferences.getString(ENDEDTIME,"none"));
        item.setCallDuration(sharedPreferences.getString(CALL_DURATION,"none"));

        return item;
    }

    public static void storePhone(Context context, String phone){
        SharedPreferences sharedPreferences=context.getSharedPreferences("SUPER_SETTINGS",0);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(PHONE,phone);
        editor.commit();
    }
    public static void storeRingTime(Context context,String ringTime){
        SharedPreferences sharedPreferences=context.getSharedPreferences("SUPER_SETTINGS",0);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(RINGTIME,ringTime);
        editor.commit();

    }
    public static  void storeAcceptedTime(Context context,String acceptedTime){
        SharedPreferences sharedPreferences=context.getSharedPreferences("SUPER_SETTINGS",0);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(ACCEPTEDTIME,acceptedTime);
        editor.commit();
    }
    public static void storeEndedTime(Context context,String endedTime){
        SharedPreferences sharedPreferences=context.getSharedPreferences("SUPER_SETTINGS",0);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(ENDEDTIME,endedTime);
        editor.commit();
    }
    public static void storeCallDuration(Context context,String durationTime){
        SharedPreferences sharedPreferences=context.getSharedPreferences("SUPER_SETTINGS",0);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(CALL_DURATION,durationTime);
        editor.commit();
    }

    public static void setLastState(Context context,String state){
        SharedPreferences sharedPreferences=context.getSharedPreferences("SUPER_SETTINGS",0);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(LAST_STATE,state);
        editor.commit();
    }
    public static String getLastState(Context context){
        SharedPreferences sharedPreferences=context.getSharedPreferences("SUPER_SETTINGS",0);
        return sharedPreferences.getString(LAST_STATE,"none");
    }
    public static void clearState(Context context){
        SharedPreferences sharedPreferences=context.getSharedPreferences("SUPER_SETTINGS",0);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(LAST_STATE,"none");
        editor.commit();
    }
}
