package com.kjsce.camplus.Utils;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kjsce.camplus.Home.Post;
import com.kjsce.camplus.R;
import com.kjsce.camplus.Utils.Adapters.CommentAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mayur on 06-03-2018.
 */

/* ---------- SIMILAR TO FEED FRAGMENT ---------- */

@TargetApi(Build.VERSION_CODES.M)
public class CommentsFragment extends Fragment implements View.OnScrollChangeListener, View.OnClickListener {

    private static final String TAG = "CommentsFragment";
    private static final String VIEW_COMMENTS_URL = "https://ajjainaakash.000webhostapp.com/view_comments.php";
    private static final String UPLOAD_COMMENT_URL = "https://ajjainaakash.000webhostapp.com/upload_comment.php";
    private Post post;
    private RecyclerView recyclerView;
    private CommentAdapter commentAdapter;
    private List<Comment> commentList;
    private RequestQueue requestQueue;
    private ProgressBar progressBar;
    private EditText commentTyped;
    private ImageView commentEnterBtn;
    private Toast toast;
    private int requestCount = 1;
    private int countOnScrollChange = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: executing onCreateView");

        View view = inflater.inflate(R.layout.fragment_comments, container, false);

        commentTyped = view.findViewById(R.id.comment_enter_data);
        commentEnterBtn = view.findViewById(R.id.comments_enter_btn);
        commentEnterBtn.setOnClickListener(this);

        //retrieving the object 'post' passed from PostAdapter inside the bundle
        Bundle bundle = getArguments();
        post = bundle.getParcelable("post");

        //initializing the commentList and setting the first element to null
        //null because we use custom recycler view with first element displaying post details and not comment
        commentList = new ArrayList<>();
        commentList.add(0, null);

        progressBar = view.findViewById(R.id.comments_progressBar);

        recyclerView = view.findViewById(R.id.comments_recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setOnScrollChangeListener(this);

        toast = Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT);
        requestQueue = Volley.newRequestQueue(this.getActivity());
        getData();

        commentAdapter = new CommentAdapter(getActivity(), commentList, post);
        recyclerView.setAdapter(commentAdapter);

        return view;
    }

    private void getData() {
        Log.d(TAG, "getData: executing getData");

        requestQueue.add(getComments(requestCount));
        requestCount++;
    }

    private StringRequest getComments(int requestCount) {
        Log.d(TAG, "getComments: executing getComments");

        progressBar.setVisibility(View.VISIBLE);

        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                if (!response.contains("over")) {

                    try {
                        JSONArray comments = new JSONArray(response.substring(response.indexOf("["),
                                response.lastIndexOf("]") + 1));

                        for (int i = 0; i < comments.length(); i++) {

                            JSONObject commentObject = comments.getJSONObject(i);

                            String commentName = commentObject.getString("name");
                            int commentId = commentObject.getInt("comment_id");
                            int postId = commentObject.getInt("post_id");
                            int commentUserId = commentObject.getInt("user_id");
                            String commentData = commentObject.getString("data");

                            Comment comment = new Comment(commentId, postId, commentUserId, commentName, commentData);
                            commentList.add(comment);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    commentAdapter.notifyDataSetChanged();
                } else {
                    if (countOnScrollChange != 2) {
                        toast.setText("No more Comments");
                        toast.show();
                    }
                }

                progressBar.setVisibility(View.GONE);

            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onResponse: ResponseErrorListener");

                toast.setText("Something went wrong!");
                toast.show();
                progressBar.setVisibility(View.GONE);
            }
        };

        CommentsFragmentRequest commentsFragmentRequest = new
                CommentsFragmentRequest(post.getPostId(), requestCount, responseListener, errorListener);

        return commentsFragmentRequest;
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
            getData();
            countOnScrollChange++;
        }
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick: clicked on commentEnterBtn");

//        progressBar.setVisibility(View.VISIBLE);

        String commentData = commentTyped.getText().toString().trim();
        if (!commentData.equals("")) {

            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    if (response.equals("connection successsuccess")) {
                        commentList.clear();
                        commentList.add(0, null);

                        requestCount = 1;
                        getData();

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Comment Upload Failed!")
                                .setNegativeButton("Retry", null)
                                .create()
                                .show();
                    }

//                    progressBar.setVisibility(View.GONE);
                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    toast.setText("No Connection or Server Error");
                    toast.show();
//                    progressBar.setVisibility(View.GONE);

                }
            };

            CommentsUploadRequest commentsUploadRequest = new CommentsUploadRequest(post.getPostId(),
                    LoginInfo.getInstance().getUserId(), commentData, responseListener, errorListener);

            requestQueue.add(commentsUploadRequest);
            commentTyped.setText("");
            closeKeyboard();
        } else {
            toast.setText("can't post a blank comment");
            toast.show();
//            progressBar.setVisibility(View.GONE);
        }
    }

    private void closeKeyboard() {
        Log.d(TAG, "closeKeyboard: executing closeKeyboard");

        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onDestroy() {
        toast.cancel();
        super.onDestroy();
    }

    class CommentsUploadRequest extends StringRequest {

        private static final String TAG = "CommentsUploadRequest";

        private final Map<String, String> params;

        public CommentsUploadRequest(int postId, int userId, String commentData, Response.Listener<String> listener,
                                     Response.ErrorListener errorListener) {
            super(Method.POST, UPLOAD_COMMENT_URL, listener, errorListener);
            Log.d(TAG, "CommentsUploadRequest: constructor");

            params = new HashMap<>();
            params.put("post_id", String.valueOf(postId));
            params.put("user_id", String.valueOf(userId));
            params.put("data", commentData);
        }

        public Map<String, String> getParams() {
            return params;
        }
    }

    class CommentsFragmentRequest extends StringRequest {

        private static final String TAG = "CommentsFragmentRequest";

        private final Map<String, String> params;

        public CommentsFragmentRequest(int postId, int requestCount, Response.Listener<String> listener,
                                       Response.ErrorListener errorListener) {
            super(Method.POST, VIEW_COMMENTS_URL, listener, errorListener);
            Log.d(TAG, "CommentsFragmentRequest: constructor");

            params = new HashMap<>();
            params.put("post_id", String.valueOf(postId));
            params.put("page", String.valueOf(requestCount));
        }

        public Map<String, String> getParams() {
            return params;
        }
    }
}