package com.lucien.colormemory.view.adapter;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.lucien.colormemory.R;
import com.lucien.colormemory.db.DBHelper.DBConstants;

/**
 * Created by Lucien on 18/7/2016.
 */
public class RankCursorAdapter extends SimpleCursorAdapter {

    private Context context;
    private LayoutInflater inflater;

    public RankCursorAdapter(Context context, Cursor cursor, String[] from) {
        super(context, 0, cursor, from, null, 0);
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder item;
        if (convertView == null) {
            item = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_rank, parent, false);
            item.tvRank = (TextView) convertView.findViewById(R.id.tvRank);
            item.tvName = (TextView) convertView.findViewById(R.id.tvName);
            item.tvScore = (TextView) convertView.findViewById(R.id.tvScore);
            convertView.setTag(item);
        } else {
            item = (ViewHolder) convertView.getTag();
        }
        Cursor cursor = getCursor();
        if (position == 0) {
            bindViewData(item, "", 0, 0);
        } else if (cursor.moveToPosition(position - 1)) {
            String name = cursor.getString(cursor.getColumnIndex(DBConstants.NAME));
            int score = cursor.getInt(cursor.getColumnIndex(DBConstants.SCORE));
            bindViewData(item, name, score, position);
        } else {
            bindViewData(item, "", 0, position);
        }
        return convertView;
    }

    private void bindViewData(ViewHolder item, String name, int score, int position) {
        if (position == 0) {
            item.tvRank.setText(R.string.title_rank);
            item.tvName.setText(R.string.title_name);
            item.tvScore.setText(R.string.title_score);
        } else if (TextUtils.isEmpty(name)) {
            item.tvRank.setText(position + "");
            item.tvName.setText("");
            item.tvScore.setText("");
        } else {
            item.tvRank.setText(position + "");
            item.tvName.setText(name);
            item.tvScore.setText(score + "");
        }
    }

    private class ViewHolder {
        TextView tvRank;
        TextView tvName;
        TextView tvScore;
    }

}
