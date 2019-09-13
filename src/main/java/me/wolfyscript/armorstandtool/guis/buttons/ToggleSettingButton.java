package me.wolfyscript.armorstandtool.guis.buttons;


import me.wolfyscript.armorstandtool.guis.MainMenu;
import me.wolfyscript.utilities.api.inventory.button.ButtonState;
import me.wolfyscript.utilities.api.inventory.button.buttons.ToggleButton;
import org.bukkit.Material;

public class ToggleSettingButton extends ToggleButton {

    public ToggleSettingButton(int id) {
        super("toggle_button_"+id, new ButtonState("toggle_button.enabled", Material.LIME_DYE, (guiHandler, player, inventory, i, inventoryClickEvent) -> {
            MainMenu.toggleStandSettings(i, player);
            return true;
        }), new ButtonState("toggle_button.disabled", Material.ROSE_RED, (guiHandler, player, inventory, i, inventoryClickEvent) -> {
            MainMenu.toggleStandSettings(i, player);
            return true;
        }));
    }
}
