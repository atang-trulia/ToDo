package com.atang.androidtodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import static com.atang.androidtodo.MainActivity.EXTRA_ORIGINAL_POSITION;
import static com.atang.androidtodo.MainActivity.EXTRA_ORIGINAL_TEXT;

/**
 * Created by atang on 3/24/16.
 */
public class EditItemActivity  extends AppCompatActivity {
    private EditText etEditItemText;
    private Button btnEditItemSave;
    int originalPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        setupView();
    }

    private void setupView() {
        etEditItemText = (EditText) findViewById(R.id.etEditItemText);
        btnEditItemSave = (Button) findViewById(R.id.btnEditItemSave);

        String originalText = getIntent().getStringExtra(EXTRA_ORIGINAL_TEXT);
        originalPosition = getIntent().getIntExtra(EXTRA_ORIGINAL_POSITION, 1);
        etEditItemText.append(originalText);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        btnEditItemSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newText = etEditItemText.getText().toString();
                Intent result = new Intent();
                result.putExtra(MainActivity.EXTRA_ORIGINAL_TEXT, newText);
                result.putExtra(EXTRA_ORIGINAL_POSITION, originalPosition);
                setResult(RESULT_OK, result);

                finish();
            }
        });

    }
}
