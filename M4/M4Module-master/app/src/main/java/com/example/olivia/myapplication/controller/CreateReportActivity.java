package com.example.olivia.myapplication.controller;

import android.content.DialogInterface;
import android.content.Intent;
import com.google.android.gms.maps.model.LatLng;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.HashMap;

import com.example.olivia.myapplication.model.User;
import com.example.olivia.myapplication.model.waterQuality;
import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

/**
 * This page allows you to create a new Purity Report.
 * If you click the create button, it saves the report
 * to the Report Activity. If you click cancel, the
 * report is not saved and the app returns to the
 * Report Activity.
 */
public class CreateReportActivity extends AppCompatActivity {
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_create_report);

        String address = "Address";
        LatLng reportLatLng = new LatLng(-33.852, 151.211);

        // checks to see if location info has been passed in from update
        // if no location info has been passed in address & updateLatLng are null
        try {
            Bundle extras = getIntent().getExtras();

            String ifNull= extras.getString("address");
            if (ifNull.length() != 0) {
                address = ifNull;
            }
            Double latitude = extras.getDouble("latitude");
            Double longitude = extras.getDouble("longitude");
            reportLatLng = new LatLng(latitude, longitude);

        } catch (Exception e) {
            Log.e("error",e.toString());
        }
        final String address1 = address;
        final LatLng reportLatLng1 = reportLatLng;


        user = (User) getIntent().getSerializableExtra("user"); //Obtaining data

        //Initializes water conditions spinner
        final Spinner etSpinner = (Spinner) findViewById(R.id.etConditionSpinner);
        final ArrayAdapter<String> adapter2 = new ArrayAdapter(this,android.R.layout.simple_spinner_item, waterQuality.values());
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etSpinner.setAdapter(adapter2);

        final Button mapButton = (Button) findViewById(R.id.location_button);
        //final EditText etTime = (EditText) findViewById(R.id.etTime);
        final TextView etLocation = (TextView) findViewById(R.id.addressTV);
        etLocation.setText(address);
        final EditText etVirusPPM = (EditText) findViewById(R.id.etVirusPPM);
        final EditText etContaminatePPM = (EditText) findViewById(R.id.etContaminatePPM);
        final Button registerButton = (Button) findViewById(R.id.createButton);
        final Button existedLocation = (Button) findViewById(R.id._existedPurity);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Gets information from textboxes
                final String location = address1;
                final String virusPPM = etVirusPPM.getText().toString();
                final String contaminatePPM = etContaminatePPM.getText().toString();
                final String condition = etSpinner.getSelectedItem().toString();
                final String lat = String.valueOf(reportLatLng1.latitude);
                final String longt = String.valueOf(reportLatLng1.longitude);



                //Checks to see if there is a missing input
                if (location.isEmpty() ||virusPPM.isEmpty() || contaminatePPM.isEmpty() ) {
                    AlertDialog.Builder myAlert = new AlertDialog.Builder(CreateReportActivity.this);
                    myAlert.setMessage("Time,location,virusPPM and comninationPPM required")
                            .setPositiveButton("Back", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .create();
                    myAlert.show();
                }else if (etLocation.getText().toString().equals("Address")) {
                    AlertDialog.Builder myAlert = new AlertDialog.Builder(CreateReportActivity.this);
                    myAlert.setMessage("Click LOCATION button and set a location")
                            .setPositiveButton("Back", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .create();
                    myAlert.show();
                }
                else {
                    HashMap postData = new HashMap();
                    postData.put("txtLocation", location);
                    postData.put("txtCreator", user.getName());
                    postData.put("txtQuality", condition);
                    postData.put("txtVirusPPM", virusPPM);
                    postData.put("txtContaminatePPM", contaminatePPM);
                    postData.put("txtLat", lat);
                    postData.put("txtLong", longt);

                    AsyncResponse asyncResponse = new AsyncResponse() {
                        @Override
                        public void processFinish(String output) {
                            if(output.contains("report")) {
                                Toast.makeText(CreateReportActivity.this, output, Toast.LENGTH_LONG).show();
                            }
                        }
                    };
                    PostResponseAsyncTask task = new PostResponseAsyncTask(CreateReportActivity.this, postData, asyncResponse);
                    //task.execute("http://192.168.2.5:81/android_connect/createPurityReport.php");
                    task.execute("http://szhougatech.com/createPurityReport.php");
                    Intent intent = new Intent(CreateReportActivity.this, MainActivity.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                    finish();

                }
            }
        });
        //cancel button that takes a user back to the welcome screen
        final Button cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateReportActivity.this, MainActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
                finish();
            }
        });

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateReportActivity.this, PickPurityReportsLocationActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
                finish();
            }
        });
        existedLocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(CreateReportActivity.this, ExistedLocationActivity.class);
                        intent.putExtra("user", user);
                        startActivity(intent);
                finish();
            }
        });

    }
}