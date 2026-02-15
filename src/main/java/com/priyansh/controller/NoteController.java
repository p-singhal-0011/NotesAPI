package com.priyansh.controller;

import com.priyansh.dto.NoteRequest;
import com.priyansh.model.Note;
import com.priyansh.service.NoteService;
import com.priyansh.util.RateLimiter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notes")
public class NoteController {

    private final NoteService service;

    public NoteController(NoteService service) {
        this.service = service;
    }

    private String clean(String s) {
        return s == null ? null : s.trim();
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody NoteRequest req) {

        if (!RateLimiter.allowRequest()) {
            return ResponseEntity.status(429)
                    .body("Rate limit exceeded: Max 5 notes per minute.");
        }

        String title = clean(req.getTitle());
        String content = clean(req.getContent());

        if (title == null || title.isBlank() ||
            content == null || content.isBlank()) {
            return ResponseEntity.badRequest()
                    .body("Title and content are required and cannot be empty.");
        }

        return ResponseEntity.status(201)
                .body(service.create(title, content));
    }

    @GetMapping
    public List<Note> getAll() {
        return service.getAll();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestBody NoteRequest req) {

        String title = clean(req.getTitle());
        String content = clean(req.getContent());

        return service.update(id, title, content)
                .map(msg -> ResponseEntity.ok(msg))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam String q) {

        q = clean(q);

        if (q == null || q.isBlank()) {
            return ResponseEntity.badRequest()
                    .body("Search query cannot be empty.");
        }

        return ResponseEntity.ok(service.search(q));
    }
}

