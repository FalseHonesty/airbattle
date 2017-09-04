package me.falsehonesty.airbattle.guns;

import me.falsehonesty.airbattle.Airbattle;
import me.falsehonesty.airbattle.guns.ammo.RifleAmmo;
import me.falsehonesty.airbattle.players.AirbattlePlayer;
import org.bukkit.Bukkit;
import org.bukkit.Sound;

public class MachineGun extends Gun {
    @Override
    public void onShoot(AirbattlePlayer abPlayer) {
        if (onCooldown) {
            abPlayer.playSound(Sound.BLOCK_ANVIL_FALL, 1, 1);
            return;
        }

        RifleAmmo round = new RifleAmmo(abPlayer.getPlayer());

        this.onCooldown = true;
        abPlayer.playSound(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);

        Bukkit.getScheduler().runTaskLater(Airbattle.plugin, () -> onCooldown = false, 20L * 2);
    }
}
