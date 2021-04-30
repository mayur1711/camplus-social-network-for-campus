package com.kjsce.camplus.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.kjsce.camplus.ForgotPassword.ForgotPasswordActivity;
import com.kjsce.camplus.Home.HomeActivity;
import com.kjsce.camplus.R;
import com.kjsce.camplus.Signup.SignupActivity;
import com.kjsce.camplus.Utils.LoginInfo;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mayur on 15-12-2017.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";

    //declaring reference variables
    TextInputLayout loginUsernameWrapper;
    TextInputLayout loginPasswordWrapper;
    TextInputEditText loginUsername;
    TextInputEditText loginPassword;
    Button forgotPasswordBtn;
    Button loginBtn;
    Button noAccountYetBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: executing onCreate");

        //tells java file which xml file its working with
        setContentView(R.layout.activity_login);

        //reference to all the fields and buttons in the activity_main
        loginUsername = findViewById(R.id.username);
        loginPassword = findViewById(R.id.password);
        loginUsernameWrapper = findViewById(R.id.username_wrapper);
        loginPasswordWrapper = findViewById(R.id.password_wrapper);
        forgotPasswordBtn = findViewById(R.id.forgot_password_btn);
        loginBtn = findViewById(R.id.login_btn);
        noAccountYetBtn = findViewById(R.id.no_account_yet_btn);

        //on button Click go to onClick method of corresponding button
        forgotPasswordBtn.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
        noAccountYetBtn.setOnClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();

        //clear all text fields and errors when activity is resumed
        loginUsernameWrapper.setErrorEnabled(false);
        loginPasswordWrapper.setErrorEnabled(false);
        loginUsername.setText("");
        loginPassword.setText("");
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick: on click");
        switch (v.getId()) {
            //on 'No Account Yet' button click
            case R.id.no_account_yet_btn:

                //create intent to move back to SignupActivity
                Intent signupIntent = new Intent(LoginActivity.this, SignupActivity.class);

                //open the LoginActivity and pass the intent
                LoginActivity.this.startActivity(signupIntent);

                break;

            case R.id.login_btn:

                Log.d(TAG, "onClick: login btn click");

                //declare and define variables to get text input from all the blocks
                String username = loginUsername.getText().toString().trim();
                String password = loginPassword.getText().toString().trim();

                //if username not entered display error
                if (username.length() == 0) {
                    Log.d(TAG, "onClick: if");
                    loginUsernameWrapper.setError("Enter username");
                }

                //if password not entered display error
                else if (password.length() == 0) {
                    Log.d(TAG, "onClick: else if");
                    loginPasswordWrapper.setError("Enter password");
                } else {
                    Log.d(TAG, "onClick: else");
                    //creating a Response Listener for response from login.php
                    Response.Listener<String> responseListener = new Response.Listener<String>() {

                        @Override
                        //String 'response' - this is the response from login.php
                        public void onResponse(String response) {

                            Log.d(TAG, "onResponse: response " + response);

                            //surrounding around try catch to ensure it does not fail for response that is not
                            // in JSON format and catches an exception
                            try {
                                //convert into json Object as the response sent by login.php will be encoded
                                //in JSON string
                                JSONObject jsonResponse = new JSONObject(response.substring(response.indexOf("{"),
                                        response.lastIndexOf("}") + 1));

                                //boolean because it is either True or False
                                boolean success = jsonResponse.getBoolean("success");

                                //successful login
                                if (success) {

                                    //declare and define variables to get details from jsonResponse
                                    int userId = jsonResponse.getInt("user_id");
                                    String name = jsonResponse.getString("name");
                                    String email = jsonResponse.getString("email");
                                    String username = jsonResponse.getString("username");
                                    String contact_no = jsonResponse.getString("contact_no");
                                    String year = jsonResponse.getString("year");
                                    String department = jsonResponse.getString("department");

                                    //create intent to move back to HomeActivity
                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);

                                    LoginInfo loginInfo = LoginInfo.getInstance();
                                    loginInfo.setUserId(userId);
                                    loginInfo.setName(name);
                                    loginInfo.setEmail(email);
                                    loginInfo.setUsername(username);
                                    loginInfo.setDepartment(department);
                                    loginInfo.setYear(year);
                                    loginInfo.setContactNo(contact_no);

                                    //open the LoginActivity and pass the intent
                                    LoginActivity.this.startActivity(intent);
                                    finish();

                                }

                                //if login not successful, display error message and a retry button
                                else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                    builder.setMessage("Login failed!")
                                            .setNegativeButton("Retry", null)
                                            .create()
                                            .show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    //create loginRequest and pass the parameters
                    LoginRequest loginRequest = new LoginRequest(username, password, responseListener);

                    //create a RequestQueue 'queue' and get a new Queue from volley for LoginActivity
                    //volley holds all the request queue
                    RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);

                    //add loginRequest to the request queue
                    queue.add(loginRequest);
                }

//                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
//                LoginActivity.this.startActivity(intent);

                break;


            case R.id.forgot_password_btn:

                //create intent to move back to ForgotPasswordActivity
                Intent forgotPasswordIntent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);

                //open the LoginActivity and pass the intent
                LoginActivity.this.startActivity(forgotPasswordIntent);

                break;

        }


    }
}
