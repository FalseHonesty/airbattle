package me.falsehonesty.airbattle.gamestates;

import me.falsehonesty.airbattle.game.Game;
import org.bukkit.event.Listener;

public abstract class Gamestate implements Listener {
    public abstract void onStart(Game game);

    public abstract void onStop();

    public abstract Gamestates getGamestateType();
}
