package in.shriyansh.streamify.activities;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import in.shriyansh.streamify.R;
import in.shriyansh.streamify.database.DbContract;
import in.shriyansh.streamify.database.DbMethods;
import in.shriyansh.streamify.network.Urls;
import in.shriyansh.streamify.utils.Constants;
import in.shriyansh.streamify.utils.PreferenceUtils;
import in.shriyansh.streamify.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class StreamDetailActivity extends AppCompatActivity implements Urls,View.OnClickListener {
    private static final String TAG = StreamDetailActivity.class.getSimpleName();

    private CollapsingToolbarLayout toolBarLayout;
    private FloatingActionButton fab;
    private Toolbar toolbar;

    private TextView subtitleSteamTv;
    private TextView descriptionTv;
    private TextView authorNameTv;
    private TextView authorPostTv;
    private TextView post1NameTv;
    private TextView post1PostTv;
    private TextView post1EmailTv;
    private TextView post1ContactTv;
    private TextView post2NameTv;
    private TextView post2PostTv;
    private TextView post2EmailTv;
    private TextView post2ContactTv;
    private TextView post3NameTv;
    private TextView post3PostTv;
    private TextView post3EmailTv;
    private TextView post3ContactTv;
    private LinearLayout post1Layout;
    private LinearLayout post2Layout;
    private LinearLayout post3Layout;
    private ImageView bgHeader;
    private ImageView post1Image;
    private ImageView post2Image;
    private ImageView post3Image;
    private ImageView authorImage;

    private LinearLayout feedbackLayout;
    private LinearLayout progressLayout;
    private EditText etFeedBack;
    private Button btnFeedBAck;
    private ProgressBar feedbackProgress;

    private long streamGlobalId;

    private RequestQueue volleyQueue;
    private DbMethods dbMethods;

    public static final String INTENT_EXTRA_KEY_STREAM_ID = "stream_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream_detail);

        initUi();
        initToolbar();

        dbMethods = new DbMethods(this);

        volleyQueue = Volley.newRequestQueue(this);

        Intent intent = getIntent();
        long streamId = intent.getLongExtra(INTENT_EXTRA_KEY_STREAM_ID, 0);

        fetchAndSetDataOnViews(streamId);
    }

    /**
     * Fetches and sets Data for current Stream onto the views.
     *
     * @param streamId Local Stream id
     */
    private void fetchAndSetDataOnViews(final long streamId) {
        final Cursor cursor = dbMethods.queryStreams(null, DbContract.Streams._ID
                + " = ? ", new String[]{streamId + ""}, null, 0);
        while (cursor.moveToNext()) {
            String title;
            String subtitle;
            String description;
            String streamImageUrl;
            StringBuilder parents;
            String authorName;
            String authorEmail;
            String authorContact;
            String authorImageUrl;
            String[] positionHolderName;
            String[] positionHolderPost;
            String[] positionHolderEmail;
            String[] positionHolderContact;
            String[] positionHolderImage;
            int positionHoldersCount;

            streamGlobalId = cursor.getInt(cursor.getColumnIndex(
                    DbContract.Streams.COLUMN_GLOBAL_ID));
            title = cursor.getString(cursor.getColumnIndex(DbContract.Streams.COLUMN_TITLE));
            subtitle = cursor.getString(cursor.getColumnIndex(DbContract.Streams.COLUMN_SUBTITLE));
            description = cursor.getString(cursor.getColumnIndex(
                    DbContract.Streams.COLUMN_DESCRIPTION));
            streamImageUrl = cursor.getString(cursor.getColumnIndex(
                    DbContract.Streams.COLUMN_IMAGE));
            parents = new StringBuilder();

            try {
                JSONArray bodyJsonArray = new JSONArray(cursor.getString(
                        cursor.getColumnIndex(DbContract.Streams.COLUMN_PARENT_BODIES)));
                for (int i = 0;i < bodyJsonArray.length();i++) {
                    JSONObject tagJson = bodyJsonArray.getJSONObject(i);
                    parents.append(tagJson.getString(Constants.JSON_KEY_PARENT_NAME)).append(" ");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                JSONObject authorJson = new JSONObject(cursor.getString(
                        cursor.getColumnIndex(DbContract.Streams.COLUMN_AUTHOR)));
                authorName = authorJson.getString(Constants.JSON_KEY_AUTHOR_NAME);
                authorEmail = authorJson.getString(Constants.JSON_KEY_AUTHOR_EMAIL);
                authorContact = authorJson.getString(Constants.JSON_KEY_AUTHOR_CONTACT);
                authorImageUrl = authorJson.getString(Constants.JSON_KEY_AUTHOR_IMAGE_URL);
            } catch (JSONException e) {
                e.printStackTrace();
                authorName = "";
                authorEmail = "";
                authorImageUrl = "";
                authorContact = "";
            }

            try {
                JSONArray positionHoldersJsonArray = new JSONArray(
                        cursor.getString(cursor.getColumnIndex(
                                DbContract.Streams.COLUMN_POSITION_HOLDERS)));
                positionHoldersCount = positionHoldersJsonArray.length();

                positionHolderName = new String[positionHoldersJsonArray.length()];
                positionHolderEmail = new String[positionHoldersJsonArray.length()];
                positionHolderPost = new String[positionHoldersJsonArray.length()];
                positionHolderContact = new String[positionHoldersJsonArray.length()];
                positionHolderImage = new String[positionHoldersJsonArray.length()];

                for (int i = 0;i < positionHoldersJsonArray.length();i++) {
                    JSONObject positionHolderJson = positionHoldersJsonArray.getJSONObject(i);
                    positionHolderName[i] = positionHolderJson.getString(
                            Constants.JSON_KEY_POSITION_HOLDER_NAME);
                    positionHolderPost[i] = positionHolderJson.getString(
                            Constants.JSON_KEY_POSITION_HOLDER_POST);
                    positionHolderEmail[i] = positionHolderJson.getString(
                            Constants.JSON_KEY_POSITION_HOLDER_EMAIL);
                    positionHolderContact[i] = positionHolderJson.getString(
                            Constants.JSON_KEY_POSITION_HOLDER_CONTACT);
                    positionHolderImage[i] = positionHolderJson.getString(
                            Constants.JSON_KEY_POSITION_HOLDER_IMAGE_URL);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                positionHoldersCount = 0;
                positionHolderName = null;
                positionHolderEmail = null;
                positionHolderPost = null;
                positionHolderContact = null;
                positionHolderImage = null;
            }

            plugDataOnViews(title, subtitle, parents.toString(), description, streamImageUrl,
                    authorName, authorEmail, authorImageUrl, authorContact, positionHoldersCount,
                    positionHolderName, positionHolderEmail, positionHolderPost,
                    positionHolderContact, positionHolderImage);
        }
    }

    /**
     * Plugs data onto views.
     *
     * @param title                 Stream title
     * @param subtitle              Stream subtitle
     * @param parents               Stream parents
     * @param description           Stream description
     * @param streamImageUrl        Stream image url
     * @param authorName            Stream author name
     * @param authorEmail           Stream author email
     * @param authorImageUrl        Stream author image url
     * @param authorContact         Stream author image url
     * @param positionHoldersCount  Stream position holders count
     * @param positionHolderName    Stream position holders name array
     * @param positionHolderEmail   Stream position holders email array
     * @param positionHolderPost    Stream position holders post array
     * @param positionHolderContact Stream position holders contact array
     * @param positionHolderImage   Stream position holders image array
     */
    private void plugDataOnViews(final String title, final String subtitle, final String parents,
                                 final String description, final String streamImageUrl,
                                 final String authorName, final String authorEmail,
                                 final String authorImageUrl, final String authorContact,
                                 final int positionHoldersCount,final String[] positionHolderName,
                                 final String[] positionHolderEmail,
                                 final String[] positionHolderPost,
                                 final String[] positionHolderContact,
                                 final String[] positionHolderImage) {
        setStreamHeaderView(title, subtitle, parents, description, streamImageUrl);
        setAuthorView(authorName, authorEmail, authorContact, authorImageUrl);
        setPostHoldersViews(positionHoldersCount, positionHolderName, positionHolderEmail,
                positionHolderPost, positionHolderContact, positionHolderImage);
    }

    /**
     * Binds the ui to Java instances.
     */
    private void initUi() {
        toolbar = findViewById(R.id.toolbar);
        toolBarLayout = findViewById(R.id.toolbar_layout);

        subtitleSteamTv = findViewById(R.id.subtitle_stream);
        descriptionTv = findViewById(R.id.description);
        bgHeader = findViewById(R.id.bgheader);
        authorNameTv = findViewById(R.id.author_name);
        authorPostTv = findViewById(R.id.author_post);
        authorImage = findViewById(R.id.stream_author_image);

        post1Layout = findViewById(R.id.post_1_layout);
        post1Image = findViewById(R.id.post_holder_1_image);
        post1NameTv = findViewById(R.id.post_holder_1_name);
        post1PostTv = findViewById(R.id.post_holder_1_post);
        post1EmailTv = findViewById(R.id.post_holder_1_email);
        post1ContactTv = findViewById(R.id.post_holder_1_contact);

        post2Layout = findViewById(R.id.post_2_layout);
        post2Image = findViewById(R.id.post_holder_2_image);
        post2NameTv = findViewById(R.id.post_holder_2_name);
        post2PostTv = findViewById(R.id.post_holder_2_post);
        post2EmailTv = findViewById(R.id.post_holder_2_email);
        post2ContactTv = findViewById(R.id.post_holder_2_contact);

        post3Layout = findViewById(R.id.post_3_layout);
        post3Image = findViewById(R.id.post_holder_3_image);
        post3NameTv = findViewById(R.id.post_holder_3_name);
        post3PostTv = findViewById(R.id.post_holder_3_post);
        post3EmailTv = findViewById(R.id.post_holder_3_email);
        post3ContactTv = findViewById(R.id.post_holder_3_contact);

        etFeedBack = findViewById(R.id.feedback_tv);
        feedbackLayout = findViewById(R.id.feedback_layout);
        progressLayout = findViewById(R.id.progress_layout);
        btnFeedBAck = findViewById(R.id.feedback_button);
        fab = findViewById(R.id.fab);
        feedbackProgress = findViewById(R.id.feedback_progress);
        feedbackProgress.setIndeterminate(true);
        feedbackProgress.getIndeterminateDrawable().setColorFilter(
                getResources().getColor(R.color.ColorPrimary),
                android.graphics.PorterDuff.Mode.MULTIPLY);
        btnFeedBAck.setOnClickListener(this);
    }

    /**
     * Initializes the toolbar.
     */
    private void initToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        toolBarLayout.setTitle(getTitle());
    }

    /**
     * Sets position holder views.
     *
     * @param positionHoldersCount  Position holders count
     * @param positionHolderName    Position holders name array
     * @param positionHolderEmail   Position holders email array
     * @param positionHolderPost    Position holders post array
     * @param positionHolderContact Position holders contact array
     * @param positionHolderImage   Position holders image url array
     */
    private void setPostHoldersViews(final int positionHoldersCount,
                                     final String[] positionHolderName,
                                     final String[] positionHolderEmail,
                                     final String[] positionHolderPost,
                                     final String[] positionHolderContact,
                                     final String[] positionHolderImage) {
        post1Layout.setVisibility(View.GONE);
        post2Layout.setVisibility(View.GONE);
        post3Layout.setVisibility(View.GONE);

        for (int i = 0;i < positionHoldersCount;i++) {
            if (i == 0) {
                setPositionHolderView(post1NameTv, post1PostTv, post1EmailTv, post1ContactTv,
                        post1Image, positionHolderName[0], positionHolderEmail[0],
                        positionHolderImage[0], positionHolderContact[0], positionHolderPost[0],
                        post1Layout);
            }

            if (i == 1) {
                setPositionHolderView(post2NameTv, post2PostTv, post2EmailTv, post2ContactTv,
                        post2Image, positionHolderName[1], positionHolderEmail[1],
                        positionHolderImage[1], positionHolderContact[1], positionHolderPost[1],
                        post2Layout);
            }

            if (i == 2) {
                setPositionHolderView(post3NameTv, post3PostTv, post3EmailTv, post3ContactTv,
                        post3Image, positionHolderName[2], positionHolderEmail[2],
                        positionHolderImage[2], positionHolderContact[2], positionHolderPost[2],
                        post2Layout);
            }
        }
    }

    /**
     * Sets position holder view.
     *
     * @param tvPositionHolderName      Position holder name tv
     * @param tvPositionHolderPost      Position holder post tv
     * @param tvPositionHolderEmail     Position holder email tv
     * @param tvPositionHolderContact    Position holder contact tv
     * @param ivPostHolderImage         Position holder image iv
     * @param positionHolderName        Position holders name
     * @param positionHolderEmail       Position holders email
     * @param positionHolderImage       Position holders image url
     * @param positionHolderContact     Position holders contact
     * @param positionHolderPost        Position holders post
     */
    private void setPositionHolderView(final TextView tvPositionHolderName,
                                       final TextView tvPositionHolderPost,
                                       final TextView tvPositionHolderEmail,
                                       final TextView tvPositionHolderContact,
                                       final ImageView ivPostHolderImage,
                                       final String positionHolderName,
                                       final String positionHolderEmail,
                                       final String positionHolderImage,
                                       final String positionHolderContact,
                                       final String positionHolderPost,
                                       final LinearLayout positionHolderLayout) {
        tvPositionHolderName.setText(positionHolderName);
        tvPositionHolderPost.setText(positionHolderPost);
        tvPositionHolderEmail.setText(positionHolderEmail);
        tvPositionHolderContact.setText(positionHolderContact);
        setImageOnView(positionHolderImage, ivPostHolderImage, R.drawable.ic_person_black_24dp);

        ivPostHolderImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAuthorDialog(positionHolderName, positionHolderEmail, positionHolderPost,
                        positionHolderContact, positionHolderImage);

            }
        });
        tvPositionHolderName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAuthorDialog(positionHolderName, positionHolderEmail, positionHolderPost,
                        positionHolderContact, positionHolderImage);
            }
        });
        positionHolderLayout.setVisibility(View.VISIBLE);
    }

    /**
     * Sets stream header view.
     *
     * @param title             Stream title
     * @param subtitle          Stream subtitle
     * @param parents           Stream parents
     * @param description       Stream description
     * @param streamImageUrl    Stream image url
     */
    private void setStreamHeaderView(final String title, final String subtitle,
                                     final String parents, final String description,
                                     final String streamImageUrl) {
        toolBarLayout.setTitle(title);
        toolBarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        subtitleSteamTv.setText(subtitle + " from " + parents);
        descriptionTv.setText(description);
        bgHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            fireImageActivityIntent(streamImageUrl, title, subtitle, description);
            }
        });
        setImageOnView(streamImageUrl, bgHeader, R.drawable.placeholder);
    }

    /**
     * Sets stream author view.
     *
     * @param authorName        Stream authors name
     * @param authorEmail       Stream authors email
     * @param authorContact     Stream authors contact
     * @param authorImageUrl    Stream authors image url
     */
    private void setAuthorView(final String authorName, final String authorEmail,
                               final String authorContact, final String authorImageUrl) {
        final String postAuthor = "Author";
        authorNameTv.setText(authorName);
        authorPostTv.setText(authorEmail);
        setImageOnView(authorImageUrl, authorImage, R.drawable.ic_person_black_24dp);

        authorImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAuthorDialog(authorName,authorEmail,postAuthor,
                        authorContact,authorImageUrl);
            }
        });

        authorNameTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAuthorDialog(authorName,authorEmail,postAuthor,
                        authorContact,authorImageUrl);
            }
        });

        setFabEmail(authorEmail);
    }

    /**
     * Sets FAB email action.
     *
     * @param authorEmail Stream authors email
     */
    private void setFabEmail(final String authorEmail) {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = PreferenceUtils.getStringPreference(
                        StreamDetailActivity.this,PreferenceUtils.PREF_USER_NAME);
                String email = PreferenceUtils.getStringPreference(
                        StreamDetailActivity.this,PreferenceUtils.PREF_USER_EMAIL);
                String contact = PreferenceUtils.getStringPreference(
                        StreamDetailActivity.this,PreferenceUtils.PREF_USER_CONTACT);
                String userGlobalId = PreferenceUtils.getStringPreference(
                        StreamDetailActivity.this,PreferenceUtils.PREF_USER_GLOBAL_ID);
                String[] emailAddress = new String[2];
                emailAddress[0] = authorEmail;
                emailAddress[1] = getResources().getString(R.string.author_email_shriyansh);
                fireMessageIntent(emailAddress, "#" + userGlobalId
                        + getResources().getString(R.string.feedback_email_subject),
                        "Hi,\n\t\tI would like to...\n\n\n" + name + "\n" + email + "\n"
                                + contact);
            }
        });
    }

    /**
     * Fires an intent to send message/mail.
     *
     * @param emailAddresses    array of email addresses
     * @param subject           subject for the message
     * @param text              message text
     */
    private void fireMessageIntent(String[] emailAddresses, String subject, String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("\"text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, emailAddresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        startActivity(Intent.createChooser(intent, "Send an Email"));
    }

    /**
     * Shows author dialog with details.
     *
     * @param streamAuthorName      Stream author's name
     * @param streamAuthorEmail     Stream author's email
     * @param streamAuthorPost      Stream author's position
     * @param streamAuthorContact   Stream author's contact number
     * @param streamAuthorImage     Stream author's image url
     */
    private void showAuthorDialog(final String streamAuthorName, final String streamAuthorEmail,
                                  String streamAuthorPost, final String streamAuthorContact,
                                  final String streamAuthorImage) {
        Dialog authorDialog;
        authorDialog = new Dialog(StreamDetailActivity.this);
        authorDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        authorDialog.setContentView(R.layout.contact_thumbnail);
        authorDialog.setCancelable(true);
        final TextView name;
        final TextView email;
        final TextView contact;
        final TextView post;
        final ImageView image;

        image = authorDialog.findViewById(R.id.author_image);
        name = authorDialog.findViewById(R.id.contact_name);
        email = authorDialog.findViewById(R.id.contact_email);
        contact = authorDialog.findViewById(R.id.contact_contact);
        post = authorDialog.findViewById(R.id.contact_post);

        name.setText(streamAuthorName);
        email.setText(streamAuthorEmail);
        contact.setText(streamAuthorContact);
        post.setText(streamAuthorPost);
        setImageOnView(streamAuthorImage, image, R.drawable.ic_person_black_24dp);

        authorDialog.show();

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            fireImageActivityIntent(streamAuthorImage, streamAuthorName, streamAuthorEmail,
                    streamAuthorContact);
            }
        });
    }

    /**
     * Fire image immersive view activity.
     *
     * @param imageUrl          image url
     * @param imageTitle        image title
     * @param imageSubtitle     image subtitle
     * @param imageDescription  image description
     */
    private void fireImageActivityIntent(final String imageUrl,
                                         final String imageTitle,
                                         final String imageSubtitle,
                                         final String imageDescription) {
        Intent intent = new Intent(StreamDetailActivity.this, ImageActivity.class);
        intent.putExtra(ImageActivity.INTENT_KEY_CONTENT_URL, Utils.getUsableDropboxUrl(imageUrl));
        intent.putExtra(ImageActivity.INTENT_KEY_CONTENT_TITLE, imageTitle);
        intent.putExtra(ImageActivity.INTENT_KEY_CONTENT_SUBTITLE, imageSubtitle);
        intent.putExtra(ImageActivity.INTENT_KEY_CONTENT_DESCRIPTION, imageDescription);
        startActivity(intent);
    }

    /**
     * Sets image onto view.
     *
     * @param imageUrl              image url
     * @param imageView             image view
     * @param placeholderResourceId image placeholder resource id
     */
    private void setImageOnView(final String imageUrl, final ImageView imageView,
                                final int placeholderResourceId) {
        Picasso.with(this)
                .load(Uri.parse(Utils.getUsableDropboxUrl(imageUrl)))
                .placeholder(placeholderResourceId)
                .error(placeholderResourceId)
                .into(imageView);
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
                    sendFeedback(PreferenceUtils.getStringPreference(
                            StreamDetailActivity.this,PreferenceUtils.PREF_USER_GLOBAL_ID),
                            streamGlobalId + "",feedback);
                }
                break;
            default:
        }
    }

    /**
     * Sends feedback for the stream to the server using volley.
     *
     * @param userId    User's global Id
     * @param streamId  Stream's global Id
     * @param feedback  Feedback text
     */
    private void sendFeedback(String userId, String streamId, String feedback) {

        Map<String, String> params = new HashMap<>();
        params.put(Constants.STREAM_FEEDBACK_PARAM_USER_ID,userId);
        params.put(Constants.STREAM_FEEDBACK_PARAM_STREAM_ID,streamId);
        params.put(Constants.STREAM_FEEDBACK_PARAM_TEXT,feedback);
        Log.d(TAG,params.toString());

        toggleProgressLayout();

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST,
                Urls.FEEDBACK, new JSONObject(params), new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject resp) {
                Log.d(TAG, resp.toString());
                try {
                    String status = resp.getString(Constants.RESPONSE_STATUS_KEY);
                    if (status.equals("200")) {
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
}
