package com.haikaroselab.loginfo.receiver;

import android.content.Context;
import android.util.Log;

import com.haikaroselab.loginfo.pojos.LogItem;
import com.haikaroselab.loginfo.toolsdb.DatabaseCode;
import com.haikaroselab.loginfo.toolsdb.PrefferenceHelper;

import java.util.Date;

/**
 * Created by root on 1/6/17.
 */

public class CallReceiver extends PhoneCallReceiver {

    @Override
    protected void onIncomingCallStarted(Context ctx, String number, Date start) {
         Log.e("incoming call"+new Date().toString(),number);
         PrefferenceHelper.storePhone(ctx,number);
         PrefferenceHelper.storeRingTime(ctx,start.toString());
    }

    @Override
    protected void onOutgoingCallStarted(Context ctx, String number, Date start) {
    }

    @Override
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {
            Log.e("ended at",new Date().toString());
            String status;

            if(PrefferenceHelper.getLastState(ctx).equals("accepted")){
                status="accepted";
                Log.e("the status is: ",status);
            }else{
                status="rejected";
                Log.e("the status is: ",status);
            }

            PrefferenceHelper.storePhone(ctx,number);
            PrefferenceHelper.storeEndedTime(ctx,end.toString());
            LogItem item=PrefferenceHelper.getLogItem(ctx);
            item.setStatus(status);
            DatabaseCode databaseCode=new DatabaseCode(ctx);
            databaseCode.insertData(item);
            PrefferenceHelper.clearState(ctx);

    }

    @Override
    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end) {
    }

    @Override
    protected void onMissedCall(Context ctx, String number, Date start) {
    }

    @Override
    protected void onCallAccepted(Context context,String nuumber,Date start){
        PrefferenceHelper.storeAcceptedTime(context,start.toString());
       // PrefferenceHelper.setLastState(context,"accepted");
    }

}