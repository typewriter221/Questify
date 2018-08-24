package in.shriyansh.streamify.activities;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import in.shriyansh.streamify.R;
import in.shriyansh.streamify.utils.Constants;
import in.shriyansh.streamify.utils.PreferenceUtils;
import in.shriyansh.streamify.network.Urls;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static in.shriyansh.streamify.network.Urls.EVENT_NOTIFICATION_URL;
import static in.shriyansh.streamify.network.Urls.GET_STREAMS;
import static in.shriyansh.streamify.network.Urls.LIST_ALL_STREAMS;

public class NotifActivity extends AppCompatActivity {

    private static final String TAG = "NotifActivity";
    private CoordinatorLayout NotifActivity;
    private RequestQueue requestQueue;
    private String[] streams;
    private Button create_event_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif);

        requestQueue = Volley.newRequestQueue(NotifActivity.this);

        initUI();
//        getStreams();

        create_event_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkHttpClient client = new OkHttpClient();

                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, "{\n\t\"title\":\"MyEvent\",\n\t\"description\":\"MyDescription\",\n\t\"imageURL\":\"\",\n\t\"location\":\"G-11 IIT BHU\",\n\t\"authorEmail\":\"akshay.sharma.mat16@iitbhu.ac.in\",\n\t\"streams\":[\n\t\t\t\"MyStream\"\n\t\t],\n\t\"tags\":[\n\t\t\t\"Noobs\",\n\t\t\t\"Python\"\n\t\t]\n}");
                okhttp3.Request request = new okhttp3.Request.Builder()
                        .url(EVENT_NOTIFICATION_URL)
                        .post(body)
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Cache-Control", "no-cache")
                        .addHeader("Postman-Token", "faa6d8dd-3b5c-420b-ba43-9eb1d413a7a6")
                        .build();

                try {
                    okhttp3.Response response = client.newCall(request).execute();
                    Log.e(TAG, response.body().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    private void initUI() {

        NotifActivity = findViewById(R.id.notif_layout);
        create_event_submit = findViewById(R.id.btn_create_event);

    }

    private void getStreams() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                LIST_ALL_STREAMS,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("status");
                            if (status.equals(Constants.RESPONSE_STATUS_VALUE_200)) {
                                int l = response.getJSONArray("response").length();
                                JSONArray resp = response.getJSONArray("response");
                                streams = new String[l];

                                for (int i = 0; i<l; i++) {
                                    JSONObject user_stream_i = resp.getJSONObject(i);
                                    streams[i] = user_stream_i.getString("title");
                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e(TAG, "Error: " + error.toString());
                        showSnackBar("Network Unreachable!","RETRY");

                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put(Constants.HTTP_HEADER_CONTENT_TYPE_KEY,
                        Constants.HTTP_HEADER_CONTENT_TYPE_JSON);
                return headers;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                Constants.HTTP_INITIAL_TIME_OUT,
                Constants.HTTP_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(jsonObjectRequest);


    }
    private void showSnackBar(String msg, String action) {
        Snackbar snackbar = Snackbar
                .make(NotifActivity, msg, Snackbar.LENGTH_LONG)
                .setAction(action, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        if (caseId == CASE_REGISTER) {
//                            register(name,rollno, email,contact);
//                        }
                    }
                });
        snackbar.setActionTextColor(getResources().getColor(R.color.pink500));
        snackbar.show();
    }

}
