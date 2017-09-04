package me.falsehonesty.airbattle.gamestates;

import me.falsehonesty.airbattle.Airbattle;
import org.bukkit.Bukkit;

public class GamestateManager {
    public Gamestate currentGamestate;

    public void changeGamestate(Gamestate gamestate) {
        if (currentGamestate != null) {
            currentGamestate.onStop();
            System.out.println("Ending gamestate:" + currentGamestate);
        }

        System.out.println("Starting gamestate:" + gamestate);
        currentGamestate = gamestate;
        currentGamestate.onStart(Airbattle.plugin.currentGame);
        Bukkit.getPluginManager().registerEvents(currentGamestate, Airbattle.plugin);
    }
}
