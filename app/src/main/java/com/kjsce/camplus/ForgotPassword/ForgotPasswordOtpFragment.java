package com.kjsce.camplus.ForgotPassword;


import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.kjsce.camplus.R;

import org.json.JSONException;
import org.json.JSONObject;


public class ForgotPasswordOtpFragment extends Fragment implements View.OnClickListener {

    //declaring variables
    TextInputLayout getOtpWrapper;
    TextInputEditText getOtp;
    Button validateOtpBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_forgot_password_otp, container, false);

        //reference to all fields and buttons in fragment_forgot_password_otp
        getOtpWrapper = view.findViewById(R.id.enter_otp_wrapper);
        getOtp = view.findViewById(R.id.enter_otp);
        validateOtpBtn = view.findViewById(R.id.validate_otp_btn);

        //display warning message
        getOtpWrapper.setError("OTP expires in 5 minutes");

        //execute OnClick method on button press
        validateOtpBtn.setOnClickListener(this);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onClick(View v) {

        //get OTP entered by user
        String enteredOtp = getOtp.getText().toString();

        //if no OTP entered display error
        if (enteredOtp.equals("")) {
            getOtp.setError("Enter 4 digit OTP");
        }

        //if OTP entered
        else {

            //convert OTP entered by user to integer type
            int otpEntered = Integer.parseInt(enteredOtp);

            //if entered OTP is not 4 digit long
            if (otpEntered < 1000 || otpEntered > 9999) {
                getOtp.setError("Enter 4 digit OTP");
            }

            //if entered OTP is 4 digit long
            else {

                //creating a Response Listener for response from forgot_password_otp.php
                com.android.volley.Response.Listener<String> responseListener = new com.android.volley.Response.Listener<String>() {
                    @Override
                    //String 'response' - this is the response from forgot_password_otp.php
                    public void onResponse(String response) {

                        //surrounding around try catch to ensure it does not fail for response that is not
                        // in JSON format and catches an exception
                        try {

                            //convert into json Object as the response sent by forgot_password_otp.php will be
                            //encoded in JSON string
                            JSONObject jsonResponse = new JSONObject(response.substring(response.indexOf("{"),
                                    response.lastIndexOf("}")+1));

                            //boolean because it is either True or False
                            boolean success = jsonResponse.getBoolean("success");

                            //if otp is valid
                            if (success) {

                                //display toast on screen
                                Toast.makeText(getActivity(), "OTP Matched", Toast.LENGTH_SHORT).show();

                                //make the 'Enter OTP' field immutable
                                getOtp.setEnabled(false);

                                //remove 'Validate OTP' button
                                validateOtpBtn.setVisibility(View.GONE);

                                //for displaying ForgotPasswordLastFragment on 'Validate OTP' button press
                                ForgotPasswordLastFragment forgotPasswordLastFragment = new ForgotPasswordLastFragment();
                                FragmentManager fragmentManager = getFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.fragment_last_container, forgotPasswordLastFragment);
                                fragmentTransaction.commit();
                            }
                            //otp invalid
                            else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPasswordOtpFragment.this.getActivity());
                                builder.setMessage("Invalid OTP")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                //create forgotPasswordOtpRequest and pass the parameters
                ForgotPasswordOtpRequest forgotPasswordOtpRequest = new ForgotPasswordOtpRequest(otpEntered, responseListener);

                //create a RequestQueue 'queue' and get a new Queue from volley for ForgotPasswordOtpFragment
                //volley holds all the request queue
                RequestQueue queue = Volley.newRequestQueue(ForgotPasswordOtpFragment.this.getActivity());

                //add forgotPasswordOtpRequest to the request queue
                queue.add(forgotPasswordOtpRequest);

            }
        }
    }
}
