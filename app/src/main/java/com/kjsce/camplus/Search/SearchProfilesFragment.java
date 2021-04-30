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
import com.kjsce.camplus.R;
import com.kjsce.camplus.Utils.Adapters.SearchProfileAdapter;
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
public class SearchProfilesFragment extends Fragment {

    private static final String TAG = "SearchProfilesFragment";
    private static final String VIEW_SEARCH_PROFILES_URL = "https://ajjainaakash.000webhostapp.com/view_search_profiles.php?search=";
    private final Global global = Global.getInstance();
    RecyclerView recyclerView;
    SearchProfileAdapter searchProfileAdapter;
    List<User> profileList;
    ProgressBar progressBar;
    RequestQueue requestQueue;
    String search;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView: executing onCreateView");

        View view = inflater.inflate(R.layout.fragment_search_profiles, container, false);

        search = global.getSearchKeyword();

        profileList = new ArrayList<>();
        profileList.clear();

        recyclerView = view.findViewById(R.id.search_profiles_recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        progressBar = view.findViewById(R.id.search_profiles_progressBar);

        requestQueue = Volley.newRequestQueue(this.getActivity());
        requestQueue.add(getProfiles());
        searchProfileAdapter = new SearchProfileAdapter(getActivity(), profileList);
        recyclerView.setAdapter(searchProfileAdapter);

        return view;
    }

    private StringRequest getProfiles() {
        Log.d(TAG, "getProfiles: executing getProfiles");

        progressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, VIEW_SEARCH_PROFILES_URL
                + search, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: response" + response);

                if (!response.contains("No Results")) {

                    try {

                        profileList.clear();
                        searchProfileAdapter.notifyDataSetChanged();

                        JSONArray profiles = new JSONArray(response.substring(response.indexOf("["),
                                response.lastIndexOf("]") + 1));

                        for (int i = 0; i < profiles.length(); i++) {

                            JSONObject profilesObject = profiles.getJSONObject(i);

                            int userId = profilesObject.getInt("user_id");
                            String userName = profilesObject.getString("name");

                            User user = new User(userId, userName);
                            profileList.add(user);
                            progressBar.setVisibility(View.GONE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    searchProfileAdapter.notifyDataSetChanged();
                } else {

                    Toast.makeText(SearchProfilesFragment.this.getActivity(), "No more Profiles", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {

            @Override
            //If error occurs a toast is displayed on the screen
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(SearchProfilesFragment.this.getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });

        return stringRequest;
    }
}