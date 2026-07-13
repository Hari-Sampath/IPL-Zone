
```markdown
# 🏏 IPL Zone (REST API Backend)

IPL Zone is a modern Java Spring Boot REST API built to serve Indian Premier League (IPL) cricket statistics, fantasy cricket analysis, and player leaderboards. This backend handles dynamic data querying and enforces clean business logic for sports statistics.

## 🚀 Project Architecture

This repository is powered by two core components:

### 1. Data ETL Pipeline (Data Source)
* **Tech Stack:** Python, Selenium, BeautifulSoup, Pandas
* **Process:** Semi-automated web scraping of IPL 2026 batting and bowling data. The pipeline merges datasets, handles column collisions, and formats the data for relational storage.
* **Storage:** PostgreSQL (`ipl_database`)

### 2. REST API (Core Application)
* **Tech Stack:** Java 17, Spring Boot, Spring Data JPA, Hibernate, Maven
* **Architecture:** Layered architecture utilizing Controllers, Services, and Repositories to enforce clean business logic and data access separation.
* **Features:** Full CRUD operations for player statistics, dynamic SQL querying (by team and partial name match), and wrapper-class null-value handling for specialized sports stats (e.g., bowlers without batting data).

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

| Method | Endpoint | Description |
| --- | --- | --- |
| `GET` | `/api/player` | Fetch all IPL players |
| `GET` | `/api/player?team={teamName}` | Fetch all players for a specific team (e.g., RCB) |
| `GET` | `/api/player?name={playername}` | Fetch statistics for a specific player (partial match) |
| `POST` | `/api/player` | Create a new player record (JSON body required) |
| `PUT` | `/api/player` | Update an existing player's stats (JSON body required) |
| `DELETE` | `/api/player/{playerName}` | Delete a player record from the database |

```

```