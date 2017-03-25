package com.apptree.newspot;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class ViewItems extends Activity {
    ListView list;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_items);
        this.list = (ListView) findViewById(R.id.list);
        this.list.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View selectView, int position, long id) {
                TextView tvViewItemId = (TextView) selectView.findViewById(R.id.tvViewItemId);
                Intent intent = new Intent(ViewItems.this, UpdateItem.class);
                intent.putExtra("viewItemId", tvViewItemId.getText().toString());
                Log.d("+++ID++++++", tvViewItemId.getText().toString());
                ViewItems.this.startActivity(intent);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_items, menu);
        return true;
    }

    protected void onStart() {
        super.onStart();
        ArrayList<MenuPojo> arrlist = Database.fillViewItemsList(this.list);
        if (arrlist.size() == 0) {
            Toast toast = Toast.makeText(this, "No Items In The Menu ..Go To Add Items !!!", 1);
            toast.setGravity(17, 0, 0);
            toast.show();
        }
        this.list.setAdapter(new CustomAdapter(this, arrlist));
    }
}
