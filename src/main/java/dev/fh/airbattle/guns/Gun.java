package dev.fh.airbattle.guns;

import dev.fh.airbattle.players.AirbattlePlayer;

public abstract class Gun {
    protected boolean onCooldown = false;

    public abstract void onShoot(AirbattlePlayer abPlayer);
}
