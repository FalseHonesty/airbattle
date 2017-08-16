package dev.fh.airbattle.commands;

import dev.fh.airbattle.Airbattle;
import dev.fh.airbattle.capturepoints.CapturePoint;
import dev.fh.airbattle.game.Game;
import dev.fh.airbattle.gamestates.Gamestates;
import dev.fh.airbattle.gamestates.PlayState;
import dev.fh.airbattle.util.AirbattleConfig;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

public class AirbattleCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("You're not a player!");
            return true;
        }

        Player p = ((Player) commandSender);

        if (strings.length == 0) {
            p.sendMessage(ChatColor.RED + "Please specify a command! (reload, cp, forcestart, setloc)");
            return true;
        }

        if (strings[0].equalsIgnoreCase("reload")) {
            AirbattleConfig.loadConfig(Airbattle.plugin.getConfig());

            if (Airbattle.plugin.currentGame != null) {
                Airbattle.plugin.currentGame.endGame();
            }

            Airbattle.plugin.currentGame = new Game();
            Airbattle.plugin.currentGame.startGame();
        }

        if (strings[0].equalsIgnoreCase("cp")) {
            Block lookingAt = p.getTargetBlock((Set<Material>) null, 30);

            if (strings.length == 1) {
                p.sendMessage("Please choose from [add, remove, list]");
            }

            switch (strings[1]) {
                case "add":
                    if (lookingAt.getType() != Material.WOOL) {
                        p.sendMessage(ChatColor.RED + "Not wool!");
                    } else {
                        AirbattleConfig.capturePoints.add(new CapturePoint(lookingAt.getLocation()));
                        p.sendMessage(ChatColor.GREEN + "Added capture point at: " + lookingAt.getLocation());
                    }

                    AirbattleConfig.setConfigOption("capturePoints", AirbattleConfig.capturePoints);
                    break;
                case "remove":
                    for (CapturePoint cp : AirbattleConfig.capturePoints) {

                        if (cp.getLocation().equals(lookingAt.getLocation())) {
                            AirbattleConfig.capturePoints.remove(cp);
                            p.sendMessage("Removed capture point at: " + cp.getLocation());
                            break;
                        }

                        p.sendMessage("Capture point not found at: " + lookingAt.getLocation());
                        AirbattleConfig.setConfigOption("capturePoints", AirbattleConfig.capturePoints);
                    }

                    break;
                case "list":
                    System.out.println(AirbattleConfig.capturePoints);
                    break;
            }
        }

        if (strings[0].equalsIgnoreCase("forcestart")
                && Airbattle.plugin.currentGame.getGamestateManager().currentGamestate.getGamestateType() == Gamestates.WAITING) {
            System.out.println("Forcestarting game...");
            Airbattle.plugin.currentGame.getGamestateManager().changeGamestate(new PlayState());
        }

        if (strings[0].equalsIgnoreCase("setloc")) {
            switch (strings[1]) {
                case "lobby":
                    AirbattleConfig.setConfigOption("lobbyLoc", p.getLocation());
                    AirbattleConfig.lobbyLoc = p.getLocation();
                    break;
                case "red":
                case "redteam":
                    AirbattleConfig.setConfigOption("redTeamLoc", p.getLocation());
                    AirbattleConfig.redTeamLoc = p.getLocation();
                    break;
                case "blue":
                case "blueteam":
                    AirbattleConfig.setConfigOption("blueTeamLoc", p.getLocation());
                    AirbattleConfig.blueTeamLoc = p.getLocation();
                    break;
                case "spectator":
                case "spec":
                    AirbattleConfig.setConfigOption("spectatorLoc", p.getLocation());
                    AirbattleConfig.spectatorLoc = p.getLocation();
                    break;
            }
        }
        return true;
    }
}
