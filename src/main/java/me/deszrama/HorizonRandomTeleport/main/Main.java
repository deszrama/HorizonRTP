package me.deszrama.HorizonRandomTeleport.main;

import me.deszrama.HorizonRandomTeleport.commands.HorizonRandomTeleport;
import me.deszrama.HorizonRandomTeleport.functionality.RTP;
import org.bstats.bukkit.Metrics;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public class Main extends JavaPlugin implements Listener {

    public static Main plugin;
    public static HashMap<UUID, Boolean> cooldown = new HashMap<UUID, Boolean>();

    @Override
    public void onEnable() {
        plugin = this;
        getCommand("HorizonRTP").setExecutor(new HorizonRandomTeleport());
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new RTP(), this);
        Metrics metrics = new Metrics(this, 14954);

        getConfig().options().copyDefaults(true);
        saveConfig();

        System.out.println("[HorizonRandomTeleport] >>> Enabled! <<<");
        System.out.println("[HorizonRandomTeleport] Current Plugin Version: 1.0");
        System.out.println("[HorizonRandomTeleport] Author: deszrama");
        System.out.println("[HorizonRandomTeleport] Github: https://github.com/deszrama");
        System.out.println("[HorizonRandomTeleport] Thanks you for downloading this plugin, it would be nice if you leave a rating or a comment");
        System.out.println("[HorizonRandomTeleport] If you find any bug please report it to my github, I will try to fix it as soon as possible!");

    }
}
