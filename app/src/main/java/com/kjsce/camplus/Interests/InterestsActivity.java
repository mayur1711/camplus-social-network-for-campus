package com.kjsce.camplus.Interests;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.kjsce.camplus.CreatePost.TagsFragment;
import com.kjsce.camplus.Home.Post;
import com.kjsce.camplus.R;
import com.kjsce.camplus.Utils.BottomNavigationViewHelper;
import com.kjsce.camplus.Utils.Adapters.PostAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by Mayur on 07-02-2018.
 */

public class InterestsActivity extends AppCompatActivity {

    private static final String TAG = "InterestsActivity";
    private Context mContext = InterestsActivity.this;
    private static final int ACTIVITY_NUM = 2;
    private FrameLayout frameLayout;
    private RelativeLayout relativeLayout;
    private BottomNavigationViewEx bottomNavigationViewEx;
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> postList;
    private ProgressBar progressBar;
    private RequestQueue requestQueue;
    private String tagIdString = "";
    private List<Integer> tagIds = new ArrayList<Integer>();
    private static String VIEW_INTERESTS_POSTS_URL = "https://ajjainaakash.000webhostapp.com/view_interests_posts.php";


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interests);
        Log.d(TAG, "onCreate: started");

        frameLayout = findViewById(R.id.interests_frameLayout);
        relativeLayout = findViewById(R.id.interests_relativeLayout3);

        setupBottomNavigationView();

        postList = new ArrayList<>();

//        progressBar = view.findViewById(R.id.updates_progressBar);
//        progressBar.setVisibility(View.GONE);

        recyclerView = findViewById(R.id.interests_recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        requestQueue = Volley.newRequestQueue(this);

        postAdapter = new PostAdapter(this, postList);
        recyclerView.setAdapter(postAdapter);

        TagsFragment tagsFragment = new TagsFragment();
        FragmentTransaction fragmentTransaction = InterestsActivity.this
                .getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.interests_frameLayout, tagsFragment);
        fragmentTransaction.addToBackStack("TagsFragment");
        fragmentTransaction.commit();
        hideLayout();
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
        
        frameLayout.setVisibility(View.VISIBLE);
        relativeLayout.setVisibility(View.GONE);
    }

    public void showLayout() {
        Log.d(TAG, "showLayout: executing showLayout");
        
        frameLayout.setVisibility(View.GONE);
        relativeLayout.setVisibility(View.VISIBLE);
    }
    
    public void sendTags() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, VIEW_INTERESTS_POSTS_URL,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: " + response);

                        try {
                            JSONArray posts = new JSONArray(response.substring(response.indexOf("["),
                                    response.lastIndexOf("]") + 1));

                            for(int i = 0; i < posts.length(); i++) {

                                JSONObject postObject = posts.getJSONObject(i);

                                int postId = postObject.getInt("post_id");
                                int postUserId = postObject.getInt("user_id");
                                int postLikesCount = postObject.getInt("like_count");
                                int postCommentCount = postObject.getInt("comment_count");
                                String postName = postObject.getString("name");
                                String postYear = postObject.getString("year");
                                String postTitle = postObject.getString("title");
                                String postContent = postObject.getString("content");
                                String postImageUrl = postObject.getString("image");
                                String postTags = postObject.getString("tags");
                                String postTimestamp = postObject.getString("timestamp");

                                Post post = new Post(postId, postUserId, postLikesCount, postCommentCount, postName,
                                        postYear, postTitle, postContent, postImageUrl, postTags, postTimestamp);

                                postList.add(post);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        postAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        Toast.makeText(InterestsActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Log.d(TAG, "getParams: executing getParams");

                Map<String, String> params = new Hashtable<String, String>();
                params.put("tag_id", tagIdString.substring(0, tagIdString.length() - 1));

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: resume");

        this.getTagArray();
        if (tagIds.size() != 0 && postList.size() == 0) {
            for (int i = 0; i < tagIds.size(); i++) {
                tagIdString += tagIds.get(i) + ",";
            }
            this.sendTags();
        }
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private void getTagArray() {
        Log.d(TAG, "getTagIdArray: executing getTagArray");

        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                tagIds = intent.getIntegerArrayListExtra("tagId");
            }
        }, new IntentFilter("sending tagId and tagName Arrays"));
    }

    @Override
    public void onBackPressed() {
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        super.onBackPressed();

        if (frameLayout.getVisibility() == View.VISIBLE && !tagIdString.equals("")) {
            Log.d(TAG, "onBackPressed: if back press");
            showLayout();
        }
        else {
            Log.d(TAG, "onBackPressed: else back press");
            finish();
        }
    }
}
