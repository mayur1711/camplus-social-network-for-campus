package com.kjsce.camplus.Home;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.kjsce.camplus.CreatePost.CreatePostActivity;
import com.kjsce.camplus.R;
import com.kjsce.camplus.Utils.BottomNavigationViewHelper;
import com.kjsce.camplus.Utils.Adapters.SectionsPagerAdapter;

/**
 * Created by Mayur on 07-02-2018.
 */

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "HomeActivity";

    private RelativeLayout relativeLayout2, relativeLayout3;
    private BottomNavigationViewEx bottomNavigationViewEx;
    private FrameLayout frameLayout;
    private ImageButton createPostBtn;
    private Context mContext = HomeActivity.this;
    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;
    private static final int ACTIVITY_NUM = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d(TAG, "onCreate: executing onCreate");

        relativeLayout2 = findViewById(R.id.home_relativeLayout2);
        relativeLayout3 = findViewById(R.id.home_relativeLayout3);
        frameLayout = findViewById(R.id.home_frameLayout);
        createPostBtn = findViewById(R.id.home_createPost_btn);

        setupBottomNavigationView();
        setupViewPager();
        createPostBtn.setOnClickListener(this);
    }

    //Responsible for adding the 2 tabs: Feed and Updates
    private void setupViewPager() {
        Log.d(TAG, "setupViewPager: setting ViewPager");

        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FeedFragment());
        adapter.addFragment(new UpdatesFragment());

        ViewPager viewPager = findViewById(R.id.container);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText("Feed");
        tabLayout.getTabAt(1).setText("Updates");

    }

    private void setupBottomNavigationView() {
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");

        bottomNavigationViewEx = findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, this, bottomNavigationViewEx);

        Menu menu = bottomNavigationViewEx.getMenu();

        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
        menuItem.setEnabled(false);
    }

    public void hideLayout() {
        Log.d(TAG, "hideLayout: executing hideLayout");

        relativeLayout2.setVisibility(View.GONE);
        relativeLayout3.setVisibility(View.GONE);
        bottomNavigationViewEx.setVisibility(View.GONE);
        frameLayout.setVisibility(View.VISIBLE);
    }

    public void showLayout() {
        Log.d(TAG, "showLayout: executing showLayout");

        relativeLayout2.setVisibility(View.VISIBLE);
        relativeLayout3.setVisibility(View.VISIBLE);
        bottomNavigationViewEx.setVisibility(View.VISIBLE);
        frameLayout.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick: on createPostBtn click");

        Intent intent = new Intent(HomeActivity.this, CreatePostActivity.class);
        HomeActivity.this.startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: resuming");

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d(TAG, "onBackPressed: back button press");

        createPostBtn.setVisibility(View.VISIBLE);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        if(frameLayout.getVisibility() == View.VISIBLE){
            showLayout();
        }
        /*else if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis())
        {
            super.onBackPressed();
        }
        else {
            Toast.makeText(getBaseContext(), "Tap back button in order to exit", Toast.LENGTH_SHORT).show();
        }

        mBackPressed = System.currentTimeMillis();*/

    }


}
