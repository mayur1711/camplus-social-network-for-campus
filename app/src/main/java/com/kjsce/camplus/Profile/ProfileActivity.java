package com.kjsce.camplus.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;
import com.kjsce.camplus.R;
import com.kjsce.camplus.Utils.ViewProfileFragment;

/**
 * Created by Mayur on 07-02-2018.
 */

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";
    private FrameLayout frameLayout, frameLayout1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Log.d(TAG, "onCreate: started");

        frameLayout = findViewById(R.id.profile_frameLayout);
        frameLayout1 = findViewById(R.id.profile_frameLayout1);

        init();
    }

    private void init() {
        Log.d(TAG, "init: inflating Profile Fragment");

        Intent intent = getIntent();
        if (intent.hasExtra("CallingActivity")) {
            Log.d(TAG, "init: searching for user id attached with intent");

            if (intent.hasExtra("UserId")) {
                Log.d(TAG, "init: inflating view profile");

                ViewProfileFragment viewProfileFragment = new ViewProfileFragment();
                Bundle args = new Bundle();
                args.putInt("UserId", intent.getIntExtra("UserId", 0));
                viewProfileFragment.setArguments(args);

                FragmentTransaction fragmentTransaction = ProfileActivity.this.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.profile_frameLayout, viewProfileFragment);
                fragmentTransaction.commit();
            } else {
                Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.d(TAG, "init: inflating profile");

            ProfileFragment profileFragment = new ProfileFragment();
            FragmentTransaction fragmentTransaction = ProfileActivity.this.getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.profile_frameLayout, profileFragment);
            fragmentTransaction.commit();
        }
    }

    public void hideLayout() {
        Log.d(TAG, "hideLayout: executing hideLayout");

        frameLayout.setVisibility(View.GONE);
        frameLayout1.setVisibility(View.VISIBLE);
    }

    public void showLayout() {
        Log.d(TAG, "showLayout: executing showLayout");

        frameLayout.setVisibility(View.VISIBLE);
        frameLayout1.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: resume profile activity");

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d(TAG, "onBackPressed: back press profile activity");

        if (frameLayout1.getVisibility() == View.VISIBLE) {
            showLayout();
        }
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}