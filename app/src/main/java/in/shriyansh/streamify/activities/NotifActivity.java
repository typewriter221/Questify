


/**
* THIS ACTIVITY WAS CREATED TO SERVE THE PURPOSE OF
 * SENDING EVENT NOTIFICATION DETAILS BUT DUE TO SOME
 * WEIRD ERRORS THE PURPOSE WAS TRANSFERRED TO "SAMPLE"
 * ACTIVITY
 * SO BASICALLY THIS ACTIVITY HAS NO ROLE IN THE APP
**/


package in.shriyansh.streamify.activities;

import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

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
import needle.Needle;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static in.shriyansh.streamify.network.Urls.EVENT_NOTIFICATION_URL;
import static in.shriyansh.streamify.network.Urls.GET_STREAMS;
import static in.shriyansh.streamify.network.Urls.LIST_ALL_STREAMS;

public class NotifActivity extends AppCompatActivity {

    private static final String TAG = "NotifActivity";
    private ConstraintLayout NotifLayout;
    private String[] streams;
    private TextView status_textview;
    private Button create_event_submit;
    private CheckBox[] streambox;
    private LinearLayout stream_checkbox_layout;

    private String httpstatus;
    private int[] streamCheckBools;

    private int i;
    private JSONObject jsonNotif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif);

        Toast.makeText(this, "heloo", Toast.LENGTH_LONG).show();
//        initUI();
//        getStreams();
//        Log.e(TAG, streams.toString());

//        createStreamCheckboxes();
        makeJsonNotif();

        create_event_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Needle.onBackgroundThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        OkHttpClient client = new OkHttpClient();
                        MediaType mediaType
                                = MediaType.parse("application/json; charset=utf-8");
//
//                        /*************************************************************/
//
                        RequestBody body = RequestBody.create(mediaType, "{\n\t\"title\":\"MyEvent\"," +
                                "\n\t\"description\":\"MyDescription\"," +
                                "\n\t\"imageURL\":\"\"," +
                                "\n\t\"location\":\"G-11 IIT BHU\"," +
                                "\n\t\"authorEmail\":\"akshay.sharma.mat16@iitbhu.ac.in\"," +
                                "\n\t\"streams\":[\n\t\t\t\"MyStream\"\n\t\t]," +
                                "\n\t\"tags\":[\n\t\t\t\"Noobs\",\n\t\t\t\"Python\"\n\t\t]\n}"
                        );
//
//                        /*****************************************************************/
//
//                        RequestBody body = RequestBody.create(JSON, jsonNotif.toString());
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
                            JSONObject object;
                            try {
                                object=new JSONObject(response.body().string());
                                httpstatus = object.getString("status");
                                Log.e(TAG, httpstatus);
                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });


            }
        });

    }

    private void makeJsonNotif() {
//        try {
//
//            jsonNotif = new JSONObject();
//
//            jsonNotif.put("title", "Mytitle23");
//            jsonNotif.put("description", "MyDescription23");
//            jsonNotif.put("imageURL", "");
//            jsonNotif.put("location", "G-11 IITBHU");
//            jsonNotif.put("authorEmail", PreferenceUtils.getStringPreference(NotifActivity.this, PreferenceUtils.PREF_USER_EMAIL));
//
//            JSONArray streamArray = new JSONArray();
//            JSONArray tagArray = new JSONArray();
//
//            streamArray.put("MyStream");
//
//            tagArray.put("Java");
//            tagArray.put("Hardcore");
//
////            for (int i=0; i<streams.length; i++) {
////                if (streamCheckBools[i]==1) {
////                    streamArray.put(streams[i]);
////                }
////            }
//
//            jsonNotif.put("streams", streamArray);
//            jsonNotif.put("tags", tagArray);
//        }
//        catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

    private void createStreamCheckboxes() {

        streambox = new CheckBox[streams.length];
        streamCheckBools = new int[streams.length];

        for (i=0; i<streams.length; i++) {
            streambox[i] = new CheckBox(NotifActivity.this);
            streambox[i].setText(streams[i]);
            streambox[i].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            streambox[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    streamCheckBools[i] = (isChecked ? 1 : 0);
                }
            });

            stream_checkbox_layout.addView(streambox[i]);
        }

    }

    private void initUI() {

        NotifLayout = findViewById(R.id.notif_layout);
        create_event_submit = findViewById(R.id.btn_create_event);
        status_textview = findViewById(R.id.status_text);
        stream_checkbox_layout = findViewById(R.id.stream_check_box_layout);

    }

    private void getStreams() {

//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
//                LIST_ALL_STREAMS,
//                null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            String status = response.getString("status");
//                            if (status.equals(Constants.RESPONSE_STATUS_VALUE_200)) {
//                                int l = response.getJSONArray("response").length();
//                                JSONArray resp = response.getJSONArray("response");
//                                streams = new String[l];
//
//                                for (int i = 0; i<l; i++) {
//                                    JSONObject user_stream_i = resp.getJSONObject(i);
//                                    streams[i] = user_stream_i.getString("title");
//                                }
//
//                                Log.e(TAG, status);
//
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Log.e(TAG, "JSONException");
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        VolleyLog.e(TAG, "Error: " + error.toString());
//                        showSnackBar("Network Unreachable!","RETRY");
//
//                    }
//                }) {
//            @Override
//            public Map<String, String> getHeaders() {
//                Map<String, String> headers = new HashMap<>();
//                headers.put(Constants.HTTP_HEADER_CONTENT_TYPE_KEY,
//                        Constants.HTTP_HEADER_CONTENT_TYPE_JSON);
//                return headers;
//            }
//        };
//
//        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
//                Constants.HTTP_INITIAL_TIME_OUT,
//                Constants.HTTP_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//        requestQueue.add(jsonObjectRequest);


    }
    private void showSnackBar(String msg, String action) {
        Snackbar snackbar = Snackbar
                .make(NotifLayout, msg, Snackbar.LENGTH_LONG)
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
