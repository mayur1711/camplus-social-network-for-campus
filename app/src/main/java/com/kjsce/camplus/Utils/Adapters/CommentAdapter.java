package com.kjsce.camplus.Utils.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kjsce.camplus.CreatePost.CustomVolleyRequest;
import com.kjsce.camplus.Home.Post;
import com.kjsce.camplus.Profile.ProfileActivity;
import com.kjsce.camplus.R;
import com.kjsce.camplus.Utils.Comment;
import com.kjsce.camplus.Utils.LoginInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Mayur on 07-03-2018.
 */

//To display the posts in RecyclerView we need adapter
public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "CommentAdapter";
    private static final String SET_LIKE_URL = "https://ajjainaakash.000webhostapp.com/set_like.php";
    private static final String REMOVE_LIKE_URL = "https://ajjainaakash.000webhostapp.com/remove_like.php";
    private static final String SET_FLAG_URL = "https://ajjainaakash.000webhostapp.com/set_flag.php";
    //List to store all comments
    private final List<Comment> commentList;
    //declaring reference variables
    private final Context context;
    private final Post post;
    private final LoginInfo loginInfo = LoginInfo.getInstance();

    //Constructor of this class
    public CommentAdapter(Context context, List<Comment> commentList, Post post) {

        Log.d(TAG, "CommentAdapter: constructor");

        this.context = context;
        this.commentList = commentList;
        this.post = post;
    }

    @Override
    public int getItemViewType(int position) {

        Log.d(TAG, "getItemViewType: executing getItemViewType");

        //if first position of recycler view, display post
        if (position == 0)
            return R.layout.layout_comment_top_post;

            //for rest positions, display comments
        else
            return R.layout.layout_comment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Log.d(TAG, "onCreateViewHolder: executing onCreateViewHolder");

        final RecyclerView.ViewHolder holder;
        final View view;
        final LayoutInflater inflater;

        switch (viewType) {

            case R.layout.layout_comment_top_post:
                inflater = LayoutInflater.from(parent.getContext());
                view = inflater.inflate(R.layout.layout_comment_top_post, parent, false);
                holder = new CommentPostViewHolder(view);
                break;

            case R.layout.layout_comment:
                inflater = LayoutInflater.from(parent.getContext());
                view = inflater.inflate(R.layout.layout_comment, parent, false);
                holder = new CommentViewHolder(view);
                break;

            default:
                inflater = LayoutInflater.from(parent.getContext());
                view = inflater.inflate(R.layout.layout_comment, parent, false);
                holder = new CommentViewHolder(view);
                break;
        }

        return holder;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        Log.d(TAG, "onBindViewHolder: executing onBindViewHolder");

        if (holder instanceof CommentPostViewHolder) {

            ((CommentPostViewHolder) holder).commentTitle.setText(post.getPostTitle());
            ((CommentPostViewHolder) holder).commentContent.setText(post.getPostContent());
            ((CommentPostViewHolder) holder).commentTags.setText(post.getPostTags());
            ((CommentPostViewHolder) holder).commentUserInfo.setText(post.getPostUserInfo());
            ((CommentPostViewHolder) holder).commentTimestamp.setText(post.getPostTimestamp());
            ((CommentPostViewHolder) holder).commentLikeCount.setText(String.valueOf(post.getPostLikeCount()));
            ((CommentPostViewHolder) holder).commentCommentCount.setText(String.valueOf(post.getPostCommentCount()));

            if (Objects.equals(post.getPostImageUrl(), "null")) {
                ((CommentPostViewHolder) holder).commentImage.setVisibility(View.GONE);
            } else {
                ((CommentPostViewHolder) holder).commentImage.setVisibility(View.VISIBLE);
                ImageLoader imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
                imageLoader.get(post.getPostImageUrl(), ImageLoader.getImageListener(((CommentPostViewHolder) holder).commentImage,
                        1, android.R.drawable.ic_dialog_alert));
                ((CommentPostViewHolder) holder).commentImage.setImageUrl(post.getPostImageUrl(), imageLoader);
            }

            //
            ((CommentPostViewHolder) holder).commentUserInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: clicked on comment fragment user info");

                    if (loginInfo.getUserId() != post.getPostUserId()) {

                        Intent intent = new Intent(context, ProfileActivity.class);
                        intent.putExtra("CallingActivity", "AnyActivity");
                        intent.putExtra("UserId", post.getPostUserId());
                        context.startActivity(intent);
                    }
                }
            });

            ((CommentPostViewHolder) holder).commentLikeGrey.setVisibility(View.VISIBLE);
            ((CommentPostViewHolder) holder).commentLikeBlue.setVisibility(View.GONE);

            ((CommentPostViewHolder) holder).commentLikeGrey.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onTouch: grey like click detected");

                    Response.Listener<String> listener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, "onResponse: ResponseListener");

                            if (response.equals("connection success")) {
                                ((CommentPostViewHolder) holder).commentLikeCount.setText(String.valueOf(1 + post.getPostLikeCount()));
                                ((CommentPostViewHolder) holder).commentLikeBlue.setVisibility(View.VISIBLE);
                                ((CommentPostViewHolder) holder).commentLikeGrey.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(context, "something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    };

                    Response.ErrorListener errorListener = new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(TAG, "onResponse: ResponseErrorListener");

                            Toast.makeText(context, "something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    };

                    SetLikeRequest setLikeRequest = new SetLikeRequest(post.getPostId(), listener, errorListener);
                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                    requestQueue.add(setLikeRequest);

                }
            });

            ((CommentPostViewHolder) holder).commentLikeBlue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onTouch: blue like click detected");

                    Response.Listener<String> listener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, "onResponse: ResponseListener");

                            if (response.equals("connection success")) {
                                ((CommentPostViewHolder) holder).commentLikeCount.setText(String.valueOf(post.getPostLikeCount()));
                                ((CommentPostViewHolder) holder).commentLikeBlue.setVisibility(View.GONE);
                                ((CommentPostViewHolder) holder).commentLikeGrey.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(context, "something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    };

                    Response.ErrorListener errorListener = new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(TAG, "onResponse: ResponseErrorListener");

                            Toast.makeText(context, "something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    };

                    RemoveLikeRequest removeLikeRequest = new RemoveLikeRequest(post.getPostId(), listener, errorListener);
                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                    requestQueue.add(removeLikeRequest);

                }
            });

            ((CommentPostViewHolder) holder).commentFlagGrey.setVisibility(View.VISIBLE);
            ((CommentPostViewHolder) holder).commentFlagRed.setVisibility(View.GONE);

            ((CommentPostViewHolder) holder).commentFlagGrey.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onTouch: grey flag touch detected");

                    Response.Listener<String> listener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, "onResponse: ResponseListener");

                            if (response.equals("connection success")) {
                                ((CommentPostViewHolder) holder).commentFlagRed.setVisibility(View.VISIBLE);
                                ((CommentPostViewHolder) holder).commentFlagGrey.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(context, "something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    };

                    Response.ErrorListener errorListener = new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(TAG, "onResponse: ResponseErrorListener");

                            Toast.makeText(context, "something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    };

                    SetFlagRequest setFlagRequest = new SetFlagRequest(post.getPostId(), listener, errorListener);
                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                    requestQueue.add(setFlagRequest);

                }
            });

        } else if (holder instanceof CommentViewHolder) {

            final Comment comment = commentList.get(position);

            ((CommentViewHolder) holder).commentName.setText(comment.getName());
            ((CommentViewHolder) holder).commentData.setText(comment.getCommentData());
        }
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    class CommentPostViewHolder extends RecyclerView.ViewHolder {

        private static final String TAG = "PostViewHolder";

        //declaring views
        TextView commentTitle, commentContent, commentTags, commentUserInfo, commentTimestamp, commentLikeCount, commentCommentCount;
        NetworkImageView commentImage;
        ImageView commentLikeGrey, commentLikeBlue, commentFlagGrey, commentFlagRed;

        public CommentPostViewHolder(View itemView) {
            super(itemView);

            Log.d(TAG, "PostViewHolder: executing PostViewHolder");

            //Initializing views
            commentTitle = itemView.findViewById(R.id.comment_title);
            commentContent = itemView.findViewById(R.id.comment_content);
            commentTags = itemView.findViewById(R.id.comment_tags);
            commentUserInfo = itemView.findViewById(R.id.comment_user_info);
            commentTimestamp = itemView.findViewById(R.id.comment_timestamp);
            commentLikeCount = itemView.findViewById(R.id.comment_like_count);
            commentCommentCount = itemView.findViewById(R.id.comment_comment_count);
            commentImage = itemView.findViewById(R.id.comment_image);
            commentLikeGrey = itemView.findViewById(R.id.comment_like_grey);
            commentLikeBlue = itemView.findViewById(R.id.comment_like_blue);
            commentFlagGrey = itemView.findViewById(R.id.comment_flag_grey);
            commentFlagRed = itemView.findViewById(R.id.comment_flag_red);
        }
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {

        private static final String TAG = "CommentViewHolder";

        //declaring views
        TextView commentName, commentData;

        public CommentViewHolder(View itemView) {
            super(itemView);

            Log.d(TAG, "CommentViewHolder: executing CommentViewHolder");

            //Initialising views
            commentName = itemView.findViewById(R.id.comment_user_name);
            commentData = itemView.findViewById(R.id.comment_data);
        }
    }

    class SetLikeRequest extends StringRequest {

        private static final String TAG = "SetLikeRequest";

        private final Map<String, String> params;

        public SetLikeRequest(int postId, Response.Listener<String> listener,
                              Response.ErrorListener errorListener) {
            super(Method.POST, SET_LIKE_URL, listener, errorListener);
            Log.d(TAG, "SetLikeRequest: constructor");

            params = new HashMap<>();
            params.put("post_id", String.valueOf(postId));
        }

        public Map<String, String> getParams() {
            return params;
        }
    }

    class RemoveLikeRequest extends StringRequest {

        private static final String TAG = "RemoveLikeRequest";

        private final Map<String, String> params;

        public RemoveLikeRequest(int postId, Response.Listener<String> listener,
                                 Response.ErrorListener errorListener) {
            super(Method.POST, REMOVE_LIKE_URL, listener, errorListener);
            Log.d(TAG, "RemoveLikeRequest: constructor");

            params = new HashMap<>();
            params.put("post_id", String.valueOf(postId));
        }

        public Map<String, String> getParams() {
            return params;
        }
    }

    class SetFlagRequest extends StringRequest {

        private static final String TAG = "SetFlagRequest";

        private final Map<String, String> params;

        public SetFlagRequest(int postId, Response.Listener<String> listener,
                              Response.ErrorListener errorListener) {
            super(Method.POST, SET_FLAG_URL, listener, errorListener);
            Log.d(TAG, "SetFlagRequest: constructor");

            params = new HashMap<>();
            params.put("post_id", String.valueOf(postId));
        }

        public Map<String, String> getParams() {
            return params;
        }
    }
}
