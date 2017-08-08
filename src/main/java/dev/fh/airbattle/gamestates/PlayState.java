package dev.fh.airbattle.gamestates;

import dev.fh.airbattle.Airbattle;
import dev.fh.airbattle.events.GameWinEvent;
import dev.fh.airbattle.capturepoints.CaptureManager;
import dev.fh.airbattle.events.TeamScoreEvent;
import dev.fh.airbattle.game.Game;
import dev.fh.airbattle.guns.Gun;
import dev.fh.airbattle.guns.MachineGun;
import dev.fh.airbattle.guns.RocketLauncher;
import dev.fh.airbattle.players.AirbattlePlayer;
import dev.fh.airbattle.players.PlayerMode;
import dev.fh.airbattle.util.AirbattleConfig;
import dev.fh.airbattle.util.NMSHelper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftArrow;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Arrays;

public class PlayState extends Gamestate {
    private Game game;
    private CaptureManager captureManager;
    private Scoreboard displayedScoreboard;

    public void onStart(Game game) {
        this.game = game;
        this.gamestateType = Gamestates.PLAYING;

        for (Player p : Bukkit.getOnlinePlayers()) {
            AirbattlePlayer abPlayer = game.playerManager.getABPlayer(p.getUniqueId());
            abPlayer.teleportToSpawn();

            abPlayer.playSound(Sound.ENTITY_PLAYER_LEVELUP, 1, 1);

            abPlayer.setInventoryAsKit();


            abPlayer.setNameAsColor();

            abPlayer.setPlayerMode(PlayerMode.ALIVE);
            p.sendTitle(ChatColor.GREEN + "The game has begun!", "", 10, 70, 20);
        }

        captureManager = new CaptureManager(game);
        NMSHelper.setMOTD(ChatColor.GREEN + "Airbattle - " + ChatColor.YELLOW + "In game");
    }

    public void onStop() {
        HandlerList.unregisterAll(this);
        captureManager.cancelTick();

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.getInventory().clear();
            p.setCustomName(p.getName());
            p.setCustomNameVisible(false);
            p.setDisplayName(p.getName());
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        if (e.getItem() == null) {
            return;
        }

        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            ItemStack heldItem = e.getItem();
            AirbattlePlayer abPlayer = game.playerManager.getABPlayer(p.getUniqueId());

            onShoot(p, heldItem);

            if (Arrays.asList(Material.STONE_HOE, Material.STONE_SPADE).contains(e.getMaterial())) {
                e.setCancelled(true);
            }
        }
    }

    public void onShoot(Player shooter, ItemStack heldItem) {
        AirbattlePlayer abPlayer = game.playerManager.getABPlayer(shooter.getUniqueId());
        switch (heldItem.getType()) {
            case STONE_HOE:
                MachineGun gun = abPlayer.getSelectedKit().getGunByClass(MachineGun.class);

                if (gun != null) {
                    gun.onShoot(abPlayer);
                }

                damageShotPlayer(shooter, 3);

                break;
            case STONE_SPADE:
                RocketLauncher launcher = abPlayer.getSelectedKit().getGunByClass(RocketLauncher.class);

                if (launcher != null) {
                    launcher.onShoot(abPlayer);
                }

                damageShotPlayer(shooter, 7);

                break;
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        if (Bukkit.getOnlinePlayers().size() == 0) {
            System.out.println("Starting new game due to lack of players!");
            Airbattle.plugin.currentGame = new Game();
            Airbattle.plugin.currentGame.startGame();
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Projectile) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onSpawn(EntitySpawnEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onFoodLoss(FoodLevelChangeEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (e.getPlayer().isGliding() && e.getPlayer().getLocation().getPitch() < -25) {
            e.getPlayer().setVelocity(e.getPlayer().getVelocity().setY(e.getPlayer().getVelocity().getY() + 0.1));
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerLoginEvent e) {
        e.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Server is currently in a game!");
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        e.setKeepInventory(true);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        e.setRespawnLocation(game.playerManager.getABPlayer(e.getPlayer().getUniqueId()).getSelectedTeam().spawnLoc);
    }

    @EventHandler
    public void onGameEnd(GameWinEvent e) {
        Bukkit.broadcastMessage(ChatColor.WHITE + "Team " + e.getWinner().color + e.getWinner().name + ChatColor.GOLD + " has won the game!");
        game.endGame();
    }

    @EventHandler
    public void onTeamScore(TeamScoreEvent e) {
        Objective obj = displayedScoreboard.getObjective("scores");

        if (e.getScorer().name.equals("red")) {
            obj.getScore(ChatColor.RED + "Red: ").setScore(e.getScorer().points);
        } else {
            obj.getScore(ChatColor.BLUE + "Blue: ").setScore(e.getScorer().points);
        }
    }

    private void initScoreboard() {
        displayedScoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Objective obj = displayedScoreboard.registerNewObjective("scores", "dummy");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        obj.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "Airbattle");

        obj.getScore(" ").setScore(AirbattleConfig.winCondition + 1);
        obj.getScore(ChatColor.RED + "Red: ").setScore(0);
        obj.getScore(ChatColor.BLUE + "Blue: ").setScore(0);
    }

    private void damageShotPlayer(Player shooter, int damage) {
        Entity lookingAt = NMSHelper.getNearestEntityInSight(shooter, 35);

        if (lookingAt instanceof Player) {
            ((Player) lookingAt).damage(damage);
        }
    }
}
