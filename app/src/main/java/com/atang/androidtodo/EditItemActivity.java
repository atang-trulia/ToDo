package com.atang.androidtodo;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Calendar;

import static com.atang.androidtodo.MainActivity.EXTRA_ORIGINAL_DATE;
import static com.atang.androidtodo.MainActivity.EXTRA_ORIGINAL_POSITION;
import static com.atang.androidtodo.MainActivity.EXTRA_ORIGINAL_PRIORITY;
import static com.atang.androidtodo.MainActivity.EXTRA_ORIGINAL_TEXT;

/**
 * Created by atang on 3/24/16.
 */
public class EditItemActivity  extends AppCompatActivity {
    private EditText etEditItemText;
    private RadioGroup rgEditItemPriorityText;
    private Button btnEditItemChangeDate;
    private TextView tvEditItemDuedate;
    private Button btnEditItemSave;
    int originalPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        etEditItemText = (EditText) findViewById(R.id.etEditItemText);
        rgEditItemPriorityText = (RadioGroup) findViewById(R.id.rgEditItemPriorityText);
        btnEditItemChangeDate = (Button) findViewById(R.id.btnEditItemChangeDate);
        tvEditItemDuedate = (TextView) findViewById(R.id.tvEditItemDuedate);
        btnEditItemSave = (Button) findViewById(R.id.btnEditItemSave);

        originalPosition = getIntent().getIntExtra(EXTRA_ORIGINAL_POSITION, 1);
        String originalText = getIntent().getStringExtra(EXTRA_ORIGINAL_TEXT);
        etEditItemText.setText(originalText);
        int priorityIdx = 0;
        switch (getIntent().getStringExtra(EXTRA_ORIGINAL_PRIORITY)) {
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

        String originalDue = getIntent().getStringExtra(EXTRA_ORIGINAL_DATE);
        tvEditItemDuedate.setText(originalDue);


        btnEditItemChangeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });

        btnEditItemSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newText = etEditItemText.getText().toString();
                int priorityId = rgEditItemPriorityText.getCheckedRadioButtonId();

                Intent result = new Intent();
                result.putExtra(MainActivity.EXTRA_ORIGINAL_TEXT, newText);
                result.putExtra(EXTRA_ORIGINAL_POSITION, originalPosition);
                result.putExtra(EXTRA_ORIGINAL_PRIORITY, ((RadioButton) findViewById(priorityId)).getText());
                result.putExtra(EXTRA_ORIGINAL_DATE, tvEditItemDuedate.getText().toString());
                setResult(RESULT_OK, result);

                finish();
            }
        });
    }


    // Example from http://developer.android.com/guide/topics/ui/controls/pickers.html
    @SuppressLint("ValidFragment")
    public class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        private TextView tvEditItemDuedate;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            String dateString;

            tvEditItemDuedate = (TextView) findViewById(R.id.tvEditItemDuedate);
            dateString = setDateString(year, month, day);
            tvEditItemDuedate.setText(dateString);
        }

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


}