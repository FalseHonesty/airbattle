package dev.fh.airbattle.events;

import dev.fh.airbattle.teams.Team;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameWinEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private @Getter @Setter Team winner;
    private boolean cancelled;

    public GameWinEvent(Team winner) {
        this.winner = winner;
        this.cancelled = false;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean toCancel) {
        this.cancelled = toCancel;
    }
}
