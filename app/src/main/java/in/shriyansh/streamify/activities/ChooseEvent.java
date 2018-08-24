package in.shriyansh.streamify.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import java.util.HashMap;
import java.util.Map;

import in.shriyansh.streamify.R;
import in.shriyansh.streamify.utils.Constants;
import in.shriyansh.streamify.utils.PreferenceUtils;

import static in.shriyansh.streamify.network.Urls.LIST_ALL_EVENTS;

public class ChooseEvent extends AppCompatActivity {

    private RadioGroup events_radio;
    private String[] events;
    private RadioButton[] event;
    private Button btn_reg_event;

    private String[] available_events;
    private String[] corresponding_event_ids;

    private RequestQueue volleyQueue;

    private final String TAG = "ChooseEventActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        volleyQueue = Volley.newRequestQueue(this);

        events_radio = findViewById(R.id.events_radio);
        events = getResources().getStringArray(R.array.events_array);

        getEvents();
        Log.e(TAG, available_events[0]);
        event = new RadioButton[available_events.length];

        for(int i=0; i<available_events.length; i++) {
            event[i] = new RadioButton(this);
            event[i].setText(available_events[i]);
            event[i].setId(i);
            event[i].setTextColor(Color.WHITE);
            event[i].setTextSize(getResources().getDimension(R.dimen.radioTextSize));
            events_radio.addView(event[i]);
        }

        btn_reg_event = findViewById(R.id.btn_event_reg);
        btn_reg_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String event_id = corresponding_event_ids[events_radio.getCheckedRadioButtonId()];
                PreferenceUtils.setStringPreference(ChooseEvent.this, PreferenceUtils.PREF_USER_EVENT, event_id);

                Intent intent = new Intent(ChooseEvent.this, ChooseNumberMembers.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void getEvents() {

        //Get All Events

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET,
                LIST_ALL_EVENTS, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject resp) {
                Log.e(TAG, resp.toString());
                try {
                    String status = resp.getString(Constants.RESPONSE_STATUS_KEY);
                    if (status.equals(Constants.RESPONSE_STATUS_VALUE_200)) {

                        JSONArray jsonArray = resp.getJSONArray("response");
                        available_events = new String[jsonArray.length()];
                        corresponding_event_ids = new String[jsonArray.length()];
                        for (int i=0; i<jsonArray.length(); i++) {
                            available_events[i] = jsonArray.getJSONObject(i).getString("title");
                            corresponding_event_ids[i] = jsonArray.getJSONObject(i).getString("id");
                        }

                    }
                    else {
                        Toast.makeText(ChooseEvent.this, "Cannot fetch events:( Try Again later", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.toString());
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                Constants.HTTP_INITIAL_TIME_OUT,
                Constants.HTTP_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        volleyQueue.add(stringRequest);

    }

}
