package me.wolfyscript.armorstandtool.commands;

import me.wolfyscript.armorstandtool.ArmorStandTool;
import me.wolfyscript.armorstandtool.data.FreeEditMode;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class LocateArmorStandsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(sender instanceof Player){
            Player player = (Player) sender;
            /*
            ArmorStandTool.getPlayerCache(player).setFreeEdit(FreeEditMode.RELOCATE_CURSOR);

            Collection<Entity> entities = player.getLocation().getWorld().getNearbyEntities(player.getLocation(), 5, 5, 5, entity -> entity instanceof ArmorStand);

            entities.forEach(entity -> {
                if(ArmorStandTool.getPlayerCache(player).getArmorStand() == null){
                    ArmorStandTool.getPlayerCache(player).setArmorStand((ArmorStand) entity);
                    player.spawnParticle(Particle.BARRIER, entity.getLocation().add(0, 0.5, 0), 1);
                }
            });

             */
        }

        return false;
    }
}
