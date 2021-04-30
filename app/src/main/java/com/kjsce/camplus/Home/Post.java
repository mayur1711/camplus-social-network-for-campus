package com.kjsce.camplus.Home;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by Mayur on 12-02-2018.
 */

//This class is used to store PostList items
public class Post implements Parcelable {

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };
    private static final String TAG = "Post";
    private final int postId;
    private final int postUserId;
    private final int postLikeCount;
    private final int postCommentCount;
    private final String postName;
    private final String postYear;
    private final String postTitle;
    private final String postTags;
    private final String postTimestamp;
    private String postContent;
    private String postImageUrl;

    public Post(int postId, int postUserId, int postLikeCount, int postCommentCount, String postName, String postYear,
                String postTitle, String postContent, String postImageUrl, String postTags, String postTimestamp) {
        Log.d(TAG, "Post: constructor");

        this.postId = postId;
        this.postUserId = postUserId;
        this.postLikeCount = postLikeCount;
        this.postCommentCount = postCommentCount;
        this.postName = postName;
        this.postYear = postYear;
        this.postTitle = postTitle;
        this.postTags = postTags;
        this.postTimestamp = postTimestamp;
        this.postImageUrl = postImageUrl;
        this.postContent = postContent;
    }

    protected Post(Parcel in) {

        postId = in.readInt();
        postUserId = in.readInt();
        postLikeCount = in.readInt();
        postCommentCount = in.readInt();
        postName = in.readString();
        postYear = in.readString();
        postTitle = in.readString();
        postTags = in.readString();
        postTimestamp = in.readString();
    }

    public int getPostId() {
        return postId;
    }

    public int getPostUserId() {
        return postUserId;
    }

    public int getPostLikeCount() {
        return postLikeCount;
    }

    public int getPostCommentCount() {
        return postCommentCount;
    }

    public String getPostName() {
        return postName;
    }

    public String getPostContent() {
        return postContent;
    }

    public String getPostImageUrl() {
        return postImageUrl;
    }

    public String getPostYear() {
        return postYear;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public String getPostTags() {
        return postTags;
    }

    public String getPostUserInfo() {
        return (postName + ", " + postYear);
    }

    public String getPostTimestamp() {
        return postTimestamp;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(postId);
        dest.writeInt(postUserId);
        dest.writeInt(postLikeCount);
        dest.writeInt(postCommentCount);
        dest.writeString(postName);
        dest.writeString(postYear);
        dest.writeString(postTitle);
        dest.writeString(postTags);
        dest.writeString(postTimestamp);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
