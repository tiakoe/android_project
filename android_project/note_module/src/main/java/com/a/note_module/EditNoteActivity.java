package com.a.note_module;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class EditNoteActivity extends AppCompatActivity {

    public static final String EXTRA_ID =
            "note_module.EXTRA_ID";
    public static final String EXTRA_TITLE =
            "note_module.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION =
            "note_module.EXTRA_DESCRIPTION";
    public static final String EXTRA_TIME =
            "note_module.EXTRA_TIME";

    private EditText editTextTitle,editTextDescription;
    private TextView textViewTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_edit_activity);

        editTextTitle = findViewById(R.id.edit_text_title);
        textViewTime=findViewById(R.id.text_view_time);
        editTextDescription = findViewById(R.id.edit_text_description);

//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);

        Intent intent = getIntent();
        if(intent.hasExtra(EXTRA_ID)){
            setTitle("Show Note");
            editTextTitle.setEnabled(false);
            editTextDescription.setEnabled(false);
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            editTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            textViewTime.setText(intent.getStringExtra(EXTRA_TIME));
        }else{
            setTitle("Edit Note");
            editTextTitle.setEnabled(true);
            editTextDescription.setEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.save_note:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveNote() {
        String title = editTextTitle.getText().toString();
        String time = textViewTime.getText().toString();
        String description = editTextDescription.getText().toString();
        System.out.println("---"+title+":"+description);

        if(title.trim().isEmpty() || description.trim().isEmpty()){
            Toast.makeText(this, "Please insert a title and description", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_TIME, time);
        data.putExtra(EXTRA_DESCRIPTION, description);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if(id != -1 ){
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();

    }
}

