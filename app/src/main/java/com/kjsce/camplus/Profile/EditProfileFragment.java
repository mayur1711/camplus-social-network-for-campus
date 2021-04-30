package com.kjsce.camplus.Profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kjsce.camplus.R;
import com.kjsce.camplus.Utils.LoginInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Mayur on 10-02-2018.
 */

public class EditProfileFragment extends Fragment {

    private static final String TAG = "EditProfileFragment";
    private static final String EDIT_PROFILE_URL = "https://ajjainaakash.000webhostapp.com/edit_profile.php";
    EditText editProfileName, editProfileEmail, editProfileUsername, editProfileContactNo;
    AutoCompleteTextView editProfileDepartment, editProfileYear;
    LoginInfo loginInfo;
    ImageView backArrow, saveBtn;
    String name, department, year, contactNo;
    int userId;
    String[] yearArray = new String[]{"FE", "SE", "TE", "BE"};
    String[] departmentArray = new String[]{"CS", "MECH", "EXTC", "IT", "ETRX"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView: executing onCreate EditProfileFragment");

        View view = inflater.inflate(R.layout.fragment_editprofile, container, false);

        backArrow = view.findViewById(R.id.editprofile_back);
        saveBtn = view.findViewById(R.id.editprofile_save);
        editProfileName = view.findViewById(R.id.editprofile_name);
        editProfileEmail = view.findViewById(R.id.editprofile_email);
        editProfileUsername = view.findViewById(R.id.editprofile_username);
        editProfileDepartment = view.findViewById(R.id.editprofile_department);
        editProfileYear = view.findViewById(R.id.editprofile_year);
        editProfileContactNo = view.findViewById(R.id.editprofile_contact_no);

        setDataToEditText();

        ArrayAdapter<String> adapterYear = new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_spinner_dropdown_item, yearArray);
        ArrayAdapter<String> adapterDepartment = new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_spinner_dropdown_item, departmentArray);

        editProfileYear.setAdapter(adapterYear);
        editProfileDepartment.setAdapter(adapterDepartment);

        editProfileYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfileYear.showDropDown();
            }
        });
        editProfileDepartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfileDepartment.showDropDown();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on save tick");

                getDataFromEditText();

                if (profileDetailsUnchanged()) {
                    Toast.makeText(getActivity(), "No changes made!", Toast.LENGTH_SHORT).show();
                } else {
                    setDataToDatabase();
                }
            }
        });

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating back to ProfileActivity");

                getActivity().finish();
            }
        });
        return view;
    }

    public void setDataToEditText() {
        Log.d(TAG, "setData: executing setData");

        loginInfo = LoginInfo.getInstance();
        userId = loginInfo.getUserId();

        editProfileName.setText(loginInfo.getName());
        editProfileEmail.setText(loginInfo.getEmail());
        editProfileUsername.setText(loginInfo.getUsername());
        editProfileDepartment.setText(loginInfo.getDepartment());
        editProfileYear.setText(loginInfo.getYear());
        editProfileContactNo.setText(loginInfo.getContactNo());
    }

    public void getDataFromEditText() {
        Log.d(TAG, "getData: executing getData");

        name = editProfileName.getText().toString().trim();
        department = editProfileDepartment.getText().toString().trim();
        year = editProfileYear.getText().toString().trim();
        contactNo = editProfileContactNo.getText().toString().trim();
    }

    public boolean profileDetailsUnchanged() {
        Log.d(TAG, "profileDetailsUnchanged: executing profileDetailsChanged");

        return Objects.equals(name, loginInfo.getName()) && Objects.equals(department, loginInfo.getDepartment()) &&
                Objects.equals(year, loginInfo.getYear()) && Objects.equals(contactNo, loginInfo.getContactNo());
    }

    public void setDataToDatabase() {

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.contains("Update Successful")) {
                    loginInfo.setName(name);
                    loginInfo.setContactNo(contactNo);
                    loginInfo.setYear(year);
                    loginInfo.setDepartment(department);
                    Toast.makeText(getActivity(), "Success!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        };

        EditProfileRequest editProfileRequest = new EditProfileRequest(userId, listener, errorListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(editProfileRequest);
    }

    public class EditProfileRequest extends StringRequest {

        private static final String TAG = "EditProfileRequest";

        private final Map<String, String> params;

        public EditProfileRequest(int userId, Response.Listener<String> listener, Response.ErrorListener errorListener) {
            super(Method.POST, EDIT_PROFILE_URL, listener, errorListener);
            Log.d(TAG, "EditProfileRequest: constructor");

            params = new HashMap<>();
            params.put("user_id", String.valueOf(userId));
            params.put("name", name);
            params.put("contact_no", contactNo);
            params.put("year", year);
            params.put("department", department);
        }

        @Override
        public Map<String, String> getParams() {
            return params;
        }
    }
}
