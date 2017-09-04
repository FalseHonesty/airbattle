package me.falsehonesty.airbattle.players;

import org.bukkit.GameMode;

public enum PlayerMode {
    ALIVE, SPECTATING, RESPAWNING;

    public void setPlayerToMode(AirbattlePlayer player) {
        player.setPlayerMode(this);

        switch (this) {
            case ALIVE:
                player.getPlayer().setGameMode(GameMode.SURVIVAL);
                player.setInventoryAsKit();
                break;
            case RESPAWNING:
                player.getPlayer().setGameMode(GameMode.SPECTATOR);
                player.getPlayer().getInventory().clear();
                break;
            case SPECTATING:
                player.getPlayer().setGameMode(GameMode.SPECTATOR);
                player.getPlayer().getInventory().clear();
                break;
        }
    }
}
