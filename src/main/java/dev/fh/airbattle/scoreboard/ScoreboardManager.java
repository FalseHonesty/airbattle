package dev.fh.airbattle.scoreboard;

import dev.fh.airbattle.Airbattle;
import dev.fh.airbattle.events.TeamScoreEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;

public class ScoreboardManager implements Listener {
    private Scoreboard scoreboard;
    private Objective objective;
    private HashMap<String, Score> scores;
    private final String REDSCORE = ChatColor.RED + "Red Score " + ChatColor.GREEN;
    private final String BLUESCORE = ChatColor.BLUE + "Blue Score " + ChatColor.GREEN;

    public ScoreboardManager() {
        Bukkit.getPluginManager().registerEvents(this, Airbattle.plugin);
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        objective = scoreboard.registerNewObjective("scoreboard", "dummy");

        scoreboard.clearSlot(DisplaySlot.SIDEBAR);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(ChatColor.AQUA.toString() + ChatColor.BOLD + "Airbattle");

        scores = new HashMap<>();

        addScore(" ", "blank1", 10);
        addScore(REDSCORE + "0", "redscore", 9);
        addScore(BLUESCORE + "0", "bluescore", 8);

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.setScoreboard(scoreboard);
        }
    }

    private void addScore(String name, String key, int value) {
        Score score = objective.getScore(name);
        score.setScore(value);

        if (scores.containsKey(key)) {
            scores.remove(key);
        }

        scores.put(key, score);
    }

    @EventHandler
    public void onTeamScore(TeamScoreEvent e) {
        scoreboard.resetScores(scores.get(e.getScorer().name + "score").getEntry());

        if (e.getScorer().name.equalsIgnoreCase("red")) {
            addScore(REDSCORE + e.getScorer().points, "redscore", 9);
        } else {
            addScore(BLUESCORE + e.getScorer().points, "bluescore", 8);
        }

    }

    public void finish() {
        objective.unregister();
        scoreboard.clearSlot(DisplaySlot.SIDEBAR);
    }
}
