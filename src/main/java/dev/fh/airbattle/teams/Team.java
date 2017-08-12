package dev.fh.airbattle.teams;

import dev.fh.airbattle.Airbattle;
import dev.fh.airbattle.events.GameWinEvent;
import dev.fh.airbattle.events.TeamScoreEvent;
import dev.fh.airbattle.players.AirbattlePlayer;
import dev.fh.airbattle.util.AirbattleConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public enum Team {
    RED (ChatColor.RED.toString(), (short) 14, AirbattleConfig.redTeamLoc, "red"),
    BLUE (ChatColor.BLUE.toString(), (short) 11, AirbattleConfig.lobbyLoc, "blue");

    public String color;
    private ArrayList<AirbattlePlayer> players;
    public Integer points;
    public Short colorData;
    public Location spawnLoc;
    public String name;
    public org.bukkit.scoreboard.Team team;

    Team(String color, Short colorData, Location spawnLoc, String name) {
        this.color = color;
        this.points = 0;
        this.players = new ArrayList<>();
        this.colorData = colorData;
        this.spawnLoc = spawnLoc;
        this.name = name;

        if (Bukkit.getScoreboardManager().getMainScoreboard().getTeam(name) != null) {
            Bukkit.getScoreboardManager().getMainScoreboard().getTeam(name).unregister();
        }

        this.team = Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam(name);
        this.team.setPrefix(color + "[" + name.substring(0, 1).toUpperCase() + "] ");
        this.team.setAllowFriendlyFire(false);
        this.team.setCanSeeFriendlyInvisibles(true);
    }

    public void addPlayer(AirbattlePlayer abPlayer) {
        players.add(abPlayer);
        team.addEntry(abPlayer.getPlayer().getName());
    }

    public void removePlayer(AirbattlePlayer abPlayer) {
        players.remove(abPlayer);
        team.removeEntry(abPlayer.getPlayer().getName());
    }

    public void addPoints(int points) {
        this.points += points;

        TeamScoreEvent teamScoreEvent = new TeamScoreEvent(points, this);
        Bukkit.getPluginManager().callEvent(teamScoreEvent);

        if (this.points >= AirbattleConfig.winCondition) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                AirbattlePlayer abPlayer = Airbattle.plugin.currentGame.playerManager.getABPlayer(p.getUniqueId());

                if (abPlayer.getSelectedTeam().name.equals(this.name)) {
                    abPlayer.onGameWin();
                } else {
                    abPlayer.onGameLoss();
                }
            }

            GameWinEvent gameWinEvent = new GameWinEvent(this);
            Bukkit.getPluginManager().callEvent(gameWinEvent);
        }
    }

    public void gameEnd() {
        this.points = 0;
        this.players.clear();

        for (String entry : this.team.getEntries()) {
            this.team.removeEntry(entry);
        }

        this.team.unregister();
    }
}
