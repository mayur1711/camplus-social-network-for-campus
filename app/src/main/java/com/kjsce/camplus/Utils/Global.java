package com.kjsce.camplus.Utils;

/**
 * Created by Mayur on 01-02-2018.
 */


//class for all the global variables
public class Global {
    private static Global instance;
    private static int otpGenerated;
    private static String emailForgotPassword, searchKeyword;

    private Global() {

    }

    public static synchronized Global getInstance() {
        if (instance == null) {
            instance = new Global();
        }
        return instance;
    }

    public int getOtpGenerated() {
        return Global.otpGenerated;
    }

    public void setOtpGenerated(int otpGenerated) {
        Global.otpGenerated = otpGenerated;
    }

    public String getEmailForgotPassword() {
        return Global.emailForgotPassword;
    }

    public void setEmailForgotPassword(String emailForgotPassword) {
        Global.emailForgotPassword = emailForgotPassword;
    }

    public String getSearchKeyword() {
        return Global.searchKeyword;
    }

    public void setSearchKeyword(String searchKeyword) {
        Global.searchKeyword = searchKeyword;
    }
}

