package com.apptree.newspot;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {
    private TextView tvRestaurantName;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.tvRestaurantName = (TextView) findViewById(R.id.tvRestaurantName);
        this.tvRestaurantName.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/lucida.TTF"));
    }

    protected void onStart() {
        super.onStart();
        this.tvRestaurantName.setText(getSharedPreferences("name", 0).getString("restaurant_name", "Your Restaurant"));
    }

    public void startAdminPanel(View v) {
        startActivity(new Intent(this, AdminLogin.class));
    }

    public void startMenu(View v) {
        startActivity(new Intent(this, DisplayItems.class));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent startMain = new Intent("android.intent.action.MAIN");
        startMain.addCategory("android.intent.category.HOME");
        startMain.setFlags(268435456);
        startActivity(startMain);
    }
}
