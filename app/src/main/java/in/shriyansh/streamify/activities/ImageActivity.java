package in.shriyansh.streamify.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import in.shriyansh.streamify.R;
import in.shriyansh.streamify.ui.TouchImageView;
import in.shriyansh.streamify.utils.Utils;

/**
 * Full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ImageActivity extends AppCompatActivity {

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;

    /**
     * Max zoom in multiplier for image.
     */
    private static final float MAX_ZOOM_LEVEL = 4f;

    private TextView titleTv;
    private TextView subtitleTv;
    private TextView descriptionTv;

    private TouchImageView contentView;
    private View controlsView;
    private boolean visible;

    public static final String INTENT_KEY_CONTENT_URL = "content_url";
    public static final String INTENT_KEY_CONTENT_TITLE = "content_title";
    public static final String INTENT_KEY_CONTENT_SUBTITLE = "content_subtitle";
    public static final String INTENT_KEY_CONTENT_DESCRIPTION = "content_description";
    private final Handler hideHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        visible = true;

        initUi();
        initActionBar();
        fetchAndPlugData();
    }

    private void initUi() {
        controlsView = findViewById(R.id.fullscreen_content_controls);
        contentView = (TouchImageView)findViewById(R.id.fullscreen_content);
        titleTv = (TextView)findViewById(R.id.image_title);
        subtitleTv = (TextView)findViewById(R.id.image_subtitle);
        descriptionTv = (TextView)findViewById(R.id.image_description);
        // Set up the user interaction to manually show or hide the system UI.
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });
        contentView.setMaxZoom(MAX_ZOOM_LEVEL);
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void fetchAndPlugData() {
        Intent intent = getIntent();
        String url = intent.getStringExtra(INTENT_KEY_CONTENT_URL);
        String title = intent.getStringExtra(INTENT_KEY_CONTENT_TITLE);
        String subtitle = intent.getStringExtra(INTENT_KEY_CONTENT_SUBTITLE);
        String description = intent.getStringExtra(INTENT_KEY_CONTENT_DESCRIPTION);
        plugDataToViews(url, title, subtitle, description);
    }

    private void plugDataToViews(final String url, final String title, final String subtitle,
                                 final String description) {
        titleTv.setText(title);
        subtitleTv.setText(subtitle);
        descriptionTv.setText(description);
        setImageOnView(url, contentView, R.drawable.placeholder);
    }

    private void setImageOnView(final String imageUrl, final TouchImageView imageView,
                                final int placeholderResourceId) {
        Picasso.with(this)
                .load(Uri.parse(Utils.getUsableDropboxUrl(imageUrl)))
                .placeholder(placeholderResourceId)
                .error(placeholderResourceId)
                .into(imageView);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button.
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggle() {
        if (visible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        controlsView.setVisibility(View.GONE);
        visible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        hideHandler.removeCallbacks(showPart2Runnable);
        hideHandler.postDelayed(hidePart2Runnable, UI_ANIMATION_DELAY);
    }

    private final Runnable hidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            contentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        contentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        visible = true;

        // Schedule a runnable to display UI elements after a delay
        hideHandler.removeCallbacks(hidePart2Runnable);
        hideHandler.postDelayed(showPart2Runnable, UI_ANIMATION_DELAY);
    }

    private final Runnable showPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            controlsView.setVisibility(View.VISIBLE);
        }
    };

    private final Runnable hideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     *
     * @param delayMillis Delay in milliseconds
     */
    private void delayedHide(int delayMillis) {
        hideHandler.removeCallbacks(hideRunnable);
        hideHandler.postDelayed(hideRunnable, delayMillis);
    }
}
