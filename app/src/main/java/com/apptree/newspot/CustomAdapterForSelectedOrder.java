package com.apptree.newspot;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

public class CustomAdapterForSelectedOrder extends ArrayAdapter<SelectedOrderPojo> {
    private final Context ctx;
    private List<SelectedOrderPojo> selectedOrderPojoList;

    public CustomAdapterForSelectedOrder(Context ctx, List<SelectedOrderPojo> selectedOrderPojoList) {
        super(ctx, R.layout.selected_order_quantity_row, selectedOrderPojoList);
        this.ctx = ctx;
        this.selectedOrderPojoList = selectedOrderPojoList;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((LayoutInflater) this.ctx.getSystemService("layout_inflater")).inflate(R.layout.selected_order_quantity_row, parent, false);
        }
        ImageView imageView = (ImageView) convertView.findViewById(R.id.icon);
        TextView tvSelectedItemName = (TextView) convertView.findViewById(R.id.tvSelectedItemName);
        TextView tvSelectedDesc = (TextView) convertView.findViewById(R.id.tvSelectedItemDesc);
        TextView tvSelectedCat = (TextView) convertView.findViewById(R.id.tvSelectedItemCat);
        final TextView tvSelectedPrice = (TextView) convertView.findViewById(R.id.tvSelectedItemPrice);
        TextView tvSelectedId = (TextView) convertView.findViewById(R.id.tvSelectedId);
        final TextView tvTotalPrice = (TextView) convertView.findViewById(R.id.tvTotalPrice);
        TextView tvSelectedDate = (TextView) convertView.findViewById(R.id.tvSelectedDate);
        final EditText etQty = (EditText) convertView.findViewById(R.id.etQty);
        Button btnPlus = (Button) convertView.findViewById(R.id.btnPlus);
        final Button btnMinus = (Button) convertView.findViewById(R.id.btnMinus);
        TextView tvCurrency = (TextView) convertView.findViewById(R.id.tvCurrency);
        String str = this.ctx.getSharedPreferences("currency", 0).getString("curr_symbol", "\u00a3");
        tvCurrency.setText(str);
        SelectedOrderPojo sp = (SelectedOrderPojo) this.selectedOrderPojoList.get(position);
        tvSelectedItemName.setText(sp.getOrderName());
        tvSelectedDesc.setText(sp.getOrderDesc());
        tvSelectedCat.setText(sp.getOrderCat());
        tvSelectedPrice.setText(sp.getOrderPrice());
        tvSelectedId.setText(sp.getOrderId());
        tvTotalPrice.setText(new StringBuilder(String.valueOf(str)).append(Integer.toString(Integer.parseInt(etQty.getText().toString()) * Integer.parseInt(tvSelectedPrice.getText().toString()))).toString());
        tvSelectedDate.setText(sp.getOrderDate());
        imageView.setImageBitmap(BitmapFactory.decodeFile(sp.getOrderPath()));
        btnPlus.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                int a = Integer.parseInt(etQty.getText().toString()) + 1;
                etQty.setText(Integer.toString(a));
                tvTotalPrice.setText("Rs. " + Integer.toString(Integer.parseInt(tvSelectedPrice.getText().toString()) * a));
            }
        });
        btnMinus.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (Integer.parseInt(etQty.getText().toString()) != 0) {
                    btnMinus.setEnabled(true);
                    int a = Integer.parseInt(etQty.getText().toString()) - 1;
                    etQty.setText(Integer.toString(a));
                    tvTotalPrice.setText("Rs. " + Integer.toString(Integer.parseInt(tvSelectedPrice.getText().toString()) * a));
                    return;
                }
                etQty.setText(Integer.toString(0));
                Toast.makeText(CustomAdapterForSelectedOrder.this.getContext(), "Reached Minimum Quantity", 0).show();
            }
        });
        return convertView;
    }
}
