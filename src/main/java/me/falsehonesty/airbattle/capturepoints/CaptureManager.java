package me.falsehonesty.airbattle.capturepoints;

import me.falsehonesty.airbattle.Airbattle;
import me.falsehonesty.airbattle.game.Game;
import me.falsehonesty.airbattle.players.AirbattlePlayer;
import me.falsehonesty.airbattle.util.AirbattleConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;

public class CaptureManager {
    private ArrayList<CapturePoint> capturePoints;
    private BukkitTask onTickTask;
    private Game game;
    private int ticksElapsed;

    public CaptureManager(Game game) {
        this.capturePoints = AirbattleConfig.capturePoints;
        this.onTickTask = Bukkit.getScheduler().runTaskTimer(Airbattle.plugin, this::onTick, 0, 1);
        this.game = game;
        this.ticksElapsed = 0;
    }

    public void cancelTick() {
        onTickTask.cancel();
    }

    private void onTick() {
        ticksElapsed++;

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

            if (ticksElapsed % 5 == 0) {
                cp.calculatePoints();
            }
        }

        if (ticksElapsed % 5 == 0) {
            ticksElapsed = 0;
        }
    }
}
