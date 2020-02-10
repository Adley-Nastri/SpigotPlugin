package me.aspergian.firstplugin.commands;

import me.aspergian.firstplugin.FirstPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MyHp implements CommandExecutor {

    private FirstPlugin plugin;

    public MyHp(FirstPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (commandSender instanceof Player){
            Player player = (Player) commandSender;
            myHpMethod(player);
        }


        return true;
    }



    private void myHpMethod(Player player){
        if(player.hasPermission("firstplugin.myhp")){
            player.sendMessage(ChatColor.GREEN + "Your HP is " + player.getHealth());
        }
    }
}

