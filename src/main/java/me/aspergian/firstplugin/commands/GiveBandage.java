package me.aspergian.firstplugin.commands;

import me.aspergian.firstplugin.FirstPlugin;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GiveBandage implements CommandExecutor {

    private FirstPlugin plugin;

    public GiveBandage(FirstPlugin plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        if (commandSender instanceof Player){
            Player player = (Player) commandSender;
            if (args.length == 0){
                bandageMethod(player, 1);
            }
            else if (args.length == 1) {
                int amt = Integer.parseInt(args[0]);
                bandageMethod(player, amt);
                player.sendMessage("Given " +amt+ " bandages");
            }
            else if (args.length == 2) {
                if(player.hasPermission("firstplugin.givebandage.others")) {
                    if( Bukkit.getServer().getPlayerExact(args[1]) != null ){
                        int amt = Integer.parseInt(args[0]);
                        Player target = Bukkit.getPlayer(args[1]);
                        bandageMethod(target, amt);
                        player.sendMessage("Given " +amt+ " bandages to " +target.getDisplayName());
                        target.sendMessage("You have been given " +amt+ " bandages by "+player.getDisplayName()+ "");
                    }
                    else {
                        player.sendMessage("That player is not online");

                    }


                }


            }


        }

        return true;
    }

    private void bandageMethod(Player player, int amount){
        if(player.hasPermission("firstplugin.givebandage")){
            FirstPlugin.ITEM_BANDAGE.setAmount(amount);
            player.getInventory().addItem(FirstPlugin.ITEM_BANDAGE.clone());
        }
    }
}