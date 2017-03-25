package com.apptree.newspot;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

public class SelectOrderQuantity extends Activity {
    CustomAdapterForSelectedOrder adapter;
    private int childViews;
    private String date;
    private EditText etTestQty;
    private ListView list;
    private ArrayList<String> nameList;
    private ArrayList<String> qtyList;
    private int total;
    private TextView tvDate;
    private TextView tvTestName;
    private TextView tvTestPrice;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("SelectedOrderQuantity", " On Create");
        setContentView(R.layout.select_order_quantity);
        this.list = (ListView) findViewById(R.id.list);
        ArrayList<String> idList = getIntent().getStringArrayListExtra("selectedItems");
        Log.d("SelectOrderQuantity", String.valueOf(idList.toArray().length));
        this.adapter = new CustomAdapterForSelectedOrder(this, Database.fillSelectedOrderList(this.list, idList));
        this.list.setAdapter(this.adapter);
        this.nameList = new ArrayList();
        this.qtyList = new ArrayList();
    }

    protected void onDestroy() {
        super.onDestroy();
        Log.d("SelectedOrderQuantity", " On Destroy");
    }

    protected void onStart() {
        super.onStart();
        Log.d("SelectedOrderQuantity", " On Start");
    }

    protected void onStop() {
        super.onStop();
        Log.d("SelectedOrderQuantity", " On Stop");
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.select_order_quantity, menu);
        return true;
    }

    public void onSubmit(View v) {
        String date = "";
        this.childViews = this.list.getChildCount();
        Log.d("ON SUBMIT", Integer.toString(this.childViews));
        for (int i = 0; i < this.childViews; i++) {
            View vt = this.list.getChildAt(i);
            this.tvTestName = (TextView) vt.findViewById(R.id.tvSelectedItemName);
            this.nameList.add(this.tvTestName.getText().toString());
            this.tvTestPrice = (TextView) vt.findViewById(R.id.tvSelectedItemPrice);
            this.etTestQty = (EditText) vt.findViewById(R.id.etQty);
            this.qtyList.add(this.etTestQty.getText().toString());
            this.tvDate = (TextView) vt.findViewById(R.id.tvSelectedDate);
            date = this.tvDate.getText().toString();
            int x = Integer.parseInt(this.tvTestPrice.getText().toString()) * Integer.parseInt(this.etTestQty.getText().toString());
            Log.d("XXXX", Integer.toString(x));
            this.total += x;
        }
        Log.d("+++AAJJAA+++", Integer.toString(this.total));
        Intent intent = new Intent(this, CheckOut.class);
        intent.putExtra("toalAmount", this.total);
        intent.putExtra("date", date);
        intent.putStringArrayListExtra("nameList", this.nameList);
        intent.putStringArrayListExtra("qtyList", this.qtyList);
        this.total = 0;
        startActivity(intent);
    }
}
