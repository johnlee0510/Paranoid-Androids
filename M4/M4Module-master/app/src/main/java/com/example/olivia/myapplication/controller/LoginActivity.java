package com.example.olivia.myapplication.controller;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.olivia.myapplication.model.User;
import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import java.util.HashMap;

import static com.example.olivia.myapplication.model.RetrieveUserData.users;

/**
 * @author KyungJun Lee
 * A login screen that offers login via username and password
 *
 * Modified by Kyung Jun Lee on 3/18/2017
 */
public class LoginActivity extends AppCompatActivity {
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private User currentUser;
    // UI references.
    private EditText _username, _password;
    private Button _signOn, _cancel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.

        _username = (EditText) findViewById(R.id.user);
        _password = (EditText) findViewById(R.id.login_password);
        _signOn = (Button) findViewById(R.id.sign_in_button);
        _cancel = (Button) findViewById(R.id.login_cancel_button);

        _signOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap postData = new HashMap();
                postData.put("txtUsername", _username.getText().toString());
                postData.put("txtPassword", _password.getText().toString());

                AsyncResponse asyncResponse = new AsyncResponse() {
                    @Override
                    public void processFinish(String output) {
                        if (output.contains("success")) {
                            for(int i = 0; i < users.size(); i++) {
                                if (users.get(i).getId().equals(_username.getText().toString())) {
                                    currentUser = users.get(i);
                                    User.setCurrentUser(currentUser);
                                }
                            }
                            Toast.makeText(LoginActivity.this, output, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("user", currentUser);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, output, Toast.LENGTH_LONG).show();
                        }
                    }
                };
                PostResponseAsyncTask task = new PostResponseAsyncTask(LoginActivity.this, postData, asyncResponse);
                task.execute("http://szhougatech.com/login.php");
            }
        });

        _cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),WelcomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

//    /**
//     * Written by Rayna
//     * Check if the password is valid: The password has to
//     * 1, length is from 8 to 14 characters
//     * 2, at least 1 digit
//     * 3, at least 1 Uppercase Letter
//     * 4, at least 1 Lowercase Letter
//     * @param password the password String that we wanna check
//     * @return boolean boolean value of if the password is valid
//     */
//    private boolean isPasswordValid(String password) {
//        String regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,14}$";
//        return password.matches(regexp);
    }
            private boolean isPasswordValid(String password) {


                String regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,14}$";
                return password.matches(regexp);
            }
            /**
             * Shows the progress UI and hides the login form.
             */

        }

