package com.example.olivia.myapplication.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.olivia.myapplication.model.Report;
import com.example.olivia.myapplication.model.User;

import static com.example.olivia.myapplication.model.RetrievePurityReportData.reports;

/**
 * This is the page that shows a list of reports. You
 * Have the option to either create reports or show
 * details for individual reports.
 */
public class ViewReportActivity extends AppCompatActivity {
    private Button locationButton, cancelButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_report_layout);
        //Get current user
        final User user = (User) getIntent().getSerializableExtra("user"); //Obtaining data
        //Initializes buttons on page
        locationButton = (Button) findViewById(R.id.location);
        cancelButton = (Button) findViewById(R.id.cancel_report);

        //The reports need to be added to an array to be shown
        //final List<Report> reports = manager.getList();

        //Sets up list of reports
        ListAdapter adapter = new ArrayAdapter<Report>(this, android.R.layout.simple_list_item_1, reports);
        final ListView reportList = (ListView) findViewById(R.id.report_list);
        reportList.setAdapter(adapter);
        reportList.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(ViewReportActivity.this, ShowReportActivity.class);

                        //store selected report's information into local variables
                        Report report = (Report) parent.getItemAtPosition(position);
                        String time = report.getTime();
                        int repNo = report.getReportNumber();
                        String WorkerName =report.getCreator();
                        String loc = report.getLocation();
                        String condition = report.getCondition();
                        double virusPPM = report.getVirusPPM();
                        double contamPPM = report.getCombinationPPM();

                        //Pass all the selected report's information to the ShowReportActivity
                        intent.putExtra("time",time);
                        intent.putExtra("repNo",repNo);
                        intent.putExtra("WorkerName",WorkerName);
                        intent.putExtra("loc",loc);
                        intent.putExtra("condition",condition);
                        intent.putExtra("virus",virusPPM);
                        intent.putExtra("contam",contamPPM);
                        intent.putExtra("user",user);
                        startActivity(intent);
                        finish();
                    }
                }
        );
        //Create button goes to create report page
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewReportActivity.this, ViewPurityReportsLocationActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });
        //Cancel button returns to Main Screen Activity
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewReportActivity.this, MainActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
                finish();
            }
        });
    }
}