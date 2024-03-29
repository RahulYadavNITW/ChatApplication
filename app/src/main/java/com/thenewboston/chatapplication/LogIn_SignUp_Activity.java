package com.thenewboston.chatapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class LogIn_SignUp_Activity extends CustomActivity {

    Button SignUp;
    Button LogIn;
    String username;
    String password;
    EditText textusername;
    EditText textpassword;
    Context staticThis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in__sign_up_);
        setTouchAndClick(R.id.signupbutton);
        setTouchAndClick(R.id.loginbutton);
        SignUp = (Button) findViewById(R.id.signupbutton);
        LogIn = (Button) findViewById(R.id.loginbutton);
        textusername = (EditText) findViewById(R.id.textUsername);
        textpassword = (EditText) findViewById(R.id.textPassword);
        getSupportActionBar().hide();
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = textusername.getText().toString();
                password = textpassword.getText().toString();

                if (username.equals("") || password.equals("")) {
                    //TODO Review - app crashes , tell me why
                    Utils.showDialog(getApplicationContext(), "Fill in all the Details");
                }
                progressDialog = ProgressDialog.show(LogIn_SignUp_Activity.this, null, "Please Wait...");
                final ParseUser user = new ParseUser();
                user.setUsername(username);
                user.setPassword(password);
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        progressDialog.dismiss();
                        if (e == null) {
                            Userlist_LeftFragment.user = user;
                            Intent intent = new Intent(LogIn_SignUp_Activity.this, HomeScreen.class);
                            //TODO Review - Explain the purpose of this.
                            startActivity(intent);
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            Utils.showDialog(LogIn_SignUp_Activity.this, "Sign Up ERROR : " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                });


            }
        });

        LogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = textusername.getText().toString();
                password = textpassword.getText().toString();
                progressDialog = ProgressDialog.show(LogIn_SignUp_Activity.this, null, "Please Wait...");

                ParseUser.logInInBackground(username, password, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        progressDialog.dismiss();
                        if (user != null) {
                            Userlist_LeftFragment.user = user;
                            Intent intent = new Intent(LogIn_SignUp_Activity.this, HomeScreen.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Utils.showDialog(LogIn_SignUp_Activity.this, "Login Error : " + e.getMessage());
                            e.printStackTrace();
                        }

                    }
                });
            }
        });
    }
    ProgressDialog progressDialog;

//    //TODO Review - you have one onClick already and then another at line 50
//    @Override
//    public void onClick(View v) {
//        if (v.getId() == R.id.signupbutton) {
//            SignUp.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                @Override
//                public void onFocusChange(View v, boolean hasFocus) {
//                    if (hasFocus)
//                        SignUp.performClick();
//                }
//            });
//
//
//        } else {
//            //TODO Review - learn what each of these methods do
//            LogIn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                @Override
//                public void onFocusChange(View v, boolean hasFocus) {
//                    if (hasFocus)
//                        LogIn.performClick();
//                }
//            });
//        }
//    }


    //TODO Review - whats the use of it
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == RESULT_OK)
            finish();
    }
}
