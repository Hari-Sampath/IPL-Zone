package com.example.IPL_Zone.player;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "player_stats_2026")
public class Player {
    @Id
    private String player;

    private String team;
    // Batting Stats (Nullable)
    @Column(name = "bat_runs")
    private Integer batRuns;

    @Column(name = "bat_avg")
    private Double batAvg;

    @Column(name = "bat_sr")
    private Double batSr;

    @Column(name = "bat_4s")
    private Integer bat4s;

    @Column(name = "bat_6s")
    private Integer bat6s;

    // Bowling Stats (Nullable)
    @Column(name = "bowl_wkts")
    private Integer bowlWkts;

    @Column(name = "bowl_econ")
    private Double bowlEcon;

    @Column(name = "bowl_avg")
    private Double bowlAvg;

    public Player(String player) {
        this.player = player;
    }

    public Double getBowlAvg() {
        return bowlAvg;
    }

    public void setBowlAvg(Double bowlAvg) {
        this.bowlAvg = bowlAvg;
    }

    public Double getBowlEcon() {
        return bowlEcon;
    }

    public void setBowlEcon(Double bowlEcon) {
        this.bowlEcon = bowlEcon;
    }

    public Integer getBowlWkts() {
        return bowlWkts;
    }

    public void setBowlWkts(Integer bowlWkts) {
        this.bowlWkts = bowlWkts;
    }

    public Integer getBat6s() {
        return bat6s;
    }

    public void setBat6s(Integer bat6s) {
        this.bat6s = bat6s;
    }

    public Integer getBat4s() {
        return bat4s;
    }

    public void setBat4s(Integer bat4s) {
        this.bat4s = bat4s;
    }

    public Double getBatSr() {
        return batSr;
    }

    public void setBatSr(Double batSr) {
        this.batSr = batSr;
    }

    public Double getBatAvg() {
        return batAvg;
    }

    public void setBatAvg(Double batAvg) {
        this.batAvg = batAvg;
    }

    public Integer getBatRuns() {
        return batRuns;
    }

    public void setBatRuns(Integer batRuns) {
        this.batRuns = batRuns;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public Player() {
        // Hibernate needs this empty constructor to build the object from the database
    }
}
