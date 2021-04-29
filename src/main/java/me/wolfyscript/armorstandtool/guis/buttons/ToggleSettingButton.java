package me.wolfyscript.armorstandtool.guis.buttons;


import me.wolfyscript.armorstandtool.data.ASTCache;
import me.wolfyscript.armorstandtool.guis.MainMenu;
import me.wolfyscript.utilities.api.inventory.gui.button.ButtonState;
import me.wolfyscript.utilities.api.inventory.gui.button.buttons.ToggleButton;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;

import java.util.function.Predicate;

public class ToggleSettingButton extends ToggleButton<ASTCache> {

    public ToggleSettingButton(int id, Predicate<ArmorStand> state) {
        super("toggle_button_"+id, (cache, guiHandler, player, guiInventory, i) -> state.test(cache.getArmorStand()), new ButtonState<>("toggle_button.enabled", Material.LIME_DYE,(cache, guiHandler, player, guiInventory, i, inventoryInteractEvent) -> {
            MainMenu.toggleStandSettings(i, player, cache);
            return true;
        }), new ButtonState<>("toggle_button.disabled", Material.RED_DYE,(cache, guiHandler, player, guiInventory, i, inventoryInteractEvent) -> {
            MainMenu.toggleStandSettings(i, player, cache);
            return true;
        }));
    }
}
