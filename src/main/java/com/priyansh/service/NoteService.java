package com.priyansh.service;

import com.priyansh.model.Note;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class NoteService {

    private final Map<Long, Note> notes = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    public Note create(String title, String content) {
        Note note = new Note(idCounter.getAndIncrement(), title, content);
        notes.put(note.getId(), note);
        return note;
    }

    public List<Note> getAll() {
        return notes.values().stream()
                .sorted((a, b) -> b.getUpdatedAt().compareTo(a.getUpdatedAt()))
                .toList();
    }

    public Optional<String> update(Long id, String title, String content) {
        Note note = notes.get(id);
        if (note == null) return Optional.empty();

        boolean changed = false;

        if (title != null && !title.equals(note.getTitle())) {
            note.setTitle(title);
            changed = true;
        }

        if (content != null && !content.equals(note.getContent())) {
            note.setContent(content);
            changed = true;
        }

        if (!changed) {
            return Optional.of("No changes detected.");
        }

        note.setUpdatedAt(LocalDateTime.now());
        return Optional.of("Updated successfully.");
    }

    public List<Note> search(String query) {
        String q = query.toLowerCase();
        return notes.values().stream()
                .filter(n ->
                        n.getTitle().toLowerCase().contains(q) ||
                        n.getContent().toLowerCase().contains(q))
                .toList();
    }
}
