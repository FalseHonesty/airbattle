package dev.fh.airbattle.events;

import dev.fh.airbattle.teams.Team;
import lombok.Getter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TeamScoreEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private @Getter int score;
    private @Getter Team scorer;
    private boolean cancelled;

    public TeamScoreEvent(int score, Team scorer) {
        this.score = score;
        this.scorer = scorer;
    }

    @Override
    public HandlerList getHandlers() {
        return null;
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
