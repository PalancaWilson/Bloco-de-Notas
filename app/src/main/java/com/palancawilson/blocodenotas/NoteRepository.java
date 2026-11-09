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
    private long nextId = 1;

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

    public Note getNote(long id) {
        for (Note note : notes) {
            if (note.getId() == id) {
                return note;
            }
        }
        return null;
    }

    public void addNote(String title, String content) {
        notes.add(new Note(nextId++, title, content));
        save();
    }

    public void deleteNote(long id) {
        notes.removeIf(note -> note.getId() == id);
        save();
    }

    public void updateNote(long id, String title, String content) {
        Note note = getNote(id);
        if (note == null) {
            return;
        }
        note.setTitle(title);
        note.setContent(content);
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
                long id = object.optLong("id", 0);
                if (id <= 0) {
                    id = nextId;
                }
                nextId = Math.max(nextId, id + 1);
                notes.add(new Note(
                        id,
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
                object.put("id", note.getId());
                object.put("title", note.getTitle());
                object.put("content", note.getContent());
            } catch (JSONException ignored) {
            }
            array.put(object);
        }
        preferences.edit().putString(KEY_NOTES, array.toString()).apply();
    }
}
