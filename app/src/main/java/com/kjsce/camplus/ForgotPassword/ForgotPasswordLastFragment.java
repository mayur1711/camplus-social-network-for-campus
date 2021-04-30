package com.kjsce.camplus.ForgotPassword;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.kjsce.camplus.Login.LoginActivity;
import com.kjsce.camplus.R;
import org.json.JSONException;
import org.json.JSONObject;


public class ForgotPasswordLastFragment extends Fragment implements View.OnClickListener {

    //declaring variables
    TextInputLayout forgotPasswordWrapper;
    TextInputEditText forgotPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_forgot_password_last, container, false);

        //reference to all fields and buttons in fragment_forgot_password_last
        forgotPasswordWrapper = (TextInputLayout) view.findViewById(R.id.set_new_password_wrapper);
        forgotPassword = (TextInputEditText) view.findViewById(R.id.set_new_password);
        Button confirmBtn = (Button) view.findViewById(R.id.confirm_btn);

        //execute OnClick method on button press
        confirmBtn.setOnClickListener(this);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onClick(View v) {

        //get password entered by the user
        String password = forgotPassword.getText().toString().trim();

        //remove error if already displayed
        forgotPasswordWrapper.setErrorEnabled(false);

        //
        if (password.length() == 0 || password.length() < 8) {
            forgotPasswordWrapper.setError("Minimum password length: 8");
        } else {

            //creating a Response Listener for response from forgot_password_changepwd.php
            com.android.volley.Response.Listener<String> responseListener = new com.android.volley.Response.Listener<String>() {

                @Override
                //String 'response' - this is the response from forgot_password_changepwd.php
                public void onResponse(String response) {

                    //surrounding around try catch to ensure it does not fail for response that is not
                    // in JSON format and catches an exception
                    try {

                        //convert into json Object as the response sent by forgot_password_changepwd.php will be
                        //encoded in JSON string
                        JSONObject jsonResponse = new JSONObject(response.substring(response.indexOf("{"),
                                response.lastIndexOf("}") + 1));

                        //boolean because it is either True or False
                        boolean success = jsonResponse.getBoolean("success");

                        //if password changed successfully
                        if (success) {

                            //display toast on screen
                            Toast.makeText(ForgotPasswordLastFragment.this.getActivity(), "Password Changed!", Toast.LENGTH_SHORT).show();

                            //create intent to move back to LoginActivity
                            Intent intent = new Intent(ForgotPasswordLastFragment.this.getActivity(), LoginActivity.class);

                            //for closing the ForgotPasswordActivity after switching to the LoginActivity
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                            //open the ForgotPasswordActivity and pass the intent
                            ForgotPasswordLastFragment.this.startActivity(intent);

                        }
                        //if password changing fails
                        else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPasswordLastFragment.this.getActivity());
                            builder.setMessage("Unsuccessful")
                                    .setNegativeButton("Retry", null)
                                    .create()
                                    .show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };

            //create forgotPasswordLastRequest and pass the parameters
            ForgotPasswordLastRequest forgotPasswordLastRequest = new ForgotPasswordLastRequest(password, responseListener);

            //create a RequestQueue 'queue' and get a new Queue from volley for ForgotPasswordLastFragment
            //volley holds all the request queue
            RequestQueue queue = Volley.newRequestQueue(ForgotPasswordLastFragment.this.getActivity());

            //add forgotPasswordLastRequest to the request queue
            queue.add(forgotPasswordLastRequest);
        }
    }
}
