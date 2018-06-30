package in.shriyansh.streamify.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import in.shriyansh.streamify.R;
import in.shriyansh.streamify.activities.ImageActivity;
import in.shriyansh.streamify.database.DbContract;
import in.shriyansh.streamify.utils.Utils;

/**
 * Adapter for Image library
 * Created by shriyansh on 7/11/15.
 */
public class ImageLibraryAdapter extends RecyclerView.Adapter<ImageLibraryAdapter.ViewHolder> {
    private final Context context;
    private final Cursor cursor;

    private static final int STAGGERED_SIZE_COUNT = 4;

    public ImageLibraryAdapter(Context context,Cursor cursor) {
        this.cursor = cursor;
        this.context = context;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        private final TextView contentTitleTv;
        private final TextView contentStreamTv;
        private final TextView contentTextTv;
        private final ImageView imageView;

        private ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            contentTitleTv = v.findViewById(R.id.content_title);
            contentStreamTv = v.findViewById(R.id.content_stream);
            contentTextTv = v.findViewById(R.id.content_text);
            imageView = v.findViewById(R.id.image_image);
        }

        @Override
        public void onClick(View view) {
            int itemPosition = getPosition();
            cursor.moveToPosition(itemPosition);
            Intent intent = new Intent(context, ImageActivity.class);
            intent.putExtra(ImageActivity.INTENT_KEY_CONTENT_URL,Utils.getUsableDropboxUrl(
                    cursor.getString(cursor.getColumnIndex(DbContract.Contents.COLUMN_IMAGE))));
            intent.putExtra(ImageActivity.INTENT_KEY_CONTENT_TITLE,cursor.getString(
                    cursor.getColumnIndex(DbContract.Contents.COLUMN_TITLE)));
            intent.putExtra(ImageActivity.INTENT_KEY_CONTENT_SUBTITLE,cursor.getString(
                    cursor.getColumnIndex(DbContract.Contents.COLUMN_STREAM)));
            intent.putExtra(ImageActivity.INTENT_KEY_CONTENT_DESCRIPTION,cursor.getString(
                    cursor.getColumnIndex(DbContract.Contents.COLUMN_TEXT)));
            context.startActivity(intent);
        }
    }

    @Override
    public ImageLibraryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_library_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ImageLibraryAdapter.ViewHolder holder, int position) {
        cursor.moveToPosition(position);
        holder.contentTitleTv.setText(cursor.getString(cursor.getColumnIndex(
                DbContract.Contents.COLUMN_TITLE)));
        holder.contentStreamTv.setText(cursor.getString(cursor.getColumnIndex(
                DbContract.Contents.COLUMN_STREAM)));
        holder.contentTextTv.setText(cursor.getString(cursor.getColumnIndex(
                DbContract.Contents.COLUMN_TEXT)));
        if (position % STAGGERED_SIZE_COUNT == 0) {
           ViewGroup.LayoutParams layoutParams = holder.imageView.getLayoutParams();
           layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                   150, context.getResources().getDisplayMetrics());
           holder.imageView.setLayoutParams(layoutParams);
        }
        if (position % STAGGERED_SIZE_COUNT == 1) {
            ViewGroup.LayoutParams layoutParams = holder.imageView.getLayoutParams();
            layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    200, context.getResources().getDisplayMetrics());
            holder.imageView.setLayoutParams(layoutParams);
        }
        if (position % STAGGERED_SIZE_COUNT == 2) {
            ViewGroup.LayoutParams layoutParams = holder.imageView.getLayoutParams();
            layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    250, context.getResources().getDisplayMetrics());
            holder.imageView.setLayoutParams(layoutParams);
        }
        if (position % STAGGERED_SIZE_COUNT == 3) {
            ViewGroup.LayoutParams layoutParams = holder.imageView.getLayoutParams();
            layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    150, context.getResources().getDisplayMetrics());
            holder.imageView.setLayoutParams(layoutParams);
        }

        Picasso.with(context)
                .load(Uri.parse(Utils.getUsableDropboxUrl(cursor.getString(cursor.getColumnIndex(
                        DbContract.Contents.COLUMN_IMAGE)))))
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }
}
