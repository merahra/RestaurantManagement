package com.apptree.newspot;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class CustomAdapter extends ArrayAdapter<MenuPojo> {
    private final Context ctx;
    private List<MenuPojo> menuPojoList;

    public CustomAdapter(Context ctx, List<MenuPojo> menuPojoList) {
        super(ctx, R.layout.view_items_row, menuPojoList);
        this.ctx = ctx;
        this.menuPojoList = menuPojoList;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((LayoutInflater) this.ctx.getSystemService("layout_inflater")).inflate(R.layout.view_items_row, parent, false);
        }
        ImageView imageView = (ImageView) convertView.findViewById(R.id.icon);
        TextView tvViewItemDesc = (TextView) convertView.findViewById(R.id.tvViewItemDesc);
        TextView tvViewItemCat = (TextView) convertView.findViewById(R.id.tvViewItemCat);
        TextView tvViewItemPrice = (TextView) convertView.findViewById(R.id.tvViewItemPrice);
        TextView tvViewItemId = (TextView) convertView.findViewById(R.id.tvViewItemId);
        MenuPojo mp = (MenuPojo) this.menuPojoList.get(position);
        ((TextView) convertView.findViewById(R.id.tvViewItemName)).setText(mp.getItemName());
        tvViewItemDesc.setText(mp.getItemDesc());
        tvViewItemCat.setText(mp.getItemCat());
        tvViewItemPrice.setText(mp.getItemPrice());
        tvViewItemId.setText(mp.getItemId());
        imageView.setImageBitmap(BitmapFactory.decodeFile(mp.getItemPath()));
        return convertView;
    }
}
