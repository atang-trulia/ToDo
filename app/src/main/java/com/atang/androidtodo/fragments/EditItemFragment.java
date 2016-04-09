package com.atang.androidtodo.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.atang.androidtodo.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.atang.androidtodo.MainActivity.EXTRA_ORIGINAL_DATE;
import static com.atang.androidtodo.MainActivity.EXTRA_ORIGINAL_PRIORITY;
import static com.atang.androidtodo.MainActivity.EXTRA_ORIGINAL_TEXT;
import static com.atang.androidtodo.MainActivity.NOT_SET;


/**
 * Created by atang on 3/24/16.
 */
public class EditItemFragment extends DialogFragment {
    private EditText etEditItemText;
    private RadioGroup rgEditItemPriorityText;
    private DatePicker datePicker;


    public EditItemFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static EditItemFragment newInstance(String title, String name, String priority, String date) {
        EditItemFragment frag = new EditItemFragment();
        Bundle args = new Bundle();

        args.putString("title", title);
        args.putString(EXTRA_ORIGINAL_TEXT, name);
        args.putString(EXTRA_ORIGINAL_PRIORITY, priority);
        args.putString(EXTRA_ORIGINAL_DATE, date);
        frag.setArguments(args);
        return frag;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_item, container);
        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etEditItemText = (EditText) view.findViewById(R.id.etEditItemText);
        rgEditItemPriorityText = (RadioGroup) view.findViewById(R.id.rgEditItemPriorityText);
        datePicker = (DatePicker) view.findViewById(R.id.datePicker);
        Button btnEditItemSave = (Button) view.findViewById(R.id.btnEditItemSave);

        // Populate old value
        String originalText = getArguments().getString(EXTRA_ORIGINAL_TEXT);
        etEditItemText.setText(originalText);

        int priorityIdx = 0;
        switch (getArguments().getString(EXTRA_ORIGINAL_PRIORITY)) {
            case "High":
                priorityIdx = 2;
                break;
            case "Medium":
                priorityIdx = 1;
                break;
            case "Low":
            default:
                priorityIdx = 0;
                break;
        }
        ((RadioButton) rgEditItemPriorityText.getChildAt(priorityIdx)).setChecked(true);

        String originalDue = getArguments().getString(EXTRA_ORIGINAL_DATE);
        if (originalDue.equalsIgnoreCase(NOT_SET)) {
            originalDue = new SimpleDateFormat("yyyyMMdd").format(new Date());
        }
        datePicker.updateDate(Integer.parseInt(originalDue.substring(0, 4)),
                Integer.parseInt(originalDue.substring(4, 6)) - 1,
                Integer.parseInt(originalDue.substring(6, 8)));


        btnEditItemSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputText = etEditItemText.getText().toString();
                String priority = (String) ((RadioButton) getView().findViewById(rgEditItemPriorityText.getCheckedRadioButtonId())).getText();
                String date = setDateString(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());

                EditItemListener listener = (EditItemListener) getActivity();
                listener.onFinishEditDialog(inputText, date, priority);
                dismiss();
            }
        });
    }


    private static String setDateString(int year, int month, int day) {
        String monthString, dayString;

        month++;
        if (month < 10) {
            monthString = "0" + month;
        } else {
            monthString = "" + month;
        }

        if (day < 10) {
            dayString = "0" + day;
        } else {
            dayString = "" + day;
        }

        return year + monthString + dayString;
    }


    @Override
    public void show(FragmentManager manager, String tag) {
        if (manager.findFragmentByTag(tag) == null) {
            super.show(manager, tag);
        }
    }
    
    public interface EditItemListener {
        void onFinishEditDialog(String inputText, String date, String priority);
    }

}