package me.aspergian.firstplugin;

import cc.acquized.itembuilder.api.ItemBuilder;

import java.util.*;

import me.aspergian.firstplugin.commands.GiveBandage;
import me.aspergian.firstplugin.commands.MyHp;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class FirstPlugin extends JavaPlugin implements Listener{



    public static final ItemStack ITEM_BANDAGE = new ItemBuilder(Material.PAPER)
            .displayname(ChatColor.RED + "Bandage")
            .lore(Arrays.asList(ChatColor.BLUE + "Right click to heal!"))
            .build();



    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info(ChatColor.AQUA + "[FirstPlugin] has been enabled!");
        ShapedRecipe hRecipe = new ShapedRecipe(new NamespacedKey(this, "Bandage"), ITEM_BANDAGE);
        hRecipe.shape(
                "###",
                "!@!",
                "###");
        hRecipe.setIngredient('@', Material.WHITE_WOOL);
        hRecipe.setIngredient('!', Material.STRING);
        hRecipe.setIngredient('#', Material.AIR);
        getServer().addRecipe(hRecipe);

        getCommand("givebandage").setExecutor(new GiveBandage(this));
        getCommand("myhp").setExecutor(new MyHp(this));








        getServer().getPluginManager().registerEvents(this, this);
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    Set<UUID> cooldown = new HashSet<>();
    @EventHandler
    public void bandage(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        if(e.getItem() == null || e.getItem().getType() == Material.AIR) return;
        if(!e.getItem().hasItemMeta()) return;
        //Optionally check if the player is right or left clicking here

        if (cooldown.contains(uuid)){
            p.sendMessage(ChatColor.RED + "You cannot use the bandage yet");
            return;
        }

        if (p.getGameMode() == GameMode.SURVIVAL && p.getHealth() <20) {
        if(e.getItem().getItemMeta().equals(ITEM_BANDAGE.getItemMeta())) {
            ItemStack newAmt = e.getItem();
            if(newAmt.getAmount() <= 1){
                newAmt = new ItemStack(Material.AIR);
            }
            else if (newAmt.getAmount() >= 1){
                newAmt.setAmount(newAmt.getAmount() - 1);
            }

            if(e.getHand() == EquipmentSlot.HAND) {
                p.getInventory().setItemInMainHand(newAmt);
            }
            else if (e.getHand() == EquipmentSlot.OFF_HAND) {
                p.getInventory().setItemInOffHand(newAmt); //Not sure if this is to be trusted
            }
            int healfactor = 4;
            if (p.getHealth() + healfactor >=20) {
                p.setHealth(20);

            }
            else if (p.getHealth() + healfactor < 20){
                p.setHealth(p.getHealth()+ healfactor);


            }


        }
            p.sendMessage(ChatColor.RED + "-1 Bandage");
            p.spawnParticle(Particle.HEART, p.getEyeLocation() ,1000);
            p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP , 1f, 1f);
            cooldown.add(uuid);
            new RemoveCooldown(uuid).runTaskLater(this, 100);
        }



    }
    public class RemoveCooldown extends BukkitRunnable {
        private UUID uuid;

        public RemoveCooldown(UUID uuid) {
            this.uuid = uuid;
        }

        @Override
        public void run() {

            cooldown.remove(uuid);
        }

        }



   /* @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("getbandage")) {
            if(!(sender instanceof Player)) return false; //Prevents class cast exception
            Player player = (Player) sender;
            player.getInventory().addItem(ITEM_BANDAGE.clone());
        }
        else if(cmd.getName().equalsIgnoreCase("bandagereload")) {
            this.saveDefaultConfig();
            this.reloadConfig();
            sender.sendMessage(ChatColor.GREEN + "Configuration reloaded!");
        }
        else if(cmd.getName().equalsIgnoreCase("myhp")) {
            Player p = (Player) sender;
            sender.sendMessage(ChatColor.GREEN + "Your HP is " + p.getHealth());
        }


        return false;
    }*/
}


