package in.shriyansh.streamify.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.squareup.picasso.Picasso;

import in.shriyansh.streamify.BuildConfig;
import in.shriyansh.streamify.R;
import in.shriyansh.streamify.network.Urls;
import in.shriyansh.streamify.utils.Constants;
import in.shriyansh.streamify.utils.PreferenceUtils;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Activity instance for About Us Page.
 */
public class AboutUsActivity extends AppCompatActivity implements Urls, View.OnClickListener {
    private static final String TAG = AboutUsActivity.class.getSimpleName();

    private Toolbar toolbar;
    private ImageView ivShriyansh;
    private ImageView ivHemant;
    private ImageView ivYash;
    private ImageView ivSatya;
    private EditText etFeedBack;
    private Button btnFeedBAck;
    private ProgressBar feedbackProgress;
    private LinearLayout feedbackLayout;
    private LinearLayout progressLayout;

    private RequestQueue volleyQueue;
    private FirebaseAnalytics firebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        initUi();
        initToolbar();

        volleyQueue = Volley.newRequestQueue(this);
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        setDevImages(DEV_SATYA_IMAGE, ivSatya);
        setDevImages(DEV_SHRIYANSH_IMAGE, ivShriyansh);
        setDevImages(DEV_HEMANT_IMAGE, ivHemant);
        setDevImages(DEV_YASH_IMAGE, ivYash);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.feedback_button:
                String feedback = etFeedBack.getText().toString();
                if (feedback.contentEquals("")) {
                    showSnackBar(R.string.snackbar_feedback_empty);
                } else {
                    sendAppFeedback(PreferenceUtils.getStringPreference(AboutUsActivity.this,
                        PreferenceUtils.PREF_USER_GLOBAL_ID), feedback);
                }
                break;
            case R.id.shriyansh:
                viewAuthorImage(getResources().getString(R.string.author_name_shriyansh),
                    getResources().getString(R.string.author_post_android_developer),
                    getResources().getString(R.string.author_email_shriyansh),
                    DEV_SHRIYANSH_IMAGE);
                break;
            case R.id.hemant:
                viewAuthorImage(getResources().getString(R.string.author_name_hemant),
                    getResources().getString(R.string.author_post_android_developer),
                    getResources().getString(R.string.author_email_hemant),
                    DEV_HEMANT_IMAGE);
                break;
            case R.id.satya:
                viewAuthorImage(getResources().getString(R.string.author_name_satya),
                    getResources().getString(R.string.author_post_web_developer),
                    getResources().getString(R.string.author_email_satya),
                    DEV_SATYA_IMAGE);
                break;
            case R.id.yash:
                viewAuthorImage(getResources().getString(R.string.author_name_yash),
                    getResources().getString(R.string.author_post_django_developer),
                    getResources().getString(R.string.author_email_yash),
                    DEV_YASH_IMAGE);
                break;
            default:
        }
    }

    /**
     * Initializes UI Elements.
     */
    private void initUi() {
        toolbar = findViewById(R.id.toolbar);

        ivShriyansh = findViewById(R.id.shriyansh);
        ivYash = findViewById(R.id.yash);
        ivHemant = findViewById(R.id.hemant);
        ivSatya = findViewById(R.id.satya);

        etFeedBack = findViewById(R.id.feedback_tv);
        btnFeedBAck = findViewById(R.id.feedback_button);
        feedbackLayout = findViewById(R.id.feedback_layout);
        progressLayout = findViewById(R.id.progress_layout);
        feedbackProgress = findViewById(R.id.feedback_progress);

        feedbackProgress.setIndeterminate(true);
        feedbackProgress.getIndeterminateDrawable().setColorFilter(
            getResources().getColor(R.color.ColorPrimary),
            android.graphics.PorterDuff.Mode.MULTIPLY);
        setClickListeners();
    }

    /**
     * Initializes Toolbar.
     */
    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Set Click listeners on UI elements.
     */
    private void setClickListeners() {
        ivShriyansh.setOnClickListener(this);
        ivSatya.setOnClickListener(this);
        ivHemant.setOnClickListener(this);
        ivYash.setOnClickListener(this);
        btnFeedBAck.setOnClickListener(this);
    }

    /**
     * Sets Developer images on UI.
     *
     * @param imageUrl Image URL for dev image
     * @param imageView View instance for setting image into
     */
    private void setDevImages(final String imageUrl, final ImageView imageView) {
        Picasso.with(this)
            .load(Uri.parse(imageUrl))
            .placeholder(R.drawable.ic_person_black_24dp)
            .error(R.drawable.ic_person_black_24dp)
            .into(imageView);
    }

    /**
     * Fires intent to open up the image and its details in ImageActivity.
     *
     * @param authorName Author's Name
     * @param authorPost Author's Position
     * @param authorEmail Author's Email
     * @param authorImageUrl Author's Image Url
     */
    private void viewAuthorImage(final String authorName, final String authorPost,
                                 final String authorEmail, final String authorImageUrl) {
        Intent intent = new Intent(AboutUsActivity.this,ImageActivity.class);
        intent.putExtra(ImageActivity.INTENT_KEY_CONTENT_URL, authorImageUrl);
        intent.putExtra(ImageActivity.INTENT_KEY_CONTENT_TITLE, authorName);
        intent.putExtra(ImageActivity.INTENT_KEY_CONTENT_SUBTITLE, authorPost);
        intent.putExtra(ImageActivity.INTENT_KEY_CONTENT_DESCRIPTION, authorEmail);
        startActivity(intent);
    }

    /**
     * Send app Feedback to server using volley.
     *
     * @param userId User's global Id
     * @param feedback feedback text
     */
    private void sendAppFeedback(final String userId, final String feedback) {

        Map<String, String> params = new HashMap<>();
        params.put(Constants.FEEDBACK_PARAM_USER_ID, userId);
        params.put(Constants.FEEDBACK_PARAM_TEXT, feedback);
        Log.d(TAG, params.toString());

        toggleProgressLayout();

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST,
            APP_FEEDBACK, new JSONObject(params), new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject resp) {
                Log.d(TAG, resp.toString());
                try {
                    String status = resp.getString(Constants.RESPONSE_STATUS_KEY);
                    if (status.equals(Constants.RESPONSE_STATUS_VALUE_OK)) {
                        showSnackBar(R.string.snackbar_feedback_success);
                        etFeedBack.setText("");
                    } else {
                        showSnackBar(R.string.snackbar_error);
                    }
                    toggleFeedbackLayout();
                } catch (JSONException e) {
                    e.printStackTrace();
                    showSnackBarWithWirelessSetting(R.string.snackbar_cannot_reach_servers);
                    toggleFeedbackLayout();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.toString());
                showSnackBarWithWirelessSetting(R.string.snackbar_cannot_reach_servers);
                toggleFeedbackLayout();
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
        Snackbar.make(findViewById(R.id.container),
            getResources().getString(stringResource), Snackbar.LENGTH_LONG).show();
    }

    /**
     * Shows Snackbar with Action button for wireless settings.
     *
     * @param stringResource Resource id for string to be shown on snackbar
     */
    private void showSnackBarWithWirelessSetting(final int stringResource) {
        Snackbar.make(findViewById(R.id.container),
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

    /**
     * Toggles on Progress view over feedback view.
     */
    private void toggleProgressLayout() {
        feedbackLayout.setVisibility(View.GONE);
        progressLayout.setVisibility(View.VISIBLE);
    }

    /**
     * Toggle back feedback View from progress view.
     */
    private void toggleFeedbackLayout() {
        feedbackLayout.setVisibility(View.VISIBLE);
        progressLayout.setVisibility(View.GONE);
    }

    /**
     * Logs Event onto Firebase analytics.
     *
     * @param metricName    Metric name
     */
    private void logEvent(final String metricName, final String metricValue) {
        Bundle logBundle = new Bundle();
        logBundle.putString(FirebaseAnalytics.Param.ITEM_ID, BuildConfig.VERSION_CODE + "");
        logBundle.putString(FirebaseAnalytics.Param.ITEM_NAME,
            AboutUsActivity.class.getSimpleName());
        logBundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, metricName + ":" + metricValue);
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, logBundle);
    }
}
