package com.example.olivia.myapplication.model;

/**
 * Created by John on 2017-03-24.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

import com.example.olivia.myapplication.controller.R;
import com.example.olivia.myapplication.controller.ViewSourceReportActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
/**
 * Created by John on 2017-03-22.
 */

public class RetrieveSourceReportData extends Activity {
    public static ArrayList<SourceReport> reports = new ArrayList<SourceReport>();

    private String myJSON;
    private static final String TAG_RESULTS="result";
    private static final String TAG_REPORT_NUMBER = "ReportNumber";
    private static final String TAG_TIME ="TIME";
    private static final String TAG_LOCATION = "Location";
    private static final String TAG_CREATOR ="Creator";
    private static final String TAG_CONDITION ="Condition";
    private static final String TAG_TYPE = "Type";
    private static final String TAG_LAT = "Latitutde";
    private static final String TAG_LONG = "Longtitude";


    private JSONArray reportInfo = null;
    private SourceReport _report;
    private ArrayList<HashMap<String, String>> reportList;

    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_report_layout);
        reportList = new ArrayList<HashMap<String,String>>();

        getData("http://szhougatech.com/getSourceReport.php");
        //getData("http://192.168.2.5:81/android_connect/getSourceReport.php");
    }

    protected void listUsers(){
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            reportInfo = jsonObj.getJSONArray(TAG_RESULTS);

            if(!reportList.isEmpty()) {
                reportList.clear();
            }
            if(_report != null) {
                _report = null;
            }
            if(!reports.isEmpty()) {
                reports.clear();
            }
            for(int i = 0; i < reportInfo.length();i++){
                JSONObject c = reportInfo.getJSONObject(i);
                String rptNum = c.getString(TAG_REPORT_NUMBER);
                String time = c.getString(TAG_TIME);
                String loc = c.getString(TAG_LOCATION);
                String creator = c.getString(TAG_CREATOR);
                String condition = c.getString(TAG_CONDITION);
                String type = c.getString(TAG_TYPE);
                String lat = c.getString(TAG_LAT);
                String longt = c.getString(TAG_LONG);




                HashMap<String,String> report = new HashMap<String,String>();

                report.put(TAG_REPORT_NUMBER,rptNum);
                report.put(TAG_TIME,time);
                report.put(TAG_LOCATION,loc);
                report.put(TAG_CREATOR,creator);
                report.put(TAG_CONDITION,condition);
                report.put(TAG_TYPE,type);
                report.put(TAG_LAT,lat);
                report.put(TAG_LONG,longt);
                reportList.add(report);
                _report = new SourceReport(rptNum,time,loc,creator,condition,type,lat,longt);
                reports.add(_report);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        User user = (User) getIntent().getSerializableExtra("user"); //Obtaining data
        Intent intent = new Intent(getApplicationContext(),ViewSourceReportActivity.class);
        intent.putExtra("user",user);
        startActivity(intent);
        finish();
    }

    public void getData(String url){
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];

                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json = null;
                    while((json = bufferedReader.readLine())!= null){
                        sb.append(json+"\n");
                    }

                    return sb.toString().trim();

                }catch(Exception e){
                    return null;
                }
            }
            @Override
            protected void onPostExecute(String result){
                myJSON = result;
                listUsers();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }

}



