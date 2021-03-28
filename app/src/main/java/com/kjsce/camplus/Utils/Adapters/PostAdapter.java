package com.kjsce.camplus.Utils.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.kjsce.camplus.CreatePost.CustomVolleyRequest;
import com.kjsce.camplus.Home.HomeActivity;
import com.kjsce.camplus.Home.Post;
import com.kjsce.camplus.Interests.InterestsActivity;
import com.kjsce.camplus.R;
import com.kjsce.camplus.Search.SearchActivity;
import com.kjsce.camplus.Utils.CommentsFragment;

import java.util.List;
import java.util.Objects;

/**
 * Created by Mayur on 12-02-2018.
 */

//To display the posts in RecyclerView we need adapter
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private static final String TAG = "PostAdapter";

    private Context context;
    private ImageLoader imageLoader;

    //List to store all posts
    private List<Post> postList;

    //Constructor of this class
    public PostAdapter(Context context, List<Post> postList) {
        Log.d(TAG, "PostAdapter: constructor" );

        this.context = context;
        this.postList = postList;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: executing onCreateViewHolder");

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_post, parent, false);

        return new PostViewHolder(view);
    }

    //bind the data with UI elements
    @Override
    public void onBindViewHolder(PostViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: executing onBindHolder");

        //Getting a particular item from list
        final Post post = postList.get(position);

        //Showing data on the views
        holder.postTitle.setText(post.getPostTitle());
        holder.postContent.setText(post.getPostContent());
        holder.postTags.setText(post.getPostTags());
        holder.postUserInfo.setText(post.getPostUserInfo());
        holder.postTimestamp.setText(post.getPostTimestamp());
        holder.postLikeCount.setText(String.valueOf(post.getPostLikeCount()));
        holder.postCommentCount.setText(String.valueOf(post.getPostCommentCount()));

        if (Objects.equals(post.getPostImageUrl(), "null")){
            holder.postImage.setVisibility(View.GONE);
        }
        else {
            holder.postImage.setVisibility(View.VISIBLE);
            imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
            imageLoader.get(post.getPostImageUrl(), ImageLoader.getImageListener(holder.postImage,
                    1, android.R.drawable.ic_dialog_alert));
            holder.postImage.setImageUrl(post.getPostImageUrl(), imageLoader);

        }

        holder.postLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "onClick: clicked on post");

                CommentsFragment commentsFragment = new CommentsFragment();

                //bundle for passing the object post to the Comments fragment
                Bundle bundle = new Bundle();
                bundle.putParcelable("post", post);
                commentsFragment.setArguments(bundle);

                //navigating to comments fragment
                if (context instanceof HomeActivity) {
                    FragmentTransaction fragmentTransaction = ((HomeActivity)context)
                            .getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.home_frameLayout,commentsFragment);
                    fragmentTransaction.addToBackStack("CommentsFragment");
                    fragmentTransaction.commit();

                    //call hideLayout method defined in HomeActivity
                    ((HomeActivity) context).hideLayout();
                }
                else if (context instanceof InterestsActivity) {
                    FragmentTransaction fragmentTransaction = ((InterestsActivity)context)
                            .getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.interests_frameLayout,commentsFragment);
                    fragmentTransaction.addToBackStack("CommentsFragment");
                    fragmentTransaction.commit();

                    //call hideLayout method defined in InterestsActivity
                    ((InterestsActivity) context).hideLayout();
                }
                else if (context instanceof SearchActivity) {
                    FragmentTransaction fragmentTransaction = ((SearchActivity)context)
                            .getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.search_frameLayout,commentsFragment);
                    fragmentTransaction.addToBackStack("CommentsFragment");
                    fragmentTransaction.commit();

                    //call hideLayout method defined in SearchActivity
                    ((SearchActivity) context).hideLayout();
                }
            }
        });
    }

    @Override
    public int getItemCount() {

        return postList.size();
    }


    class PostViewHolder extends RecyclerView.ViewHolder {

        private static final String TAG = "PostViewHolder";

        //views
        TextView postTitle, postContent, postTags, postUserInfo, postTimestamp, postLikeCount, postCommentCount;
        LinearLayout postLinearLayout;
        NetworkImageView postImage;

        public PostViewHolder(View itemView) {
            super(itemView);
            Log.d(TAG, "PostViewHolder: executing postViewHolder");

            //Initializing views
            postTitle = itemView.findViewById(R.id.post_title);
            postContent = itemView.findViewById(R.id.post_content);
            postTags = itemView.findViewById(R.id.post_tags);
            postUserInfo = itemView.findViewById(R.id.post_user_info);
            postTimestamp = itemView.findViewById(R.id.post_timestamp);
            postLikeCount = itemView.findViewById(R.id.post_like_count);
            postCommentCount = itemView.findViewById(R.id.post_comment_count);
            postLinearLayout = itemView.findViewById(R.id.post_linearLayout);
            postImage = itemView.findViewById(R.id.post_image);
        }
    }
}
