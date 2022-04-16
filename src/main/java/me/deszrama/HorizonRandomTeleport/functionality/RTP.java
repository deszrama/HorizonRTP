package me.deszrama.HorizonRandomTeleport.functionality;

import me.deszrama.HorizonRandomTeleport.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class RTP implements Listener {

    boolean loop = false;

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        Main.cooldown.put(player.getUniqueId(), false);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e){
        if (Main.plugin.getConfig().getBoolean("RandomTeleport.autoRespawn")){
            Player player = e.getPlayer();
            respawn(player);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if(e.getClickedBlock().getType() == Material.STONE_BUTTON) {
                World world2 = Bukkit.getWorld(Main.plugin.getConfig().getString("RandomTeleport.respawnWorld"));
                Player player = e.getPlayer();
                if (world2 != null){
                    Location btnLoc = e.getClickedBlock().getLocation();
                    int i = 0;
                    for (String Buttons : Main.plugin.getConfig().getConfigurationSection("Buttons").getKeys(false)) {
                        i++;
                        String cnv = String.valueOf(i);
                        int x1 = Main.plugin.getConfig().getInt("Buttons." + cnv + ".X");
                        int y1 = Main.plugin.getConfig().getInt("Buttons." + cnv + ".Y");
                        int z1 = Main.plugin.getConfig().getInt("Buttons." + cnv + ".Z");

                        Location confLoc = new Location(world2, x1, y1, z1);

                        if (btnLoc.equals(confLoc)){
                            respawn(player);
                        }
                    }
                }
            }
        }
    }

    public static void respawn(Player player){
        if (player != null){
            World world = Bukkit.getWorld(Main.plugin.getConfig().getString("RandomTeleport.respawnWorld"));
            if (world != null){
                int x = getRandomInt(Main.plugin.getConfig().getInt("RandomTeleport.respawnMaxX"), Main.plugin.getConfig().getInt("RandomTeleport.respawnMinX"));
                int z = getRandomInt(Main.plugin.getConfig().getInt("RandomTeleport.respawnMaxZ"), Main.plugin.getConfig().getInt("RandomTeleport.respawnMinZ"));

                safeTeleport(player, world, x, z);
            }
        }
    }

    public static Integer getRandomInt(Integer max, Integer min){
        Random ran = new Random();
        return ran.nextInt((max - min) + 1) + min;
    }

    public static void safeTeleport(Player player, World world, Integer x, Integer z){
        int y = 255;
        Location checkloc = new Location(world, x, y, z);
        if (checkloc.getBlock().getType().isAir()){
            boolean loop = true;
            while(loop){
                y--;
                if (new Location(world, x, y, z).getBlock().getType() != Material.AIR){
                    loop = false;
                    if (new Location(world, x, y, z).getBlock().getType() != Material.WATER && new Location(world, x, y, z).getBlock().getType() != Material.LAVA){
                        if (!Main.cooldown.get(player.getUniqueId())){
                            Location location = new Location(world, x, y, z);
                            player.teleport(location);
                            player.sendMessage(Main.plugin.getConfig().getString("Messages.SuccessfullTP").replace('&', '§'));
                            Main.cooldown.put(player.getUniqueId(), true);

                            Bukkit.getScheduler().runTaskLater(Main.plugin, new Runnable() {
                                @Override
                                public void run() {
                                    Main.cooldown.put(player.getUniqueId(), false);
                                }
                            }, 100);
                        } else {
                            player.sendMessage(Main.plugin.getConfig().getString("Messages.cooldown").replace('&', '§'));
                        }
                    } else {
                        player.sendMessage(Main.plugin.getConfig().getString("Messages.DenialTP").replace('&', '§'));
                    }
                }
            }
        } else {
            if (checkloc.getBlock().getType() != Material.WATER && checkloc.getBlock().getType() != Material.LAVA){
                if (!Main.cooldown.get(player.getUniqueId())){
                    Location location = new Location(world, x, y, z);
                    player.teleport(location);
                    player.sendMessage(Main.plugin.getConfig().getString("Messages.SuccessfullTP").replace('&', '§'));
                    Main.cooldown.put(player.getUniqueId(), true);
                    Bukkit.getScheduler().runTaskLater(Main.plugin, new Runnable() {
                        @Override
                        public void run() {
                            Main.cooldown.put(player.getUniqueId(), false);
                        }
                    }, 100);
                } else {
                    player.sendMessage(Main.plugin.getConfig().getString("Messages.cooldown").replace('&', '§'));
                }
            } else {
                player.sendMessage(Main.plugin.getConfig().getString("Messages.DenialTP").replace('&', '§'));
            }
        }
    }

}
