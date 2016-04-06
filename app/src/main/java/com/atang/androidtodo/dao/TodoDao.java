package com.atang.androidtodo.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.atang.androidtodo.models.TodoItem;
import com.atang.androidtodo.database.TodoSqlLiteHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by atang on 3/29/16.
 */
public class TodoDao {

    private SQLiteDatabase db;
    private TodoSqlLiteHelper dbhelper;

    public TodoDao(Context context) {
        dbhelper = dbhelper.getInstance(context);
        db = dbhelper.getWritableDatabase();
    }

    public void close(){
        db.close();
    }

    public void createToDo(TodoItem todo){
        ContentValues contentValues = new ContentValues();
        contentValues.put("todo", todo.getText());
        contentValues.put("priority", todo.getPriority());
        contentValues.put("date", todo.getCompletionDate());

        db.insert("todos", null, contentValues);
    }

    public void deleteToDo(int todoId){
        db.delete("todos", "_id=" + todoId, null);
    }

    public void updateToDo(TodoItem todo) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("todo", todo.getText());
        contentValues.put("priority", todo.getPriority());
        contentValues.put("date", todo.getCompletionDate());

        db.update("todos", contentValues, "_id=" + todo.getId(), null);
    }

    public List<TodoItem> getToDo() {
        List<TodoItem> todoList = new ArrayList<TodoItem>();
        String[] columns = new String[]{"_id","todo","priority","date"};
        String sortOrder = " ASC";      // ASC or DESC
//        Cursor cursor = db.query("todos", columns, null, null, null, null, "date"+sortOrder);
        Cursor cursor = db.query("todos", columns, null, null, null, null, null);

        cursor.moveToFirst();
        try {
            //loop through the results
            while(!cursor.isAfterLast()){
                TodoItem todoitem = new TodoItem();
                todoitem.setId(cursor.getInt(0));
                todoitem.setText(cursor.getString(1));
                todoitem.setPriority(cursor.getString(2));
                todoitem.setCompletionDate(cursor.getString(3));
                todoList.add(todoitem);
                cursor.moveToNext();
            }
        } finally {
            if (!cursor.isClosed())
                cursor.close();
        }
//        db.close();

        return todoList;
    }
    
}
