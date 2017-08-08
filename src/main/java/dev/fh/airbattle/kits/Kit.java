package dev.fh.airbattle.kits;

import dev.fh.airbattle.guns.Gun;
import dev.fh.airbattle.guns.MachineGun;
import dev.fh.airbattle.guns.RocketLauncher;
import dev.fh.airbattle.util.AirbattleConfig;
import dev.fh.airbattle.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum Kit {

    SOLDIERKIT(AirbattleConfig.soldierItems,
            new ItemBuilder(Material.CHAINMAIL_HELMET).setUnbreakable(true).build(),
            new ItemBuilder(Material.ELYTRA).setUnbreakable(true).build(),
            new ItemBuilder(Material.CHAINMAIL_LEGGINGS).setUnbreakable(true).build(),
            new ItemBuilder(Material.CHAINMAIL_BOOTS).setUnbreakable(true).build(),
            new ItemBuilder(Material.IRON_SWORD).setName(ChatColor.RED + "Soldier")
                    .setLore("A soldier is a full-out combat kit", "They are very strong in combat, as they have a large arsenal of weapons",
                            "However, they are not very maneuverable in the sky.", "They are ideal for holding a point").build(),
            ChatColor.RED + "Soldier Kit",
            new ArrayList<>(Arrays.asList(new MachineGun(), new RocketLauncher())));

    public HashMap<Integer, ItemStack> items;
    public ArrayList<Gun> guns;
    public ItemStack helm;
    public ItemStack chest;
    public ItemStack legs;
    public ItemStack boots;
    public ItemStack symbol;
    public String name;

    Kit(HashMap<Integer, ItemStack> items, ItemStack helm, ItemStack chest, ItemStack legs, ItemStack boots, ItemStack symbol,
        String name, ArrayList<Gun> guns) {
        this.items = items;
        this.helm = helm;
        this.chest = chest;
        this.legs = legs;
        this.boots = boots;
        this.symbol = symbol;
        this.name = name;
        this.guns = guns;
    }

    public void setPlayerInventory(Player p) {
        PlayerInventory inv = p.getInventory();
        for (Map.Entry<Integer, ItemStack> entry : items.entrySet()) {
            inv.setItem(entry.getKey(), entry.getValue());
        }

        inv.setHelmet(helm);
        inv.setChestplate(chest);
        inv.setLeggings(legs);
        inv.setBoots(boots);
    }

    public <T extends Gun> T getGunByClass(Class<? extends T> gunClass) {
        for (Gun gun : this.guns) {
            if (gun.getClass().equals(gunClass)) {
                return gunClass.cast(gun);
            }
        }

        return null;
    }
}
