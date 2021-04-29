package me.wolfyscript.armorstandtool.guis.buttons;

import me.wolfyscript.armorstandtool.data.ASTCache;
import me.wolfyscript.utilities.api.inventory.gui.button.ButtonState;
import me.wolfyscript.utilities.api.inventory.gui.button.buttons.ActionButton;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;

public class ValueDisplayButton extends ActionButton<ASTCache> {

    public ValueDisplayButton(String xyz) {
        super("loc_"+xyz, new ButtonState<>("locations." + xyz, Material.YELLOW_DYE, (hashMap, cache, guiHandler, player, guiInventory, itemStack, i, b) -> {
            ArmorStand stand = cache.getArmorStand();
            hashMap.put("%value%", xyz.equals("x") ? stand.getLocation().getX() : xyz.equals("y") ? stand.getLocation().getY() : xyz.equals("z") ? stand.getLocation().getZ() : 0);
            return itemStack;
        }));
    }
}
