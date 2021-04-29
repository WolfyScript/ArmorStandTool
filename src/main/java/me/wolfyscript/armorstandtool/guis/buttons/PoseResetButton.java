package me.wolfyscript.armorstandtool.guis.buttons;

import me.wolfyscript.armorstandtool.data.OptionType;
import me.wolfyscript.armorstandtool.data.ASTCache;
import me.wolfyscript.armorstandtool.util.ArmorStandUtils;
import me.wolfyscript.utilities.api.inventory.gui.button.ButtonState;
import me.wolfyscript.utilities.api.inventory.gui.button.buttons.ActionButton;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;

public class PoseResetButton extends ActionButton<CustomCache> {

    public PoseResetButton(String xyz) {
        super("pose_" + xyz, new ButtonState<>("pose." + xyz, Material.YELLOW_DYE, (customCache, guiHandler, player, guiInventory, i, inventoryInteractEvent) -> {
            ArmorStand stand = ArmorStandTool.getPlayerCache(player).getArmorStand();
            switch (ArmorStandTool.getPlayerCache(player).getCurrentOption()) {
                case ROTATION_RIGHT_LEG:
                    stand.setRightLegPose(ArmorStandUtils.resetRotation(stand.getRightLegPose(), xyz));
                    break;
                case ROTATION_RIGHT_ARM:
                    stand.setRightArmPose(ArmorStandUtils.resetRotation(stand.getRightArmPose(), xyz));
                    break;
                case ROTATION_LEFT_LEG:
                    stand.setLeftLegPose(ArmorStandUtils.resetRotation(stand.getLeftLegPose(), xyz));
                    break;
                case ROTATION_LEFT_ARM:
                    stand.setLeftArmPose(ArmorStandUtils.resetRotation(stand.getLeftArmPose(), xyz));
                    break;
                case ROTATION_HEAD:
                    stand.setHeadPose(ArmorStandUtils.resetRotation(stand.getHeadPose(), xyz));
                    break;
                case ROTATION_BODY:
                    stand.setBodyPose(ArmorStandUtils.resetRotation(stand.getBodyPose(), xyz));
                    break;
                default:
                    //No setting!
            }
            return true;
        }, (hashMap, customCache, guiHandler, player, guiInventory, itemStack, i, b) -> {
            hashMap.put("%value%", getPose(ArmorStandTool.getPlayerCache(player).getCurrentOption(), xyz, ArmorStandTool.getPlayerCache(player).getArmorStand()));
            return itemStack;
        }));
    }

    private static double getPose(OptionType optionType, String xyz, ArmorStand stand) {
        switch (optionType) {
            case ROTATION_RIGHT_LEG:
                return ArmorStandUtils.getPose(stand.getRightLegPose(), xyz);
            case ROTATION_RIGHT_ARM:
                return ArmorStandUtils.getPose(stand.getRightArmPose(), xyz);
            case ROTATION_LEFT_LEG:
                return ArmorStandUtils.getPose(stand.getLeftLegPose(), xyz);
            case ROTATION_LEFT_ARM:
                return ArmorStandUtils.getPose(stand.getLeftArmPose(), xyz);
            case ROTATION_HEAD:
                return ArmorStandUtils.getPose(stand.getHeadPose(), xyz);
            case ROTATION_BODY:
                return ArmorStandUtils.getPose(stand.getBodyPose(), xyz);
            default:
                return 0d;
        }
    }
}
