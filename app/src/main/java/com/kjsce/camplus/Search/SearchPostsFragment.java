package com.kjsce.camplus.Search;


import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.kjsce.camplus.Home.Post;
import com.kjsce.camplus.R;
import com.kjsce.camplus.Utils.Adapters.PostAdapter;
import com.kjsce.camplus.Utils.Global;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mayur on 27-04-2018.
 */

@TargetApi(Build.VERSION_CODES.M)
public class SearchPostsFragment extends Fragment {

    private static final String TAG = "SearchPostsFragment";
    private static final String VIEW_SEARCH_POSTS_URL = "https://ajjainaakash.000webhostapp.com/view_search_posts.php?search=";
    private final Global global = Global.getInstance();
    RecyclerView recyclerView;
    PostAdapter postAdapter;
    List<Post> postList;
    ProgressBar progressBar;
    RequestQueue requestQueue;
    String search;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: executing onCreateView");

        View view = inflater.inflate(R.layout.fragment_search_posts, container, false);

        search = global.getSearchKeyword();

        postList = new ArrayList<>();

        recyclerView = view.findViewById(R.id.search_posts_recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        progressBar = view.findViewById(R.id.search_posts_progressBar);

        requestQueue = Volley.newRequestQueue(this.getActivity());
        requestQueue.add(getPosts());
        postAdapter = new PostAdapter(getActivity(), postList);
        recyclerView.setAdapter(postAdapter);

        return view;
    }

    private StringRequest getPosts() {
        Log.d(TAG, "getPosts: executing getPosts");

        progressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, VIEW_SEARCH_POSTS_URL +
                search, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: response" + response);

                if (!response.contains("No Results")) {

                    try {

                        postList.clear();
                        postAdapter.notifyDataSetChanged();

                        JSONArray posts = new JSONArray(response.substring(response.indexOf("["),
                                response.lastIndexOf("]") + 1));

                        for (int i = 0; i < posts.length(); i++) {

                            JSONObject postObject = posts.getJSONObject(i);

                            int postId = postObject.getInt("post_id");
                            int postUserId = postObject.getInt("user_id");
                            String postTitle = postObject.getString("title");
                            String postContent = postObject.getString("content");
                            String postImageUrl = postObject.getString("image");
                            int postLikesCount = postObject.getInt("like_count");
                            int postCommentCount = postObject.getInt("comment_count");
                            String postName = postObject.getString("name");
                            String postYear = postObject.getString("year");
                            String postTags = postObject.getString("tags");
                            String postTimestamp = postObject.getString("timestamp");

                            Post post = new Post(postId, postUserId, postLikesCount, postCommentCount, postName,
                                    postYear, postTitle, postContent, postImageUrl, postTags, postTimestamp);
                            postList.add(post);
                            progressBar.setVisibility(View.GONE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    postAdapter.notifyDataSetChanged();
                } else {

                    Toast.makeText(SearchPostsFragment.this.getActivity(), "No more Posts", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {

            @Override
            //If error occurs a toast is displayed on the screen
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(SearchPostsFragment.this.getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });

        return stringRequest;
    }
}