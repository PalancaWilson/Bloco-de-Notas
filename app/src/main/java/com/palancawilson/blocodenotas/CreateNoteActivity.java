package com.palancawilson.blocodenotas;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

public class CreateNoteActivity extends AppCompatActivity {

    public static final String EXTRA_NOTE_ID = "com.palancawilson.blocodenotas.EXTRA_NOTE_ID";

    private EditText editTextTitle;
    private EditText editTextContent;
    private long noteId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_create_note);
        applyWindowInsets();

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextContent = findViewById(R.id.editTextContent);

        noteId = getIntent().getLongExtra(EXTRA_NOTE_ID, -1);
        if (noteId >= 0) {
            Note note = NoteRepository.getInstance(this).getNote(noteId);
            if (note == null) {
                finish();
                return;
            }
            editTextTitle.setText(note.getTitle());
            editTextContent.setText(note.getContent());
        }

        findViewById(R.id.buttonOk).setOnClickListener(view -> saveNote());
        findViewById(R.id.buttonCancel).setOnClickListener(view -> {
            setResult(RESULT_CANCELED);
            finish();
        });
    }

    private void saveNote() {
        String title = editTextTitle.getText().toString().trim();
        String content = editTextContent.getText().toString();

        if (title.isEmpty()) {
            editTextTitle.setError(getString(R.string.error_title_required));
            editTextTitle.requestFocus();
            return;
        }

        NoteRepository repository = NoteRepository.getInstance(this);
        if (noteId >= 0) {
            repository.updateNote(noteId, title, content);
        } else {
            repository.addNote(title, content);
        }

        setResult(RESULT_OK);
        finish();
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
