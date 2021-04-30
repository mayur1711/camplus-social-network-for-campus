package com.kjsce.camplus.Signup;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.kjsce.camplus.Login.LoginActivity;
import com.kjsce.camplus.R;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mayur on 15-12-2017.
 */

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    //declaring reference variables
    TextInputEditText signupName;
    TextInputEditText signupEmail;
    TextInputEditText signupUsername;
    TextInputEditText signupContactNo;
    AutoCompleteTextView signupYear;
    AutoCompleteTextView signupDepartment;
    TextInputEditText signupPassword;
    Button signupBtn;

    //declare and define string arrays
    String[] yearArray = new String[]{"FE", "SE", "TE", "BE"};
    String[] departmentArray = new String[]{"CS", "MECH", "EXTC", "IT", "ETRX"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //tells java file which xml file its working with
        setContentView(R.layout.activity_signup);

        //reference to all the fields and buttons in the activity_signup
        signupName = findViewById(R.id.name);
        signupEmail = findViewById(R.id.email);
        signupUsername = findViewById(R.id.signup_username);
        signupContactNo = findViewById(R.id.contact_no);
        signupYear = findViewById(R.id.year);
        signupDepartment = findViewById(R.id.department);
        signupPassword = findViewById(R.id.signup_password);
        signupBtn = findViewById(R.id.signup_btn);


        //initialize ArrayAdapters
        //ArrayAdapter converts array items into view(dropdown list)
        ArrayAdapter<String> adapterYear = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, yearArray);

        ArrayAdapter<String> adapterDepartment = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, departmentArray);

        //connect adapters to the fields in layout
        signupYear.setAdapter(adapterYear);

        signupDepartment.setAdapter(adapterDepartment);

        //on clicking the fields in layout dropdown list is displayed
        signupYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signupYear.showDropDown();
            }
        });
        signupDepartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signupDepartment.showDropDown();
            }
        });

        //monitors each word entered by user
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                //get email entered by user word by word in textEntered
                Editable textEntered = signupEmail.getText();

                //place the text before '@' in email id into the username block
                signupUsername.setText(textEntered.toString().split("@")[0]);

                //check if the domain name of email id is '@somaiya.edu'
                // if not display Error message
                if (!textEntered.toString().matches("[a-zA-Z0-9._-]+@somaiya+[.]+edu") && s.length() > 0) {
                    signupEmail.setError("Enter valid Somaiya Email Id!");
                }
            }
        };

        //use textWatcher to monitor input text for the Email block
        signupEmail.addTextChangedListener(textWatcher);

        //on Submit button click go to method OnClick()
        signupBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        //declare and define variables to get text input from all the blocks
        String name = signupName.getText().toString().trim();
        String email = signupEmail.getText().toString().trim();
        String username = email.split("@")[0];
        String contact_no = signupContactNo.getText().toString().trim();
        String year = signupYear.getText().toString();
        String department = signupDepartment.getText().toString();
        String password = signupPassword.getText().toString().trim();


        //If Name is not entered create toast and display on button press
        if (name.length() == 0) {
            Toast.makeText(this, "Enter Name", Toast.LENGTH_SHORT).show();
        }

        //If Email Id is invalid create toast and display on button press
        else if (signupEmail.getError() != null || email.length() == 0) {
            Toast.makeText(this, "Enter valid Email Id", Toast.LENGTH_SHORT).show();
        }

        //If Contact no. fails minimum 7 digit and exceeds 10 digit count create toast and display on button press
        else if (contact_no.length() != 0 && (contact_no.length() < 7 || contact_no.length() > 10)) {
            Toast.makeText(this, "Enter 8 or 10 digit Contact No", Toast.LENGTH_SHORT).show();
        }

        //If no password is entered create toast and display on button press
        else if (password.length() < 6) {
            Toast.makeText(this, "Set Password with a strength of minimum 8 characters", Toast.LENGTH_SHORT).show();
        }

        //If nothing wrong, allow button press and perform further operations
        else {

            //creating a Response Listener for response from signup.php
            Response.Listener<String> responseListener = new Response.Listener<String>() {

                @Override
                //String 'response' - this is the response from signup.php
                public void onResponse(String response) {

                    //surrounding around try catch to ensure it does not fail for response that is not
                    // in JSON format and catches an exception
                    try {

                        //convert into json Object as the response sent by signup.php will be encoded
                        //in JSON string
                        JSONObject jsonResponse = new JSONObject(response.substring(response.indexOf("{"),
                                response.lastIndexOf("}") + 1));

                        //boolean because it is either True or False
                        boolean success = jsonResponse.getBoolean("success");

                        //successful registration
                        if (success) {

                            //display a short toast on screen
                            Toast.makeText(SignupActivity.this, "Registration Successful!",
                                    Toast.LENGTH_SHORT).show();

                            //create intent to move back to LoginActivity
                            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);

                            //for closing SignupActivity after switching to the LoginActivity
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                            //open the SignupActivity and pass the intent
                            SignupActivity.this.startActivity(intent);
                        }

                        //if registration not successful, display error message and a retry button
                        else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                            builder.setMessage("Email Id already registered!")
                                    .setNegativeButton("Retry", null)
                                    .create()
                                    .show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };

            //create signupRequest and pass the parameters
            SignupRequest signupRequest = new SignupRequest(name, email, username, contact_no,
                    year, department, password, responseListener);

            //create a RequestQueue 'queue' and get a new Queue from volley for SignupActivity
            //volley holds all the request queue
            RequestQueue queue = Volley.newRequestQueue(SignupActivity.this);

            //add signupRequest to the request queue
            queue.add(signupRequest);

        }
    }
}