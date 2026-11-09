package com.palancawilson.blocodenotas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

public class ReadNoteActivity extends AppCompatActivity {

    public static final String EXTRA_NOTE_ID = "com.palancawilson.blocodenotas.EXTRA_NOTE_ID";

    private long noteId;
    private TextView textViewTitle;
    private TextView textViewContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_read_note);
        applyWindowInsets();

        noteId = getIntent().getLongExtra(EXTRA_NOTE_ID, -1);
        if (NoteRepository.getInstance(this).getNote(noteId) == null) {
            finish();
            return;
        }

        textViewTitle = findViewById(R.id.textViewTitle);
        textViewContent = findViewById(R.id.textViewContent);

        findViewById(R.id.buttonEdit).setOnClickListener(view -> {
            Intent intent = new Intent(this, CreateNoteActivity.class);
            intent.putExtra(CreateNoteActivity.EXTRA_NOTE_ID, noteId);
            startActivity(intent);
        });

        findViewById(R.id.buttonBack).setOnClickListener(view -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        showNote();
    }

    private void showNote() {
        Note note = NoteRepository.getInstance(this).getNote(noteId);
        if (note == null) {
            finish();
            return;
        }
        textViewTitle.setText(note.getTitle());
        textViewContent.setText(note.getContent());
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
