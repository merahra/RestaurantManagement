package com.apptree.newspot;

import android.app.Activity;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class SetName extends Activity {
    private TextView editText1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_name);
        this.editText1 = (EditText) findViewById(R.id.editText1);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.set_name, menu);
        return true;
    }

    public void onConfirm(View v) {
        Editor editor = getSharedPreferences("name", 0).edit();
        editor.putString("restaurant_name", this.editText1.getText().toString());
        editor.commit();
        Log.d("RESTAURANT NAME", "editText1.getText().toString()");
        finish();
    }
}
