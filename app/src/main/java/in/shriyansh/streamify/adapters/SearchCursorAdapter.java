package in.shriyansh.streamify.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import in.shriyansh.streamify.R;
import in.shriyansh.streamify.database.DbContract;

/**
 * Created by shriyanshgautam on 01/09/17.
 * TODO : This is not launched.
 */

public class SearchCursorAdapter extends CursorAdapter {
    public SearchCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.search_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView text = (TextView) view.findViewById(R.id.search_text);
        ImageView searchImage = (ImageView) view.findViewById(R.id.search_image);
        text.setText(cursor.getString(cursor.getColumnIndex(DbContract.Streams.COLUMN_TITLE)));
        view.setTag("Stream");
    }
}
