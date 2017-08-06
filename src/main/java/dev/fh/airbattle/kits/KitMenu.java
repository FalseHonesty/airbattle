package dev.fh.airbattle.kits;

import dev.fh.airbattle.gamestates.WaitingState;
import dev.fh.airbattle.players.AirbattlePlayer;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class KitMenu {
    @Getter
    private Inventory inventory;
    private WaitingState ownerState;

    public HashMap<ItemStack, Kit> kitMap = new HashMap<>();
    public static HashMap<String, Kit> kitNameMap = new HashMap<>();

    public KitMenu(WaitingState ownerState) {
        inventory = Bukkit.getServer().createInventory(null, 9, "Kit Menu");
        this.ownerState = ownerState;
    }

    public void addItems(ItemStack... items) {
        inventory.addItem(items);
    }

    public void openInventory(Player p) {
        p.openInventory(inventory);
    }

    public void registerKit(ItemStack itemStack, Kit kit, String name) {
        kitMap.put(itemStack, kit);
        kitNameMap.put(name, kit);
        addItems(itemStack);
    }

    public void onClick(InventoryClickEvent e) {
        if (kitMap.keySet().contains(e.getCurrentItem())) {
            AirbattlePlayer ABplayer = ownerState.getPlayerManager().getABPlayer(e.getWhoClicked().getUniqueId());
            ABplayer.setSelectedKit(kitMap.get(e.getCurrentItem()));
            e.getWhoClicked().sendMessage("Selected " + kitMap.get(e.getCurrentItem()).name);
        }
    }
}
