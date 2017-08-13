package dev.fh.airbattle.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

/**
 * Copyright 2017 (c) FalseHonesty
 */

public class ItemBuilder {
    private ItemStack item;

    public ItemBuilder(Material material, Integer stackSize) {
        item = new ItemStack(material, stackSize);
    }

    public ItemBuilder(Material material) {
        this(material, 1);
    }

    /**
     * Set the name of the item currently being built.
     * @param name the name to set on the item, can use color codes
     * @return current item builder allowing chaining of methods
     */
    public ItemBuilder setName(String name) {
        ItemMeta im = item.getItemMeta();
        im.setDisplayName(name);
        item.setItemMeta(im);
        return this;
    }

    /**
     * Set the lore of the item currently being built.
     * The lore is the lines of text displayed when hovering on
     * an item in the inventory.
     * @param lore a variable amount of lines to add on to the lore, can use color codes
     * @return current item builder allowing chaining of methods
     */
    public ItemBuilder setLore(String... lore) {
        return setLore(Arrays.asList(lore));
    }

    /**
     * Set the lore of the item currently being built.
     * The lore is the lines of text displayed when hovering on
     * an item in the inventory.
     * @param lore a list of lines to add on to the lore, can use color codes
     * @return current item builder allowing chaining of methods
     */
    public ItemBuilder setLore(List<String> lore) {
        ItemMeta im = item.getItemMeta();
        im.setLore(lore);
        item.setItemMeta(im);
        return this;
    }

    /**
     * Set whether or not the item being built can take damage.
     * @param unbreakable a boolean for being unbreakable, set to true for an invincible item
     * @return current item builder allowing chaining of methods
     */
    public ItemBuilder setUnbreakable(Boolean unbreakable) {
        ItemMeta im = item.getItemMeta();
        im.setUnbreakable(unbreakable);
        item.setItemMeta(im);
        return this;
    }

    /**
     * Set the durability of the item being built.
     * For some items durability deals with color, amount
     * of times it can be used, etc.
     * @param damage a short of the damage to set to the item being built
     * @return current item builder allowing for chaining of methods
     */
    public ItemBuilder setDamage(short damage) {
        item.setDurability(damage);
        return this;
    }

    /**
     * Build the item with all of the settings previously added.
     * @return the item the item builder has been creating
     */
    public ItemStack build() {
        return item;
    }
}
