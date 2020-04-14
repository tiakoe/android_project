package com.a.note_module;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class NoteMainActivity extends AppCompatActivity {

    private NoteViewModel noteViewModel;

    public static final int ADD_NOTE_REQUEST = 1;
    public static final int QUERY_NOTE_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_main_activity);

        //悬浮球监听
        FloatingActionButton buttonAddNote = findViewById(R.id.button_add_note);
        buttonAddNote.setOnClickListener(v -> {
            Intent intent = new Intent(NoteMainActivity.this, EditNoteActivity.class);
            startActivityForResult(intent, ADD_NOTE_REQUEST);
        });

        //设置RecyclerView属性
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        //绑定RecyclerView适配器
        final NoteAdapter adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter);

        //创建ViewModel实例
        noteViewModel =
                new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(NoteViewModel.class);

        noteViewModel.getAllNotes().observe(this, adapter::submitList);

        //        滑动删除
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                noteViewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));
                Toast.makeText(NoteMainActivity.this, "Note deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        //        设置item监听
        adapter.setOnItemClickListener(note -> {
            Intent intent = new Intent(NoteMainActivity.this, EditNoteActivity.class);
            intent.putExtra(EditNoteActivity.EXTRA_ID, note.getId());
            intent.putExtra(EditNoteActivity.EXTRA_TITLE, note.getTitle());
            intent.putExtra(EditNoteActivity.EXTRA_DESCRIPTION, note.getDescription());
            intent.putExtra(EditNoteActivity.EXTRA_TIME, note.getTime());
            startActivityForResult(intent, QUERY_NOTE_REQUEST);
        });
    }

    //    返回主页执行
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        System.out.println("--"+requestCode + " " + resultCode);
        if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {
            assert data != null;
            String title = data.getStringExtra(EditNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(EditNoteActivity.EXTRA_DESCRIPTION);
            String time = data.getStringExtra(EditNoteActivity.EXTRA_TIME);

            Note note = new Note(title, description, time);
            noteViewModel.insert(note);
            Toast.makeText(this, "note saved", Toast.LENGTH_SHORT).show();
        } else if (requestCode == QUERY_NOTE_REQUEST && resultCode == RESULT_OK) {
            assert data != null;
            int id = data.getIntExtra(EditNoteActivity.EXTRA_ID, -1);

            if (id == -1) {
                Toast.makeText(this, "Note can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }
            String title = data.getStringExtra(EditNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(EditNoteActivity.EXTRA_DESCRIPTION);
            String time = data.getStringExtra(EditNoteActivity.EXTRA_TIME);
            Note note = new Note(title, description, time);
            note.setId(id);
            noteViewModel.update(note);
            Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete_all_notes) {
            noteViewModel.deleteAll();
            Toast.makeText(this, "All notes deleted", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

