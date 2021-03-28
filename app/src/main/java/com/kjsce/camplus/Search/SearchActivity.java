package com.kjsce.camplus.Search;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.kjsce.camplus.R;
import com.kjsce.camplus.Utils.BottomNavigationViewHelper;
import com.kjsce.camplus.Utils.Global;
import com.kjsce.camplus.Utils.Adapters.SectionsPagerAdapter;

import java.util.Locale;

/**
 * Created by Mayur on 27-04-2018.
 */

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = "SearchActivity";

    private RelativeLayout relativeLayout1, relativeLayout2, relativeLayout3;
    private BottomNavigationViewEx bottomNavigationViewEx;
    private FrameLayout frameLayout;
    private EditText mSearchParam;
    private SearchPostsFragment searchPostsFragment;
    private SearchProfilesFragment searchProfilesFragment;
    private Global global = Global.getInstance();
    private Context mContext = SearchActivity.this;
    private static final int ACTIVITY_NUM = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Log.d(TAG, "onCreate: started");

        relativeLayout1 = findViewById(R.id.search_relativeLayout1);
        relativeLayout2 = findViewById(R.id.search_relativeLayout2);
        relativeLayout3 = findViewById(R.id.search_relativeLayout3);
        frameLayout = findViewById(R.id.search_frameLayout);

        closeKeyboard();
        setupBottomNavigationView();
        initTextListener();
//        setupViewPager();
    }

    private void initTextListener(){
        Log.d(TAG, "initTextListener: initializing initTextListener");
        mSearchParam = findViewById(R.id.search_keyword);

        mSearchParam.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                final String text = mSearchParam.getText().toString().trim().toLowerCase(Locale.getDefault());

                if (!text.equals("")) {
                    searchForMatch(text);
                }
            }
        });
    }

    private void searchForMatch(String keyword) {
        Log.d(TAG, "searchForMatch: keyword" + keyword);

        global.setSearchKeyword(keyword);
        searchPostsFragment = new SearchPostsFragment();
        searchProfilesFragment = new SearchProfilesFragment();
//        Bundle bundle = new Bundle();
//        bundle.putString("NAME_KEY", keyword);
//        searchPostsFragment.setArguments(bundle);
//        searchProfilesFragment.setArguments(bundle);
        setupViewPager();
    }

    private void closeKeyboard(){
        if(getCurrentFocus() !=null){
            InputMethodManager imm=(InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void setupViewPager() {
        Log.d(TAG, "setupViewPager: executing setupViewPager");

        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(searchProfilesFragment);
        adapter.addFragment(searchPostsFragment);
        ViewPager viewPager = findViewById(R.id.container);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setText("Profiles");
        tabLayout.getTabAt(1).setText("Posts");
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

        relativeLayout1.setVisibility(View.GONE);
        relativeLayout2.setVisibility(View.GONE);
        relativeLayout3.setVisibility(View.GONE);
        bottomNavigationViewEx.setVisibility(View.GONE);
        frameLayout.setVisibility(View.VISIBLE);
    }

    public void showLayout() {
        Log.d(TAG, "showLayout: executing showLayout");

        relativeLayout1.setVisibility(View.VISIBLE);
        relativeLayout2.setVisibility(View.VISIBLE);
        relativeLayout3.setVisibility(View.VISIBLE);
        bottomNavigationViewEx.setVisibility(View.VISIBLE);
        frameLayout.setVisibility(View.GONE);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(frameLayout.getVisibility() == View.VISIBLE){
            showLayout();
        }
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
