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
    private Objective sidebar;
    private Objective healthDisplay;
    private HashMap<String, Score> scores;
    private final String REDSCORE = ChatColor.RED + "Red Score " + ChatColor.GREEN;
    private final String BLUESCORE = ChatColor.BLUE + "Blue Score " + ChatColor.GREEN;

    public ScoreboardManager() {
        Bukkit.getPluginManager().registerEvents(this, Airbattle.plugin);
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        sidebar = scoreboard.registerNewObjective("scoreboard", "dummy");

        healthDisplay = scoreboard.registerNewObjective("healthDisplay", "health");
        healthDisplay.setDisplaySlot(DisplaySlot.BELOW_NAME);

        scoreboard.clearSlot(DisplaySlot.SIDEBAR);
        sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);
        sidebar.setDisplayName(ChatColor.AQUA.toString() + ChatColor.BOLD + "Airbattle");

        scores = new HashMap<>();

        addScore(" ", "blank1", 10);
        addScore(REDSCORE + "0", "redscore", 9);
        addScore(BLUESCORE + "0", "bluescore", 8);

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.setScoreboard(scoreboard);
        }
    }

    private void addScore(String name, String key, int value) {
        Score score = sidebar.getScore(name);
        score.setScore(value);

        if (scores.containsKey(key)) {
            scores.remove(key);
        }

        scores.put(key, score);
    }

    @EventHandler
    public void onTeamScore(TeamScoreEvent e) {
        if (e.isCancelled()) {
            return;
        }

        scoreboard.resetScores(scores.get(e.getScorer().name + "score").getEntry());

        if (e.getScorer().name.equalsIgnoreCase("red")) {
            addScore(REDSCORE + e.getScorer().points, "redscore", 9);
        } else {
            addScore(BLUESCORE + e.getScorer().points, "bluescore", 8);
        }

    }

    public void finish() {
        sidebar.unregister();
        healthDisplay.unregister();
        scoreboard.clearSlot(DisplaySlot.SIDEBAR);
        scoreboard.clearSlot(DisplaySlot.BELOW_NAME);
    }
}
