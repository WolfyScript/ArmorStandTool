package me.wolfyscript.armorstandtool;

import com.intellectualcrafters.plot.api.PlotAPI;
import com.intellectualcrafters.plot.object.Plot;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PlotUtils {

    private static PlotAPI plotAPI = new PlotAPI();

    public static boolean hasPerm(Player player, Location location){
        Plot plot = plotAPI.getPlot(location);
        return plot != null && !plot.isDenied(player.getUniqueId());
    }
}
