package me.deszrama.HorizonRandomTeleport.functionality;

import me.deszrama.HorizonRandomTeleport.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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

    public static void respawn(Player player){
        if (player != null){
            World world = Bukkit.getWorld(Main.plugin.getConfig().getString("RandomTeleport.respawnWorld"));
            if (world != null){
                int x = getRandomInt(Main.plugin.getConfig().getInt("RandomTeleport.respawnMaxX"), Main.plugin.getConfig().getInt("RandomTeleport.respawnMinX"));
                int z = getRandomInt(Main.plugin.getConfig().getInt("RandomTeleport.respawnMaxZ"), Main.plugin.getConfig().getInt("RandomTeleport.respawnMinZ"));

                safeTeleport(player, world, x, z);

            } else {

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
