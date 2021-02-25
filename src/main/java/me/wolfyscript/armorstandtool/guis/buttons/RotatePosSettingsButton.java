package me.wolfyscript.armorstandtool.guis.buttons;

import me.wolfyscript.armorstandtool.ArmorStandTool;
import me.wolfyscript.armorstandtool.data.OptionType;
import me.wolfyscript.utilities.api.inventory.gui.button.ButtonState;
import me.wolfyscript.utilities.api.inventory.gui.button.buttons.ActionButton;
import me.wolfyscript.utilities.api.inventory.gui.cache.CustomCache;
import org.bukkit.Material;

import java.util.Locale;

public class RotatePosSettingsButton extends ActionButton<CustomCache> {

    public RotatePosSettingsButton(String id, Material material) {
        super(id, new ButtonState<>(id, material, (customCache, guiHandler, player, guiInventory, i, inventoryInteractEvent) -> {
            ArmorStandTool.getPlayerCache(player).setCurrentOption(OptionType.valueOf(id.toUpperCase(Locale.ROOT)));
            guiHandler.openWindow("settings");
            return true;
        }));
    }
}
