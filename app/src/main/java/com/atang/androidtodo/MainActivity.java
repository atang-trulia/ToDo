package com.atang.androidtodo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.atang.androidtodo.dao.TodoDao;
import com.atang.androidtodo.fragments.EditItemFragment;
import com.atang.androidtodo.models.TodoItem;


public class MainActivity extends AppCompatActivity implements EditItemFragment.EditItemListener {

    public static final String EXTRA_ORIGINAL_TEXT = "EXTRA_ORIGINAL_TEXT";
    public static final String EXTRA_ORIGINAL_POSITION = "EXTRA_ORIGINAL_POSITION";
    public static final String EXTRA_ORIGINAL_PRIORITY = "EXTRA_ORIGINAL_PRIORITY";
    public static final String EXTRA_ORIGINAL_DATE = "EXTRA_ORIGINAL_DATE";
    public static final String NOT_SET = "not set";

    ListView listView;
    EditText etNewItem;
    CustomAdapter customAdapter;
    TodoItem selectedTodoItem;
    private int REQUEST_CODE_EDIT = 200;
    private TodoDao todoDao;
    int pos;


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
                pos = position;
                selectedTodoItem = customAdapter.getItem(position);
                showEditItemDialog(selectedTodoItem.getText(), selectedTodoItem.getPriority(), selectedTodoItem.getCompletionDate());

                // Intent intentEditItem = new Intent(MainActivity.this, EditItemActivity.class);
                // intentEditItem.putExtra(EXTRA_ORIGINAL_TEXT, todoItem.getText());
                // intentEditItem.putExtra(EXTRA_ORIGINAL_POSITION, position);
                // intentEditItem.putExtra(EXTRA_ORIGINAL_PRIORITY, todoItem.getPriority());
                // intentEditItem.putExtra(EXTRA_ORIGINAL_DATE, todoItem.getCompletionDate());
                // startActivityForResult(intentEditItem, REQUEST_CODE_EDIT);
            }
        });
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        todoDao = new TodoDao(this);
//        customAdapter = new CustomAdapter(this, todoDao.getToDo());
//
//        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_EDIT) {
//            String name = data.getExtras().getString(EXTRA_ORIGINAL_TEXT);
//            int position = data.getExtras().getInt(EXTRA_ORIGINAL_POSITION);
//            String priority=data.getExtras().getString(EXTRA_ORIGINAL_PRIORITY);
//            String date=data.getExtras().getString(EXTRA_ORIGINAL_DATE);
//
//            TodoItem todoItem = customAdapter.getItem(position);
//            todoItem.setText(name);
//            todoItem.setPriority(priority);
//            todoItem.setCompletionDate(date);
//            todoDao.updateToDo(todoItem);
//
//            customAdapter = new CustomAdapter(this, todoDao.getToDo());
//            listView.setAdapter(customAdapter);
//            customAdapter.notifyDataSetChanged();
//        }
//    }

    public void onAddItem(View view){
        if (etNewItem.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please Enter an Item", Toast.LENGTH_SHORT).show();
            return;
        }

        TodoItem newItem = new TodoItem();
        newItem.setText(etNewItem.getText().toString());
        newItem.setPriority("Low");
        newItem.setCompletionDate(NOT_SET);
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.action_exit:
                System.exit(0);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showEditItemDialog(String name, String priority, String date) {
        FragmentManager fm = getSupportFragmentManager();
        EditItemFragment editItemDialog = EditItemFragment.newInstance("Edit Task", name, priority, date);
        editItemDialog.show(fm, "fragment_edit_item");
    }

    @Override
    public void onFinishEditDialog(String inputText, String date, String priority) {
        todoDao = new TodoDao(this);
        customAdapter = new CustomAdapter(this, todoDao.getToDo());

        TodoItem todoItem = customAdapter.getItem(pos);
        todoItem.setText(inputText);
        todoItem.setPriority(priority);
        todoItem.setCompletionDate(date);

        todoDao.updateToDo(todoItem);
        customAdapter = new CustomAdapter(this, todoDao.getToDo());
        listView.setAdapter(customAdapter);
        customAdapter.notifyDataSetChanged();
    }

}
