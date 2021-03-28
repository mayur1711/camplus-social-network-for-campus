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
import com.kjsce.camplus.Home.Post;
import com.kjsce.camplus.Profile.ProfileActivity;
import com.kjsce.camplus.R;
import com.kjsce.camplus.Utils.CommentsFragment;
import com.kjsce.camplus.Utils.ProfileInfo;

import java.util.List;
import java.util.Objects;

/**
 * Created by Mayur on 12-03-2018.
 */

public class ProfileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "ProfileAdapter";

    private Context context;
    private List<Post> postList;
    private ImageLoader imageLoader;

    public ProfileAdapter(Context context, List<Post> postList) {

        Log.d(TAG, "ProfileAdapter: constructor");

        this.context = context;
        this.postList = postList;

    }

    @Override
    public int getItemViewType(int position) {

        Log.d(TAG, "getItemViewType: executing getItemViewType");

        //if first position of recycler view, display user info
        if (position == 0)
            return R.layout.layout_profile_top_userinfo;

        //for rest positions, display posts
        else
            return R.layout.layout_post;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Log.d(TAG, "onCreateViewHolder: executing onCreateViewHolder");

        final RecyclerView.ViewHolder holder;
        final View view;
        final LayoutInflater inflater;

        switch (viewType) {

            case R.layout.layout_profile_top_userinfo:
                inflater = LayoutInflater.from(parent.getContext());
                view = inflater.inflate(R.layout.layout_profile_top_userinfo, parent, false);
                holder = new ProfileAdapter.ProfileInfoViewHolder(view);
                break;

            case R.layout.layout_post:
                inflater = LayoutInflater.from(parent.getContext());
                view = inflater.inflate(R.layout.layout_post, parent, false);
                holder = new ProfileAdapter.ProfilePostsViewHolder(view);
                break;

            default:
                inflater = LayoutInflater.from(parent.getContext());
                view = inflater.inflate(R.layout.layout_post, parent, false);
                holder = new ProfileAdapter.ProfilePostsViewHolder(view);
                break;
        }

        return holder;    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Log.d(TAG, "onBindViewHolder: executing onBindViewHolder");

        if (holder instanceof ProfileInfoViewHolder) {

            ProfileInfo profileInfo = ProfileInfo.getInstance();

            ((ProfileInfoViewHolder) holder).profileName.setText(profileInfo.getName());
            ((ProfileInfoViewHolder) holder).profileEmail.setText(profileInfo.getEmail());
            ((ProfileInfoViewHolder) holder).profileYear.setText(profileInfo.getYear());
            ((ProfileInfoViewHolder) holder).profileDepartment.setText(profileInfo.getDepartment());
            ((ProfileInfoViewHolder) holder).profileContactNo.setText(profileInfo.getContactNo());

        }
        else if (holder instanceof ProfilePostsViewHolder) {

            final Post post = postList.get(position);

            ((ProfilePostsViewHolder) holder).profilePostTitle.setText(post.getPostTitle());
            ((ProfilePostsViewHolder) holder).profilePostContent.setText(post.getPostContent());
            ((ProfilePostsViewHolder) holder).profilePostTags.setText(post.getPostTags());
            ((ProfilePostsViewHolder) holder).profilePostTimestamp.setText(post.getPostTimestamp());
            ((ProfilePostsViewHolder) holder).profilePostUserInfo.setText(post.getPostUserInfo());
            ((ProfilePostsViewHolder) holder).profilePostLikeCount.setText(String.valueOf(post.getPostLikeCount()));
            ((ProfilePostsViewHolder) holder).profilePostCommentCount.setText(String.valueOf(post.getPostCommentCount()));

            if (Objects.equals(post.getPostImageUrl(), "null")){
                ((ProfilePostsViewHolder) holder).profilePostImage.setVisibility(View.GONE);
            }
            else {
                imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
                imageLoader.get(post.getPostImageUrl(), ImageLoader.getImageListener(((ProfilePostsViewHolder) holder).profilePostImage,
                        1, android.R.drawable.ic_dialog_alert));
                ((ProfilePostsViewHolder) holder).profilePostImage.setVisibility(View.VISIBLE);
                ((ProfilePostsViewHolder) holder).profilePostImage.setImageUrl(post.getPostImageUrl(), imageLoader);
            }

            ((ProfilePostsViewHolder) holder).profilePostLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.d(TAG, "onClick: clicked on post");

                    CommentsFragment commentsFragment = new CommentsFragment();

                    //bundle for passing the object post to the Comments fragment
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("post", post);
                    commentsFragment.setArguments(bundle);

                    //navigating to comments fragment
                    FragmentTransaction fragmentTransaction = ((ProfileActivity)context)
                            .getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.profile_frameLayout1, commentsFragment);
                    fragmentTransaction.addToBackStack("CommentsFragment");
                    fragmentTransaction.commit();

                    //call hideLayout method defined in HomeActivity
                    ((ProfileActivity) context).hideLayout();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }


    class ProfileInfoViewHolder extends RecyclerView.ViewHolder {

        private static final String TAG = "ProfileInfoViewHolder";

        TextView profileName, profileEmail, profileDepartment, profileYear, profileContactNo;

        public ProfileInfoViewHolder(View itemView) {
            super(itemView);

            Log.d(TAG, "ProfileInfoViewHolder: constructor");

            profileName = itemView.findViewById(R.id.profile_name);
            profileEmail = itemView.findViewById(R.id.profile_email);
            profileDepartment = itemView.findViewById(R.id.profile_department);
            profileYear = itemView.findViewById(R.id.profile_year);
            profileContactNo = itemView.findViewById(R.id.profile_contact_no);
        }
    }

    class ProfilePostsViewHolder extends RecyclerView.ViewHolder {

        private static final String TAG = "ProfilePostsViewHolder";

        //views
        TextView profilePostTitle, profilePostContent, profilePostTags, profilePostUserInfo, profilePostTimestamp,
                profilePostLikeCount, profilePostCommentCount;
        NetworkImageView profilePostImage;
        LinearLayout profilePostLinearLayout;

        public ProfilePostsViewHolder(View itemView) {
            super(itemView);

            Log.d(TAG, "ProfilePostsViewHolder: constructor");

            //Initializing views
            profilePostTitle = itemView.findViewById(R.id.post_title);
            profilePostContent = itemView.findViewById(R.id.post_content);
            profilePostImage = itemView.findViewById(R.id.post_image);
            profilePostTags = itemView.findViewById(R.id.post_tags);
            profilePostUserInfo = itemView.findViewById(R.id.post_user_info);
            profilePostTimestamp = itemView.findViewById(R.id.post_timestamp);
            profilePostLikeCount = itemView.findViewById(R.id.post_like_count);
            profilePostCommentCount = itemView.findViewById(R.id.post_comment_count);
            profilePostLinearLayout = itemView.findViewById(R.id.post_linearLayout);
        }
    }
}
