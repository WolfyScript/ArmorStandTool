package me.wolfyscript.armorstandtool.guis.buttons;

import me.wolfyscript.armorstandtool.ArmorStandTool;
import me.wolfyscript.armorstandtool.guis.SettingsGui;
import me.wolfyscript.utilities.api.inventory.gui.button.ButtonState;
import me.wolfyscript.utilities.api.inventory.gui.button.buttons.ActionButton;
import me.wolfyscript.utilities.api.inventory.gui.cache.CustomCache;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class ValueEditButton extends ActionButton<CustomCache> {

    public ValueEditButton(float value, Material material) {
        super("value_" + value, new ButtonState<>("value", material,(customCache, guiHandler, player, guiInventory, slot, event) -> {
            if(event instanceof InventoryClickEvent){
                ArmorStand stand = ArmorStandTool.getPlayerCache(player).getArmorStand();
                float currentValue = value;
                if (((InventoryClickEvent) event).isShiftClick()) {
                    currentValue *= 0.001f;
                }
                if (slot >= 0 && slot < 8) {
                    changeValue(player, currentValue, 0, 0);
                }
                if (slot >= 9 && slot < 17) {
                    changeValue(player, 0, currentValue, 0);
                }
                if (slot >= 18 && slot < 26) {
                    changeValue(player, 0, 0, currentValue);
                }
                if (slot >= 27 && slot < 35) {
                    Location loc = stand.getLocation();
                    loc.setYaw(loc.getYaw() + currentValue);
                    SettingsGui.teleportStand(stand, loc, player);
                }
            }
            return true;
        }, (hashMap, customCache, guiHandler, player, guiInventory, itemStack, i, b) -> {
            hashMap.put("%var%", value);
            DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
            df.setMaximumFractionDigits(5);
            hashMap.put("%var_shift%", df.format(value * 0.001f));
            return itemStack;
        }));
    }

    private static void changeValue(Player player, float x, float y, float z) {
        ArmorStand stand = ArmorStandTool.getPlayerCache(player).getArmorStand();
        Location currentLocation = stand.getLocation();
        switch (ArmorStandTool.getPlayerCache(player).getCurrentOption()) {
            case POSITION:
                SettingsGui.teleportStand(stand, currentLocation.add(x, y, z), player);
                break;
            case ROTATION_BODY:
                stand.setBodyPose(stand.getBodyPose().add(x, y, z));
                break;
            case ROTATION_HEAD:
                stand.setHeadPose(stand.getHeadPose().add(x, y, z));
                break;
            case ROTATION_LEFT_ARM:
                stand.setLeftArmPose(stand.getLeftArmPose().add(x, y, z));
                break;
            case ROTATION_LEFT_LEG:
                stand.setLeftLegPose(stand.getLeftLegPose().add(x, y, z));
                break;
            case ROTATION_RIGHT_ARM:
                stand.setRightArmPose(stand.getRightArmPose().add(x, y, z));
                break;
            case ROTATION_RIGHT_LEG:
                stand.setRightLegPose(stand.getRightLegPose().add(x, y, z));
                break;
        }
    }
}
