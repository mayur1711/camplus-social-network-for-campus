package com.kjsce.camplus.ForgotPassword;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.kjsce.camplus.Utils.Global;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mayur on 04-02-2018.
 */

//This class allows us to make a request to the forgot_password_changepwd.php on the server and get a response as a string
public class ForgotPasswordLastRequest extends StringRequest {

    //url of forgot_password_changepwd.php on the server
    private static String FORGOT_PASSWORD_LAST_REQUEST_URL = "https://ajjainaakash.000webhostapp.com/forgot_password_changepwd.php";

    //
    private Map<String,String> params;

    //constructor of the class
    public ForgotPasswordLastRequest(String password, Response.Listener<String> listener) {

        //pass data to volley so that it allows volley to execute our request for us
        //Method.POST - send some data to forgot_password_changepwd.php and it responds back with some data
        //listener - when volley finishes our request it informs the listener
        //errorListener - when error occurs volley responds via errorListener
        super(Method.POST, FORGOT_PASSWORD_LAST_REQUEST_URL, listener, null);

        //make volley pass the information to the request
        //do that by using params
        //create params - HashMap
        params = new HashMap<>();

        //instantiating class Global
        Global global = Global.getInstance();

        //put data into the HashMap
        params.put("email", global.getEmailForgotPassword());
        params.put("password", password);

    }

    //volley needs to access the data
    //when the ForgotPasswordLastRequest is called volley will call the getParams() which will return params
    public Map<String, String> getParams() {
        return params;
    }
}
