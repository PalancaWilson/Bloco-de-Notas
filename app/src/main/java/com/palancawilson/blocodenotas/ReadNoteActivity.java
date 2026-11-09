package com.palancawilson.blocodenotas;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

public class ReadNoteActivity extends AppCompatActivity {

    public static final String EXTRA_TITLE = "com.palancawilson.blocodenotas.EXTRA_TITLE";
    public static final String EXTRA_CONTENT = "com.palancawilson.blocodenotas.EXTRA_CONTENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_read_note);
        applyWindowInsets();

        TextView textViewTitle = findViewById(R.id.textViewTitle);
        TextView textViewContent = findViewById(R.id.textViewContent);

        textViewTitle.setText(getIntent().getStringExtra(EXTRA_TITLE));
        textViewContent.setText(getIntent().getStringExtra(EXTRA_CONTENT));

        findViewById(R.id.buttonBack).setOnClickListener(view -> finish());
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
