package dev.fh.airbattle.gamestates;

import dev.fh.airbattle.Airbattle;
import org.bukkit.Bukkit;

public class GamestateManager {
    public Gamestate currentGamestate;
    public Gamestates currentGamestateType;

    public void changeGamestate(Gamestate gamestate) {
        if (currentGamestate != null) {
            currentGamestate.onStop();
            System.out.println("Ending gamestate:" + currentGamestate);
        }

        System.out.println("Starting gamestate:" + gamestate);
        currentGamestate = gamestate;
        currentGamestate.onStart(Airbattle.plugin.currentGame);
        Bukkit.getPluginManager().registerEvents(currentGamestate, Airbattle.plugin);
        currentGamestateType = gamestate.gamestateType;
    }
}
