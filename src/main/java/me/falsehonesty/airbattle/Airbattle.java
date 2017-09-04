package me.falsehonesty.airbattle;

import co.aikar.commands.BukkitCommandManager;
import me.falsehonesty.airbattle.capturepoints.CapturePoint;
import me.falsehonesty.airbattle.commands.AirbattleCommand;
import me.falsehonesty.airbattle.game.Game;
import me.falsehonesty.airbattle.util.AirbattleConfig;
import me.falsehonesty.airbattle.util.NMSHelper;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

public class Airbattle extends JavaPlugin {
    public static Airbattle plugin;

    public Game currentGame;

    @Override
    public void onEnable() {
        plugin = this;
        AirbattleConfig.setupConfig(getConfig());

        BukkitCommandManager commandManager = new BukkitCommandManager(this);
        commandManager.registerCommand(new AirbattleCommand());

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
