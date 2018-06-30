package in.shriyansh.streamify.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import in.shriyansh.streamify.R;
import in.shriyansh.streamify.adapters.ImageLibraryAdapter;
import in.shriyansh.streamify.database.DbContract;
import in.shriyansh.streamify.database.DbMethods;
import in.shriyansh.streamify.fragments.News;
import in.shriyansh.streamify.network.Urls;

public class ImageLibrary extends AppCompatActivity implements Urls {

    private FloatingActionButton fab;
    private Toolbar toolbar;

    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager staggeredLayoutManager;
    private LinearLayoutManager linearLayoutManager;

    private DbMethods dbMethods;

    public static final String INTENT_KEY_NOTIFICATION_GLOBAL_ID = "news_global_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_library);

        initUi();
        initToolbar();

        dbMethods = new DbMethods(this);

        Intent intent = getIntent();
        int parentGlobalId = intent.getIntExtra(INTENT_KEY_NOTIFICATION_GLOBAL_ID,0);

        setToolbarTitle(parentGlobalId, intent);
        Cursor cursor = getImageCursor(parentGlobalId);

        ImageLibraryAdapter imageLibraryAdapter = new ImageLibraryAdapter(this, cursor);
        recyclerView.setAdapter(imageLibraryAdapter);

        setLayoutChangerFab();
    }

    private void setToolbarTitle(final int parentGlobalId, final Intent intent) {
        if (parentGlobalId != 0) {
            String title = intent.getStringExtra(News.NEWS_TITLE_KEY);
            getSupportActionBar().setTitle(title);
        }
    }

    private Cursor getImageCursor(final int parentGlobalId) {
        Cursor cursor;
        if (parentGlobalId == 0) {
            cursor = dbMethods.queryContent(null, DbContract.Contents.COLUMN_TYPE
                    + " = ? ", new String[]{DbContract.Contents.VALUE_TYPE_IMAGE + ""},
                    DbContract.Contents.COLUMN_PARENT_ID + " DESC", 0);
        } else {
            cursor = dbMethods.queryContent(null,
                    DbContract.Contents.COLUMN_PARENT_ID + " = ? ",
                    new String[]{parentGlobalId + ""},
                    DbContract.Contents.COLUMN_PARENT_ID + " DESC ,"
                            + DbContract.Contents.COLUMN_GLOBAL_ID + " DESC ", 0);
            if (cursor.getCount() == 0) {
                showSnackBar(R.string.snackbar_no_content_found);
            }
        }
        return cursor;
    }

    private void initUi() {
        toolbar = findViewById(R.id.toolbar);
        fab = findViewById(R.id.fab);
        recyclerView = findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        staggeredLayoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        staggeredLayoutManager.setGapStrategy(
                StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        linearLayoutManager = new LinearLayoutManager(this);

        // Set default layout to staggered.
        recyclerView.setLayoutManager(staggeredLayoutManager);
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setLayoutChangerFab() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recyclerView.getLayoutManager() == staggeredLayoutManager) {
                    recyclerView.setLayoutManager(linearLayoutManager);
                    fab.setImageResource(R.drawable.ic_dashboard_white_24dp);
                } else {
                    recyclerView.setLayoutManager(staggeredLayoutManager);
                    fab.setImageResource(R.drawable.ic_list_white_24dp);
                }
            }
        });
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
}
