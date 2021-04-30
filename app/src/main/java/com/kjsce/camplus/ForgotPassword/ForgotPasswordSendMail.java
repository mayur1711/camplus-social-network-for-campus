package com.kjsce.camplus.ForgotPassword;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import com.kjsce.camplus.Utils.Global;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by Priyanshu Diwan on 30-01-2018.
 */

//Class is extending AsyncTask because this class is going to perform a networking operation
public class ForgotPasswordSendMail extends AsyncTask<Void, Void, Void> {

    //declaring variables
    private final Context context;
    private final String email;
    private final String message;
    private final String subject;
    private final String adminEMAIL;
    private final String adminPASSWORD;

    //ProgressDialog to show while sending email
    private ProgressDialog progressDialog;

    //class Constructor
    public ForgotPasswordSendMail(Context context, String email) {

        //initializing variables
        this.context = context;
        this.email = email;

        //admin credentials
        adminEMAIL = "testcamplus@gmail.com";
        adminPASSWORD = "testcamplus2018";

        //getting value of otpGenerated from the Global class
        Global global = Global.getInstance();
        int otpGenerated = global.getOtpGenerated();

        //subject and message to be sent in the mail
        subject = "Camplus Password Reset OTP";
        message = "OTP for resetting password is: " + Integer.toString(otpGenerated);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        //showing progress dialog while sending email
        progressDialog = ProgressDialog.show(context, "Sending mail", "Please wait...",
                false, false);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        //dismissing the progress dialog
        progressDialog.dismiss();

        //showing a success message
        Toast.makeText(context, "OTP Sent", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Void doInBackground(Void... params) {

        //creating properties
        Properties props = new Properties();

        //configuring properties for gmail
        //if you are not using gmail you may need to change the values
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        //creating a new session
        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {

            //authenticating the password
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(adminEMAIL, adminPASSWORD);
            }
        });

        try {
            //creating MimeMessage object
            MimeMessage mm = new MimeMessage(session);

            //setting sender address
            mm.setFrom(new InternetAddress(adminEMAIL));

            //adding receiver
            mm.addRecipient(Message.RecipientType.TO, new InternetAddress(email));

            //adding subject
            mm.setSubject(subject);

            //adding message
            mm.setText(message);

            //sending email
            Transport.send(mm);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }
}