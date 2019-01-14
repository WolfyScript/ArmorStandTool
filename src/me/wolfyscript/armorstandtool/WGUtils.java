package me.wolfyscript.armorstandtool;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

public class WGUtils {

    public static boolean teleportStand(ArmorStand stand, Location location, Player player) {
        if (hasPermBuild(location, player)) {
            stand.teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
            return true;
        }
        return false;
    }

    public static boolean hasPermBuild(Location location, Player player) {
        if (hasPermBuild(location, player, Flags.BUILD)) {
            return true;
        }
        return false;
    }

    public static boolean hasPermBuild(Location location, Player player, StateFlag... flag) {
        com.sk89q.worldedit.util.Location loc = BukkitAdapter.adapt(location);
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(loc);
        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
        return set.testState(localPlayer, flag) || player.hasPermission("worldguard.region.bypass.*") || player.hasPermission("worldguard.region.bypass."+location.getWorld().getName());
    }

}
