package com.apptree.newspot;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class ViewOrdersByTable extends Activity {
    private EditText etTableNo;
    private ListView listComplete;
    private ListView listIncomplete;
    private String str;
    private TextView tvCompletedOrders;
    private TextView tvPendingOrders;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_orders_by_table);
        this.etTableNo = (EditText) findViewById(R.id.etTableNo);
        this.listIncomplete = (ListView) findViewById(R.id.listIncomplete);
        this.listComplete = (ListView) findViewById(R.id.listComplete);
        this.tvPendingOrders = (TextView) findViewById(R.id.tvPendingOrders);
        this.tvCompletedOrders = (TextView) findViewById(R.id.tvCompletedOrders);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_orders_by_table, menu);
        return true;
    }

    public void onGo(View v) {
        SQLiteDatabase db;
        int i;
        this.etTableNo.setError(null);
        if (TextUtils.isEmpty(this.etTableNo.getText().toString())) {
            this.etTableNo.setError(getString(R.string.error_field_required));
            this.etTableNo.requestFocus();
        }
        this.str = this.etTableNo.getText().toString();
        ArrayList<String> nameList = new ArrayList();
        ArrayList<String> qtyList = new ArrayList();
        try {
            db = new DBHelper(this).getReadableDatabase();
            ArrayList<HashMap<String, Object>> orderByTableList = new ArrayList();
            Cursor c = db.query(Database.ORDER_TABLE_NAME, null, "order_table_no = ? ", new String[]{this.str}, null, null, "_id DESC");
            if (c.getCount() != 0) {
                this.tvPendingOrders.setVisibility(0);
                this.listIncomplete.setVisibility(0);
                while (c.moveToNext()) {
                    Log.d("VIEW ORDERS BY TAble LIST", Integer.toString(c.getCount()));
                    InputStream byteArrayInputStream = new ByteArrayInputStream(c.getBlob(c.getColumnIndex(Database.ORDER_NAME)));
                    ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                    nameList = (ArrayList) objectInputStream.readObject();
                    byteArrayInputStream = new ByteArrayInputStream(c.getBlob(c.getColumnIndex(Database.ORDER_QTY)));
                    objectInputStream = new ObjectInputStream(byteArrayInputStream);
                    qtyList = (ArrayList) objectInputStream.readObject();
                    StringBuffer sb = new StringBuffer();
                    for (i = 0; i < nameList.size(); i++) {
                        sb.append(new StringBuilder(String.valueOf(Integer.toString(i + 1))).append(". ").append((String) nameList.get(i)).append(" (").append((String) qtyList.get(i)).append(")").append("\n").toString());
                        Log.d("String buffer", sb.toString());
                    }
                    Log.d("VIEW ORDERS LIST", (String) nameList.get(0));
                    Log.d("VIEW ORDERS LIST", (String) qtyList.get(0));
                    Log.d("VIEW ORDERS LIST", Integer.toString(qtyList.size()));
                    HashMap<String, Object> hMap = new HashMap();
                    hMap.put("name", sb);
                    HashMap<String, Object> hashMap = hMap;
                    hashMap.put("date", c.getString(c.getColumnIndex(Database.ORDER_DATE)));
                    hMap.put(Database.ITEM_PRICE, c.getString(c.getColumnIndex(Database.ORDER_PRICE)));
                    hashMap = hMap;
                    hashMap.put("billNo", c.getString(c.getColumnIndex(Database.ORDER_PAYMENT_REFERENCE_NO)));
                    orderByTableList.add(hMap);
                    objectInputStream.close();
                    objectInputStream.close();
                    byteArrayInputStream.close();
                    byteArrayInputStream.close();
                }
                c.close();
                db.close();
            }
            ArrayList<HashMap<String, Object>> arrayList = orderByTableList;
            this.listIncomplete.setAdapter(new SimpleAdapter(this, arrayList, R.layout.view_orders_by_table_row, new String[]{"name", "date", Database.ITEM_PRICE, "billNo"}, new int[]{R.id.tvViewOrderedByTableItems, R.id.tvViewOrderedByTableDate, R.id.tvViewOrderedByTableAmount, R.id.tvViewOrderedByTableBillNo}));
        } catch (Exception e) {
        }
        try {
            db = new DBHelper(this).getReadableDatabase();
            arrayList = new ArrayList();
            SQLiteDatabase sQLiteDatabase = db;
            Cursor cr = sQLiteDatabase.query(Database.COMPLETED_ORDER_TABLE_NAME, null, "completed_order_table_no = ? ", new String[]{this.str}, null, null, "_id DESC");
            if (cr.getCount() != 0) {
                this.tvCompletedOrders.setVisibility(0);
                this.listComplete.setVisibility(0);
                while (cr.moveToNext()) {
                    Log.d("VIEW ORDERS BY TAble LIST", Integer.toString(cr.getCount()));
                    byteArrayInputStream = new ByteArrayInputStream(cr.getBlob(cr.getColumnIndex(Database.COMPLETED_ORDER_NAME)));
                    objectInputStream = new ObjectInputStream(byteArrayInputStream);
                    nameList = (ArrayList) objectInputStream.readObject();
                    byteArrayInputStream = new ByteArrayInputStream(cr.getBlob(cr.getColumnIndex(Database.COMPLETED_ORDER_QTY)));
                    objectInputStream = new ObjectInputStream(byteArrayInputStream);
                    qtyList = (ArrayList) objectInputStream.readObject();
                    StringBuffer sbOther = new StringBuffer();
                    for (i = 0; i < nameList.size(); i++) {
                        sbOther.append(new StringBuilder(String.valueOf(Integer.toString(i + 1))).append(". ").append((String) nameList.get(i)).append(" (").append((String) qtyList.get(i)).append(")").append("\n").toString());
                        Log.d("String buffer", sbOther.toString());
                    }
                    Log.d("VIEW ORDERS LIST", (String) nameList.get(0));
                    Log.d("VIEW ORDERS LIST", (String) qtyList.get(0));
                    Log.d("VIEW ORDERS LIST", Integer.toString(qtyList.size()));
                    hMap = new HashMap();
                    hMap.put("name", sbOther);
                    hashMap = hMap;
                    hashMap.put("date", cr.getString(cr.getColumnIndex(Database.COMPLETED_ORDER_DATE)));
                    hMap.put(Database.ITEM_PRICE, cr.getString(cr.getColumnIndex(Database.COMPLETED_ORDER_PRICE)));
                    hashMap = hMap;
                    hashMap.put("billNo", cr.getString(cr.getColumnIndex(Database.COMPLETED_ORDER_PAYMENT_REFERENCE_NO)));
                    arrayList.add(hMap);
                    objectInputStream.close();
                    objectInputStream.close();
                    byteArrayInputStream.close();
                    byteArrayInputStream.close();
                }
                cr.close();
                db.close();
                this.listComplete.setAdapter(new SimpleAdapter(this, arrayList, R.layout.view_orders_by_table_completed_row, new String[]{"name", "date", Database.ITEM_PRICE, "billNo"}, new int[]{R.id.tvViewOrderedByTableItems, R.id.tvViewOrderedByTableDate, R.id.tvViewOrderedByTableAmount, R.id.tvViewOrderedByTableBillNo}));
            }
        } catch (Exception e2) {
        }
    }
}
