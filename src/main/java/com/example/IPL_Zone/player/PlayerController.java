package com.example.IPL_Zone.player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/player")
public class PlayerController {

    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    // 1. READ: Get all players OR filter by team OR filter by player name
    // Test URLs: GET http://localhost:8080/api/player  OR  GET http://localhost:8080/api/player?team=RCB
    @GetMapping
    public List<Player> getPlayers(@RequestParam(required = false) String team, @RequestParam(required = false) String name) {
        if (team != null && !team.trim().isEmpty()) {
            return playerService.getPlayersFromTeams(team); // Calls your custom team search
        }
        if (name != null && !name.trim().isEmpty()) {
            return playerService.getPlayersFromName(name); // Calls your custom team search
        }
        return playerService.getPlayers(); // Calls the built-in findAll
    }

    // 2. CREATE: Add a brand new player
    // Test URL: POST http://localhost:8080/api/player
    @PostMapping
    public ResponseEntity<Player> addPlayer(@RequestBody Player player) {
        Player createdPlayer = playerService.createPlayer(player);
        return new ResponseEntity<>(createdPlayer, HttpStatus.CREATED); // Returns 201 Created
    }

    // 3. UPDATE: Update an existing player's stats
    // Test URL: PUT http://localhost:8080/api/player
    @PutMapping
    public ResponseEntity<Player> updatePlayer(@RequestBody Player player) {
        Player updatedPlayer = playerService.updatePlayer(player);
        return new ResponseEntity<>(updatedPlayer, HttpStatus.OK); // Returns 200 OK
    }

    // 4. DELETE: Remove a player from the database
    // Test URL: DELETE http://localhost:8080/api/player/Abhishek Sharma SRH
    @DeleteMapping(path = "{playerName}")
    public ResponseEntity<Void> deletePlayer(@PathVariable("playerName") String playerName) {
        playerService.deletePlayer(playerName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Returns 204 No Content
    }
}