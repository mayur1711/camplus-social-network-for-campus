package com.kjsce.camplus.Utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.kjsce.camplus.Home.HomeActivity;
import com.kjsce.camplus.Interests.InterestsActivity;
import com.kjsce.camplus.Profile.ProfileActivity;
import com.kjsce.camplus.R;
import com.kjsce.camplus.Search.SearchActivity;

/**
 * Created by Mayur on 07-02-2018.
 */

public class BottomNavigationViewHelper {
    private static final String TAG = "BottomNavigationViewHel";

    public static void setupBottomNavigationView(BottomNavigationViewEx bottomNavigationViewEx) {

        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");

        bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.enableShiftingMode(false);
    }

    public static void enableNavigation(final Context context, final Activity callingActivity, BottomNavigationViewEx viewEx) {
        viewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.ic_home:
                        Intent homeIntent = new Intent(context, HomeActivity.class);//ACTIVITY_NUM = 0
                        homeIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        context.startActivity(homeIntent);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;

                    case R.id.ic_search:
                        Intent searchIntent = new Intent(context, SearchActivity.class);//ACTIVITY_NUM = 1
                        searchIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        context.startActivity(searchIntent);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;

                    case R.id.ic_interests:
                        Intent interestsIntent = new Intent(context, InterestsActivity.class);//ACTIVITY_NUM = 2
                        interestsIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        context.startActivity(interestsIntent);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;

                    case R.id.ic_profile:
                        Intent profileIntent = new Intent(context, ProfileActivity.class);//ACTIVITY_NUM = 3
                        profileIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        context.startActivity(profileIntent);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;
                }

                return false;
            }
        });

    }
}
