package com.kjsce.camplus.CreatePost;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kjsce.camplus.R;
import com.kjsce.camplus.Utils.LoginInfo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CreatePostActivity extends AppCompatActivity {

    private static final String TAG = "CreatePostActivity";
    private TextInputEditText postTitle, postContent, postTags;
    private Button postBtn;
    private ImageButton addImageBtn, addTagsBtn;
    private Bitmap bitmap;
    private ImageView postImage;
    private String title, content, image, tagNameString, tagIdString;
    private FrameLayout frameLayout;
    private ScrollView scrollView;
    private int PICK_IMAGE_REQUEST = 1;
    private List<Integer> tagIds = new ArrayList<Integer>();
    private List<String> tagNames = new ArrayList<String>();
    private static String UPLOAD_POST_URL = "https://ajjainaakash.000webhostapp.com/upload_post.php";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        Log.d(TAG, "onCreate: executing onCreate CreatePostActivity");

        postTitle = findViewById(R.id.createPost_title);
        postContent = findViewById(R.id.createPost_content);
        postTags = findViewById(R.id.createPost_tags);
        postBtn = findViewById(R.id.createPost_post_btn);
        postImage = findViewById(R.id.createPost_image);
        addImageBtn = findViewById(R.id.createPost_addImage_btn);
        addTagsBtn = findViewById(R.id.createPost_addTags_btn);
        frameLayout = findViewById(R.id.createPost_frameLayout);
        scrollView = findViewById(R.id.createPost_scrollView);

        addImageBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Log.d(TAG, "onClick: on Add Image button click");
                showFileChooser();
            }
        });

        addTagsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: on Add Tag button click");

                TagsFragment tagsFragment = new TagsFragment();
                FragmentTransaction fragmentTransaction = CreatePostActivity.this
                        .getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.createPost_frameLayout, tagsFragment);
                fragmentTransaction.commit();
                hideLayout();
            }
        });

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "onClick: on Post button click");

                title = postTitle.getText().toString().trim();
                content = postContent.getText().toString().trim();

                if (title.length() == 0) {
                    Toast.makeText(CreatePostActivity.this, "Enter Post title!", Toast.LENGTH_SHORT).show();
                } else if (content.length() == 0) {
                    Toast.makeText(CreatePostActivity.this, "Enter Post content!", Toast.LENGTH_SHORT).show();
                } else if (tagIds.size() == 0) {
                    Toast.makeText(CreatePostActivity.this, "Select Post tags!", Toast.LENGTH_SHORT).show();
                } else {
                    uploadPostInfo();
                }

            }
        });
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();

            try {
                //Getting the Bitmap from Gallery
                postImage.setVisibility(View.VISIBLE);
                bitmap = MediaStore.Images.Media.getBitmap(CreatePostActivity.this.getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                postImage.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showFileChooser() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void uploadPostInfo() {

        Log.d(TAG, "uploadImage: executing uploadImage");

        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(CreatePostActivity.this, "Uploading...",
                "Please wait...", false, false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_POST_URL,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        //Dismissing the progress dialog
                        loading.dismiss();

                        Log.d(TAG, "onResponse: " + s);
                        if (s.trim().equals("connection successsuccess")) {
                            Toast.makeText(CreatePostActivity.this, "SUCCESS!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(CreatePostActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();

                        //Showing toast
                        Toast.makeText(CreatePostActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Log.d(TAG, "getParams: executing getParams");

                //Converting Bitmap to String
                if (bitmap == null) {
                    image = "null";
                } else {
                    image = getStringImage(bitmap);
                }

                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                LoginInfo loginInfo = LoginInfo.getInstance();
                //Adding parameters
                params.put("user_id", String.valueOf(loginInfo.getUserId()));
                params.put("image", image);
                params.put("title", title);
                params.put("content", content);
                params.put("tag_id", tagIdString.substring(0, tagIdString.length() - 1));

                //returning parameters
                return params;
            }
        };
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(CreatePostActivity.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    public void hideLayout() {
        Log.d(TAG, "hideLayout: executing hidelayout");
        frameLayout.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);
        postBtn.setVisibility(View.GONE);
    }

    public void showLayout() {
        Log.d(TAG, "showLayout: executing showLayout");
        frameLayout.setVisibility(View.GONE);
        scrollView.setVisibility(View.VISIBLE);
        postBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: resume");

        this.getTagArray();
        if (tagIds.size() != 0) {
            tagNameString = "";
            tagIdString = "";
            for (int i = 0; i < tagIds.size(); i++) {
                tagNameString += tagNames.get(i) + ", ";
                tagIdString += tagIds.get(i) + ",";
            }
            postTags.setText(tagNameString.substring(0, tagNameString.length()-2));
        }
    }

    private void getTagArray() {
        Log.d(TAG, "getTagIdArray: executing getTagArray");

        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                tagIds = intent.getIntegerArrayListExtra("tagId");
                tagNames = intent.getStringArrayListExtra("tagName");
            }
        }, new IntentFilter("sending tagId and tagName Arrays"));
    }
}
