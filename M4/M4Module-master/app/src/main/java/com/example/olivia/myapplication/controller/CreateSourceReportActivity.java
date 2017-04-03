package com.example.olivia.myapplication.controller;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.olivia.myapplication.model.User;
import com.example.olivia.myapplication.model.WaterCondition;
import com.example.olivia.myapplication.model.waterType;
import com.google.android.gms.maps.model.LatLng;
import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;
import java.util.HashMap;

/**
 * This page allows you to create a new Source Report.
 * If you click the create button, it saves the source report
 * to the ShowSourceReport Activity. If you click cancel, the
 * report is not saved and the app returns to the
 * Main page.
 */
public class CreateSourceReportActivity extends AppCompatActivity {
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_create_source_report);
        user = (User) getIntent().getSerializableExtra("user"); //Obtaining data
        String address = "Address";
        LatLng reportLatLng = new LatLng(-33.852, 151.211);;
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
            System.out.println(e);
        }
        final String address1 = address;
        final LatLng reportLatLng1 = reportLatLng;

        //Initializes water conditions spinner
        final Spinner etSpinner = (Spinner) findViewById(R.id.etConditionSpinner_source);
        final ArrayAdapter<String> adapter2 = new ArrayAdapter(this,android.R.layout.simple_spinner_item, WaterCondition.values());
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etSpinner.setAdapter(adapter2);

        //Initializes water type spinner
        final Spinner etSpinner2 = (Spinner) findViewById(R.id.etTypeSpinner_source);
        final ArrayAdapter<String> adapter3 = new ArrayAdapter(this,android.R.layout.simple_spinner_item, waterType.values());
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etSpinner2.setAdapter(adapter3);

        final Button mapButton = (Button) findViewById(R.id.location_button_source);
        final TextView etLocation = (TextView) findViewById(R.id.addressTV_source);
        etLocation.setText(address);
        final Button registerButton = (Button) findViewById(R.id.createButton_source);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Gets information from textboxes
                final String location = address1;
                final String condition = etSpinner.getSelectedItem().toString();
                final String type = etSpinner2.getSelectedItem().toString();
                final String lat = String.valueOf(reportLatLng1.latitude);
                final String longt = String.valueOf(reportLatLng1.longitude);

                //Checks to see if there is a missing input
                if (/*time.isEmpty() || */location.isEmpty() || type.contains("SELECT") || condition.contains("SELECT") ) {
                    AlertDialog.Builder myAlert = new AlertDialog.Builder(CreateSourceReportActivity.this);
                    myAlert.setMessage("water type and water condition are required")
                            .setPositiveButton("Back", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .create();
                    myAlert.show();
                } else if (etLocation.getText().toString().equals("Address")) {
                    AlertDialog.Builder myAlert = new AlertDialog.Builder(CreateSourceReportActivity.this);
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
                    postData.put("txtCondition", condition);
                    postData.put("txtType", type);
                    postData.put("txtLat", lat);
                    postData.put("txtLong", longt);

                    AsyncResponse asyncResponse = new AsyncResponse() {
                        @Override
                        public void processFinish(String output) {
                            if(output.contains("report")) {
                                Toast.makeText(CreateSourceReportActivity.this, output, Toast.LENGTH_LONG).show();
                            }
                        }
                    };
                    PostResponseAsyncTask task = new PostResponseAsyncTask(CreateSourceReportActivity.this, postData, asyncResponse);
                    task.execute("http://szhougatech.com/createSourceReport.php");
                    //task.execute("http://192.168.2.5:81/android_connect/createSourceReport.php");
                    Intent intent = new Intent(CreateSourceReportActivity.this, MainActivity.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                    finish();
                }
            }
        });
        //cancel button that takes a user back to the welcome screen
        final Button cancelButton = (Button) findViewById(R.id.cancelButton_source);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateSourceReportActivity.this, MainActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
                finish();
            }
        });

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateSourceReportActivity.this, PickSourceReportLocationActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
                finish();
            }
        });
    }
}
