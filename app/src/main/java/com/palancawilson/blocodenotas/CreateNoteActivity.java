package com.palancawilson.blocodenotas;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Atividade usada para criar uma nova nota ou editar uma nota existente.
 * Se receber um identificador de nota, entra em modo de edição.
 */
public class CreateNoteActivity extends AppCompatActivity {

    public static final String EXTRA_ID_NOTA = "com.palancawilson.blocodenotas.EXTRA_NOTE_ID";

    private EditText campoTitulo;
    private EditText campoTexto;
    private long idNota = -1;

    @Override
    protected void onCreate(Bundle estadoGuardado) {
        super.onCreate(estadoGuardado);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_create_note);
        ajustarEspacoDasBarras();

        campoTitulo = findViewById(R.id.campoTitulo);
        campoTexto = findViewById(R.id.campoTexto);

        // Se recebemos um identificador, estamos a editar uma nota existente.
        idNota = getIntent().getLongExtra(EXTRA_ID_NOTA, -1);
        if (idNota >= 0) {
            Nota nota = RepositorioNotas.obterInstancia(this).obterNota(idNota);
            if (nota == null) {
                finish();
                return;
            }

            // Preenche os campos com os dados atuais da nota.
            campoTitulo.setText(nota.obterTitulo());
            campoTexto.setText(nota.obterConteudo());
        }

        findViewById(R.id.botaoOk).setOnClickListener(visao -> guardarNota());
        findViewById(R.id.botaoCancelar).setOnClickListener(visao -> {
            setResult(RESULT_CANCELED);
            finish();
        });
    }

    // Valida os campos e guarda a nota (nova ou editada).
    private void guardarNota() {
        String titulo = campoTitulo.getText().toString().trim();
        String conteudo = campoTexto.getText().toString();

        // O título é obrigatório.
        if (titulo.isEmpty()) {
            campoTitulo.setError(getString(R.string.erro_titulo_obrigatorio));
            campoTitulo.requestFocus();
            return;
        }

        RepositorioNotas repositorio = RepositorioNotas.obterInstancia(this);
        if (idNota >= 0) {
            repositorio.atualizarNota(idNota, titulo, conteudo);
        } else {
            repositorio.adicionarNota(titulo, conteudo);
        }

        setResult(RESULT_OK);
        finish();
    }

    // Evita que o conteúdo fique escondido atrás das barras do sistema.
    private void ajustarEspacoDasBarras() {
        View raiz = findViewById(R.id.raiz);
        int espacoEsquerda = raiz.getPaddingLeft();
        int espacoTopo = raiz.getPaddingTop();
        int espacoDireita = raiz.getPaddingRight();
        int espacoFundo = raiz.getPaddingBottom();

        ViewCompat.setOnApplyWindowInsetsListener(raiz, (visao, margens) -> {
            Insets barrasSistema = margens.getInsets(WindowInsetsCompat.Type.systemBars());
            visao.setPadding(
                    espacoEsquerda + barrasSistema.left,
                    espacoTopo + barrasSistema.top,
                    espacoDireita + barrasSistema.right,
                    espacoFundo + barrasSistema.bottom
            );
            return margens;
        });
    }
}
