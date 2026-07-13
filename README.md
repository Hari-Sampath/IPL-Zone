You are absolutely right, I completely missed the single-player lookup endpoint! Good catch. If a user clicks on "Virat Kohli" in your Next.js frontend, the UI needs a way to fetch just his specific stats without downloading the entire database again.

Let's fix that Controller missing piece right now, and then I have drafted a professional, full-stack `README.md` for your GitHub repository that includes your SQL database fix.

### 1. The Missing Controller Endpoint

First, add this quick method to your `PlayerService.java` so it knows how to pull a single player:

```java
    // Add this to PlayerService.java
    public Player getPlayerByName(String playerName) {
        return playerRepository.findById(playerName)
                .orElseThrow(() -> new RuntimeException("Player " + playerName + " not found!"));
    }

```

Next, add this exact block to your `PlayerController.java` right below your other `@GetMapping`:

```java
    // Add this to PlayerController.java
    // READ: Get a specific player by their exact name
    // Test URL: GET http://localhost:8080/api/player/Abhishek Sharma
    @GetMapping(path = "{playerName}")
    public ResponseEntity<Player> getPlayerByName(@PathVariable("playerName") String playerName) {
        Player player = playerService.getPlayerByName(playerName);
        return new ResponseEntity<>(player, HttpStatus.OK);
    }

```

*(Make sure to restart Spring Boot after adding this!)*

---

### 2. Your Project README.md

Here is a comprehensive, professional README for your repository. It outlines your three-phase architecture, the tech stack, the API routes, and critically, it includes a dedicated "Troubleshooting/Data Fix" section for that SQL script.

Copy and paste everything below this line into a file named `README.md` in the root of your project:

```markdown
# 🏏 IPL Zone (Full-Stack Cricket Analytics Platform)

IPL Zone is a modern, comprehensive web application built to serve Indian Premier League (IPL) cricket statistics, fantasy cricket analysis, and player leaderboards. Inspired by top-tier sports analytics dashboards, it features a robust data pipeline, a Java Spring Boot REST API, and an upcoming premium UI.

## 🚀 Project Architecture

This project is built in three distinct phases:

### Phase 1: Data ETL Pipeline (Complete)
* **Tech Stack:** Python, Selenium, BeautifulSoup, Pandas
* **Process:** Semi-automated web scraping of IPL 2026 batting and bowling data. The pipeline merges datasets, handles column collisions, and formats the data for relational storage.
* **Storage:** PostgreSQL (`ipl_database`)

### Phase 2: REST API Backend (Current)
* **Tech Stack:** Java 17, Spring Boot, Spring Data JPA, Hibernate, Maven
* **Architecture:** Layered architecture utilizing Controllers, Services, and Repositories to enforce clean business logic and data access separation.
* **Features:** Full CRUD operations for player statistics, dynamic querying, and null-value handling for specialized sports stats (e.g., bowlers without batting data).

### Phase 3: Frontend Web Client (Upcoming)
* **Tech Stack:** Next.js (App Router), React 18, Tailwind CSS, Framer Motion, Recharts
* **Design:** Premium dark mode UI with glassmorphism, dynamic data tables, and interactive visual analytics.

---

## 🛠️ Local Setup & Installation

### 1. Database Configuration
Ensure PostgreSQL is installed and running locally on port `5432`. Create a database named `ipl_database`.

Update your `src/main/resources/application.properties` with your PostgreSQL credentials:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/ipl_database
spring.datasource.username=postgres
spring.datasource.password=YOUR_PASSWORD

```

### 2. Running the Backend

Ensure Java 17 and Maven are installed. Navigate to the root directory and run:

```bash
mvn spring-boot:run

```

The API will start on `http://localhost:8080`.

---

## ⚠️ Data Troubleshooting & ETL Fixes

Due to formatting anomalies during the Python web-scraping phase, the player's name and their team were occasionally merged into the `@Id` column, leaving the `team` column `null`.

**The Fix:**
If your API returns `[]` when filtering by team, run this SQL script directly in pgAdmin/PostgreSQL to clean the data. It uses regex to extract the team abbreviation from the end of the string and migrate it to the proper column:

```sql
UPDATE player_stats_2026
SET 
    team = substring(player from '([^ ]+)$'),
    player = trim(regexp_replace(player, '\s+[^ ]+$', ''))
WHERE team IS NULL;

```

*Note: Restart your Spring Boot server after executing this script to refresh Hibernate's internal cache.*

---

## 📡 API Reference

Base URL: `http://localhost:8080/api/player`

| Method | Endpoint                        | Description |
| --- |---------------------------------| --- |
| `GET` | `/api/player`                   | Fetch all IPL players |
| `GET` | `/api/player?team={teamName}`   | Fetch all players for a specific team (e.g., RCB) |
| `GET` | `/api/player?name={playername}` | Fetch statistics for a specific player |
| `POST` | `/api/player`                   | Create a new player record (JSON body required) |
| `PUT` | `/api/player`                   | Update an existing player's stats (JSON body required) |
| `DELETE` | `/api/player/{playerName}`      | Delete a player record from the database |

```

```