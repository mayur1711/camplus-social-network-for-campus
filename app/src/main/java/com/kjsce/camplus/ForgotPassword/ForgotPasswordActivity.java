package com.kjsce.camplus.ForgotPassword;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.kjsce.camplus.R;
import com.kjsce.camplus.Utils.Global;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mayur on 16-12-2017.
 */

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    //declaring variables
    TextInputLayout forgotEmailWrapper;
    TextInputEditText forgotEmail;
    Button sendOtpBtn;
    int otpGenerated;
    Global global = Global.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //tells java file which xml file its working with
        setContentView(R.layout.activity_forgot_password);

        //reference to all fields and buttons in activity_forgot_password
        forgotEmailWrapper = findViewById(R.id.forgot_email_wrapper);
        forgotEmail = findViewById(R.id.forgot_email);
        sendOtpBtn = findViewById(R.id.send_otp_btn);

        //execute OnClick method on button press
        sendOtpBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        //getting email address of user to send the OTP
        final String emailForgotPassword = forgotEmail.getText().toString().trim();

        //store emailForgotPassword in the Global class to access in any other class
        global.setEmailForgotPassword(emailForgotPassword);

        //remove error if already displayed
        forgotEmailWrapper.setErrorEnabled(false);

        //If Email Id is invalid display error on button press
        if (!emailForgotPassword.matches("[a-zA-Z0-9._-]+@somaiya+[.]+edu") || emailForgotPassword.length() == 0) {
            forgotEmailWrapper.setError("Enter Somaiya Email Id");
        } else {

            //creating a Response Listener for response from forgot_password_email.php
            Response.Listener<String> responseListener = new Response.Listener<String>() {

                @Override
                //String 'response' - this is the response from forgot_password.php
                public void onResponse(String response) {

                    //surrounding around try catch to ensure it does not fail for response that is not
                    // in JSON format and catches an exception
                    try {

                        //convert into json Object as the response sent by forgot_password.php will be
                        //encoded in JSON string
                        JSONObject jsonResponse = new JSONObject(response.substring(response.indexOf("{"),
                                response.lastIndexOf("}") + 1));

                        //boolean because it is either True or False
                        boolean success = jsonResponse.getBoolean("success");

                        //if entered email id exists in the database
                        if (success) {

                            //make the 'Enter Somaiya Email ID' field immutable
                            forgotEmail.setEnabled(false);

                            //remove 'Send OTP' button
                            sendOtpBtn.setVisibility(View.GONE);

                            //get otp generated in the database via jsonResponse
                            otpGenerated = jsonResponse.getInt("otpGenerated");

                            //store otpGenerated in the Global class to access in any other class
                            global.setOtpGenerated(otpGenerated);

                            //creating ForgotPasswordSendMail object
                            ForgotPasswordSendMail forgotPasswordSendMail = new
                                    ForgotPasswordSendMail(ForgotPasswordActivity.this, emailForgotPassword);

                            //executing ForgotPasswordSendMail to send email
                            forgotPasswordSendMail.execute();

                            //for displaying ForgotPasswordOtpFragment on 'Send OTP' button press
                            ForgotPasswordOtpFragment forgotPasswordOtpFragment = new ForgotPasswordOtpFragment();
                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.fragment_otp_container, forgotPasswordOtpFragment);
                            //add fragment to BackStack
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }

                        //if entered email id does not exist in the database
                        else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPasswordActivity.this);
                            builder.setMessage("Email id is not registered!")
                                    .setNegativeButton("Edit", null)
                                    .create()
                                    .show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };

            //create forgotPasswordActivityRequest and pass the parameters
            ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest(emailForgotPassword, responseListener);

            //create a RequestQueue 'queue' and get a new Queue from volley for ForgotPasswordActivity
            //volley holds all the request queue
            RequestQueue queue = Volley.newRequestQueue(ForgotPasswordActivity.this);

            //add forgotPasswordActivityRequest to the request queue
            queue.add(forgotPasswordRequest);

        }
    }

    @Override
    //class for defining the action of back button press
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
        } else {
            forgotEmail.setEnabled(true);
            sendOtpBtn.setVisibility(View.VISIBLE);
            getFragmentManager().popBackStack();

        }
    }
}
