package me.wolfyscript.armorstandtool;

import com.intellectualcrafters.plot.api.PlotAPI;
import com.intellectualcrafters.plot.object.Plot;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlotUtils {

    private static PlotAPI plotAPI = new PlotAPI();

    public static boolean hasPerm(Player player, Location location){
        Plot plot = plotAPI.getPlot(location);

        UUID uuid =player.getUniqueId();
        if(!isPlotWorld(player.getWorld()))
            return true;
        return plot != null && (plot.getTrusted().contains(uuid) || plot.isOwner(uuid) || plot.getMembers().contains(uuid) || plot.isAdded(uuid));
    }

    public static boolean isPlotWorld(World world){
        return plotAPI.isPlotWorld(world);
    }
}
