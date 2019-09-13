package me.wolfyscript.armorstandtool.guis.buttons;

import me.wolfyscript.armorstandtool.ArmorStandTool;
import me.wolfyscript.armorstandtool.data.OptionType;
import me.wolfyscript.utilities.api.inventory.GuiHandler;
import me.wolfyscript.utilities.api.inventory.button.ButtonActionRender;
import me.wolfyscript.utilities.api.inventory.button.ButtonState;
import me.wolfyscript.utilities.api.inventory.button.buttons.ActionButton;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class PoseResetButton extends ActionButton {

    public PoseResetButton(String xyz) {
        super("pose_"+xyz, new ButtonState("pose."+xyz, Material.DANDELION_YELLOW, new ButtonActionRender() {
            @Override
            public boolean run(GuiHandler guiHandler, Player player, Inventory inventory, int i, InventoryClickEvent inventoryClickEvent) {
                ArmorStand stand = ArmorStandTool.getPlayerCache(player).getArmorStand();
                switch (ArmorStandTool.getPlayerCache(player).getCurrentOption()) {
                    case ROTATION_RIGHT_LEG:
                        switch (xyz) {
                            case "x":
                                stand.setRightLegPose(stand.getRightLegPose().setX(0));
                                break;
                            case "y":
                                stand.setRightLegPose(stand.getRightLegPose().setY(0));
                                break;
                            case "z":
                                stand.setRightLegPose(stand.getRightLegPose().setZ(0));
                        }
                        break;
                    case ROTATION_RIGHT_ARM:
                        switch (xyz) {
                            case "x":
                                stand.setRightArmPose(stand.getRightArmPose().setX(0));
                                break;
                            case "y":
                                stand.setRightArmPose(stand.getRightArmPose().setY(0));
                                break;
                            case "z":
                                stand.setRightArmPose(stand.getRightArmPose().setZ(0));
                        }
                        break;
                    case ROTATION_LEFT_LEG:
                        switch (xyz) {
                            case "x":
                                stand.setLeftLegPose(stand.getLeftLegPose().setX(0));
                                break;
                            case "y":
                                stand.setLeftLegPose(stand.getLeftLegPose().setY(0));
                                break;
                            case "z":
                                stand.setLeftLegPose(stand.getLeftLegPose().setZ(0));
                        }
                        break;
                    case ROTATION_LEFT_ARM:
                        switch (xyz) {
                            case "x":
                                stand.setLeftArmPose(stand.getLeftArmPose().setX(0));
                                break;
                            case "y":
                                stand.setLeftArmPose(stand.getLeftArmPose().setY(0));
                                break;
                            case "z":
                                stand.setLeftArmPose(stand.getLeftArmPose().setZ(0));
                        }
                        break;
                    case ROTATION_HEAD:
                        switch (xyz) {
                            case "x":
                                stand.setHeadPose(stand.getHeadPose().setX(0));
                                break;
                            case "y":
                                stand.setHeadPose(stand.getHeadPose().setY(0));
                                break;
                            case "z":
                                stand.setHeadPose(stand.getHeadPose().setZ(0));
                        }
                        break;
                    case ROTATION_BODY:
                        switch (xyz) {
                            case "x":
                                stand.setBodyPose(stand.getBodyPose().setX(0));
                                break;
                            case "y":
                                stand.setBodyPose(stand.getBodyPose().setY(0));
                                break;
                            case "z":
                                stand.setBodyPose(stand.getBodyPose().setZ(0));
                        }
                }
                return true;
            }

            @Override
            public ItemStack render(HashMap<String, Object> hashMap, GuiHandler guiHandler, Player player, ItemStack itemStack, int i, boolean b) {
                hashMap.put("%value%", getPose(ArmorStandTool.getPlayerCache(player).getCurrentOption(), xyz, ArmorStandTool.getPlayerCache(player).getArmorStand()));
                return itemStack;
            }
        }));
    }

    private static double getPose(OptionType optionType, String xyz, ArmorStand stand) {
        switch (optionType) {
            case ROTATION_RIGHT_LEG:
                switch (xyz) {
                    case "x":
                        return stand.getRightLegPose().getX();
                    case "y":
                        return stand.getRightLegPose().getY();
                    case "z":
                        return stand.getRightLegPose().getZ();
                }
                break;
            case ROTATION_RIGHT_ARM:
                switch (xyz) {
                    case "x":
                        return stand.getRightArmPose().getX();
                    case "y":
                        return stand.getRightArmPose().getY();
                    case "z":
                        return stand.getRightArmPose().getZ();
                }
                break;
            case ROTATION_LEFT_LEG:
                switch (xyz) {
                    case "x":
                        return stand.getLeftLegPose().getX();
                    case "y":
                        return stand.getLeftLegPose().getY();
                    case "z":
                        return stand.getLeftLegPose().getZ();
                }
                break;
            case ROTATION_LEFT_ARM:
                switch (xyz) {
                    case "x":
                        return stand.getLeftArmPose().getX();
                    case "y":
                        return stand.getLeftArmPose().getY();
                    case "z":
                        return stand.getLeftArmPose().getZ();
                }
                break;
            case ROTATION_HEAD:
                switch (xyz) {
                    case "x":
                        return stand.getHeadPose().getX();
                    case "y":
                        return stand.getHeadPose().getY();
                    case "z":
                        return stand.getHeadPose().getZ();
                }
                break;
            case ROTATION_BODY:
                switch (xyz) {
                    case "x":
                        return stand.getBodyPose().getX();
                    case "y":
                        return stand.getBodyPose().getY();
                    case "z":
                        return stand.getBodyPose().getZ();
                }
        }
        return 0.0;
    }
}
