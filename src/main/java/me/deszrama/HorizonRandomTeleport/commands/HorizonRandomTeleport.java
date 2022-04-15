package me.deszrama.HorizonRandomTeleport.commands;

import me.deszrama.HorizonRandomTeleport.functionality.RTP;
import me.deszrama.HorizonRandomTeleport.main.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class HorizonRandomTeleport implements Listener, CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length >= 1){
            if (args[0].equalsIgnoreCase("help")){
                if (sender instanceof Player){
                    Player player = (Player) sender;
                    if (player.hasPermission("HorizonRTP.admin")){
                        player.sendMessage("§8------>> §3§oHorizonRTP §8<<------");
                        player.sendMessage("§8* §7/HorizonRTP reload - reloads the configuration file");
                        player.sendMessage("§8* §7/HorizonRTP autoRespawn - Enables automatic repsawn when the player dies");
                        player.sendMessage("§8* §7/HorizonRTP setWorld (World name) - Sets the world on which the player has to respawn");
                        player.sendMessage("§8* §7/HorizonRTP setCoords (MinX MinZ MaxX MaxZ) - Sets the coordinates on which the player can respawn");
                        player.sendMessage("§8------>> §3§oHorizonRTP §8<<------");
                    } else {
                        player.sendMessage(Main.plugin.getConfig().getString("Messages.PermissionsNeeded").replace('&', '§'));
                    }
                }
            }

            if (args[0].equalsIgnoreCase("reload")){
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    if (player.hasPermission("HorizonRTP.admin")) {
                        Main.plugin.reloadConfig();
                        player.sendMessage("§3§oHorizonRTP §8>> §aConfiguration file successfully reloaded");
                    } else {
                        player.sendMessage(Main.plugin.getConfig().getString("Messages.PermissionsNeeded").replace('&', '§'));
                    }
                }
            }

            if (args[0].equalsIgnoreCase("autoRespawn")){
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    if (player.hasPermission("HorizonRTP.admin")) {
                        if (Main.plugin.getConfig().getBoolean("RandomTeleport.autoRespawn")){
                            Main.plugin.getConfig().set("RandomTeleport.autoRespawn", false);
                            Main.plugin.saveConfig();
                            player.sendMessage("§3§oHorizonRTP §8>> §cThe feature was successfully turned off");
                        } else {
                            Main.plugin.getConfig().set("RandomTeleport.autoRespawn", true);
                            Main.plugin.saveConfig();
                            player.sendMessage("§3§oHorizonRTP §8>> §aThe feature was successfully turned on");
                        }

                    } else {
                        player.sendMessage(Main.plugin.getConfig().getString("Messages.PermissionsNeeded").replace('&', '§'));
                    }
                }
            }

            if (args[0].equalsIgnoreCase("setWorld")){
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    if (player.hasPermission("HorizonRTP.admin")) {
                        if (args[1] != null){
                            Main.plugin.getConfig().set("RandomTeleport.respawnWorld", args[1]);
                            Main.plugin.saveConfig();
                            player.sendMessage("§3§oHorizonRTP §8>> §aThe world was set up successfully!");
                        } else {
                            player.sendMessage("§3§oHorizonRTP §8>> §cYou must enter a world name!");
                        }
                    } else {
                        player.sendMessage(Main.plugin.getConfig().getString("Messages.PermissionsNeeded").replace('&', '§'));
                    }
                }
            }

            if (args[0].equalsIgnoreCase("setCoords")){
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    if (player.hasPermission("HorizonRTP.admin")) {
                        if (args[1] != null && args[2] != null && args[3] != null && args[4] != null){
                            if (isInt(args[1]) && isInt(args[2]) && isInt(args[3]) && isInt(args[4])){
                                Main.plugin.getConfig().set("RandomTeleport.respawnMinX", Integer.parseInt(args[1]));
                                Main.plugin.getConfig().set("RandomTeleport.respawnMinZ", Integer.parseInt(args[2]));
                                Main.plugin.getConfig().set("RandomTeleport.respawnMaxX", Integer.parseInt(args[3]));
                                Main.plugin.getConfig().set("RandomTeleport.respawnMaxZ", Integer.parseInt(args[4]));
                                Main.plugin.saveConfig();
                                player.sendMessage("§3§oHorizonRTP §8>> §aNew coordinates have been successfully set!");
                            } else {
                                player.sendMessage("§3§oHorizonRTP §8>> §cCoordinates must be an integer!");
                            }
                        } else {
                            player.sendMessage("§3§oHorizonRTP §8>> §cYou must enter all coordinates!");
                        }
                    } else {
                        player.sendMessage(Main.plugin.getConfig().getString("Messages.PermissionsNeeded").replace('&', '§'));
                    }
                }
            }
        } else {
            if (sender instanceof Player){
                Player player = (Player) sender;
                if (player.hasPermission("HorizonRTP.rtp")){
                    RTP.respawn(player);
                } else {
                    player.sendMessage(Main.plugin.getConfig().getString("Messages.PermissionsNeeded").replace('&', '§'));
                }
            } else {
                System.out.println("§cSorry! Console cannot execute this command!");
            }
        }

        return false;
    }

    public static boolean isInt(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }


}
