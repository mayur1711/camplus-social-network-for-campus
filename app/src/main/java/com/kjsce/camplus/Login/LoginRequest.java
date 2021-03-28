package com.kjsce.camplus.Login;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mayur on 22-12-2017.
 */

//This class allows us to make a request to the login.php on the server and get a response as a string
public class LoginRequest extends StringRequest {

    private static final String TAG = "LoginRequest";

    //url of login.php on the server
    private static String LOGIN_REQUEST_URL = "https://ajjainaakash.000webhostapp.com/login.php";

    //
    private Map<String, String> params;

    //constructor of the class
    public LoginRequest(String username, String password, Response.Listener<String> listener) {


        //pass data to volley so that it allows volley to execute our request for us
        //Method.POST - send some data to login.php and it responds back with some data
        //listener - when volley finishes our request it informs the listener
        //errorListener - when error occurs volley responds via errorListener
        super(Method.POST, LOGIN_REQUEST_URL, listener, null);
        Log.d(TAG, "LoginRequest: constructor");

        //make volley pass the information to the request
        //do that by using params
        //create params - HashMap
        params = new HashMap<>();

        //put data into the HashMap
        params.put("username", username);
        params.put("password", password);
    }

    @Override
    //volley needs to access the data
    //when the LoginRequest is called volley will call the getParams() which will return params
    public Map<String, String> getParams() {
        Log.d(TAG, "getParams: executing getParams");
        return params;
    }
}