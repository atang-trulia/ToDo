package com.atang.androidtodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_ORIGINAL_TEXT = "EXTRA_ORIGINAL_TEXT";
    public static final String EXTRA_ORIGINAL_POSITION = "EXTRA_ORIGINAL_POSITION";
    ListView listView;
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    private int REQUEST_CODE_EDIT = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.lvItems);
        readItemsFromFile();
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(itemsAdapter);
        setupListViewListener();
    }

    private void setupListViewListener() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
          @Override
          public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id){
            items.remove(position);
            itemsAdapter.notifyDataSetChanged();
            writeItemsToFile();
            return true;
          }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intentEditItem = new Intent(MainActivity.this, EditItemActivity.class);
                String originalText = (String) ((AppCompatTextView) view).getText();
                intentEditItem.putExtra(EXTRA_ORIGINAL_TEXT, originalText);
                intentEditItem.putExtra(EXTRA_ORIGINAL_POSITION, position);
                startActivityForResult(intentEditItem, REQUEST_CODE_EDIT);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_EDIT) {
            int originalPosition = data.getExtras().getInt(EXTRA_ORIGINAL_POSITION);
            String newText = data.getExtras().getString(EXTRA_ORIGINAL_TEXT);
            items.set(originalPosition, newText);
            itemsAdapter.notifyDataSetChanged();
            writeItemsToFile();
        }
    }

    public void onAddItem(View view){
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String newItemText = etNewItem.getText().toString();
        itemsAdapter.add(newItemText);
        etNewItem.setText("");
        writeItemsToFile();
    }

    private void readItemsFromFile() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            items = new ArrayList<String>();
            e.printStackTrace();
        }
    }

    private void writeItemsToFile() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
