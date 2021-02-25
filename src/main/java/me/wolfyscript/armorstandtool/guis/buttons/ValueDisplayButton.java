package me.wolfyscript.armorstandtool.guis.buttons;

import me.wolfyscript.armorstandtool.ArmorStandTool;
import me.wolfyscript.utilities.api.inventory.gui.button.ButtonState;
import me.wolfyscript.utilities.api.inventory.gui.button.buttons.ActionButton;
import me.wolfyscript.utilities.api.inventory.gui.cache.CustomCache;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;

public class ValueDisplayButton extends ActionButton<CustomCache> {

    public ValueDisplayButton(String xyz) {
        super("loc_"+xyz, new ButtonState<>("locations." + xyz, Material.YELLOW_DYE, (hashMap, customCache, guiHandler, player, guiInventory, itemStack, i, b) -> {
            ArmorStand stand = ArmorStandTool.getPlayerCache(player).getArmorStand();
            hashMap.put("%value%", xyz.equals("x") ? stand.getLocation().getX() : xyz.equals("y") ? stand.getLocation().getY() : xyz.equals("z") ? stand.getLocation().getZ() : 0);
            return itemStack;
        }));
    }
}
