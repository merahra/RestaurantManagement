package com.apptree.newspot;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class CustomAdapterDisplayMenu extends ArrayAdapter<MenuPojo> {
    private final Context ctx;
    private List<MenuPojo> menuPojoList;

    public CustomAdapterDisplayMenu(Context ctx, List<MenuPojo> menuPojoList) {
        super(ctx, R.layout.menu_list_row, menuPojoList);
        this.ctx = ctx;
        this.menuPojoList = menuPojoList;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((LayoutInflater) this.ctx.getSystemService("layout_inflater")).inflate(R.layout.menu_list_row, parent, false);
        }
        ImageView imageView = (ImageView) convertView.findViewById(R.id.icon);
        TextView tvItemName = (TextView) convertView.findViewById(R.id.tvItemName);
        TextView tvDesc = (TextView) convertView.findViewById(R.id.tvDesc);
        TextView tvCat = (TextView) convertView.findViewById(R.id.tvCat);
        TextView tvPrice = (TextView) convertView.findViewById(R.id.tvPrice);
        TextView tvId = (TextView) convertView.findViewById(R.id.tvId);
        TextView tvCurrency = (TextView) convertView.findViewById(R.id.tvCurrency);
        CheckBox checkbox = (CheckBox) convertView.findViewById(R.id.chkItem);
        checkbox.setFocusable(false);
        checkbox.setFocusableInTouchMode(false);
        checkbox.setClickable(false);
        String str = this.ctx.getSharedPreferences("currency", 0).getString("curr_symbol", "\u00a3");
        Log.d("CURRENCY VALUE", str);
        MenuPojo mp = (MenuPojo) this.menuPojoList.get(position);
        tvItemName.setText(mp.getItemName());
        tvDesc.setText(mp.getItemDesc());
        tvCat.setText(mp.getItemCat());
        tvPrice.setText(mp.getItemPrice());
        tvId.setText(mp.getItemId());
        tvCurrency.setText(str);
        imageView.setImageBitmap(BitmapFactory.decodeFile(mp.getItemPath()));
        checkbox.setChecked(mp.isSelected());
        return convertView;
    }
}
