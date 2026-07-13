import pandas as pd
from bs4 import BeautifulSoup
from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from sqlalchemy import create_engine
import time
import re

def fetch_stat_table(driver, url, stat_prefix):
    """
    Helper function to load a URL, pause for manual interaction, 
    scrape the table, and prefix the column names.
    """
    print(f"Fetching {stat_prefix.upper()} data from {url}...")
    driver.get(url)
    
    print("\n" + "="*55)
    print(f"🛑 MANUAL ACTION REQUIRED FOR {stat_prefix.upper()} 🛑")
    print("1. Look at the Chrome window that just opened.")
    print("2. Scroll down and manually click 'View All' (if needed).")
    print("3. Wait for the list of players to fully load on the screen.")
    input("4. ONCE LOADED, PRESS [ENTER] RIGHT HERE IN THE TERMINAL... ")
    print("Resuming scrape...\n" + "="*55)
    
    soup = BeautifulSoup(driver.page_source, "html.parser")
    table = soup.find("table")
    
    if not table:
        print(f"Error: Could not find the stats table for {stat_prefix}.")
        return pd.DataFrame()
        
    # 1. Extract and clean headers
    raw_headers = [th.text.strip().lower() for th in table.find_all("th")]
    clean_headers = []
    
    for header in raw_headers:
        h = re.sub(r'[^a-z0-9]', '_', header).strip('_')
        if 'player' in h:
            clean_headers.append('player')
        elif 'team' in h or h == '': 
            clean_headers.append('team')
        else:
            clean_headers.append(f"{stat_prefix}_{h}")
            
    # 2. Extract Rows
    rows = []
    for tr in table.find_all("tr")[1:]:
        tds = tr.find_all("td")
        if tds:
            row_data = [re.sub(r'\s+', ' ', td.text.strip()) for td in tds]
            rows.append(row_data)
            
    # 3. Create DataFrame
    df = pd.DataFrame(rows, columns=clean_headers)
    df = df.loc[:, ~df.columns.duplicated()] 
    
    return df

def scrape_comprehensive_ipl_data():
    """
    Scrapes both batting and bowling stats for 2026 and merges them.
    """
    print("Initializing browser (Headless mode disabled)...")
    chrome_options = Options()
    chrome_options.add_argument("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
    chrome_options.add_argument("window-size=1920,1080")
    
    # WE DISABLED HEADLESS SO YOU CAN INTERACT WITH THE PAGE
    # chrome_options.add_argument("--headless") 
    
    chrome_options.add_argument("--disable-gpu")
    chrome_options.add_argument("--no-sandbox")
    
    driver = webdriver.Chrome(options=chrome_options)
    
    batting_url = "https://www.iplt20.com/stats/2026/most-runs"
    bowling_url = "https://www.iplt20.com/stats/2026/most-wickets"
    
    df_batting = fetch_stat_table(driver, batting_url, "bat")
    df_bowling = fetch_stat_table(driver, bowling_url, "bowl")
    
    driver.quit()
    
    if df_batting.empty and df_bowling.empty:
        return None
        
    print("Merging batting and bowling data...")
    df_merged = pd.merge(df_batting, df_bowling, on='player', how='outer')
    
    if 'team_x' in df_merged.columns and 'team_y' in df_merged.columns:
        df_merged['team'] = df_merged['team_x'].fillna(df_merged['team_y'])
        df_merged = df_merged.drop(columns=['team_x', 'team_y'])
    
    cols_to_drop = [c for c in df_merged.columns if 'pos' in c or c.startswith('bat__') or c.startswith('bowl__')]
    df_merged = df_merged.drop(columns=cols_to_drop, errors='ignore')
    
    return df_merged

def save_to_postgres(df):
    """
    Writes the merged DataFrame directly to PostgreSQL.
    """
    DB_USER = 'postgres'
    DB_PASSWORD = 'Your password' # make changes here
    DB_HOST = 'localhost'
    DB_PORT = '5432'
    DB_NAME = 'ipl_database'
    
    engine_url = f'postgresql://{DB_USER}:{DB_PASSWORD}@{DB_HOST}:{DB_PORT}/{DB_NAME}'
    engine = create_engine(engine_url)
    
    try:
        print(f"Connecting to PostgreSQL database '{DB_NAME}'...")
        df.to_sql(name='player_stats_2026', con=engine, if_exists='replace', index=False)
        print("Success! Comprehensive data written to the 'player_stats_2026' table.")
    except Exception as e:
        print(f"Database Error: {e}")

if __name__ == "__main__":
    print("--- Starting IPL Comprehensive Data Pipeline ---")
    ipl_dataframe = scrape_comprehensive_ipl_data()
    
    if ipl_dataframe is not None and not ipl_dataframe.empty:
        print(f"Successfully compiled records for {len(ipl_dataframe)} unique players.")
        print(ipl_dataframe.head(10))
        
        save_to_postgres(ipl_dataframe)
    else:
        print("Pipeline aborted due to empty data.")
    print("--- Pipeline Finished ---")
    