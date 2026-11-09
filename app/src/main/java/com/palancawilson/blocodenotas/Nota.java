package com.palancawilson.blocodenotas;

/**
 * Representa uma nota do bloco de notas.
 * Cada nota tem um identificador, um título e um conteúdo (o texto da nota).
 */
public class Nota {

    private final long id;
    private String titulo;
    private String conteudo;

    public Nota(long id, String titulo, String conteudo) {
        this.id = id;
        this.titulo = titulo;
        this.conteudo = conteudo;
    }

    // Devolve o identificador único da nota.
    public long obterId() {
        return id;
    }

    // Devolve o título da nota.
    public String obterTitulo() {
        return titulo;
    }

    // Altera o título da nota.
    public void definirTitulo(String titulo) {
        this.titulo = titulo;
    }

    // Devolve o texto (conteúdo) da nota.
    public String obterConteudo() {
        return conteudo;
    }

    // Altera o texto (conteúdo) da nota.
    public void definirConteudo(String conteudo) {
        this.conteudo = conteudo;
    }
}
