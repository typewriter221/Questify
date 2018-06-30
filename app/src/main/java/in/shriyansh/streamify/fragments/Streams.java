package in.shriyansh.streamify.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import in.shriyansh.streamify.R;
import in.shriyansh.streamify.activities.StreamDetailActivity;
import in.shriyansh.streamify.adapters.StreamAdapter;
import in.shriyansh.streamify.database.DbContract;
import in.shriyansh.streamify.database.DbMethods;
import in.shriyansh.streamify.network.Urls;
import in.shriyansh.streamify.utils.Constants;
import in.shriyansh.streamify.utils.PreferenceUtils;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Fragment for streams.
 */
public class Streams extends Fragment implements Urls {
    private static final String TAG = Streams.class.getSimpleName();

    private SwipeRefreshLayout announcementRefreshLayout;
    private ListView streamListView;

    private StreamAdapter streamAdapter;

    private DbMethods dbMethods;
    private RequestQueue volleyQueue;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbMethods = new DbMethods(getActivity());
        volleyQueue = Volley.newRequestQueue(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.stream_layout,container,false);
        streamListView = view.findViewById(R.id.stream_list);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            streamListView.setNestedScrollingEnabled(true);
        }
        streamAdapter = new StreamAdapter(getActivity(),dbMethods.queryStreams(null,
                null,null,
                DbContract.Streams.COLUMN_GLOBAL_ID + " DESC ",0));
        streamListView.setAdapter(streamAdapter);
        announcementRefreshLayout = view.findViewById(R.id.news_refresh_layout);
        announcementRefreshLayout.setColorSchemeResources(R.color.ColorPrimary, R.color.pink500,
                R.color.teal500);

        streamListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (l != 1) {
                    Intent intent = new Intent(getActivity(), StreamDetailActivity.class);
                    intent.putExtra(StreamDetailActivity.INTENT_EXTRA_KEY_STREAM_ID,l);
                    startActivityForResult(intent, 3);
                }
            }
        });

        /*
         * Implementing refresh
         */
        announcementRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getStreams(PreferenceUtils.getStringPreference(getActivity(),
                        PreferenceUtils.PREF_USER_GLOBAL_ID));
            }
        });

        return view;
    }

    private void getStreams(String userId) {
        Map<String, String> params = new HashMap<>();
        params.put(Constants.STREAM_PARAM_USER_ID,userId);
        Log.d(TAG,params.toString());

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST,
                GET_STREAMS, new JSONObject(params), new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject resp) {
                Log.d(TAG, resp.toString());
                try {
                    String status = resp.getString(Constants.RESPONSE_STATUS_KEY);
                    if (status.equals(Constants.RESPONSE_STATUS_VALUE_OK)) {
                        long count = dbMethods.insertStreams(resp.getJSONObject("data")
                                .getJSONArray("streams"));
                        streamAdapter.changeCursor(dbMethods.queryStreams(null,
                                null,null,
                                DbContract.Streams.COLUMN_GLOBAL_ID + " DESC ",0));
                        streamAdapter.notifyDataSetChanged();

                        String streamCount = count == 0 ? "No" : count + "";
                        if (isAdded()) {
                            showSnackBar(streamCount + " new Streams.");
                        }
                    } else {
                        if (isAdded()) {
                            showSnackBar(R.string.snackbar_error_fetching_streams);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (isAdded()) {
                        showSnackBar(R.string.snackbar_error_fetching_streams);
                    }
                }
                announcementRefreshLayout.setRefreshing(false);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.toString());
                if (isAdded()) {
                    showSnackBarWithWirelessSetting(R.string.snackbar_cannot_reach_servers);
                }
                announcementRefreshLayout.setRefreshing(false);
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

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                Constants.HTTP_INITIAL_TIME_OUT,
                Constants.HTTP_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        volleyQueue.add(stringRequest);
    }

    /**
     * Shows Snackbar without any action button.
     *
     * @param stringResource Resource id for string to be shown on snackbar
     */
    private void showSnackBar(final int stringResource) {
        Snackbar.make(getActivity().findViewById(R.id.container),
                getResources().getString(stringResource), Snackbar.LENGTH_LONG).show();
    }

    /**
     * Shows Snackbar without any action button.
     *
     * @param string   String to be shown on snackbar
     */
    private void showSnackBar(final String string) {
        Snackbar.make(getActivity().findViewById(R.id.container),
                string, Snackbar.LENGTH_LONG).show();
    }

    /**
     * Shows Snackbar with Action button for wireless settings.
     *
     * @param stringResource Resource id for string to be shown on snackbar
     */
    private void showSnackBarWithWirelessSetting(final int stringResource) {
        Snackbar.make(getActivity().findViewById(R.id.container),
                getResources().getString(stringResource), Snackbar.LENGTH_LONG)
                .setAction(getResources().getString(R.string.snackbar_action_settings),
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                            }
                        })
                .show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getActivity();
    }
}