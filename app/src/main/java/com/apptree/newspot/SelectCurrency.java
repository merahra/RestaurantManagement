package com.apptree.newspot;

import android.app.Activity;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class SelectCurrency extends Activity {
    private SimpleAdapter adapter;
    private Spinner spinnerCurrency;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_currency);
        this.spinnerCurrency = (Spinner) findViewById(R.id.spinner1);
        String[] currSymbol = new String[]{"$", "\u00a3", "\u20ac", "Rs"};
        String[] currName = new String[]{"USD", "GBP", "EUR", "INR"};
        ArrayList<Map<String, String>> list = new ArrayList();
        for (int i = 0; i < currSymbol.length; i++) {
            LinkedHashMap<String, String> hMap = new LinkedHashMap();
            hMap.put("currency_symbol", currSymbol[i]);
            hMap.put("currency_name", currName[i]);
            list.add(hMap);
        }
        this.spinnerCurrency.setAdapter(new SimpleAdapter(this, list, R.layout.select_currency_row, new String[]{"currency_symbol", "currency_name"}, new int[]{R.id.textView1, R.id.textView2}));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.selectcurrency, menu);
        return true;
    }

    public void onConfirm(View v) {
        TextView tv = (TextView) this.spinnerCurrency.getSelectedView().findViewById(R.id.textView1);
        String str = tv.getText().toString();
        Log.d("ON CONFIRM", tv.getText().toString());
        Editor editor = getSharedPreferences("currency", 0).edit();
        editor.putString("curr_symbol", str);
        editor.commit();
        finish();
    }
}
