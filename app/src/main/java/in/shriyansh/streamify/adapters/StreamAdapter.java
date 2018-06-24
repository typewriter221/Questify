package in.shriyansh.streamify.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
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
import in.shriyansh.streamify.activities.ImageActivity;
import in.shriyansh.streamify.database.DbContract;
import in.shriyansh.streamify.database.DbMethods;
import in.shriyansh.streamify.network.URLs;
import in.shriyansh.streamify.utils.Constants;
import in.shriyansh.streamify.utils.PreferenceUtils;
import in.shriyansh.streamify.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by shriyansh on 11/10/15.
 */
public class StreamAdapter extends CursorAdapter implements URLs {
    private static final String TAG = StreamAdapter.class.getSimpleName();

    private final Context context;
    private final int white;
    private final int pink;

    private Dialog authorDialog;

    private final DbMethods dbMethods;
    private final RequestQueue volleyQueue;

    /**
     * Stream adapter constructor.
     *
     * @param context   Activity context
     * @param cursor    Database cursor
     */
    public StreamAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        this.context = context;
        white = context.getResources().getColor(R.color.white);
        pink = context.getResources().getColor(R.color.pink500);
        dbMethods = new DbMethods(context);
        volleyQueue = Volley.newRequestQueue(context);
        initAuthorDialog();
    }

    private void initAuthorDialog() {
        authorDialog = new Dialog(context);
        authorDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //setting custom layout to dialog
        authorDialog.setContentView(R.layout.contact_thumbnail);
        authorDialog.setCancelable(true);
    }

    @Override
    public int getItemViewType(int position) {
        Cursor cursor = getCursor();
        cursor.moveToPosition(position);
        if (cursor.getInt(cursor.getColumnIndex(DbContract.Streams.COLUMN_GLOBAL_ID))
                == Constants.INSTRUCTIONS_RECORD_ID) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View v;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (getItemViewType(cursor.getPosition()) == 0) {
            v = inflater.inflate(R.layout.instruction_item, parent, false);
        } else {
            v = inflater.inflate(R.layout.stream_item, parent, false);
        }
        return v;
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {

        if (getItemViewType(cursor.getPosition()) == 1) {
            TextView title;
            TextView subtitle;
            TextView description;
            TextView authorName;
            TextView authorEmail;
            TextView streamParentTv;
            final Button subscribe;
            ImageView authorImage;
            ImageView streamImageView;

            title = (TextView)view.findViewById(R.id.stream_title);
            subtitle = (TextView)view.findViewById(R.id.stream_subtitle);
            description = (TextView)view.findViewById(R.id.stream_description);
            streamImageView = (ImageView)view.findViewById(R.id.stream_image);
            authorName = (TextView)view.findViewById(R.id.stream_author_name);
            authorEmail = (TextView)view.findViewById(R.id.stream_author_email);
            subscribe = (Button)view.findViewById(R.id.btn_subscribe);
            authorImage = (ImageView)view.findViewById(R.id.stream_author_image);
            streamParentTv = (TextView)view.findViewById(R.id.stream_parent_1);

            authorName.setTag(cursor.getPosition());
            authorImage.setTag(cursor.getPosition());
            subscribe.setTag(cursor.getInt(cursor.getColumnIndex(
                    DbContract.Streams.COLUMN_GLOBAL_ID)));

            fetchAndSetData(cursor, title, subtitle, description, authorName, authorEmail,
                    streamParentTv, subscribe, authorImage, streamImageView);

            authorImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showAuthorDialog(view, cursor);
                }
            });

            authorName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showAuthorDialog(view, cursor);
                }
            });
        } else {
            TextView gotIt;
            TextView titleTv;
            TextView descriptionTv;

            gotIt = (TextView)view.findViewById(R.id.instruction_done);
            titleTv = (TextView)view.findViewById(R.id.instruction_title);
            descriptionTv = (TextView)view.findViewById(R.id.instruction_description);

            fetchAndSetInstructionData(cursor, titleTv, descriptionTv, gotIt);
        }
    }

    /**
     *  Fetches list item data and plugs into item view.
     *
     * @param cursor            Database cursor
     * @param title             Stream title text view
     * @param subtitle          Stream subtitle text view
     * @param description       Stream description text view
     * @param authorName        Stream author name text view
     * @param authorEmail       Stream author email text view
     * @param streamParentTv    Stream parent text view
     * @param subscribe         Stream subscribe button
     * @param authorImage       Stream author image view
     * @param streamImageView   Stream image view
     */
    private void fetchAndSetData(final Cursor cursor, final TextView title, final TextView subtitle,
                                 final TextView description, final TextView authorName,
                                 final TextView authorEmail, final TextView streamParentTv,
                                 final Button subscribe, final ImageView authorImage,
                                 final ImageView streamImageView) {
        String streamTitle;
        String streamSubtitle;
        String streamDescription;
        String streamImageUrl;
        String streamAuthorName;
        String streamAuthorImage;
        String streamAuthorEmail;
        StringBuilder streamParent = new StringBuilder();
        final int isSubscribed;

        streamTitle = cursor.getString(cursor.getColumnIndex(DbContract.Streams.COLUMN_TITLE));
        streamSubtitle = cursor.getString(cursor.getColumnIndex(
                DbContract.Streams.COLUMN_SUBTITLE));
        streamDescription = cursor.getString(cursor.getColumnIndex(
                DbContract.Streams.COLUMN_DESCRIPTION));
        streamImageUrl  = cursor.getString(cursor.getColumnIndex(DbContract.Streams.COLUMN_IMAGE));
        isSubscribed = cursor.getInt(cursor.getColumnIndex(
                DbContract.Streams.COLUMN_IS_SUBSCRIBED));

        try {
            JSONObject authorJson = new JSONObject(cursor.getString(cursor.getColumnIndex(
                    DbContract.Streams.COLUMN_AUTHOR)));
            streamAuthorName = authorJson.getString(Constants.JSON_KEY_AUTHOR_NAME);
            streamAuthorEmail = authorJson.getString(Constants.JSON_KEY_AUTHOR_EMAIL);
            streamAuthorImage = authorJson.getString(Constants.JSON_KEY_AUTHOR_IMAGE_URL);
        } catch (JSONException e) {
            e.printStackTrace();
            streamAuthorName = "";
            streamAuthorEmail = "";
            streamAuthorImage = "";
        }

        try {
            JSONArray bodyJsonArray = new JSONArray(cursor.getString(cursor.getColumnIndex(
                    DbContract.Streams.COLUMN_PARENT_BODIES)));
            for (int i = 0;i < bodyJsonArray.length();i++) {
                JSONObject tagJson = bodyJsonArray.getJSONObject(i);
                streamParent.append(tagJson.getString("name")).append(" ");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        plugDataToView(title, subtitle, description, authorName, authorEmail, streamParentTv,
                subscribe, authorImage, streamImageView, streamTitle, streamSubtitle,
                streamDescription, streamImageUrl, streamAuthorName, streamAuthorImage,
                streamAuthorEmail, streamParent.toString(), isSubscribed);
    }

    /**
     *  Plugs data into list item views.
     *
     * @param title             Stream title text view
     * @param subtitle          Stream subtitle text view
     * @param description       Stream description text view
     * @param authorName        Stream author name text view
     * @param authorEmail       Stream author email text view
     * @param streamParentTv    Stream parent text view
     * @param subscribe         Stream subscribe button
     * @param authorImage       Stream author image view
     * @param streamImageView   Stream image view
     * @param streamTitle       Stream title
     * @param streamSubtitle    Stream subtitle
     * @param streamDescription Stream description
     * @param streamImageUrl    Stream image URL
     * @param streamAuthorName  Stream author name
     * @param streamAuthorImage Stream author image URL
     * @param streamAuthorEmail Stream author email
     * @param streamParent      Stream parents
     * @param isSubscribed      Is stream subscribed
     */
    private void plugDataToView(final TextView title, final TextView subtitle,
                                 final TextView description, final TextView authorName,
                                 final TextView authorEmail, final TextView streamParentTv,
                                 final Button subscribe, final ImageView authorImage,
                                 final ImageView streamImageView, final String streamTitle,
                                 final String streamSubtitle, final String streamDescription,
                                 final String streamImageUrl, final String streamAuthorName,
                                 final String streamAuthorImage, String streamAuthorEmail,
                                 final String streamParent, final int isSubscribed) {
        streamParentTv.setText(streamParent);
        streamParentTv.setVisibility(View.VISIBLE);
        if (streamParent.contentEquals("")) {
            streamParentTv.setVisibility(View.GONE);
        }

        title.setText(streamTitle);
        subtitle.setText(streamSubtitle);
        description.setText(streamDescription);
        authorName.setText(streamAuthorName);

        authorEmail.setText(streamAuthorEmail);
        setImageOnView(context, streamAuthorImage, authorImage, R.drawable.ic_person_black_24dp);
        if (streamImageUrl.contentEquals("")) {
            streamImageView.setVisibility(View.GONE);
        } else {
            setImageOnView(context, streamImageUrl, streamImageView, R.drawable.placeholder);
        }
        //authorPost.setText(streamAuthorPost);
        if (isSubscribed == 1) {
            subscribe.setBackgroundResource(R.drawable.button_solid);
            subscribe.setTextColor(white);
            subscribe.setText(context.getResources().getString(R.string.stream_unsubscribe));

        } else {
            subscribe.setBackgroundResource(R.drawable.button_selector);
            subscribe.setTextColor(pink);
            subscribe.setText(context.getResources().getString(R.string.stream_subscribe));
        }

        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int globalId = (Integer) view.getTag();
                if (isSubscribed == 1) {
                    subscribe.setBackgroundResource(R.drawable.button_selector);
                    subscribe.setTextColor(pink);
                    subscribe.setText(context.getResources().getString(R.string.stream_subscribe));
                    dbMethods.subscribeStream(globalId + "", false);
                    unsubscribeStream(PreferenceUtils.getStringPreference(context,
                            PreferenceUtils.PREF_USER_GLOBAL_ID),globalId + "");
                } else {
                    subscribe.setBackgroundResource(R.drawable.button_solid);
                    subscribe.setTextColor(white);
                    subscribe.setText(context.getResources()
                            .getString(R.string.stream_unsubscribe));
                    dbMethods.subscribeStream(globalId + "", true);
                    subscribeStream(PreferenceUtils.getStringPreference(context,
                            PreferenceUtils.PREF_USER_GLOBAL_ID),globalId + "");
                }
                changeCursor(dbMethods.queryStreams(null, null, null,
                        DbContract.Streams.COLUMN_GLOBAL_ID + " DESC ", 0));
                notifyDataSetChanged();
            }
        });
    }

    /**
     * Fetches and sets instruction data.
     *
     * @param cursor        Database cursor
     * @param titleTv       Title text view
     * @param descriptionTv Description text view
     * @param gotIt         Done text view
     */
    private void fetchAndSetInstructionData(final Cursor cursor, final TextView titleTv,
                                            final TextView descriptionTv, final TextView gotIt) {
        String title;
        String description;

        title = cursor.getString(cursor.getColumnIndex(DbContract.Streams.COLUMN_TITLE));
        description = cursor.getString(cursor.getColumnIndex(
                DbContract.Streams.COLUMN_DESCRIPTION));

        plugDataToInstructionsView(titleTv, descriptionTv, gotIt, title, description);
    }

    /**
     * Plugs data into instruction view.
     *
     * @param titleTv       Title text view
     * @param descriptionTv Description text view
     * @param gotIt         Done text view
     * @param title         Title
     * @param description   Description
     */
    private void plugDataToInstructionsView(final TextView titleTv, final TextView descriptionTv,
                                            final TextView gotIt, final String title,
                                            final String description) {
        titleTv.setText(title);
        descriptionTv.setText(description);

        gotIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbMethods.deleteStreams(DbContract.Streams.COLUMN_GLOBAL_ID + " = ? ",
                        new String[]{Constants.INSTRUCTIONS_RECORD_ID + ""});
                changeCursor(dbMethods.queryStreams(null,null,null,
                        DbContract.Streams.COLUMN_GLOBAL_ID + " DESC ",0));
                notifyDataSetChanged();
            }
        });
    }

    /**
     * Shows author dialog.
     *
     * @param view List item view
     * @param cursor Database cursor
     */
    private void showAuthorDialog(View view, final Cursor cursor) {
        final TextView name;
        final TextView email;
        final TextView contact;
        final ImageView image;
        String streamAuthorName;
        String streamAuthorEmail;
        String streamAuthorContact;
        String streamAuthorImage;

        int position = (Integer) view.getTag();
        cursor.moveToPosition(position);

        try {
            JSONObject authorJson = new JSONObject(cursor.getString(cursor.getColumnIndex(
                    DbContract.Streams.COLUMN_AUTHOR)));
            streamAuthorName = authorJson.getString(Constants.JSON_KEY_AUTHOR_NAME);
            streamAuthorEmail = authorJson.getString(Constants.JSON_KEY_AUTHOR_EMAIL);
            streamAuthorContact = authorJson.getString(Constants.JSON_KEY_AUTHOR_CONTACT);
            streamAuthorImage = authorJson.getString(Constants.JSON_KEY_AUTHOR_IMAGE_URL);
        } catch (JSONException e) {
            e.printStackTrace();
            streamAuthorName = "";
            streamAuthorEmail = "";
            streamAuthorContact = "";
            streamAuthorImage = "";
        }

        name = (TextView) authorDialog.findViewById(R.id.contact_name);
        email = (TextView) authorDialog.findViewById(R.id.contact_email);
        contact = (TextView) authorDialog.findViewById(R.id.contact_contact);
        image = (ImageView) authorDialog.findViewById(R.id.author_image);

        name.setText(streamAuthorName);
        email.setText(streamAuthorEmail);
        contact.setText(streamAuthorContact);
        setImageOnView(context, streamAuthorImage, image, R.drawable.ic_person_black_24dp);

        authorDialog.show();

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject authorJson = new JSONObject(cursor.getString(cursor.getColumnIndex(
                            DbContract.Streams.COLUMN_AUTHOR)));
                    fireImageActivityIntent(context,
                            authorJson.getString(Constants.JSON_KEY_AUTHOR_IMAGE_URL),
                            authorJson.getString(Constants.JSON_KEY_AUTHOR_NAME),
                            authorJson.getString(Constants.JSON_KEY_AUTHOR_EMAIL),
                            authorJson.getString(Constants.JSON_KEY_AUTHOR_CONTACT));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     *  Fires intent for image immersive view activity.
     *
     * @param context           Activity context
     * @param imageUrl          Image URL
     * @param imageTitle        Image Title
     * @param imageSubtitle     Image Subtitle
     * @param imageDescription  Image Description
     */
    private void fireImageActivityIntent(final Context context, final String imageUrl,
                                         final String imageTitle,
                                         final String imageSubtitle,
                                         final String imageDescription) {
        Intent intent = new Intent(context, ImageActivity.class);
        intent.putExtra(ImageActivity.INTENT_KEY_CONTENT_URL, Utils.getUsableDropboxUrl(imageUrl));
        intent.putExtra(ImageActivity.INTENT_KEY_CONTENT_TITLE, imageTitle);
        intent.putExtra(ImageActivity.INTENT_KEY_CONTENT_SUBTITLE, imageSubtitle);
        intent.putExtra(ImageActivity.INTENT_KEY_CONTENT_DESCRIPTION, imageDescription);
        context.startActivity(intent);
    }

    /**
     * Sets image on image view.
     *
     * @param context               Activity context
     * @param imageUrl              Image URL
     * @param imageView             Image view
     * @param placeholderResourceId Placeholder resource id
     */
    private void setImageOnView(final Context context, final String imageUrl,
                                final ImageView imageView, final int placeholderResourceId) {
        Picasso.with(context)
                .load(Uri.parse(Utils.getUsableDropboxUrl(imageUrl)))
                .placeholder(placeholderResourceId)
                .error(placeholderResourceId)
                .into(imageView);
    }

    private void subscribeStream(String userId, final String streamId) {
        Map<String, String> params = new HashMap<>();
        params.put(Constants.STREAM_SUBSCRIBE_PARAM_USER_ID, userId + "");
        params.put(Constants.STREAM_SUBSCRIBE_PARAM_STREAM_ID, streamId + "");
        Log.d(TAG,params.toString());

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST,
                SUBSCRIBE_STREAM, new JSONObject(params), new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject resp) {
                Log.d(TAG, resp.toString());
                try {
                    String status = resp.getString(Constants.RESPONSE_STATUS_KEY);
                    if (status.equals(Constants.RESPONSE_STATUS_VALUE_OK)) {
                        dbMethods.subscribeStream(streamId,true);
                        changeCursor(dbMethods.queryStreams(null, null,
                                null, DbContract.Streams.COLUMN_GLOBAL_ID
                                        + " DESC ", 0));
                        notifyDataSetChanged();
                    } else {
                        dbMethods.subscribeStream(streamId,false);
                        changeCursor(dbMethods.queryStreams(null, null,
                                null, DbContract.Streams.COLUMN_GLOBAL_ID
                                        + " DESC ", 0));
                        notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dbMethods.subscribeStream(streamId,false);
                    changeCursor(dbMethods.queryStreams(null, null,
                            null, DbContract.Streams.COLUMN_GLOBAL_ID
                                    + " DESC ", 0));
                    notifyDataSetChanged();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.toString());
                dbMethods.subscribeStream(streamId,false);
                changeCursor(dbMethods.queryStreams(null, null, null,
                        DbContract.Streams.COLUMN_GLOBAL_ID + " DESC ", 0));
                notifyDataSetChanged();
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

    private void unsubscribeStream(String userId, final String streamId) {
        Map<String, String> params = new HashMap<>();
        params.put(Constants.STREAM_SUBSCRIBE_PARAM_USER_ID, userId + "");
        params.put(Constants.STREAM_SUBSCRIBE_PARAM_STREAM_ID, streamId + "");
        Log.d(TAG,params.toString());

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST,
                UN_SUBSCRIBE_STREAM, new JSONObject(params), new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject resp) {
                Log.d(TAG, resp.toString());
                try {
                    String status = resp.getString(Constants.RESPONSE_STATUS_KEY);
                    if (status.equals(Constants.RESPONSE_STATUS_VALUE_OK)) {
                        dbMethods.subscribeStream(streamId,false);
                        changeCursor(dbMethods.queryStreams(null, null,
                                null, DbContract.Streams.COLUMN_GLOBAL_ID
                                        + " DESC ", 0));
                        notifyDataSetChanged();
                    } else {
                        dbMethods.subscribeStream(streamId,true);
                        changeCursor(dbMethods.queryStreams(null, null,
                                null, DbContract.Streams.COLUMN_GLOBAL_ID
                                        + " DESC ", 0));
                        notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dbMethods.subscribeStream(streamId,true);
                    changeCursor(dbMethods.queryStreams(null, null,
                            null, DbContract.Streams.COLUMN_GLOBAL_ID
                                    + " DESC ", 0));
                    notifyDataSetChanged();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.toString());
                dbMethods.subscribeStream(streamId,true);
                changeCursor(dbMethods.queryStreams(null, null, null,
                        DbContract.Streams.COLUMN_GLOBAL_ID + " DESC ", 0));
                notifyDataSetChanged();
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
}
