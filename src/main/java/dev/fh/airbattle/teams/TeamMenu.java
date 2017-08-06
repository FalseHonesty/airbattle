package dev.fh.airbattle.teams;

import dev.fh.airbattle.gamestates.WaitingState;
import dev.fh.airbattle.players.AirbattlePlayer;
import dev.fh.airbattle.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class TeamMenu {
    private Inventory inventory;
    private WaitingState ownerState;

    public TeamMenu(WaitingState ownerState) {
        inventory = Bukkit.getServer().createInventory(null, 9, "Team Menu");
        inventory.setItem(0, new ItemBuilder(Material.WOOL, 1)
                .setName(ChatColor.RED + "Team Red").setDamage((short) 14).build());
        inventory.setItem(1, new ItemBuilder(Material.WOOL, 1)
                .setName(ChatColor.BLUE + "Team Blue").setDamage((short) 11).build());

        this.ownerState = ownerState;
    }

    public void onClick(InventoryClickEvent e) {
        if (e.getCurrentItem().getType() != Material.WOOL) {
            return;
        }

        e.setCancelled(true);

        if (e.getCurrentItem().getDurability() == 11) {
            AirbattlePlayer abPlayer = ownerState.getPlayerManager().getABPlayer(e.getWhoClicked().getUniqueId());

            abPlayer.setSelectedTeam(Team.BLUE);
            Team.BLUE.addPlayer(abPlayer);
            Team.RED.removePlayer(abPlayer);

            e.getWhoClicked().sendMessage("Now on " + ChatColor.BLUE + "Team Blue");
        } else if (e.getCurrentItem().getDurability() == 14) {
            AirbattlePlayer abPlayer = ownerState.getPlayerManager().getABPlayer(e.getWhoClicked().getUniqueId());

            abPlayer.setSelectedTeam(Team.RED);
            Team.RED.addPlayer(abPlayer);
            Team.BLUE.removePlayer(abPlayer);

            e.getWhoClicked().sendMessage("Now on " + ChatColor.RED + "Team Red");
        }
    }

    public void openInventory(Player p) {
        p.openInventory(inventory);
    }
}
