package com.a.note_module;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;


public class EditNoteActivity extends AppCompatActivity {

    public static final String EXTRA_ID =
            "note_module.EXTRA_ID";
    public static final String EXTRA_TITLE =
            "note_module.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION =
            "note_module.EXTRA_DESCRIPTION";
    public static final String EXTRA_TIME =
            "note_module.EXTRA_TIME";

    private boolean showFlag = false;

    private EditText editTextTitle, editTextDescription;
    private TextView textViewTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_edit_activity);

        editTextTitle = findViewById(R.id.edit_text_title);
        textViewTime = findViewById(R.id.text_view_time);
        editTextDescription = findViewById(R.id.edit_text_description);


        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Show Note");
            showFlag = true;
            editTextTitle.setEnabled(false);
            editTextDescription.setEnabled(false);
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            editTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            textViewTime.setText(intent.getStringExtra(EXTRA_TIME));
        } else {
            setTitle("Edit Note");
            editTextTitle.setEnabled(true);
            editTextDescription.setEnabled(true);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat();
            sdf.applyPattern("yyyy-MM-dd HH:mm:ss a");
            Date date = new Date();
            textViewTime.setText(sdf.format(date));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.edit_note_menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        saveNote();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (showFlag && item.getItemId() == R.id.edit_note) {
            setTitle("Edit Note");
            editTextTitle.setEnabled(true);
            editTextDescription.setEnabled(true);
            return true;
        }
        saveNote();
        return super.onOptionsItemSelected(item);
    }

    private void saveNote() {
        String title = editTextTitle.getText().toString();
        String time = textViewTime.getText().toString();
        String description = editTextDescription.getText().toString();

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_TIME, time);
        data.putExtra(EXTRA_DESCRIPTION, description);
        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
    }
}

