package com.example.IPL_Zone.player;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service; // Use @Service instead of @Component for clarity

import java.util.List;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public List<Player> getPlayers(){
        return playerRepository.findAll();
    }

    public List<Player> getPlayersFromTeams(String team){
        if (team.isEmpty()){
            return getPlayers();
        }
        return playerRepository.findByTeam(team); // Calling your custom interface method
    }

    @Transactional
    public void deletePlayer(String playerName) {
        playerRepository.deleteById(playerName);
    }

    // 1. CREATE NEW PLAYER
    public Player createPlayer(Player newPlayer) {
        // We check if they exist first so we don't accidentally overwrite an existing player
        if (playerRepository.existsById(newPlayer.getPlayer())) {
            throw new RuntimeException("Player " + newPlayer.getPlayer() + " already exists!");
        }

        // Because the ID is new, this executes an INSERT query
        return playerRepository.save(newPlayer);
    }

    // 2. UPDATE EXISTING PLAYER
    public Player updatePlayer(Player updatedPlayer) {
        // We check if they exist first so we don't accidentally create a new player
        if (!playerRepository.existsById(updatedPlayer.getPlayer())) {
            throw new RuntimeException("Player " + updatedPlayer.getPlayer() + " does not exist!");
        }

        // Because the ID already exists, this executes an UPDATE query
        return playerRepository.save(updatedPlayer);
    }

    public List<Player> getPlayersFromName(String name) {
        return playerRepository.findByPlayerContainingIgnoreCase(name);
    }
}