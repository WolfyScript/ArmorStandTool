package me.wolfyscript.armorstandtool.guis.buttons;

import me.wolfyscript.armorstandtool.ArmorStandTool;
import me.wolfyscript.armorstandtool.guis.SettingsGui;
import me.wolfyscript.utilities.api.inventory.button.ButtonState;
import me.wolfyscript.utilities.api.inventory.button.buttons.ActionButton;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class ValueEditButton extends ActionButton {

    public ValueEditButton(float value, Material material) {
        super("value_" + value, new ButtonState("value", material, (guiHandler, player, inventory, slot, event) -> {
            ArmorStand stand = ArmorStandTool.getPlayerCache(player).getArmorStand();
            float currentValue = value;
            if (event.isShiftClick()) {
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
            return true;
        }, (hashMap, guiHandler, player, itemStack, i, b) -> {
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
