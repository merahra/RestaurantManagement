package com.apptree.newspot;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class CompletedOrders extends Activity {
    private SimpleAdapter adapter;
    private ListView list;
    private ArrayList<HashMap<String, Object>> testList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.completed_orders);
        this.list = (ListView) findViewById(R.id.list);
        fetchOrders();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.completed_orders, menu);
        return true;
    }

    public void fetchOrders() {
        Log.d("VIEW ORDERS LIST", "RUNNING");
        ArrayList<String> nameList = new ArrayList();
        ArrayList<String> qtyList = new ArrayList();
        try {
            Log.d("VIEW ORDERS LIST", "Entered Try Block");
            SQLiteDatabase db = new DBHelper(this).getReadableDatabase();
            this.testList = new ArrayList();
            Cursor c = db.query(Database.COMPLETED_ORDER_TABLE_NAME, null, null, null, null, null, "_id DESC");
            if (c.getCount() == 0) {
                Toast.makeText(this, "No Orders To Display !!!", 1).show();
            }
            while (c.moveToNext()) {
                Log.d("VIEW ORDERS LIST", Integer.toString(c.getCount()));
                ByteArrayInputStream bis = new ByteArrayInputStream(c.getBlob(c.getColumnIndex(Database.COMPLETED_ORDER_NAME)));
                ObjectInputStream objectInputStream = new ObjectInputStream(bis);
                nameList = (ArrayList) objectInputStream.readObject();
                ByteArrayInputStream bi = new ByteArrayInputStream(c.getBlob(c.getColumnIndex(Database.COMPLETED_ORDER_QTY)));
                objectInputStream = new ObjectInputStream(bi);
                qtyList = (ArrayList) objectInputStream.readObject();
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < nameList.size(); i++) {
                    sb.append(new StringBuilder(String.valueOf(Integer.toString(i + 1))).append(". ").append((String) nameList.get(i)).append(" (").append((String) qtyList.get(i)).append(")").append("\n").toString());
                    Log.d("String buffer", sb.toString());
                }
                Log.d("VIEW ORDERS LIST", (String) nameList.get(0));
                Log.d("VIEW ORDERS LIST", (String) qtyList.get(0));
                Log.d("VIEW ORDERS LIST", Integer.toString(qtyList.size()));
                objectInputStream.close();
                objectInputStream.close();
                bis.close();
                bi.close();
                HashMap<String, Object> hMap = new HashMap();
                hMap.put("name", sb);
                hMap.put("orderId", c.getString(c.getColumnIndex(Database.ORDER_ID)));
                hMap.put("date", c.getString(c.getColumnIndex(Database.COMPLETED_ORDER_DATE)));
                hMap.put(Database.ITEM_PRICE, c.getString(c.getColumnIndex(Database.COMPLETED_ORDER_PRICE)));
                hMap.put("table_no", c.getString(c.getColumnIndex(Database.COMPLETED_ORDER_TABLE_NO)));
                hMap.put("bill_no", c.getString(c.getColumnIndex(Database.COMPLETED_ORDER_PAYMENT_REFERENCE_NO)));
                this.testList.add(hMap);
            }
            this.adapter = new SimpleAdapter(this, this.testList, R.layout.completed_order_list_row, new String[]{"name", "date", Database.ITEM_PRICE, "table_no", "bill_no", "orderId"}, new int[]{R.id.tvCompletedOrderItems, R.id.tvCompletedOrderDate, R.id.tvCompletedOrderAmount, R.id.tvCompletedOrderTableNo, R.id.tvCompletedOrderBillNo, R.id.orderId});
            this.list.setAdapter(this.adapter);
        } catch (Exception e) {
        }
    }
}
