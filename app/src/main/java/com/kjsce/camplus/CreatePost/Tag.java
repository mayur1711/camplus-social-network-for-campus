package com.kjsce.camplus.CreatePost;

import android.util.Log;

/**
 * Created by Mayur on 03-04-2018.
 */

public class Tag {

    private static final String TAG = "Tag";

    private int tagId;
    private String tagName;

    public Tag(int tagId, String tagName) {
        Log.d(TAG, "Tag: constructor");

        this.tagId = tagId;
        this.tagName = tagName;
    }

    public int getTagId() {
        return tagId;
    }

    public String getTagName() {
        return tagName;
    }
}
