package com.kjsce.camplus.Utils.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.kjsce.camplus.CreatePost.Tag;
import com.kjsce.camplus.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mayur on 03-04-2018.
 */

public class TagAdapter extends BaseAdapter {

    private static final String TAG = "TagAdapter";
    private final List<Integer> tagIds = new ArrayList<Integer>();
    private final List<String> tagNames = new ArrayList<String>();
    private final List<Tag> tagList;
    private final Context context;

    public TagAdapter(Context context, List<Tag> tagList) {
        this.tagList = tagList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return tagList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Log.d(TAG, "getView: executing getView");
        final TextView tagTextView;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_tag, parent, false);
        }

        final Tag tag = tagList.get(position);
        tagTextView = convertView.findViewById(R.id.tag_name);

        tagTextView.setText(tag.getTagName());
        tagTextView.setTag(R.drawable.tag_up);
        final View finalConvertView = convertView;

        tagTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicking on tag button");
                int resId = (Integer) finalConvertView.getTag();
                if (resId == R.drawable.tag_down) {
                    tagTextView.setBackgroundResource(R.drawable.tag_up);
                    tagTextView.setTag(R.drawable.tag_up);
                    tagIds.remove(tagIds.indexOf(tag.getTagId()));
                    tagNames.remove(tagNames.indexOf(tag.getTagName()));
                } else {
                    tagTextView.setBackgroundResource(R.drawable.tag_down);
                    tagTextView.setTag(R.drawable.tag_down);
                    tagIds.add(tag.getTagId());
                    tagNames.add(tag.getTagName());
                }
            }
        });

        Intent intent = new Intent("sending tagId and tagName Arrays");
        intent.putIntegerArrayListExtra("tagId", (ArrayList<Integer>) tagIds);
        intent.putStringArrayListExtra("tagName", (ArrayList<String>) tagNames);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

        return convertView;
    }
}
