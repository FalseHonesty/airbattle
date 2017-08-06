package dev.fh.airbattle.capturepoints;

import dev.fh.airbattle.Airbattle;
import dev.fh.airbattle.game.Game;
import dev.fh.airbattle.players.AirbattlePlayer;
import dev.fh.airbattle.util.AirbattleConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;

public class CaptureManager {
    private ArrayList<CapturePoint> capturePoints;
    private BukkitTask onTickTask;
    private Game game;

    public CaptureManager(Game game) {
        this.capturePoints = AirbattleConfig.capturePoints;
        this.onTickTask = Bukkit.getScheduler().runTaskTimer(Airbattle.plugin, this::onTick, 0, 1);
        this.game = game;
    }

    public void cancelTick() {
        onTickTask.cancel();
    }

    private void onTick() {
        for (CapturePoint cp : capturePoints) {
            if (cp == null) {
                System.out.println("cp == null?");
                continue;
            }

            ArrayList<Player> playersInRange = new ArrayList<>();
            int redTeamPlayers = 0;
            int blueTeamPlayers = 0;

            for (Player p : Bukkit.getOnlinePlayers()) {
                if (cp.isPlayerInRange(p)) {
                    playersInRange.add(p);

                    AirbattlePlayer abPlayer = game.playerManager.getABPlayer(p.getUniqueId());

                    if (abPlayer.getSelectedTeam().name.equalsIgnoreCase("red")) {
                        redTeamPlayers++;
                    } else {
                        blueTeamPlayers++;
                    }
                }
            }

            if (playersInRange.size() > 0) cp.capturing(redTeamPlayers, blueTeamPlayers);
            else cp.noneCapturing();

            cp.calculatePoints();
        }
    }
}
