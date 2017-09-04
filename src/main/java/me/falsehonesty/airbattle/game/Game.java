package me.falsehonesty.airbattle.game;

import me.falsehonesty.airbattle.gamestates.FinishState;
import me.falsehonesty.airbattle.gamestates.GamestateManager;
import me.falsehonesty.airbattle.gamestates.WaitingState;
import me.falsehonesty.airbattle.players.PlayerManager;
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
