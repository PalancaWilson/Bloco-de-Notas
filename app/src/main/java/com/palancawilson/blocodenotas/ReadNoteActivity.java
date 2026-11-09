package com.palancawilson.blocodenotas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Atividade que mostra o título e o texto de uma nota.
 * Também permite editar ou apagar essa nota.
 */
public class ReadNoteActivity extends AppCompatActivity {

    public static final String EXTRA_ID_NOTA = "com.palancawilson.blocodenotas.EXTRA_NOTE_ID";

    private long idNota;
    private TextView textoTitulo;
    private TextView textoConteudo;

    @Override
    protected void onCreate(Bundle estadoGuardado) {
        super.onCreate(estadoGuardado);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_read_note);
        ajustarEspacoDasBarras();

        // Recebe o identificador da nota que deve ser mostrada.
        idNota = getIntent().getLongExtra(EXTRA_ID_NOTA, -1);
        if (RepositorioNotas.obterInstancia(this).obterNota(idNota) == null) {
            finish();
            return;
        }

        textoTitulo = findViewById(R.id.textoTitulo);
        textoConteudo = findViewById(R.id.textoConteudo);

        // O botão Editar abre a mesma atividade de criação, mas em modo de edição.
        findViewById(R.id.botaoEditar).setOnClickListener(visao -> {
            Intent intencao = new Intent(this, CreateNoteActivity.class);
            intencao.putExtra(CreateNoteActivity.EXTRA_ID_NOTA, idNota);
            startActivity(intencao);
        });

        // O botão Apagar pede confirmação antes de remover a nota.
        findViewById(R.id.botaoApagar).setOnClickListener(visao ->
                new AlertDialog.Builder(this)
                        .setTitle(R.string.apagar_nota_titulo)
                        .setMessage(R.string.apagar_nota_mensagem)
                        .setNegativeButton(R.string.cancelar, null)
                        .setPositiveButton(R.string.apagar, (dialogo, opcao) -> {
                            RepositorioNotas.obterInstancia(this).apagarNota(idNota);
                            finish();
                        })
                        .show());

        // O botão Voltar apenas fecha esta atividade.
        findViewById(R.id.botaoVoltar).setOnClickListener(visao -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Volta a mostrar a nota para refletir alterações feitas no modo de edição.
        mostrarNota();
    }

    // Coloca o título e o texto da nota nos campos do ecrã.
    private void mostrarNota() {
        Nota nota = RepositorioNotas.obterInstancia(this).obterNota(idNota);
        if (nota == null) {
            finish();
            return;
        }
        textoTitulo.setText(nota.obterTitulo());
        textoConteudo.setText(nota.obterConteudo());
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
