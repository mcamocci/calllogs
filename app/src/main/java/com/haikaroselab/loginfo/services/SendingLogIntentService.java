package com.haikaroselab.loginfo.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.haikaroselab.loginfo.pojos.LogItem;
import com.haikaroselab.loginfo.toolsdb.CommonInformation;
import com.haikaroselab.loginfo.toolsdb.DatabaseCode;
import com.haikaroselab.loginfo.toolsdb.PrefferenceHelper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;


import cz.msebera.android.httpclient.Header;


public class SendingLogIntentService extends IntentService {

   public SendingLogIntentService(){
      super("SendingLogIntentService");
   }

    @Override
    protected void onHandleIntent(Intent intent) {

        for(LogItem item: new DatabaseCode(getBaseContext()).getAllLogs()){
            doTask(CommonInformation.CATEGORY_LIST,item);
            /*Log.e("ringing time",item.getTimeCalled());
            Log.e("time accepted",item.getTimeAccepted());
            Log.e("time rejected",item.getTimeEnded());
            Log.e("time ended",item.getTimeEnded());
            Log.e("call status",item.getStatus());*/

        }

    }

    public void doTask(String url, final LogItem item){

        AsyncHttpClient client=new AsyncHttpClient();
        RequestParams params=new RequestParams();
        params.put(PrefferenceHelper.PHONE,item.getPhoneNumber());
        params.put(PrefferenceHelper.RINGTIME,item.getTimeCalled());
        params.put(PrefferenceHelper.ACCEPTEDTIME,item.getTimeAccepted());
        params.put(PrefferenceHelper.ENDEDTIME,item.getTimeEnded());
        //params.put(PrefferenceHelper.CALL_DURATION,item.getCallDuration());
        params.put(PrefferenceHelper.LAST_STATE,item.getStatus());


        client.get(getBaseContext(), url, params, new TextHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
                Log.e("net start","started");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("start","failed"+responseString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                new DatabaseCode(getBaseContext()).deleteAllLogsWhereId(item.getId());
                Log.e("the net","went fine");
            }
        });
    }

}
