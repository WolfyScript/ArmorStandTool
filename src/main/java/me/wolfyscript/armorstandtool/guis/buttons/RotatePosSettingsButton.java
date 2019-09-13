package me.wolfyscript.armorstandtool.guis.buttons;

import me.wolfyscript.armorstandtool.ArmorStandTool;
import me.wolfyscript.armorstandtool.data.OptionType;
import me.wolfyscript.utilities.api.inventory.GuiHandler;
import me.wolfyscript.utilities.api.inventory.button.ButtonAction;
import me.wolfyscript.utilities.api.inventory.button.ButtonState;
import me.wolfyscript.utilities.api.inventory.button.buttons.ActionButton;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.Locale;

public class RotatePosSettingsButton extends ActionButton {

    public RotatePosSettingsButton(String id, Material material) {
        super(id, new ButtonState(id, material, (guiHandler, player, inventory, i, inventoryClickEvent) -> {
            ArmorStandTool.getPlayerCache(player).setCurrentOption(OptionType.valueOf(id.toUpperCase(Locale.ROOT)));
            guiHandler.changeToInv("settings");
            return true;
        }));
    }
}
