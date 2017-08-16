package dev.fh.airbattle.gamestates;

import dev.fh.airbattle.Airbattle;
import dev.fh.airbattle.game.Game;
import dev.fh.airbattle.teams.Team;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public class FinishState extends Gamestate {

    private BukkitTask celebrateTask;
    private Team winner;

    public FinishState(Team winner) {
        this.winner = winner;
    }

    public void onStart(Game game) {

        if (winner == null) return;

        Bukkit.getScheduler().runTaskLater(Airbattle.plugin, () -> {
            Airbattle.plugin.currentGame = new Game();
            Airbattle.plugin.currentGame.startGame();
        }, 20L * 10);

        celebrateTask = Bukkit.getScheduler().runTaskTimer(Airbattle.plugin, this::celebrate, 0L, 5L);
    }

    private void celebrate() {

    }

    public void onStop() {
        if (winner == null) return;

        celebrateTask.cancel();

        Team.BLUE.gameEnd();
        Team.RED.gameEnd();
    }

    @Override
    public Gamestates getGamestateType() {
        return Gamestates.FINISHING;
    }
}
