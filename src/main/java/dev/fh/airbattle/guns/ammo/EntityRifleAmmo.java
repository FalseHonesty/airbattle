package dev.fh.airbattle.guns.ammo;

import net.minecraft.server.v1_12_R1.EntityArrow;
import net.minecraft.server.v1_12_R1.EntityLiving;
import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.World;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftArrow;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.inventivetalent.particle.ParticleEffect;

public class EntityRifleAmmo extends EntityArrow {

    private int particleInterval = 0;

    public EntityRifleAmmo(World world) {
        super(world);
    }

    public EntityRifleAmmo(World world, double d0, double d1, double d2) {
        super(world, d0, d1, d2);
    }

    public EntityRifleAmmo(World world, EntityLiving entityliving) {
        super(world, entityliving);
    }

    @Override
    protected ItemStack j() {
        return null;
    }

    @Override
    public void B_() {
        this.velocityChanged = true;

        particleInterval++;

        if (!this.inGround && particleInterval % 5 == 0) {
            CraftArrow craftArrow = new CraftArrow(((CraftServer) Bukkit.getServer()), this);
            ParticleEffect.CRIT_MAGIC.sendColor(Bukkit.getOnlinePlayers(), craftArrow.getLocation(), Color.WHITE);
        }

        super.B_();
    }

    public void launch(Player player) {
        this.a(((CraftPlayer) player).getHandle(), player.getLocation().getPitch(), player.getLocation().getYaw(), 0f, 3f, 1f);
    }
}