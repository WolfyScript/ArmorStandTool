package me.wolfyscript.armorstandtool;

import com.griefcraft.lwc.LWC;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class LWCUtil {

    public static boolean hasPermToInteract( Player player, Entity entity){
        LWC lwc = LWC.getInstance();
        //lwc.wrapPlayer(player).isProtectionAccessible(lwc.getProtectionCache().getProtection(entity.getLocation().getBlock()));
        boolean hasPerm = lwc.canAccessProtection(player, entity.getLocation().getBlock());
        System.out.println("LWC: "+hasPerm);
        return hasPerm;
    }



}
