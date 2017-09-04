package me.falsehonesty.airbattle.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import me.falsehonesty.airbattle.Airbattle;
import me.falsehonesty.airbattle.capturepoints.CapturePoint;
import me.falsehonesty.airbattle.game.Game;
import me.falsehonesty.airbattle.gamestates.Gamestates;
import me.falsehonesty.airbattle.gamestates.PlayState;
import me.falsehonesty.airbattle.util.AirbattleConfig;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.Set;

@CommandAlias("airbattle|ab")
public class AirbattleCommand extends BaseCommand {

    @Subcommand("help")
    @Default @UnknownHandler
    public void doHelp(Player player) {
        player.sendMessage(ChatColor.RED + "Please specify a command! (reload, cp, forcestart, setloc)");
    }

    @Subcommand("reload")
    @CommandPermission("airbattle.admin")
    public void doReload(Player player, String[] args) {
        AirbattleConfig.loadConfig(Airbattle.plugin.getConfig());

        if (Airbattle.plugin.currentGame != null) {
            Airbattle.plugin.currentGame.endGame();
        }

        Airbattle.plugin.currentGame = new Game();
        Airbattle.plugin.currentGame.startGame();
    }

    @Subcommand("cp add")
    @CommandPermission("airbattle.admin")
    public void doCaptureAdd(Player p, String[] args) {
        Block lookingAt = p.getTargetBlock((Set<Material>) null, 30);

        if (lookingAt.getType() != Material.WOOL) {
            p.sendMessage(ChatColor.RED + "Not wool!");
        } else {
            AirbattleConfig.capturePoints.add(new CapturePoint(lookingAt.getLocation()));
            p.sendMessage(ChatColor.GREEN + "Added capture point at: " + lookingAt.getLocation());
        }

        AirbattleConfig.setConfigOption("capturePoints", AirbattleConfig.capturePoints);
    }

    @Subcommand("cp remove")
    @CommandPermission("airbattle.admin")
    public void doCaptureRemove(Player p, String[] args) {
        Block lookingAt = p.getTargetBlock((Set<Material>) null, 30);

        for (CapturePoint cp : AirbattleConfig.capturePoints) {

            if (cp.getLocation().equals(lookingAt.getLocation())) {
                AirbattleConfig.capturePoints.remove(cp);
                p.sendMessage("Removed capture point at: " + cp.getLocation());
                break;
            }

            p.sendMessage("Capture point not found at: " + lookingAt.getLocation());
            AirbattleConfig.setConfigOption("capturePoints", AirbattleConfig.capturePoints);
        }
    }

    @Subcommand("cp list")
    @CommandPermission("airbattle.admin")
    public void doCaptureList(Player p, String[] args) {
        System.out.println(AirbattleConfig.capturePoints);
    }

    @Subcommand("forcestart")
    public void doForcestart(Player p, String[] args) {
        if (Airbattle.plugin.currentGame.getGamestateManager().currentGamestate.getGamestateType() == Gamestates.WAITING) {
            System.out.println("Forcestarting game...");
            Airbattle.plugin.currentGame.getGamestateManager().changeGamestate(new PlayState());
        }
    }

    @Subcommand("setloc")
    public void doSetloc(Player p, String[] args) {
        switch (args[0]) {
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
}
