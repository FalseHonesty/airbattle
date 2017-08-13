package dev.fh.airbattle.respawn;

import dev.fh.airbattle.Airbattle;
import dev.fh.airbattle.players.AirbattlePlayer;
import dev.fh.airbattle.players.PlayerMode;
import dev.fh.airbattle.util.AirbattleConfig;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;

/**
 * Copyright 2017 (c) FalseHonesty
 */

public class RespawnManager {
    private int timeUntilRespawn;
    private BukkitTask secondTask;
    private ArrayList<AirbattlePlayer> deadPlayers;

    public RespawnManager() {
        timeUntilRespawn = AirbattleConfig.respawnTimer;

        secondTask = Bukkit.getScheduler().runTaskTimer(Airbattle.plugin, this::onTick, 0L, 20L);
        deadPlayers = new ArrayList<>();
    }

    private void onTick() {
        timeUntilRespawn--;

        if (timeUntilRespawn == 0) {
            timeUntilRespawn = AirbattleConfig.respawnTimer;

            for (AirbattlePlayer deadPlayer : deadPlayers) {
                PlayerMode.ALIVE.setPlayerToMode(deadPlayer);
            }
        }
    }

    // I had to make the joke :)

    /**
     * Adds a player to the respawn wave.
     * All players in this wave will respawn at the same time.
     * Also sets the player to the {@link PlayerMode} RESPAWNING
     * This time is configurable in the config file.
     * @param player player to respawn next wave
     */
    public void addPlayerToDeadPool(AirbattlePlayer player) {
        deadPlayers.add(player);
        PlayerMode.RESPAWNING.setPlayerToMode(player);
    }
}
