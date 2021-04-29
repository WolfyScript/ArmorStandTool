package me.wolfyscript.armorstandtool.util;

import me.wolfyscript.utilities.api.WolfyUtilities;
import me.wolfyscript.utilities.util.protection.PSUtils;
import me.wolfyscript.utilities.util.protection.WGUtils;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.util.EulerAngle;

public class ArmorStandUtils {

    private ArmorStandUtils(){ }

    public static boolean teleportStand(ArmorStand stand, Location location, Player player) {
        if (WolfyUtilities.hasPlotSquared() && PSUtils.isPlotWorld(location.getWorld())) {
            if (PSUtils.hasPerm(player, location)) {
                stand.teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
                return true;
            }
            return false;
        } else if (WolfyUtilities.hasWorldGuard()) {
            return WGUtils.teleportEntity(stand, location, player);
        } else {
            stand.teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
            return true;
        }

    }

    public static EulerAngle resetRotation(EulerAngle pose, String xyz) {
        return new EulerAngle(xyz.equals("x") ? 0 : pose.getX(), xyz.equals("y") ? 0 : pose.getY(), xyz.equals("z") ? 0 : pose.getZ());
    }

    public static double getPose(EulerAngle pose, String xyz) {
        switch (xyz) {
            case "x":
                return pose.getX();
            case "y":
                return pose.getY();
            case "z":
                return pose.getZ();
            default:
                return 0d;
        }
    }


}
