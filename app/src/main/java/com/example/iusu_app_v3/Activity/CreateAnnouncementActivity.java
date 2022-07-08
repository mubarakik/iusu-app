package com.example.iusu_app_v3.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.iusu_app_v3.Models.Student;
import com.example.iusu_app_v3.R;
import com.example.iusu_app_v3.SharedPreferenceManager;
import com.example.iusu_app_v3.URLs;
import com.example.iusu_app_v3.VolleyMultipartRequest;
import com.example.iusu_app_v3.VolleySingleton;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CreateAnnouncementActivity extends AppCompatActivity {

    TextInputLayout annTitleTIL,annDescriptionTIL;
    Button annSubmitButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_announcement);

        ActionBar actionBar = getSupportActionBar();
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        annTitleTIL= findViewById(R.id.ann_title_layout_tv);
        annDescriptionTIL=findViewById(R.id.ann_description_layout_tv);
        annSubmitButton=findViewById(R.id.submit_ann_btn);

        annSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadAnnouncement();
            }
        });



    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void uploadAnnouncement() {
        final String annTitle = annTitleTIL.getEditText().getText().toString();
        final String annDescription = annDescriptionTIL.getEditText().getText().toString();
        final String goId = SharedPreferenceManager.getInstance(this).getStudent().getGo_id();

        annTitleTIL.setError(null);
        annDescriptionTIL.setError(null);


        //first we will do the validations
        if (TextUtils.isEmpty(annTitle)) {
            annTitleTIL.setError("Please enter title");
            annTitleTIL.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(annDescription)) {
            annDescriptionTIL.setError("Please enter first name");
            annDescriptionTIL.requestFocus();
            return;
        }



        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_ANN_UPLOAD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("tagconvertstr", "["+response+"]");
                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            //if no error in response
                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("title", annTitle);
                params.put("description", annDescription);
                params.put("go_id",goId);
                return params;
            }
        };

        //adding the request to volley
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
        Intent intent = new Intent(CreateAnnouncementActivity.this,AnnouncementActivity.class);
        startActivity(intent);
    }

}