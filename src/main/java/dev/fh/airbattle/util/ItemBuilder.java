package dev.fh.airbattle.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class ItemBuilder {
    private ItemStack item;

    public ItemBuilder(Material material, Integer stackSize) {
        item = new ItemStack(material, stackSize);
    }

    public ItemBuilder(Material material) {
        this(material, 1);
    }

    public ItemBuilder setName(String name) {
        ItemMeta im = item.getItemMeta();
        im.setDisplayName(name);
        item.setItemMeta(im);
        return this;
    }

    public ItemBuilder setLore(String... lore) {
        return setLore(Arrays.asList(lore));
    }

    public ItemBuilder setLore(List<String> lore) {
        ItemMeta im = item.getItemMeta();
        im.setLore(lore);
        item.setItemMeta(im);
        return this;
    }

    public ItemBuilder setUnbreakable(Boolean unbreakable) {
        ItemMeta im = item.getItemMeta();
        im.setUnbreakable(unbreakable);
        item.setItemMeta(im);
        return this;
    }

    public ItemBuilder setDamage(short damage) {
        item.setDurability(damage);
        return this;
    }

    public ItemStack build() {
        return item;
    }
}
