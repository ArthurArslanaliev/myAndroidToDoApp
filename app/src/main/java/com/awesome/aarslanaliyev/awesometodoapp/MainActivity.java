package com.awesome.aarslanaliyev.awesometodoapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private FileStorage storage;
    private ListView listView;

    final int DELETE_ACTION_ID = 1;
    final int CHECK_ITEM_ID = 3;
    final int UNCHECK_ITEM_ID = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        items = new ArrayList<>();

        listView = (ListView) findViewById(R.id.lvItems);
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_checked, items);
        listView.setAdapter(itemsAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        storage = new FileStorage(getFilesDir(), getString(R.string.file_storage));
        storage.readItems(listView, itemsAdapter);

        registerForContextMenu(listView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.game_menu, menu);
        return true;
    }

    public void onAddItem(View v) {
        EditText newItem = (EditText) findViewById(R.id.newItem);
        String itemText = newItem.getText().toString();
        String formattedItem = String.format("%s - %s", itemText, getCurrentTimeStamp());

        items.add(formattedItem);
        itemsAdapter.notifyDataSetChanged();

        newItem.setText("");
        storage.writeViewItems(items, listView);
    }

    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

        CheckedTextView targetView = (CheckedTextView) info.targetView;
        if (targetView.isChecked()) {
            menu.add(0, UNCHECK_ITEM_ID, 0, R.string.context_menu_action_uncheck);
        } else {
            menu.add(0, CHECK_ITEM_ID, 0, R.string.context_menu_action_check);
        }

        menu.add(0, DELETE_ACTION_ID, 2, R.string.context_menu_action_delete);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int actionId = item.getItemId();

        switch (actionId) {
            case DELETE_ACTION_ID:
                items.remove(info.position);
                itemsAdapter.notifyDataSetChanged();
                storage.writeViewItems(items, listView);
                return true;
            case CHECK_ITEM_ID:
                listView.setItemChecked(info.position, true);
                itemsAdapter.notifyDataSetChanged();
                storage.writeViewItems(items, listView);
                return true;
            case UNCHECK_ITEM_ID:
                listView.setItemChecked(info.position, false);
                itemsAdapter.notifyDataSetChanged();
                storage.writeViewItems(items, listView);
                return true;
        }
        return true;
    }

    public String getCurrentTimeStamp() {
        Locale locale = getResources().getConfiguration().locale;
        return new SimpleDateFormat("EEE, d MMM HH:mm", locale).format(new Date());
    }

    public void Quit(MenuItem item) {
        finish();
        System.exit(0);
    }
}
