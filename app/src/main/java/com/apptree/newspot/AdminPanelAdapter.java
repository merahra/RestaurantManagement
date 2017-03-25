package com.apptree.newspot;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class AdminPanelAdapter extends BaseAdapter {
    private Context context;
    private List<RowItem> rowItems;

    private class ViewHolder {
        ImageView imageView;
        TextView txtTitle;

        private ViewHolder() {
        }
    }

    public AdminPanelAdapter(Context context, List<RowItem> rowItems) {
        this.context = context;
        this.rowItems = rowItems;
    }

    public int getCount() {
        return this.rowItems.size();
    }

    public Object getItem(int position) {
        return this.rowItems.get(position);
    }

    public long getItemId(int position) {
        return (long) this.rowItems.indexOf(getItem(position));
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater mInflater = (LayoutInflater) this.context.getSystemService("layout_inflater");
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.admin_panel_list_row, null);
            holder = new ViewHolder();
            holder.txtTitle = (TextView) convertView.findViewById(R.id.tvOption);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imgAdminPanel);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        RowItem rowItem = (RowItem) getItem(position);
        holder.txtTitle.setTypeface(Typeface.createFromAsset(this.context.getAssets(), "fonts/CorporateDemiItalic.otf"), 1);
        holder.txtTitle.setText(rowItem.getTitle());
        holder.imageView.setImageResource(rowItem.getImageId());
        return convertView;
    }
}
