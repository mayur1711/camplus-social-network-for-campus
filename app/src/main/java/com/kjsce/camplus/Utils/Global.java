package com.kjsce.camplus.Utils;

/**
 * Created by Mayur on 01-02-2018.
 */


//class for all the global variables
public class Global {
    private static Global instance;
    private static int otpGenerated ;
    private static String emailForgotPassword, searchKeyword;

    private Global(){

    }

    public void setOtpGenerated(int otpGenerated) {
        Global.otpGenerated = otpGenerated;
    }

    public int getOtpGenerated() {
        return Global.otpGenerated;
    }

    public void setEmailForgotPassword(String emailForgotPassword) {
        Global.emailForgotPassword = emailForgotPassword;
    }

    public String getEmailForgotPassword() {
        return Global.emailForgotPassword;
    }

    public void setSearchKeyword(String searchKeyword) {
        Global.searchKeyword = searchKeyword;
    }

    public String getSearchKeyword() {
        return Global.searchKeyword;
    }

    public static synchronized Global getInstance() {
        if (instance == null) {
            instance = new Global();
        }
        return instance;
    }
}

