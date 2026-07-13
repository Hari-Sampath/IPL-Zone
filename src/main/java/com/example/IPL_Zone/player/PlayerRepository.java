package com.example.IPL_Zone.player;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, String> {

    // You ONLY need to add this one!
    // Spring Boot magically writes the SQL: SELECT * FROM player_stats_2026 WHERE team = ?
    List<Player> findByTeam(String team);
    List<Player> findByPlayerContainingIgnoreCase(String name);

    // Delete and Find by Player name are already built-in as deleteById() and findById()
}