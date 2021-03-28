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
import com.kjsce.camplus.Utils.Adapters.PostAdapter;
import com.kjsce.camplus.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mayur on 09-02-2018.
 */

@TargetApi(Build.VERSION_CODES.M)
public class FeedFragment extends Fragment implements RecyclerView.OnScrollChangeListener, SwipeRefreshLayout.OnRefreshListener{

    private static final String TAG = "FeedFragment";

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> postList;
    private ProgressBar progressBar;
    private RequestQueue requestQueue;
    private SwipeRefreshLayout swipeRefreshLayout;

    //requestCount corresponds to the page no. to be accessed from the url
    private int requestCount = 1;

    //url of view_posts.php on the server
    private static String VIEW_FEED_POSTS_URL = "https://ajjainaakash.000webhostapp.com/view_feed_posts.php?page=";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: executing onCreateView");

        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        //initializing the postList
        postList = new ArrayList<>();

        //initialising progressbar
        progressBar = view.findViewById(R.id.feed_progressBar);
        progressBar.setVisibility(View.GONE);

        //initialising recyclerView
        recyclerView = view.findViewById(R.id.feed_recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setOnScrollChangeListener(this);

        //getting a new requestQueue from Volley
        requestQueue = Volley.newRequestQueue(this.getActivity());

        swipeRefreshLayout = view.findViewById(R.id.feed_swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark, android.R.color.holo_blue_dark);

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: executing run");

                swipeRefreshLayout.setRefreshing(true);
                getData();

                //initializing postAdapter
                postAdapter = new PostAdapter(getActivity(), postList);

                //Adding adapter to recyclerView
                recyclerView.setAdapter(postAdapter);
            }
        });
        return view;
    }

    //This method will get data from the web api
    private void getData() {
        Log.d(TAG, "getData: executing getData");

        //Adding the stringRequest to the queue by calling the method getPosts
        requestQueue.add(getPosts(requestCount));
        requestCount++;

    }

    //Request to get json response from server - we are passing an integer here
    //This integer is used to specify the page number for the request ?page = requestCount
    //This method would return a StringRequest that will be added to the request queue
    private StringRequest getPosts(int requestCount) {
        Log.d(TAG, "getPosts: executing getPosts");

        //Json StringRequest of volley
        StringRequest stringRequest = new StringRequest(Request.Method.POST, VIEW_FEED_POSTS_URL +
                String.valueOf(requestCount), new Response.Listener<String>() {

            @Override
            //String 'response' - this is the response from view_posts.php
            public void onResponse(String response) {

                //if there are more posts to display
                if (!response.contains("over")) {

                    //surrounding around try catch to ensure it does not fail for response that is not
                    // in JSON format and catches an exception
                    try {

                        //convert into json Array object as the response sent by view_posts.php will be encoded
                        //in JSON string
                        JSONArray posts = new JSONArray(response.substring(response.indexOf("["),
                                response.lastIndexOf("]") + 1));

                        //traversing through all the object
                        for(int i = 0; i < posts.length(); i++) {

                            //getting post object from json array
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

                            //Instantiating class Post and adding data to post object
                            Post post = new Post(postId, postUserId, postLikesCount, postCommentCount, postName,
                                    postYear, postTitle, postContent, postImageUrl, postTags, postTimestamp);

                            //adding the post to postList
                            postList.add(post);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //Notifying the adapter that data has been added or changed
                    postAdapter.notifyDataSetChanged();
                }

                //if no more posts to display display a toast on screen
                else {
                    Toast.makeText(FeedFragment.this.getActivity(), "No more Posts", Toast.LENGTH_SHORT).show();
                }

                progressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);

            }
        }, new Response.ErrorListener() {

            @Override
            //If error occurs a toast is displayed on the screen
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(FeedFragment.this.getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        //returning the string request
        return stringRequest;
    }

    //This method would check that the recyclerView scroll has reached the bottom or not
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

        //If scrolled at last then
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