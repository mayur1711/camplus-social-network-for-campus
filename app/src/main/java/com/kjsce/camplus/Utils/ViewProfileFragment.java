package com.kjsce.camplus.Utils;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.*;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.kjsce.camplus.Home.Post;
import com.kjsce.camplus.Messaging.MessagingActivity;
import com.kjsce.camplus.Profile.ProfileActivity;
import com.kjsce.camplus.R;
import com.kjsce.camplus.Utils.Adapters.ProfileAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mayur on 13-04-2018.
 */

/* ---------- SIMILAR TO PROFILE AND COMMENTS FRAGMENT ---------- */
/* ---------- VIEW PROFILE FRAGMENT IS USED TO VIEW SOMEONE ELSE'S PROFILE ---------- */

@RequiresApi(api = Build.VERSION_CODES.M)
public class ViewProfileFragment extends Fragment implements View.OnScrollChangeListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "ViewProfileFragment";

    private static final int ACTIVITY_NUM = 3;
    private static final String VIEW_PROFILE_INFO_URL = "https://ajjainaakash.000webhostapp.com/view_profile_info.php";
    private static final String VIEW_PROFILE_POSTS_URL = "https://ajjainaakash.000webhostapp.com/view_profile_posts.php";
    private ProgressBar progressBar;
    private BottomNavigationViewEx bottomNavigationViewEx;
    private Toolbar toolbar;
    private Context mContext;
    private List<Post> postList;
    private RecyclerView recyclerView;
    private ProfileAdapter profileAdapter;
    private RequestQueue requestQueue;
    private ProfileInfo profileInfo;
    private TextView profileUsername;
    private ImageView profileMessage;
    private String username;
    private Toast toast;
    private int userId;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int requestCount = 1;
    private int countOnScrollChange = 1;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: executing onCreateView");

        View view = inflater.inflate(R.layout.fragment_view_profile, container, false);

        profileMessage = view.findViewById(R.id.view_profile_message);
        bottomNavigationViewEx = view.findViewById(R.id.bottomNavViewBar);
        toolbar = view.findViewById(R.id.view_profile_toolbar);
        profileUsername = view.findViewById(R.id.view_profile_username);
        mContext = this.getActivity();

        profileMessage.setClickable(false);

        setupBottomNavigationView();
        setupToolbar();

        //initializing the postList and setting the first element to null
        //null because we use a custom recycler view with first element displaying user info and not user posts
        postList = new ArrayList<>();
        postList.add(0, null);

        progressBar = view.findViewById(R.id.view_profile_progressBar);
        progressBar.setVisibility(View.GONE);

        recyclerView = view.findViewById(R.id.view_profile_recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setOnScrollChangeListener(this);

        toast = Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT);
        requestQueue = Volley.newRequestQueue(this.getActivity());

        Bundle args = getArguments();
        userId = args.getInt("UserId");

        profileInfo = ProfileInfo.getInstance();
        profileUsername.setText(profileInfo.getUsername());

        swipeRefreshLayout = view.findViewById(R.id.view_profile_swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark, android.R.color.holo_blue_dark);

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: executing run");

                swipeRefreshLayout.setRefreshing(true);

                getProfileInfo();
                getData();

                profileAdapter = new ProfileAdapter(getActivity(), postList);
                recyclerView.setAdapter(profileAdapter);

            }
        });


        profileMessage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating to messaging");

                Intent intent = new Intent(getActivity(), MessagingActivity.class);
                intent.putExtra("ReceiverUserId", userId);
                intent.putExtra("ReceiverUsername", username);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

            }
        });

        return view;
    }

    private void getData() {
        Log.d(TAG, "getData: executing getData");

        requestQueue.add(getProfilePosts(requestCount));
        requestCount++;
    }

    private void getProfileInfo() {
        Log.d(TAG, "getProfileInfo: executing getProfileInfo");

        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonResponse = new JSONObject(response.substring(response.indexOf("{"),
                            response.lastIndexOf("}") + 1));

                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {

                        profileMessage.setClickable(true);

                        String name = jsonResponse.getString("name");
                        String email = jsonResponse.getString("email");
                        username = jsonResponse.getString("username");
                        String contactNo = jsonResponse.getString("contact_no");
                        String year = jsonResponse.getString("year");
                        String department = jsonResponse.getString("department");

                        profileInfo.setName(name);
                        profileInfo.setEmail(email);
                        profileInfo.setUsername(username);
                        profileInfo.setContactNo(contactNo);
                        profileInfo.setYear(year);
                        profileInfo.setDepartment(department);

                        profileUsername.setText(profileInfo.getUsername());

                        profileAdapter.notifyDataSetChanged();
                    } else {
                        toast.setText("Something went wrong!");
                        toast.show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                toast.setText("Something went wrong!");
                toast.show();
            }
        };

        ViewProfileInfoRequest viewProfileInfoRequest = new ViewProfileInfoRequest(userId, responseListener, errorListener);
        requestQueue.add(viewProfileInfoRequest);
    }

    private StringRequest getProfilePosts(int requestCount) {
        Log.d(TAG, "getProfilePosts: executing getProfilePosts");

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (!response.contains("over")) {

                    try {

                        JSONArray profilePosts = new JSONArray(response.substring(response.indexOf("["),
                                response.lastIndexOf("]") + 1));

                        for (int i = 0; i < profilePosts.length(); i++) {

                            JSONObject profileObject = profilePosts.getJSONObject(i);

                            int postId = profileObject.getInt("post_id");
                            int postUserId = profileObject.getInt("user_id");
                            int postLikesCount = profileObject.getInt("like_count");
                            int postCommentCount = profileObject.getInt("comment_count");
                            String postName = profileObject.getString("name");
                            String postYear = profileObject.getString("year");
                            String postTags = profileObject.getString("tags");
                            String postTitle = profileObject.getString("title");
                            String postContent = profileObject.getString("content");
                            String postImageUrl = profileObject.getString("image");
                            String postTimestamp = profileObject.getString("timestamp");

                            Post post = new Post(postId, postUserId, postLikesCount, postCommentCount,
                                    postName, postYear, postTitle, postContent, postImageUrl, postTags, postTimestamp);
                            postList.add(post);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    profileAdapter.notifyDataSetChanged();
                } else {
                    if (countOnScrollChange != 2) {
                        toast.setText("No more Posts");
                        toast.show();
                    }
                }

                progressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                toast.setText("Something went wrong!");
                toast.show();
                progressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
            }
        };

        ViewProfilePostsRequest viewProfilePostsRequest = new ViewProfilePostsRequest(userId, requestCount,
                responseListener, errorListener);

        return viewProfilePostsRequest;

    }

    private void setupBottomNavigationView() {
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");

        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, getActivity(), bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
        menuItem.setEnabled(false);
    }

    private boolean isLastItemDisplaying(RecyclerView recyclerView) {
        Log.d(TAG, "isLastItemDisplaying: executing isLastItemDisplaying");

        if (recyclerView.getAdapter().getItemCount() != 0) {
            int lastVisibleItemPosition = ((LinearLayoutManager)
                    recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            return lastVisibleItemPosition != RecyclerView.NO_POSITION &&
                    lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1;
        }
        return false;
    }

    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        Log.d(TAG, "onScrollChange: executing onScrollChange");

        if (isLastItemDisplaying(recyclerView)) {
            if (countOnScrollChange != 1) {
                progressBar.setVisibility(View.VISIBLE);
            }
            getData();
            countOnScrollChange++;
        }
    }

    private void setupToolbar() {
        Log.d(TAG, "setupToolbar: executing setupToolbar");

        ((ProfileActivity) getActivity()).setSupportActionBar(toolbar);
    }

    @Override
    public void onDestroy() {
        toast.cancel();
        super.onDestroy();
    }

    @Override
    public void onRefresh() {
        Log.d(TAG, "onRefresh: executing onRefresh");

        postList.clear();
        postList.add(0, null);
        requestCount = 1;
        getData();
    }

    class ViewProfilePostsRequest extends StringRequest {

        private static final String TAG = "ProfilePostsRequest";

        private final Map<String, String> params;

        ViewProfilePostsRequest(int userId, int requestCount, Response.Listener<String> listener,
                                Response.ErrorListener errorListener) {
            super(Method.POST, VIEW_PROFILE_POSTS_URL, listener, errorListener);
            Log.d(TAG, "ProfilePostsRequest: constructor");

            params = new HashMap<>();
            params.put("user_id", String.valueOf(userId));
            params.put("page", String.valueOf(requestCount));
        }

        public Map<String, String> getParams() {
            return params;
        }
    }

    class ViewProfileInfoRequest extends StringRequest {

        private static final String TAG = "ProfileInfoRequest";

        private final Map<String, String> params;

        ViewProfileInfoRequest(int userId, Response.Listener<String> listener, Response.ErrorListener errorListener) {
            super(Method.POST, VIEW_PROFILE_INFO_URL, listener, errorListener);
            Log.d(TAG, "ProfileInfoRequest: constructor");

            params = new HashMap<>();
            params.put("user_id", String.valueOf(userId));
        }

        public Map<String, String> getParams() {
            return params;
        }
    }
}