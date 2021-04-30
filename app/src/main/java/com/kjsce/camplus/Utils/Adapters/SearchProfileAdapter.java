package com.kjsce.camplus.Utils.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.kjsce.camplus.Profile.ProfileActivity;
import com.kjsce.camplus.R;
import com.kjsce.camplus.Search.User;

import java.util.List;

/**
 * Created by Mayur on 27-04-2018.
 */

public class SearchProfileAdapter extends RecyclerView.Adapter<SearchProfileAdapter.SearchProfileViewHolder> {

    private static final String TAG = "SearchProfileAdapter";

    private final Context context;
    private final List<User> profileList;

    public SearchProfileAdapter(Context context, List<User> profileList) {
        Log.d(TAG, "SearchProfileAdapter: constructor");

        this.context = context;
        this.profileList = profileList;

    }

    public SearchProfileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: executing onCreateViewHolder");

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_user, parent, false);
        return new SearchProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchProfileViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: executing onBindHolder");

        final User user = profileList.get(position);

        holder.userName.setText(user.getUserName());

        holder.userLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra("CallingActivity", "AnyActivity");
                intent.putExtra("UserId", user.getUserId());
                context.startActivity(intent);
            }
        });

    }

    @Override

    public int getItemCount() {

        return profileList.size();
    }

    class SearchProfileViewHolder extends RecyclerView.ViewHolder {

        private static final String TAG = "SearchProfileViewHolder";

        TextView userName;
        LinearLayout userLinearLayout;

        public SearchProfileViewHolder(View itemView) {
            super(itemView);
            Log.d(TAG, "SearchProfileViewHolder: executing SearchProfileViewHolder");

            userName = itemView.findViewById(R.id.user_info);
            userLinearLayout = itemView.findViewById(R.id.user_linearLayout);
        }
    }
}
