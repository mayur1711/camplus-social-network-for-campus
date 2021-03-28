package com.kjsce.camplus.Signup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mayur on 20-12-2017.
 */

//This class allows us to make a request to the signup.php on the server and get a response as a string
public class SignupRequest extends StringRequest{

    //url of signup.php on the server
    private static String SIGNUP_REQUEST_URL = "https://ajjainaakash.000webhostapp.com/signup.php";

    //
    private Map<String, String> params;

    //constructor of the class
    public SignupRequest(String name, String email, String username, String contact_no, String year,
                         String department, String password, Response.Listener<String> listener){

        //pass data to volley so that it allows volley to execute our request for us
        //Method.POST - send some data to signup.php and it responds back with some data
        //listener - when volley finishes our request it informs the listener
        //errorListener - when error occurs volley responds via errorListener
        super(Method.POST, SIGNUP_REQUEST_URL, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        //make volley pass the information to the request
        //do that by using params
        //create params - HashMap
        params = new HashMap<>();

        //put data into the HashMap
        params.put("name", name);
        params.put("email", email);
        params.put("username", username);
        params.put("contact_no", contact_no);
        params.put("year", year);
        params.put("department", department);
        params.put("password", password);
//        params.put("gender", gender);
    }

    @Override
    //volley needs to access the data
    //when the SignupRequest is called volley will call the getParams() which will return params
    public Map<String, String> getParams() {
        return params;
    }
}
