package com.apptree.newspot;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

public class AdminPanel extends Activity {
    public static final Integer[] images = new Integer[]{Integer.valueOf(R.drawable.add), Integer.valueOf(R.drawable.view), Integer.valueOf(R.drawable.delete), Integer.valueOf(R.drawable.vieworder), Integer.valueOf(R.drawable.viewbytable), Integer.valueOf(R.drawable.complete), Integer.valueOf(R.drawable.currency), Integer.valueOf(R.drawable.edit_name)};
    public static final String[] titles = new String[]{"Add Items To Menu", "View Items In Menu", "Delete Items In Menu", "View Orders", "View Orders By Table", "Completed Orders", "Select Currency", "Set Your Restaurant name"};
    private ListView list;
    List<RowItem> rowItems;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_panel);
        this.list = (ListView) findViewById(R.id.listNew);
        this.rowItems = new ArrayList();
        for (int i = 0; i < titles.length; i++) {
            this.rowItems.add(new RowItem(images[i].intValue(), titles[i]));
        }
        this.list.setAdapter(new AdminPanelAdapter(this, this.rowItems));
        this.list.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View selectedView, int position, long id) {
                switch (position) {
                    case 0:
                        AdminPanel.this.startActivity(new Intent(AdminPanel.this, AddItem.class));
                        return;
                    case 1:
                        AdminPanel.this.startActivity(new Intent(AdminPanel.this, ViewItems.class));
                        return;
                    case 2:
                        AdminPanel.this.startActivity(new Intent(AdminPanel.this, DeleteItems.class));
                        return;
                    case 3:
                        AdminPanel.this.startActivity(new Intent(AdminPanel.this, ViewOrders.class));
                        return;
                    case 4:
                        AdminPanel.this.startActivity(new Intent(AdminPanel.this, ViewOrdersByTable.class));
                        return;
                    case 5:
                        AdminPanel.this.startActivity(new Intent(AdminPanel.this, CompletedOrders.class));
                        return;
                    case 6:
                        AdminPanel.this.startActivity(new Intent(AdminPanel.this, SelectCurrency.class));
                        return;
                    case 7:
                        AdminPanel.this.startActivity(new Intent(AdminPanel.this, SetName.class));
                        return;
                    default:
                        return;
                }
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_panel, menu);
        return true;
    }
}
