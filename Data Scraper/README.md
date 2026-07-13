# IPL 2026 Comprehensive Data Scraper Pipeline 🏏

A Python-based ETL (Extract, Transform, Load) pipeline that scrapes comprehensive IPL 2026 batting and bowling statistics, merges them into a single consolidated dataset, and saves the final records directly into a PostgreSQL database.

## 📌 Features

* **Semi-Automated Scraping:** Uses Selenium to open the browser, allowing the user to manually bypass dynamic loading (like clicking "View All") before scraping the data with BeautifulSoup.
* **Intelligent Data Merging:** Fetches both "Most Runs" and "Most Wickets" tables, dynamically prefixes columns to avoid naming collisions (`bat_` vs `bowl_`), and performs an outer join to capture all players.
* **Direct Database Integration:** Uses SQLAlchemy to automatically write the cleaned DataFrame to a local PostgreSQL database.

---

## ⚙️ Prerequisites

Before running this script, ensure you have the following installed on your system:

* **Python 3.7+**
* **Google Chrome** (The script uses Chrome in non-headless mode)
* **PostgreSQL** installed and running locally.

### Required Python Libraries

You can install the required Python packages using `pip`. Note: You will need a PostgreSQL driver like `psycopg2` for SQLAlchemy to connect successfully.

```bash
pip install pandas beautifulsoup4 selenium sqlalchemy psycopg2-binary

```

---

## 🛠️ Setup & Configuration

### 1. Database Setup

Ensure your local PostgreSQL server is running. You must create a database named `ipl_database` before running the script. You can do this via pgAdmin or the psql command line:

```sql
CREATE DATABASE ipl_database;

```

### 2. Update Credentials

Open the Python script and locate the `save_to_postgres()` function. Update the `DB_PASSWORD` variable with your actual PostgreSQL password:

```python
DB_USER = 'postgres'
DB_PASSWORD = 'Your_Actual_Password_Here' # <-- Update this
DB_HOST = 'localhost'
DB_PORT = '5432'
DB_NAME = 'ipl_database'

```

---

## 🚀 How to Use

Because the IPL stats webpage uses dynamic loading, this script is designed to pause and wait for your manual input to ensure all data is loaded before scraping.

1. **Run the script** from your terminal:
```bash
python ipl_scraper.py

```


2. **Follow the Terminal Prompts:**
* A Google Chrome window will open automatically, navigating to the **Batting Stats** page.
* **Wait and scroll down.** If there is a "View All" or "Load More" button on the webpage, click it manually.
* Once you visually confirm all players have loaded in the browser, go back to your terminal and **press `[ENTER]**`.


3. **Repeat for Bowling:**
* The browser will then navigate to the **Bowling Stats** page.
* Repeat the process: click "View All", wait for the list to load, and press `[ENTER]` in the terminal.


4. **Data Processing:**
* The script will automatically close the browser, clean the headers, merge the batting and bowling data, and push the final table (`player_stats_2026`) to your PostgreSQL database.



---

## 📊 Output

If successful, the terminal will print the first 10 rows of the consolidated dataset.

In your PostgreSQL database, a new table named `player_stats_2026` will be created (or replaced if it already exists). The table includes:

* `player`: The player's name.
* `team`: The player's franchise.
* `bat_*`: Columns containing batting statistics (e.g., `bat_runs`, `bat_avg`).
* `bowl_*`: Columns containing bowling statistics (e.g., `bowl_wkts`, `bowl_econ`).
