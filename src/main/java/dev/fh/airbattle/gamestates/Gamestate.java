package dev.fh.airbattle.gamestates;

import dev.fh.airbattle.game.Game;
import org.bukkit.event.Listener;

public abstract class Gamestate implements Listener {
    public Gamestates gamestateType;

    public abstract void onStart(Game game);

    public abstract void onStop();
}