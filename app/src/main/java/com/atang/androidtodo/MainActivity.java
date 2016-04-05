package com.atang.androidtodo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.atang.androidtodo.Model.TodoItem;
import com.atang.androidtodo.dao.TodoDao;


public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_ORIGINAL_TEXT = "EXTRA_ORIGINAL_TEXT";
    public static final String EXTRA_ORIGINAL_POSITION = "EXTRA_ORIGINAL_POSITION";
    public static final String EXTRA_ORIGINAL_PRIORITY = "EXTRA_ORIGINAL_PRIORITY";
    public static final String EXTRA_ORIGINAL_DATE = "EXTRA_ORIGINAL_DATE";

    ListView listView;
    EditText etNewItem;
    CustomAdapter customAdapter;
    private int REQUEST_CODE_EDIT = 200;
    private TodoDao todoDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.lvItems);
        etNewItem = (EditText) findViewById(R.id.etNewItem);
        todoDao = new TodoDao(this);
        customAdapter = new CustomAdapter(this, todoDao.getToDo());
        listView.setAdapter(customAdapter);
        setupListViewListener();
    }

    private void setupListViewListener() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
          @Override
          public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id){
            TodoItem selectedItem = customAdapter.getItem(position);
            todoDao.deleteToDo(selectedItem.getId());
            customAdapter.remove(selectedItem);
            listView.setAdapter(customAdapter);
            customAdapter.notifyDataSetChanged();
            return true;
          }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TodoItem todoItem = customAdapter.getItem(position);

                Intent intentEditItem = new Intent(MainActivity.this, EditItemActivity.class);
                intentEditItem.putExtra(EXTRA_ORIGINAL_TEXT, todoItem.getText());
                intentEditItem.putExtra(EXTRA_ORIGINAL_POSITION, position);
                intentEditItem.putExtra(EXTRA_ORIGINAL_PRIORITY, todoItem.getPriority());
                intentEditItem.putExtra(EXTRA_ORIGINAL_DATE, todoItem.getCompletionDate());
                startActivityForResult(intentEditItem, REQUEST_CODE_EDIT);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        todoDao = new TodoDao(this);
        CustomAdapter customAdapter;
        customAdapter = new CustomAdapter(this, todoDao.getToDo());

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_EDIT) {
            String name = data.getExtras().getString(EXTRA_ORIGINAL_TEXT);
            int position = data.getExtras().getInt(EXTRA_ORIGINAL_POSITION);
            String priority=data.getExtras().getString(EXTRA_ORIGINAL_PRIORITY);
            String date=data.getExtras().getString(EXTRA_ORIGINAL_DATE);

            TodoItem todoItem = customAdapter.getItem(position);
            todoItem.setText(name);
            todoItem.setPriority(priority);
            todoItem.setCompletionDate(date);
            todoDao.updateToDo(todoItem);

            customAdapter = new CustomAdapter(this, todoDao.getToDo());
            listView.setAdapter(customAdapter);
            customAdapter.notifyDataSetChanged();
        }
    }

    public void onAddItem(View view){
        if ((etNewItem.getText().toString() == null) || (etNewItem.getText().toString().isEmpty())) {
            Toast.makeText(getApplicationContext(), "Please Enter an Item", Toast.LENGTH_SHORT).show();
            return;
        }

        TodoItem newItem = new TodoItem();
        newItem.setText(etNewItem.getText().toString());
        newItem.setPriority("Low");
        newItem.setCompletionDate("not set");
        etNewItem.setText("");

        todoDao.createToDo(newItem);

        customAdapter = new CustomAdapter(this, todoDao.getToDo());
        listView.setAdapter(customAdapter);
        customAdapter.notifyDataSetChanged();

        // Hide Soft Keyboard
        // http://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard
        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

//    private void readItemsFromFile() {
//        File filesDir = getFilesDir();
//        File todoFile = new File(filesDir, "todo.txt");
//        try {
//            items = new ArrayList<String>(FileUtils.readLines(todoFile));
//        } catch (IOException e) {
//            items = new ArrayList<String>();
//            e.printStackTrace();
//        }
//    }

//    private void writeItemsToFile() {
//        File filesDir = getFilesDir();
//        File todoFile = new File(filesDir, "todo.txt");
//        try {
//            FileUtils.writeLines(todoFile, items);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


    @Override
    protected void onDestroy() {
        todoDao.close();
        super.onDestroy();
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
