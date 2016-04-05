package com.atang.androidtodo;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.atang.androidtodo.Model.TodoItem;

import java.util.List;

/**
 * Created by atang on 3/30/16.
 */
public class CustomAdapter extends ArrayAdapter<TodoItem> {

    private final Context context;
    private final List<TodoItem> todolist;

    public CustomAdapter(Context context, List<TodoItem> todolist) {
        super(context,R.layout.todolist,todolist);
        this.context=context;
        this.todolist=todolist;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.todolist, parent, false);
        }

        final TextView titleView = (TextView) convertView.findViewById(R.id.todoText);
        final TextView priority = (TextView) convertView.findViewById(R.id.priorityView);
        final TextView date = (TextView) convertView.findViewById(R.id.dateView);
        final TodoItem toDoItem = getItem(position);


        // Priority
        String priorityString = toDoItem.getPriority();
        priority.setText(priorityString);
        switch (priorityString) {
            case "Low":
                priority.setTextColor(ContextCompat.getColor(context, R.color.DARKGREEN));
                break;
            case "Medium":
                priority.setTextColor(ContextCompat.getColor(context, R.color.ORANGE));
                break;
            case "High":
                priority.setTextColor(ContextCompat.getColor(context, R.color.RED));
                break;
        }

        // Date
        date.setText(toDoItem.getCompletionDate());

        // Text
        titleView.setText(toDoItem.getText());
          // if overdue, show overdue icon
//        if (!toDoItem.getCompletionDate().equalsIgnoreCase("not set")) {
//            Calendar cal = Calendar.getInstance();
//            int nowYear = cal.get(Calendar.YEAR);
//            int nowMonth = cal.get(Calendar.MONTH) + 1;
//            int nowDate = cal.get(Calendar.DATE);
//            int currentDate = nowYear * 10000 + nowMonth * 100 + nowDate;
//            int dueDate = Integer.parseInt(toDoItem.getCompletionDate());
//
//            if (currentDate > dueDate) {
//                titleView.setText(toDoItem.getText() + "  OVERDUE");
//            }
//        }

        return convertView;
    }

}