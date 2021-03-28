package com.kjsce.camplus.CreatePost;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kjsce.camplus.Interests.InterestsActivity;
import com.kjsce.camplus.R;
import com.kjsce.camplus.Utils.Adapters.TagAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mayur on 31-03-2018.
 */

public class TagsFragment extends Fragment {

    private static final String TAG = "TagsFragment";
    private ImageView saveBtn;
    private GridView gridView;
    private List<Tag> tagList;
    private TagAdapter tagAdapter;
    private RequestQueue requestQueue;
    private List<Integer> tagIds = new ArrayList<Integer>();
    private List<String> tagNames = new ArrayList<String>();

    private static String VIEW_TAGS_URL = "https://ajjainaakash.000webhostapp.com/view_tags.php";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView: executing onCreateView");
        View view = inflater.inflate(R.layout.fragment_tags, container, false);

        gridView = view.findViewById(R.id.tags_gridView);
        saveBtn = view.findViewById(R.id.tags_save);

        tagList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this.getActivity());

        getTagsFromDatabase();

        tagAdapter = new TagAdapter(getActivity(), tagList);
        gridView.setAdapter(tagAdapter);

        //getting tagId and tagName from TagAdapter
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                tagIds = intent.getIntegerArrayListExtra("tagId");
                tagNames = intent.getStringArrayListExtra("tagName");

            }
        }, new IntentFilter("sending tagId and tagName Arrays"));

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "onClick: save tag button click");
                if (tagIds.size() == 0) {
                    Toast.makeText(getActivity(), "Select tags!", Toast.LENGTH_SHORT).show();
                }
                else if (tagIds.size() > 3) {
                    Toast.makeText(getActivity(), "Not more than 3 tags are allowed", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent tagIntent = new Intent(getActivity().getBaseContext(), InterestsActivity.class);
                    tagIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    getActivity().startActivity(tagIntent);

                    if (getActivity() instanceof CreatePostActivity) {
                        ((CreatePostActivity) getActivity()).showLayout();
                    }
                    else if (getActivity() instanceof InterestsActivity) {
                        ((InterestsActivity) getActivity()).showLayout();
                    }
                }
            }
        });
        return view;
    }

    private void getTagsFromDatabase() {

        Log.d(TAG, "getTags: executing getTags");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, VIEW_TAGS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray tags = new JSONArray(response.substring(response.indexOf("["),
                            response.lastIndexOf("]") + 1));
                    for(int i = 0; i < tags.length(); i++) {

                        JSONObject tagObject = tags.getJSONObject(i);
                        int tagId = tagObject.getInt("interest_id");
                        String tagName = tagObject.getString("tag");

                        Tag tag = new Tag(tagId, tagName);
                        tagList.add(tag);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                tagAdapter.notifyDataSetChanged();

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TagsFragment.this.getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }
}
