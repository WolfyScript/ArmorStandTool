package me.wolfyscript.armorstandtool.guis.buttons;

import me.wolfyscript.armorstandtool.data.OptionType;
import me.wolfyscript.armorstandtool.data.ASTCache;
import me.wolfyscript.utilities.api.inventory.gui.button.buttons.ActionButton;
import org.bukkit.Material;

import java.util.Locale;

public class RotatePosSettingsButton extends ActionButton<ASTCache> {

    public RotatePosSettingsButton(String id, Material material) {
        super(id, material, (cache, guiHandler, player, guiInventory, i, inventoryInteractEvent) -> {
            cache.setCurrentOption(OptionType.valueOf(id.toUpperCase(Locale.ROOT)));
            guiHandler.openWindow("settings");
            return true;
        });
    }
}
