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

/**
 * Atividade principal.
 * Mostra os títulos das notas e um botão para criar uma nova nota.
 */
public class ListNotesActivity extends AppCompatActivity {

    private final List<Nota> notas = new ArrayList<>();
    private ArrayAdapter<String> adaptador;

    @Override
    protected void onCreate(Bundle estadoGuardado) {
        super.onCreate(estadoGuardado);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_list_notes);
        ajustarEspacoDasBarras();

        ListView listaNotas = findViewById(R.id.listaNotas);
        View textoListaVazia = findViewById(R.id.textoListaVazia);

        // Mostra a mensagem quando ainda não existe nenhuma nota.
        listaNotas.setEmptyView(textoListaVazia);

        // O adaptador transforma os títulos das notas em linhas da lista.
        adaptador = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        listaNotas.setAdapter(adaptador);

        // Ao clicar numa nota, abre a atividade que mostra o seu conteúdo.
        listaNotas.setOnItemClickListener((pai, visao, posicao, identificador) -> {
            Nota nota = notas.get(posicao);
            Intent intencao = new Intent(this, ReadNoteActivity.class);
            intencao.putExtra(ReadNoteActivity.EXTRA_ID_NOTA, nota.obterId());
            startActivity(intencao);
        });

        // O botão abre a atividade para criar uma nova nota.
        findViewById(R.id.botaoNovaNota).setOnClickListener(visao ->
                startActivity(new Intent(this, CreateNoteActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Atualiza a lista sempre que voltamos a esta atividade.
        // Assim, notas criadas, editadas ou apagadas aparecem imediatamente.
        atualizarLista();
    }

    // Lê novamente as notas do repositório e atualiza o ecrã.
    private void atualizarLista() {
        notas.clear();
        notas.addAll(RepositorioNotas.obterInstancia(this).obterNotas());

        adaptador.clear();
        for (Nota nota : notas) {
            adaptador.add(nota.obterTitulo());
        }
        adaptador.notifyDataSetChanged();
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
