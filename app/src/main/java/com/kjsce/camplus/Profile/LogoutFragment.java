package com.kjsce.camplus.Profile;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.kjsce.camplus.Login.LoginActivity;
import com.kjsce.camplus.R;

/**
 * Created by Mayur on 10-02-2018.
 */

public class LogoutFragment extends Fragment {

    private static final String TAG = "LogoutFragment";

    private Button logoutBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_logout, container, false);

        logoutBtn = view.findViewById(R.id.logout_btn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        return view;
    }
}
