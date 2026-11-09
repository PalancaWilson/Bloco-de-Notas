package com.palancawilson.blocodenotas;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Repositório das notas: guarda, lê, atualiza e apaga as notas.
 * As notas ficam gravadas nas SharedPreferences, por isso continuam
 * disponíveis depois de fechar e abrir a aplicação outra vez.
 */
public final class RepositorioNotas {

    private static final String NOME_PREFERENCIAS = "bloco_de_notas";
    private static final String CHAVE_NOTAS = "notes";

    // Só existe uma instância do repositório em toda a aplicação.
    private static RepositorioNotas instancia;

    private final SharedPreferences preferencias;
    private final List<Nota> notas = new ArrayList<>();
    private long proximoId = 1;

    private RepositorioNotas(Context contexto) {
        preferencias = contexto.getApplicationContext()
                .getSharedPreferences(NOME_PREFERENCIAS, Context.MODE_PRIVATE);
        carregar();
    }

    // Cria o repositório na primeira utilização e reutiliza-o depois.
    public static synchronized RepositorioNotas obterInstancia(Context contexto) {
        if (instancia == null) {
            instancia = new RepositorioNotas(contexto);
        }
        return instancia;
    }

    // Devolve uma cópia da lista de notas.
    public List<Nota> obterNotas() {
        return Collections.unmodifiableList(new ArrayList<>(notas));
    }

    // Procura uma nota pelo seu identificador.
    public Nota obterNota(long id) {
        for (Nota nota : notas) {
            if (nota.obterId() == id) {
                return nota;
            }
        }
        return null;
    }

    // Cria uma nova nota e guarda-a na lista.
    public void adicionarNota(String titulo, String conteudo) {
        notas.add(new Nota(proximoId++, titulo, conteudo));
        guardar();
    }

    // Apaga a nota que tem o identificador indicado.
    public void apagarNota(long id) {
        notas.removeIf(nota -> nota.obterId() == id);
        guardar();
    }

    // Altera o título e o texto de uma nota já existente.
    public void atualizarNota(long id, String titulo, String conteudo) {
        Nota nota = obterNota(id);
        if (nota == null) {
            return;
        }
        nota.definirTitulo(titulo);
        nota.definirConteudo(conteudo);
        guardar();
    }

    // Lê as notas que estão guardadas no telemóvel.
    private void carregar() {
        String json = preferencias.getString(CHAVE_NOTAS, null);
        if (json == null) {
            return;
        }

        try {
            JSONArray listaJson = new JSONArray(json);
            for (int i = 0; i < listaJson.length(); i++) {
                JSONObject objeto = listaJson.getJSONObject(i);

                // Notas antigas podem não ter identificador; nesse caso criamos um.
                long id = objeto.optLong("id", 0);
                if (id <= 0) {
                    id = proximoId;
                }
                proximoId = Math.max(proximoId, id + 1);

                notas.add(new Nota(
                        id,
                        objeto.optString("title", ""),
                        objeto.optString("content", "")
                ));
            }
        } catch (JSONException ignored) {
        }
    }

    // Converte a lista de notas para JSON e grava-a no telemóvel.
    private void guardar() {
        JSONArray listaJson = new JSONArray();
        for (Nota nota : notas) {
            JSONObject objeto = new JSONObject();
            try {
                objeto.put("id", nota.obterId());
                objeto.put("title", nota.obterTitulo());
                objeto.put("content", nota.obterConteudo());
            } catch (JSONException ignored) {
            }
            listaJson.put(objeto);
        }
        preferencias.edit().putString(CHAVE_NOTAS, listaJson.toString()).apply();
    }
}
