package com.example.olivia.myapplication.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.olivia.myapplication.model.RetrieveGraphData;
import com.example.olivia.myapplication.model.RetrievePurityReportData;
import com.example.olivia.myapplication.model.RetrieveSourceReportData;
import com.example.olivia.myapplication.model.User;

/**
 * @author Kyung Jun Lee
 *
 * MainActivity is the main page of the water app once the user logs in to his/her profile.
 * Depends on the current user's user type, the app shows different functionalities.
 * Modified by Kyung Jun Lee on 3/8/2017
 */

public class MainActivity extends AppCompatActivity {

    private TextView _userInfo;
    private User user;
    Button _submit, _view, _purityLevel, _viewHistory,
            _trend, _security, _signOut, _profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        try {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                user = (User) getIntent().getSerializableExtra("user"); //Obtaining data
            }
        } catch (Exception e) {
            Log.d("debug", "something went wrong");
        }
        //shows the current user's name and type of the user.
        _userInfo = (TextView) findViewById(R.id._userInfo);
        _userInfo.setText("Hello, " + user.getName() + "(" + user.getUserType() + ")");

        _submit = (Button) findViewById(R.id._submit);
        _view = (Button) findViewById(R.id._view);
        _purityLevel = (Button) findViewById(R.id._purityLevel);
        _viewHistory = (Button) findViewById(R.id._viewHistory);
        _trend = (Button) findViewById(R.id._trend);
        _security = (Button) findViewById(R.id._security);
        _profile = (Button) findViewById(R.id._profile);
        _signOut = (Button) findViewById(R.id._signOut);

        /*if the user logs in, screen displays following functionalities:
            - Submit a report on water availability
            - View available water sources
            - My Profile (Edit)
            - Sign out
        */
        if (user.getUserType().equals("user")) {
            _purityLevel.setVisibility(View.GONE);
            _viewHistory.setVisibility(View.GONE);
            _trend.setVisibility(View.GONE);
            _security.setVisibility(View.GONE);
            /*if the worker logs in, screen displays following functionalities:
               - Submit a report on water availability
               - View available water sources
               - Report on water purity levels
               - My Profile (Edit)
               - Sign out
             */
        } else if (user.getUserType().equals("worker")) {
            _viewHistory.setVisibility(View.GONE);
            _trend.setVisibility(View.GONE);
            _security.setVisibility(View.GONE);

            /*if the worker logs in, screen displays following functionalities:
               - Submit a report on water availability
               - View available water sources
               - Report on water purity levels
               - View historical reports
               - View trends of water purity
               - My Profile (Edit)
               - Sign out
             */
        } else if (user.getUserType().equals("manager")) {
            _security.setVisibility(View.GONE);
        }
         //else (admin page), it will show all the functionalities.
        _submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CreateSourceReportActivity.class);
                intent.putExtra("user",user);
                startActivity(intent);
            }
        });
        _view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RetrieveSourceReportData.class);
                intent.putExtra("user",user);
                startActivity(intent);
            }
        });

        _purityLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CreateReportActivity.class);
                intent.putExtra("user",user);
                startActivity(intent);
            }
        });

        _viewHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RetrievePurityReportData.class);
                intent.putExtra("user",user);
                startActivity(intent);
            }
        });

        _profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                intent.putExtra("user",user);
                startActivity(intent);
            }
        });
        _trend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RetrieveGraphData.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });
        _signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }
}
