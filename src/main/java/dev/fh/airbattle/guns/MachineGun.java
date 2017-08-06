package dev.fh.airbattle.guns;

import dev.fh.airbattle.Airbattle;
import dev.fh.airbattle.guns.ammo.EntityRifleAmmo;
import dev.fh.airbattle.players.AirbattlePlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;

public class MachineGun extends Gun {
    public static Material item = Material.STONE_HOE;

    private boolean onCooldown = false;

    @Override
    public void onShoot(AirbattlePlayer abPlayer) {
        if (onCooldown) {
            abPlayer.playSound(Sound.BLOCK_ANVIL_FALL, 1, 1);
            return;
        }

        EntityRifleAmmo round = new EntityRifleAmmo(((CraftWorld) abPlayer.getPlayer().getWorld()).getHandle());
        round.setNoGravity(true);
        round.launch(abPlayer.getPlayer());

        this.onCooldown = true;
        abPlayer.playSound(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);

        Bukkit.getScheduler().runTaskLater(Airbattle.plugin, () -> {
            onCooldown = false;
        }, 20L * 2);
    }
}
