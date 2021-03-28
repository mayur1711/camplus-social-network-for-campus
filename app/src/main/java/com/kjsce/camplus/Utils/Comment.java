package com.kjsce.camplus.Utils;

import android.util.Log;

/**
 * Created by Mayur on 07-03-2018.
 */

//This class is used to store CommentList items
public class Comment {

    private static final String TAG = "Comment";

    private int commentId, postId, commentUserId;
    private String commentName, commentData;

    public Comment(int commentId, int postId, int commentUserId, String commentName, String commentData) {
        Log.d(TAG, "Comment: constructor");

        this.commentId = commentId;
        this.postId = postId;
        this.commentUserId = commentUserId;
        this.commentName = commentName;
        this.commentData = commentData;
    }

    public int getCommentId() {
        return commentId;
    }

    public int getPostId() {
        return postId;
    }

    public int getCommentUserId() {
        return commentUserId;
    }

    public String getName() {
        return commentName;
    }

    public String getCommentData() {
        return commentData;
    }
}
