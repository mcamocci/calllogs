package com.haikaroselab.loginfo.receiver;

import java.util.Date;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.haikaroselab.loginfo.pojos.LogItem;
import com.haikaroselab.loginfo.toolsdb.DatabaseCode;
import com.haikaroselab.loginfo.toolsdb.PrefferenceHelper;

import static com.haikaroselab.loginfo.toolsdb.PrefferenceHelper.ACCEPTEDTIME;

public abstract class PhoneCallReceiver extends BroadcastReceiver {


    //The receiver will be recreated whenever android feels like it.  We need a static variable to remember data between instantiations

    private static int lastState = TelephonyManager.CALL_STATE_IDLE;
    private static Date callStartTime;
    private static boolean isIncoming;
    private static String savedNumber;  //because the passed incoming is only valid in ringing


    @Override
    public void onReceive(Context context, Intent intent) {

            String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
            String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
            int state = 0;
            if(stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE)){
                state = TelephonyManager.CALL_STATE_IDLE;
                Log.e("the phone status now is","rejected");
                Log.e("the last state was",PrefferenceHelper.getLastState(context));

                if(PrefferenceHelper.getLastState(context).equals("none")){
                    PrefferenceHelper.setLastState(context,"rejected");
                }else{
                    PrefferenceHelper.setLastState(context,"accepted");
                }


                Log.e("ended at",new Date().toString());
                String status;

                if(!(PrefferenceHelper.getLastState(context).equals("accepted"))){
                    PrefferenceHelper.storeEndedTime(context,new Date().toString());
                    LogItem item=PrefferenceHelper.getLogItem(context);
                    item.setTimeAccepted("none");
                    item.setCallDuration("none");
                    item.setStatus("rejected");
                    DatabaseCode databaseCode=new DatabaseCode(context);
                    databaseCode.insertData(item);
                    PrefferenceHelper.clearState(context);
                }


            }
            else if(stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){
                state = TelephonyManager.CALL_STATE_OFFHOOK;
                PrefferenceHelper.setLastState(context,"accepted");
                PrefferenceHelper.storeAcceptedTime(context,new Date().toString());
                Log.e("the phone is ","accepted now");

            }
            else if(stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)){
                state = TelephonyManager.CALL_STATE_RINGING;
            }


            onCallStateChanged(context, state, number);

    }

    //Derived classes should override these to respond to specific events of interest
    protected void onIncomingCallStarted(Context ctx, String number, Date start){}
    protected void onOutgoingCallStarted(Context ctx, String number, Date start){}
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end){}
    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end){}
    protected void onMissedCall(Context ctx, String number, Date start){}
    protected void onCallAccepted(Context ctx,String number,Date start){}

    //Deals with actual events

    //Incoming call-  goes from IDLE to RINGING when it rings, to OFFHOOK when it's answered, to IDLE when its hung up
    //Outgoing call-  goes from IDLE to OFFHOOK when it dials out, to IDLE when hung up
    public void onCallStateChanged(Context context, int state, String number) {
        if(lastState == state){
            //No change, debounce extras
            return;
        }
        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:
                isIncoming = true;
                callStartTime = new Date();
                savedNumber = number;
                Toast.makeText(context,number+callStartTime.toString(),Toast.LENGTH_SHORT);
                onIncomingCallStarted(context, number, callStartTime);
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                //Transition of ringing->offhook are pickups of incoming calls.  Nothing done on them
                PrefferenceHelper.setLastState(context,"accepted");
                PrefferenceHelper.storeAcceptedTime(context,new Date().toString());
                String time="";
                SharedPreferences sharedPreferences=context.getSharedPreferences("SUPER_SETTINGS",0);
                time=sharedPreferences.getString(ACCEPTEDTIME,"none");
                Log.e("the accept time is:",time);
                if(lastState != TelephonyManager.CALL_STATE_RINGING){
                    isIncoming = false;
                    callStartTime = new Date();
                    onOutgoingCallStarted(context, savedNumber, callStartTime);
                }

                onCallAccepted(context,number,new Date());

                break;
            case TelephonyManager.CALL_STATE_IDLE:
                //Went to idle-  this is the end of a call.  What type depends on previous state(s)
                if(lastState == TelephonyManager.CALL_STATE_RINGING){
                    //Ring but no pickup-  a miss
                    onMissedCall(context, savedNumber, callStartTime);
                }
                else if(isIncoming){
                    if((PrefferenceHelper.getLastState(context).equals("accepted"))){
                        PrefferenceHelper.setLastState(context,"accepted");
                        onIncomingCallEnded(context, savedNumber, callStartTime, new Date());
                    }else{
                        PrefferenceHelper.setLastState(context,"rejected");
                    }

                }
                else{
                    onOutgoingCallEnded(context, savedNumber, callStartTime, new Date());
                }
                break;
        }
        lastState = state;
    }
}