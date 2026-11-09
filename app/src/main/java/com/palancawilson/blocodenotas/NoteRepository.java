package com.palancawilson.blocodenotas;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class NoteRepository {

    private static final String PREFS_NAME = "bloco_de_notas";
    private static final String KEY_NOTES = "notes";

    private static NoteRepository instance;

    private final SharedPreferences preferences;
    private final List<Note> notes = new ArrayList<>();

    private NoteRepository(Context context) {
        preferences = context.getApplicationContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        load();
    }

    public static synchronized NoteRepository getInstance(Context context) {
        if (instance == null) {
            instance = new NoteRepository(context);
        }
        return instance;
    }

    public List<Note> getNotes() {
        return Collections.unmodifiableList(new ArrayList<>(notes));
    }

    public void addNote(Note note) {
        notes.add(note);
        save();
    }

    private void load() {
        String json = preferences.getString(KEY_NOTES, null);
        if (json == null) {
            return;
        }

        try {
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                notes.add(new Note(
                        object.optString("title", ""),
                        object.optString("content", "")
                ));
            }
        } catch (JSONException ignored) {
        }
    }

    private void save() {
        JSONArray array = new JSONArray();
        for (Note note : notes) {
            JSONObject object = new JSONObject();
            try {
                object.put("title", note.getTitle());
                object.put("content", note.getContent());
            } catch (JSONException ignored) {
            }
            array.put(object);
        }
        preferences.edit().putString(KEY_NOTES, array.toString()).apply();
    }
}
