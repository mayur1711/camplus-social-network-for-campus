package com.kjsce.camplus.Home;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kjsce.camplus.R;
import com.kjsce.camplus.Utils.LoginInfo;
import com.kjsce.camplus.Utils.Adapters.PostAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mayur on 09-02-2018.
 */

/* ---------- SIMILAR TO FEED FRAGMENT ---------- */

@TargetApi(Build.VERSION_CODES.M)
public class UpdatesFragment extends Fragment implements RecyclerView.OnScrollChangeListener, SwipeRefreshLayout.OnRefreshListener{

    private static final String TAG = "UpdatesFragment";

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> postList;
    private ProgressBar progressBar;
    private RequestQueue requestQueue;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LoginInfo loginInfo = LoginInfo.getInstance();
    private static String VIEW_UPDATES_POSTS_URL = "https://ajjainaakash.000webhostapp.com/view_updates_posts.php?page=";
    int requestCount = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: executing onCreateView");

        View view = inflater.inflate(R.layout.fragment_updates, container, false);

        postList = new ArrayList<>();

        progressBar = view.findViewById(R.id.updates_progressBar);
        progressBar.setVisibility(View.GONE);

        recyclerView = view.findViewById(R.id.updates_recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setOnScrollChangeListener(this);

        requestQueue = Volley.newRequestQueue(this.getActivity());

        swipeRefreshLayout = view.findViewById(R.id.updates_swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark, android.R.color.holo_blue_dark);

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: executing run");

                swipeRefreshLayout.setRefreshing(true);
                getData();
                postAdapter = new PostAdapter(getActivity(), postList);
                recyclerView.setAdapter(postAdapter);
            }
        });
        return view;
    }

    private void getData() {
        Log.d(TAG, "getData: executing getData");

        requestQueue.add(getPosts(requestCount));
        requestCount++;
    }

    private StringRequest getPosts(int requestCount) {
        Log.d(TAG, "getPosts: executing getPosts");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, VIEW_UPDATES_POSTS_URL +
                String.valueOf(requestCount) + "&user_id=" + String.valueOf(loginInfo.getUserId()), new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                if (!response.contains("over")) {

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

                else {
                    Toast.makeText(UpdatesFragment.this.getActivity(), "No more Posts", Toast.LENGTH_SHORT).show();
                }

                progressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);

            }
        }, new Response.ErrorListener() {

            @Override
            //If error occurs a toast is displayed on the screen
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(UpdatesFragment.this.getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return stringRequest;
    }

    private boolean isLastItemDisplaying(RecyclerView recyclerView) {
        Log.d(TAG, "isLastItemDisplaying: executing isLastItemDisplaying");

        if (recyclerView.getAdapter().getItemCount() != 0) {
            int lastVisibleItemPosition = ((LinearLayoutManager)
                    recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION &&
                    lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        Log.d(TAG, "onScrollChange: executing onScrollChange");

        if (isLastItemDisplaying(recyclerView)) {
            progressBar.setVisibility(View.VISIBLE);
            getData();
        }
    }

    @Override
    public void onRefresh() {
        Log.d(TAG, "onRefresh: executing onRefresh");

        postList.clear();
        requestCount = 1;
        getData();
    }
}
