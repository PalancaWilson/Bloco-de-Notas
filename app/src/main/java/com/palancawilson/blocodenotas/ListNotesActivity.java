package com.palancawilson.blocodenotas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class ListNotesActivity extends AppCompatActivity {

    private final List<Note> notes = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_list_notes);
        applyWindowInsets();

        ListView listViewNotes = findViewById(R.id.listViewNotes);
        View textViewEmpty = findViewById(R.id.textViewEmpty);
        listViewNotes.setEmptyView(textViewEmpty);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        listViewNotes.setAdapter(adapter);

        listViewNotes.setOnItemClickListener((parent, view, position, id) -> {
            Note note = notes.get(position);
            Intent intent = new Intent(this, ReadNoteActivity.class);
            intent.putExtra(ReadNoteActivity.EXTRA_NOTE_ID, note.getId());
            startActivity(intent);
        });

        findViewById(R.id.buttonNewNote).setOnClickListener(view ->
                startActivity(new Intent(this, CreateNoteActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshNotes();
    }

    private void refreshNotes() {
        notes.clear();
        notes.addAll(NoteRepository.getInstance(this).getNotes());

        adapter.clear();
        for (Note note : notes) {
            adapter.add(note.getTitle());
        }
        adapter.notifyDataSetChanged();
    }

    private void applyWindowInsets() {
        View root = findViewById(R.id.main);
        int paddingLeft = root.getPaddingLeft();
        int paddingTop = root.getPaddingTop();
        int paddingRight = root.getPaddingRight();
        int paddingBottom = root.getPaddingBottom();

        ViewCompat.setOnApplyWindowInsetsListener(root, (view, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(
                    paddingLeft + systemBars.left,
                    paddingTop + systemBars.top,
                    paddingRight + systemBars.right,
                    paddingBottom + systemBars.bottom
            );
            return insets;
        });
    }
}
