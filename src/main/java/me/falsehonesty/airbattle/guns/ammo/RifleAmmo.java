package me.falsehonesty.airbattle.guns.ammo;

import me.falsehonesty.airbattle.Airbattle;
import net.minecraft.server.v1_12_R1.EntityArrow;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitTask;

public class RifleAmmo implements Listener {
    private BukkitTask tickTask;
    private Arrow arrow;
    private int ticksElapsed;

    public RifleAmmo(Player shooter) {
        arrow = shooter.launchProjectile(Arrow.class);
        arrow.setGravity(false);
        arrow.setVelocity(arrow.getVelocity().clone().multiply(1.5));
        Bukkit.getPluginManager().registerEvents(this, Airbattle.plugin);

        ticksElapsed = 0;
        tickTask = Bukkit.getScheduler().runTaskTimer(Airbattle.plugin, this::onTick, 0L, 1L);
    }

    private void onTick() {
        EntityArrow nmsArrow = ((CraftArrow) arrow).getHandle();
        nmsArrow.velocityChanged = true;

        if (nmsArrow.inGround || ticksElapsed >= 50) {
            arrow.remove();
            tickTask.cancel();
            return;
        }

        arrow.getWorld().spawnParticle(Particle.REDSTONE, arrow.getLocation().getX(), arrow.getLocation().getY(),
                arrow.getLocation().getZ(), 0, 0, 0, 0, 0);

        ticksElapsed++;
    }

    @EventHandler
    public void onProjectileHit(EntityDamageByEntityEvent e) {
        if (e.getCause() == EntityDamageEvent.DamageCause.PROJECTILE && e.getDamager().equals(arrow)) {
            e.setCancelled(true);
            tickTask.cancel();
        }
    }
}
