package dev.fh.airbattle;

import dev.fh.airbattle.capturepoints.CapturePoint;
import dev.fh.airbattle.commands.AirbattleCommand;
import dev.fh.airbattle.game.Game;
import dev.fh.airbattle.util.AirbattleConfig;
import dev.fh.airbattle.util.NMSHelper;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

public class Airbattle extends JavaPlugin {
    public static Airbattle plugin;

    public Game currentGame;

    @Override
    public void onEnable() {
        plugin = this;
        getCommand("airbattle").setExecutor(new AirbattleCommand());

        FileConfiguration conf = getConfig();

        conf.addDefaults(AirbattleConfig.getConfigDefaults());
        conf.options().copyDefaults(true);
        saveConfig();

        AirbattleConfig.loadConfig(getConfig());

        if (AirbattleConfig.pluginEnabled) {
            NMSHelper.setServerPlayers(AirbattleConfig.maxTeamSize * 2);
            AirbattleConfig.lobbyLoc.getWorld().setGameRuleValue("announceAdvancements", "false");
            currentGame = new Game();
            currentGame.startGame();
        }
    }

    @Override
    public void onLoad() {
        ConfigurationSerialization.registerClass(CapturePoint.class);
    }

    @Override
    public void onDisable() {

    }
}
