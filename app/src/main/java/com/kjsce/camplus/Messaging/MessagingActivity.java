package com.kjsce.camplus.Messaging;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.kjsce.camplus.R;
import com.kjsce.camplus.Utils.LoginInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mayur on 07-02-2018.
 */

public class MessagingActivity extends AppCompatActivity {

    private static final String TAG = "MessagingActivity";

    LinearLayout linearLayout;
    RelativeLayout relativeLayout;
    ImageView sendButton;
    EditText messageContent;
    ScrollView scrollView;
    Firebase reference1, reference2;
    private TextView username;
    private LoginInfo loginInfo;
    private int senderUserId, receiverUserId;
    private String receiverUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: on create messaging activity");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        loginInfo = LoginInfo.getInstance();
        senderUserId = loginInfo.getUserId();
        final String fromUserId = Integer.toString(senderUserId);

        receiverUserId = getIntent().getIntExtra("ReceiverUserId", 0);
        receiverUsername = getIntent().getStringExtra("ReceiverUsername");
        final String toUserId = Integer.toString(receiverUserId);

        linearLayout = findViewById(R.id.message_linearLayout);
        relativeLayout = findViewById(R.id.message_relativeLayout1);
        sendButton = findViewById(R.id.message_send_btn);
        messageContent = findViewById(R.id.message_content);
        scrollView = findViewById(R.id.message_scrollView);
        username = findViewById(R.id.message_username);

        username.setText(receiverUsername);


        Firebase.setAndroidContext(this);
        reference1 = new Firebase("https://crafty-ring-202310.firebaseio.com/messages/" + fromUserId + "_" + toUserId);
        reference2 = new Firebase("https://crafty-ring-202310.firebaseio.com/messages/" + toUserId + "_" + fromUserId);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on send button");

                String messageText = messageContent.getText().toString();

                if (!messageText.equals("")) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", messageText);
                    map.put("user", fromUserId);
                    reference1.push().setValue(map);
                    reference2.push().setValue(map);
                    messageContent.setText("");
                }
            }
        });

        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String userName = map.get("user").toString();

                if (userName.equals(fromUserId)) {
                    addMessageBox("You:\n" + message, 1);
                } else {
                    addMessageBox(receiverUsername + ":\n" + message, 2);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void addMessageBox(String message, int type) {
        TextView textView = new TextView(MessagingActivity.this);
        textView.setText(message);

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.weight = 1.0f;

        if (type == 1) {
            lp2.gravity = Gravity.LEFT;
            textView.setBackgroundResource(R.drawable.bubble_in);
        } else {
            lp2.gravity = Gravity.RIGHT;
            textView.setBackgroundResource(R.drawable.bubble_out);
        }
        textView.setLayoutParams(lp2);
        linearLayout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }
}
