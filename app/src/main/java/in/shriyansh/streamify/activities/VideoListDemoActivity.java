/*
 * Copyright 2012 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package in.shriyansh.streamify.activities;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewPropertyAnimator;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.OnFullscreenListener;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailLoader.ErrorReason;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.squareup.picasso.Picasso;

import in.shriyansh.streamify.R;
import in.shriyansh.streamify.database.DbContract;
import in.shriyansh.streamify.database.DbMethods;
import in.shriyansh.streamify.network.Urls;
import in.shriyansh.streamify.utils.Constants;
import in.shriyansh.streamify.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A sample Activity showing how to manage multiple YouTubeThumbnailViews in an adapter for display
 * in a List. When the list items are clicked, the video is played by using a YouTubePlayerFragment.
 * The demo supports custom fullscreen and transitioning between portrait and landscape without
 * rebuffering.
 */
@TargetApi(13)
public final class VideoListDemoActivity extends AppCompatActivity implements OnFullscreenListener,
    Urls {

  public static final String INTENT_KEY_NOTIFICATION_GLOBAL_ID = "news_global_id";
  /** The duration of the animation sliding up the video in portrait. */
  private static final int ANIMATION_DURATION_MILLIS = 300;
  /** The padding between the video list and the video in landscape orientation. */
  private static final int LANDSCAPE_VIDEO_PADDING_DP = 5;
  /** The request code when calling startActivityForResult to recover from an API service error. */
  private static final int RECOVERY_DIALOG_REQUEST = 1;
  DbMethods dbMethods;
  private VideoListFragment listFragment;
  private VideoFragment videoFragment;
  private View videoBox;
  private View closeButton;
  private boolean isFullscreen;

  private static void setLayoutSize(View view, int width, int height) {
    LayoutParams params = view.getLayoutParams();
    params.width = width;
    params.height = height;
    view.setLayoutParams(params);
  }

  private static void setLayoutSizeAndGravity(View view, int width, int height, int gravity) {
    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
    params.width = width;
    params.height = height;
    params.gravity = gravity;
    view.setLayoutParams(params);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.video_list_demo);
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    ActionBar actionBar =  getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
    }

    dbMethods = new DbMethods(this);

    listFragment = (VideoListFragment) getFragmentManager().findFragmentById(R.id.list_fragment);

    videoFragment =
        (VideoFragment) getFragmentManager().findFragmentById(R.id.video_fragment_container);

    videoBox = findViewById(R.id.video_box);
    closeButton = findViewById(R.id.close_button);
    videoBox.setVisibility(View.INVISIBLE);
    layout();

    checkYouTubeApi();
  }

  @Override
  protected void onResume() {
    super.onResume();
    Intent intent = getIntent();
    int parentGlobalId;
    parentGlobalId = intent.getIntExtra("news_global_id",0);

    Cursor cursor;
    if (parentGlobalId != 0) {
      cursor = dbMethods.queryContent(null, DbContract.Contents.COLUMN_PARENT_ID
          + " = ? ", new String[]{parentGlobalId + ""},
          DbContract.Contents.COLUMN_PARENT_ID + " DESC ,"
              + DbContract.Contents.COLUMN_GLOBAL_ID + "  DESC ", 0);

      if (cursor.getCount() == 0) {
        Snackbar.make(findViewById(R.id.container), "Please specify both Title and Content",
            Snackbar.LENGTH_LONG)
            .show();
      } else {
        while (cursor.moveToNext()) {
          String title = cursor.getString(cursor.getColumnIndex(DbContract.Contents.COLUMN_TITLE));
          String stream = cursor.getString(cursor.getColumnIndex(
              DbContract.Contents.COLUMN_STREAM));
          String videoId = cursor.getString(cursor.getColumnIndex(
              DbContract.Contents.COLUMN_VIDEO_ID));
          listFragment.playVideo(videoId, title, stream);
        }

      }
    }
  }

  private void checkYouTubeApi() {
    YouTubeInitializationResult errorReason =
        YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(this);
    if (errorReason.isUserRecoverableError()) {
      errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
    } else if (errorReason != YouTubeInitializationResult.SUCCESS) {
      String errorMessage =
          String.format(getString(R.string.error_player), errorReason.toString());
      Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == RECOVERY_DIALOG_REQUEST) {
      // Recreate the activity if user performed a recovery action
      recreate();
    }
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    layout();
  }

  @Override
  public void onFullscreen(boolean isFullscreen) {
    this.isFullscreen = isFullscreen;
    layout();
  }

  /**
   * Sets up the layout programatically for the three different states. Portrait, landscape or
   * fullscreen+landscape. This has to be done programmatically because we handle the orientation
   * changes ourselves in order to get fluent fullscreen transitions, so the xml layout resources
   * do not get reloaded.
   */
  private void layout() {
    boolean isPortrait =
        getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

    listFragment.getView().setVisibility(isFullscreen ? View.GONE : View.VISIBLE);
    listFragment.setLabelVisibility(isPortrait);
    closeButton.setVisibility(isPortrait ? View.VISIBLE : View.GONE);

    if (isFullscreen) {
      videoBox.setTranslationY(0); // Reset any translation that was applied in portrait.
      setLayoutSize(videoFragment.getView(), MATCH_PARENT, MATCH_PARENT);
      setLayoutSizeAndGravity(videoBox, MATCH_PARENT, MATCH_PARENT, Gravity.TOP | Gravity.LEFT);
    } else if (isPortrait) {
      setLayoutSize(listFragment.getView(), MATCH_PARENT, MATCH_PARENT);
      setLayoutSize(videoFragment.getView(), MATCH_PARENT, WRAP_CONTENT);
      setLayoutSizeAndGravity(videoBox, MATCH_PARENT, WRAP_CONTENT, Gravity.BOTTOM);
    } else {
      videoBox.setTranslationY(0); // Reset any translation that was applied in portrait.
      int screenWidth = dpToPx(getResources().getConfiguration().screenWidthDp);
      setLayoutSize(listFragment.getView(), screenWidth / 4, MATCH_PARENT);
      int videoWidth = screenWidth - screenWidth / 4 - dpToPx(LANDSCAPE_VIDEO_PADDING_DP);
      setLayoutSize(videoFragment.getView(), videoWidth, WRAP_CONTENT);
      setLayoutSizeAndGravity(videoBox, videoWidth, WRAP_CONTENT,
          Gravity.RIGHT | Gravity.CENTER_VERTICAL);
    }
  }

  /**
   * Listener for on click close.
   *
   * @param view  View
   */
  public void onClickClose(@SuppressWarnings("unused") View view) {
    listFragment.getListView().clearChoices();
    listFragment.getListView().requestLayout();
    videoFragment.pause();
    ViewPropertyAnimator animator = videoBox.animate()
        .translationYBy(videoBox.getHeight())
        .setDuration(ANIMATION_DURATION_MILLIS);
    runOnAnimationEnd(animator, new Runnable() {
      @Override
      public void run() {
        videoBox.setVisibility(View.INVISIBLE);
      }
    });
  }

  @TargetApi(16)
  private void runOnAnimationEnd(ViewPropertyAnimator animator, final Runnable runnable) {
    if (Build.VERSION.SDK_INT >= 16) {
      animator.withEndAction(runnable);
    } else {
      animator.setListener(new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
          runnable.run();
        }
      });
    }
  }

  private int dpToPx(int dp) {
    return (int) (dp * getResources().getDisplayMetrics().density + 0.5f);
  }

  /**
   * A fragment that shows a static list of videos.
   */
  public static class VideoListFragment extends ListFragment {

    //private  List<VideoEntry> VIDEO_LIST;
    //static {
    //     ArrayList<VideoEntry> list = new ArrayList<VideoEntry>();
    //
    //      list.add(new VideoEntry("YouTube Collection", "Y_UmWdcTrrc"));
    //      list.add(new VideoEntry("GMail Tap", "1KhZKNZO8mQ"));
    //      list.add(new VideoEntry("Chrome Multitask", "UiLSiqyDf4Y"));
    //      list.add(new VideoEntry("Google Fiber", "re0VRK6ouwI"));
    //      list.add(new VideoEntry("Autocompleter", "blB_X38YSxQ"));
    //      list.add(new VideoEntry("GMail Motion", "Bu927_ul_X0"));
    //      list.add(new VideoEntry("Translate for Animals", "3I24bSteJpw"));
    //VIDEO_LIST = Collections.unmodifiableList(list);
    //}




    ArrayList<VideoEntry> videoList;
    private View videoBox;
    private DbMethods dbMethods;
    private PageAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      dbMethods = new DbMethods(getActivity());
      videoList = new ArrayList<>();


      Cursor cursor = dbMethods.queryContent(null, DbContract.Contents.COLUMN_TYPE
          + " = ? ",new String[]{DbContract.Contents.VALUE_TYPE_VIDEO + ""},
          DbContract.Contents.COLUMN_GLOBAL_ID + " DESC", 0);
      while (cursor.moveToNext()) {
        videoList.add(new VideoEntry(cursor.getString(cursor.getColumnIndex(
            DbContract.Contents.COLUMN_TITLE)),
            cursor.getString(cursor.getColumnIndex(DbContract.Contents.COLUMN_VIDEO_ID)),
            cursor.getString(cursor.getColumnIndex(DbContract.Contents.COLUMN_STREAM)),
            cursor.getString(cursor.getColumnIndex(DbContract.Contents.COLUMN_TEXT))));
      }
      adapter = new PageAdapter(getActivity(),videoList);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
      super.onActivityCreated(savedInstanceState);
      videoBox = getActivity().findViewById(R.id.video_box);
      getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
      setListAdapter(adapter);

    }


    /**
     * Plays youtube video.
     *
     * @param videoId     Video Id
     * @param videoTitle  Video Title
     * @param stream      Stream name
     */
    public void playVideo(String videoId,String videoTitle,String stream) {

      VideoFragment videoFragment =
          (VideoFragment) getFragmentManager().findFragmentById(R.id.video_fragment_container);
      videoFragment.setVideoId(videoId);
      TextView videoTitleTv = getActivity().findViewById(R.id.video_title);
      videoTitleTv.setText(videoTitle);
      TextView streamTitleTv = getActivity().findViewById(R.id.video_stream);
      streamTitleTv.setText(stream);

      // The videoBox is INVISIBLE if no video was previously selected, so we need to show it now.
      if (videoBox.getVisibility() != View.VISIBLE) {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
          // Initially translate off the screen so that it can be animated in from below.
          videoBox.setTranslationY(videoBox.getHeight());
        }
        videoBox.setVisibility(View.VISIBLE);
      }

      // If the fragment is off the screen, we animate it in.
      if (videoBox.getTranslationY() > 0) {
        videoBox.animate().translationY(0).setDuration(ANIMATION_DURATION_MILLIS);
      }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
      String videoId = videoList.get(position).videoId;
      String title = videoList.get(position).title;
      String streamTitle = videoList.get(position).streamTitle;

      playVideo(videoId,title,streamTitle);


      //      VideoFragment videoFragment =
      //      (VideoFragment) getFragmentManager().findFragmentById(R.id.video_fragment_container);
      //      videoFragment.setVideoId(videoId);
      //      TextView videoTitle=(TextView)getActivity().findViewById(R.id.video_title);
      //        videoTitle.setText(title);
      //      TextView streamTitleTv=(TextView)getActivity().findViewById(R.id.video_stream);
      //      streamTitleTv.setText(streamTitle);
      //
      //
      // The videoBox is INVISIBLE if no video was previously selected, so we need to show it now.
      //      if (videoBox.getVisibility() != View.VISIBLE) {
      //  if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
      //          // Initially translate off the screen so that it can be animated in from below.
      //          videoBox.setTranslationY(videoBox.getHeight());
      //        }
      //        videoBox.setVisibility(View.VISIBLE);
      //      }
      //
      //      // If the fragment is off the screen, we animate it in.
      //      if (videoBox.getTranslationY() > 0) {
      //        videoBox.animate().translationY(0).setDuration(ANIMATION_DURATION_MILLIS);
      //      }
    }

    @Override
    public void onDestroyView() {
      super.onDestroyView();

      adapter.releaseLoaders();
    }

    public void setLabelVisibility(boolean visible) {
      adapter.setLabelVisibility(visible);
    }

  }

  // Utility methods for layouting.

  /**
   * Adapter for the video list. Manages a set of YouTubeThumbnailViews, including initializing each
   * of them only once and keeping track of the loader of each one. When the ListFragment gets
   * destroyed it releases all the loaders.
   */
  private static final class PageAdapter extends BaseAdapter {

    private final List<VideoEntry> entries;
    private final List<View> entryViews;
    private final Map<YouTubeThumbnailView, YouTubeThumbnailLoader> thumbnailViewToLoaderMap;
    private final LayoutInflater inflater;
    private final ThumbnailListener thumbnailListener;

    private boolean labelsVisible;
    private Context context;

    private PageAdapter(Context context, List<VideoEntry> entries) {
      this.entries = entries;
      this.context = context;
      entryViews = new ArrayList<>();
      thumbnailViewToLoaderMap = new HashMap<>();
      inflater = LayoutInflater.from(context);
      thumbnailListener = new ThumbnailListener();

      labelsVisible = true;
    }

    public void releaseLoaders() {
      for (YouTubeThumbnailLoader loader : thumbnailViewToLoaderMap.values()) {
        loader.release();
      }
    }

    public void setLabelVisibility(boolean visible) {
      labelsVisible = visible;
      for (View view : entryViews) {
        view.findViewById(R.id.text).setVisibility(visible ? View.VISIBLE : View.GONE);
      }
    }

    @Override
    public int getCount() {
      return entries.size();
    }

    @Override
    public VideoEntry getItem(int position) {
      return entries.get(position);
    }

    @Override
    public long getItemId(int position) {
      return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      View view = convertView;
      VideoEntry entry = entries.get(position);

      //      // There are three cases here
      //      if (view == null) {
      //   // 1) The view has not yet been created - we need to initialize the YouTubeThumbnailView.
      view = inflater.inflate(R.layout.video_list_item, parent, false);
      //  YouTubeThumbnailView thumbnail = (YouTubeThumbnailView) view.findViewById(R.id.thumbnail);
      //
      //        thumbnail.setTag(entry.videoId);
      //        thumbnail.initialize(YOUTUBE_DEVELOPER_KEY, thumbnailListener);
      //      } else {
      //  YouTubeThumbnailView thumbnail = (YouTubeThumbnailView) view.findViewById(R.id.thumbnail);
      //        YouTubeThumbnailLoader loader = thumbnailViewToLoaderMap.get(thumbnail);
      //        if (loader == null) {
      //        // 2) The view is already created, and is currently being initialized. We store the
      //          //    current videoId in the tag.
      //          thumbnail.setTag(entry.videoId);
      //        } else {
      //     // 3) The view is already created and already initialized. Simply set the right videoId
      //          //    on the loader.
      //          thumbnail.setImageResource(R.drawable.video_loading_thumbnail);
      //          loader.setVideo(entry.videoId);
      //        }
      //      }
      ImageView thumbnail = view.findViewById(R.id.youtube_thumbnail);
      Log.d("Shriyansh Thumbnail",Utils.getYoutubeVideoThumbnailFromId(entry.videoId));
      Picasso.with(context)
          .load(Uri.parse(Utils.getUsableDropboxUrl(Utils.getYoutubeVideoThumbnailFromId(
              entry.videoId))))
          .placeholder(R.drawable.video_loading_thumbnail)
          .error(R.drawable.video_no_thumbnail)
          .into(thumbnail)
      ;
      TextView label = (view.findViewById(R.id.title));
      label.setText(entry.title);
      TextView stream = (view.findViewById(R.id.stream_title));
      stream.setText(entry.streamTitle);
      TextView text = (view.findViewById(R.id.text));
      text.setText(entry.videoText);



      label.setVisibility(labelsVisible ? View.VISIBLE : View.GONE);
      return view;
    }

    private final class ThumbnailListener implements
        YouTubeThumbnailView.OnInitializedListener,
        YouTubeThumbnailLoader.OnThumbnailLoadedListener {

      @Override
      public void onInitializationSuccess(
          YouTubeThumbnailView view, YouTubeThumbnailLoader loader) {
        loader.setOnThumbnailLoadedListener(this);
        thumbnailViewToLoaderMap.put(view, loader);
        view.setImageResource(R.drawable.video_loading_thumbnail);
        String videoId = (String) view.getTag();
        loader.setVideo(videoId);
      }

      @Override
      public void onInitializationFailure(
          YouTubeThumbnailView view, YouTubeInitializationResult loader) {
        view.setImageResource(R.drawable.ic_video_library_white_24dp);
      }

      /**
       * Call back for thumbnail load.
       *
       * @param view    Video view
       * @param videoId Video id
       */
      @Override
      public void onThumbnailLoaded(YouTubeThumbnailView view, String videoId) {
      }

      @Override
      public void onThumbnailError(YouTubeThumbnailView view, ErrorReason errorReason) {
        view.setImageResource(R.drawable.ic_video_library_white_24dp);
      }
    }

  }

  public static final class VideoFragment extends YouTubePlayerFragment
      implements OnInitializedListener {

    private YouTubePlayer player;
    private String videoId;

    public static VideoFragment newInstance() {
      return new VideoFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      initialize(Constants.YOUTUBE_DEVELOPER_KEY, this);
    }

    @Override
    public void onDestroy() {
      if (player != null) {
        player.release();
      }
      super.onDestroy();
    }

    /**
     * Sets Video id.
     *
     * @param videoId   Video id
     */
    public void setVideoId(String videoId) {
      if (videoId != null && !videoId.equals(this.videoId)) {
        this.videoId = videoId;
        if (player != null) {
          player.cueVideo(videoId);
        }
      }
    }

    /**
     * Pauses the video.
     */
    public void pause() {
      if (player != null) {
        player.pause();
      }
    }

    @Override
    public void onInitializationSuccess(Provider provider, YouTubePlayer player, boolean restored) {
      this.player = player;
      player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
      player.setOnFullscreenListener((VideoListDemoActivity) getActivity());
      if (!restored && videoId != null) {
        player.cueVideo(videoId);
      }
    }

    @Override
    public void onInitializationFailure(Provider provider, YouTubeInitializationResult result) {
      this.player = null;
    }

  }

  private static final class VideoEntry {
    private final String title;
    private final String videoId;
    private final String streamTitle;
    private final String videoText;

    private VideoEntry(String title, String videoId,String streamTitle,String videoText) {
      this.title = title;
      this.videoId = videoId;
      this.streamTitle = streamTitle;
      this.videoText = videoText;
    }
  }
}
