package dev.fh.airbattle.guns.ammo;

import dev.fh.airbattle.Airbattle;
import net.minecraft.server.v1_12_R1.EntityArrow;
import net.minecraft.server.v1_12_R1.EntityEgg;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftArrow;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEgg;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitTask;

public class RocketAmmo implements Listener {
    private Egg egg;
    private BukkitTask tickTask;
    private int ticksElapsed;

    public RocketAmmo(Player shooter) {
        egg = shooter.launchProjectile(Egg.class);
        egg.setGravity(false);
        egg.setVelocity(egg.getVelocity().multiply(2));

        Bukkit.getPluginManager().registerEvents(this, Airbattle.plugin);

        ticksElapsed = 0;
        tickTask = Bukkit.getScheduler().runTaskTimer(Airbattle.plugin, this::onTick, 0L, 1L);
    }

    private void onTick() {
        EntityEgg nmsEgg = ((CraftEgg) egg).getHandle();
        nmsEgg.velocityChanged = true;

        if (ticksElapsed >= 50 || egg.isDead()) {
            egg.remove();
            tickTask.cancel();
            return;
        }

        egg.getWorld().spawnParticle(Particle.REDSTONE, egg.getLocation().getX(), egg.getLocation().getY(),
                egg.getLocation().getZ(), 0, 0, 0, 0, 0);

        ticksElapsed++;
    }

    @EventHandler
    public void onProjectileHit(EntityDamageByEntityEvent e) {
        if (e.getCause() == EntityDamageEvent.DamageCause.PROJECTILE && e.getDamager().equals(egg)) {
            e.setCancelled(true);
            tickTask.cancel();
        }
    }
}
