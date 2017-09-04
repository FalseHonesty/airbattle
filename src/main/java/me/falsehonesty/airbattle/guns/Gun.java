package me.falsehonesty.airbattle.guns;

import me.falsehonesty.airbattle.players.AirbattlePlayer;

public abstract class Gun {
    protected boolean onCooldown = false;

    public abstract void onShoot(AirbattlePlayer abPlayer);
}
