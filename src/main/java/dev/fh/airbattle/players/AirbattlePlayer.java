package dev.fh.airbattle.players;

import dev.fh.airbattle.kits.Kit;
import dev.fh.airbattle.teams.Team;
import dev.fh.airbattle.util.NMSHelper;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class AirbattlePlayer {
    private @Getter @Setter Kit selectedKit;
    private @Getter @Setter Player player;
    private @Getter @Setter PlayerMode playerMode;
    private @Getter @Setter Team selectedTeam;

    public AirbattlePlayer(Kit kit, Player player, Team selectedTeam) {
        this.selectedKit = kit;
        this.player = player;
        this.selectedTeam = selectedTeam;
    }

    public void sendActionbarMessage(String message) {
        NMSHelper.sendActionbarMessage(message, player);
    }

    public void teleportToSpawn() {
        player.teleport(selectedTeam.spawnLoc);
    }

    public void playSound(Sound soundToPlay, int volume, int pitch) {
        player.playSound(player.getLocation(), soundToPlay, volume, pitch);
    }

    public void setInventoryAsKit() {
        selectedKit.setPlayerInventory(player);
    }

    public void setNameAsColor() {
        player.setDisplayName(selectedTeam.color + player.getName());
        player.setCustomName(selectedTeam.color + player.getName());
        player.setCustomNameVisible(true);
    }

    public void onGameWin() {
        player.sendTitle(ChatColor.GREEN + "You won :)", ChatColor.GRAY + "Good work!", 10, 70, 20);
    }

    public void onGameLoss() {
        player.sendTitle(ChatColor.RED + "You lost :(", ChatColor.GRAY + "Better luck next time!", 10, 70, 20);
    }
}
