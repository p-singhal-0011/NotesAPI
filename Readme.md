# Notes API (Spring Boot)

Minimal backend API for managing notes with basic intelligence.

## Features
- Create note with validation
- Get all notes (sorted by most recently updated)
- Partial update with no-change detection
- Case-insensitive search
- Rate limit: 5 creations per minute

## Tech Stack
- Java 17
- Spring Boot
- In-memory storage (ConcurrentHashMap)

## Run
mvn spring-boot:run
