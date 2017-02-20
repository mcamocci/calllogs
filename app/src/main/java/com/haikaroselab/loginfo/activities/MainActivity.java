package com.haikaroselab.loginfo.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.haikaroselab.loginfo.R;
import com.haikaroselab.loginfo.adapters.LogsItemAdapter;
import com.haikaroselab.loginfo.pojos.LogItem;
import com.haikaroselab.loginfo.services.SendingLogIntentService;
import com.haikaroselab.loginfo.toolsdb.DatabaseCode;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LogsItemAdapter adapter;
    private List<LogItem> itemList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        itemList=new DatabaseCode(getBaseContext()).getAllLogs();

        adapter=new LogsItemAdapter(getBaseContext(),itemList);
        layoutManager=new LinearLayoutManager(getBaseContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView=(RecyclerView)findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent=null;
        int id=item.getItemId();
        if(id==R.id.send_log){
            showNetDialog();
        }else if(id==android.R.id.home){
            finish();
        }else if(id==R.id.about){
            intent=new Intent(MainActivity.this,AboutActivity.class);
            startActivity(intent);
        }else if(id==R.id.delete){
            showDialog();
        }else if(id==R.id.grant){
            askForPermission(Manifest.permission.READ_PHONE_STATE,2);
        }
        return false;
    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
            }
        } else {
            Toast.makeText(this,"permission already granted!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED){
            switch (requestCode) {

                case 2:
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + "{This is a telephone number}"));
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        startActivity(callIntent);
                    }
                    break;

            }
            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    public void showDialog(){
        final AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("Delete the call logs");
        adb.setMessage("Are you sure you want to delete logs?");

        //Set the Yes/Positive and No/Negative Button text
        String yesButtonText = "Yes";
        String noButtonText = "No";
        //Define the positive button text and action on alert dialog
        adb.setPositiveButton(yesButtonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which){
                //something to be done here
                new DatabaseCode(getBaseContext()).deleteAllLogs();
                adapter=new LogsItemAdapter(getBaseContext(),new DatabaseCode(getBaseContext()).getAllLogs());
                recyclerView.setAdapter(adapter);
            }
        });

        //Define the negative button text and action on alert dialog
        adb.setNegativeButton(noButtonText, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                //something else to be also done here
            }
        });

        //Display the Alert Dialog on app interface
        adb.show();
    }

    public void showNetDialog(){
        final AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("Upload call logs");
        adb.setMessage("This option will upload the present logs to the cloud?");

        //Set the Yes/Positive and No/Negative Button text
        String yesButtonText = "Yes";
        String noButtonText = "No";
        //Define the positive button text and action on alert dialog
        adb.setPositiveButton(yesButtonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which){
                //something to be done here
                Intent intent=new Intent(getBaseContext(), SendingLogIntentService.class);
                startService(intent);

            }
        });

        //Define the negative button text and action on alert dialog
        adb.setNegativeButton(noButtonText, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                //something else to be also done here
            }
        });

        //Display the Alert Dialog on app interface
        adb.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter=new LogsItemAdapter(getBaseContext(),new DatabaseCode(getBaseContext()).getAllLogs());
        recyclerView.setAdapter(adapter);
    }
}
