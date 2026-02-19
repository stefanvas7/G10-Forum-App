# G10 Forum App

A lightweight Java forum/Q&A project with:
- A **Spring Boot backend** (`Forum-Server`) that stores posts as JSON files
- A **Java Swing desktop client** (`Forum-App`) that reads/writes posts through REST endpoints

## Repository Layout

```text
G10-Forum-App/
├── Forum-App/        # Swing desktop client (Maven project)
└── Forum-Server/     # Spring Boot REST API (Maven project)
```

## Tech Stack

- Java 20
- Maven
- Spring Boot 3.0.6 (`Forum-Server`)
- Java Swing (`Forum-App`)
- JSON file persistence (`Forum-Server/src/main/resources/postJSON`)


## Run the Project

### 1) Start the backend server

From `Forum-Server`:

```bash
cd Forum-Server
./mvnw spring-boot:run
```

Server default URL: `http://localhost:8080`

### 2) Start the desktop client

Open `Forum-App` in your IDE and run the `main` method in:
- `src/main/java/client.java`

The client expects the backend to already be running on `localhost:8080`.

## API Endpoints

All endpoints are currently `GET` endpoints.

- `GET /`
	- Returns a JSON array of posts for page 1
	- Also calls test-data setup on each request
- `GET /next/{page}`
	- Returns posts for a specific page
- `GET /previous/{page}`
	- Returns posts for a specific page
- `GET /post/{id}`
	- Returns one post object by ID
- `GET /post/create/{question}/{description}`
	- Creates a post
- `GET /post/{postid}/{answer}/{username}`
	- Adds an answer to a post

Example calls:

```bash
curl http://localhost:8080/
curl http://localhost:8080/post/<post-id>
curl "http://localhost:8080/post/create/Hello/First-post"
```

## Data Format

Each post is stored as one JSON file:

```json
{
	"id": "uuid",
	"question": "Question text",
	"description": "Optional description",
	"answers": [
		{
			"username": "alice",
			"answer": "My answer"
		}
	]
}
```

Location:
- `Forum-Server/src/main/resources/postJSON/*.json`
