package me.wolfyscript.armorstandtool.guis.buttons;


import me.wolfyscript.armorstandtool.guis.MainMenu;
import me.wolfyscript.utilities.api.inventory.gui.button.ButtonState;
import me.wolfyscript.utilities.api.inventory.gui.button.buttons.ToggleButton;
import me.wolfyscript.utilities.api.inventory.gui.cache.CustomCache;
import org.bukkit.Material;

public class ToggleSettingButton extends ToggleButton<CustomCache> {

    public ToggleSettingButton(int id) {
        super("toggle_button_"+id, new ButtonState<>("toggle_button.enabled", Material.LIME_DYE,(c, guiHandler, player, guiInventory, i, inventoryInteractEvent) -> {
            MainMenu.toggleStandSettings(i, player);
            return true;
        }), new ButtonState<>("toggle_button.disabled", Material.RED_DYE,(customCache, guiHandler, player, guiInventory, i, inventoryInteractEvent) -> {
            MainMenu.toggleStandSettings(i, player);
            return true;
        }));
    }
}
