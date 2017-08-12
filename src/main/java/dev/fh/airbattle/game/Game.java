package dev.fh.airbattle.game;

import dev.fh.airbattle.gamestates.FinishState;
import dev.fh.airbattle.gamestates.GamestateManager;
import dev.fh.airbattle.gamestates.WaitingState;
import dev.fh.airbattle.players.PlayerManager;
import lombok.Getter;

public class Game {
    @Getter
    private GamestateManager gamestateManager;

    public PlayerManager playerManager;

    public Game() {
        playerManager = new PlayerManager();

        gamestateManager = new GamestateManager();
    }

    public void startGame() {
        gamestateManager.changeGamestate(new WaitingState());
    }

    public void endGame() {
        gamestateManager.changeGamestate(new FinishState(null));
    }
}
