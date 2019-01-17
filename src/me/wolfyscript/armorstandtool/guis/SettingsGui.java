package me.wolfyscript.armorstandtool.guis;

import me.wolfyscript.armorstandtool.ArmorStandTool;
import me.wolfyscript.armorstandtool.data.OptionType;
import me.wolfyscript.armorstandtool.data.PlayerCache;
import me.wolfyscript.utilities.api.WolfyUtilities;
import me.wolfyscript.utilities.api.inventory.*;
import me.wolfyscript.utilities.api.utils.protection.PSUtils;
import me.wolfyscript.utilities.api.utils.protection.WGUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

public class SettingsGui extends GuiWindow {

    public SettingsGui(InventoryAPI inventoryAPI) {
        super("settings", inventoryAPI, 54);
    }

    @Override
    public void onInit() {
        createItem("add_001", Material.CACTUS_GREEN);
        createItem("add_01", Material.CACTUS_GREEN);
        createItem("add_1", Material.CACTUS_GREEN);
        createItem("add_1", Material.CACTUS_GREEN);
        createItem("remove_001", Material.ROSE_RED);
        createItem("remove_01", Material.ROSE_RED);
        createItem("remove_1", Material.ROSE_RED);
        createItem("x_pos", Material.DANDELION_YELLOW);
        createItem("y_pos", Material.DANDELION_YELLOW);
        createItem("z_pos", Material.DANDELION_YELLOW);
        createItem("x_value", Material.DANDELION_YELLOW);
        createItem("y_value", Material.DANDELION_YELLOW);
        createItem("z_value", Material.DANDELION_YELLOW);
        createItem("yaw", Material.DANDELION_YELLOW);
        createItem("free_edit", Material.CYAN_DYE);
        createItem("back", Material.MAGENTA_DYE);
    }

    @EventHandler
    public void onInventoryOpen(GuiUpdateEvent event) {
        if (event.verify(this)) {
            ArmorStand stand = ArmorStandTool.getPlayerCache(event.getPlayer()).getArmorStand();
            event.setItem(0, "remove_1");
            event.setItem(1, "remove_01");
            event.setItem(2, "remove_001");
            event.setItem(9, "remove_1");
            event.setItem(10, "remove_01");
            event.setItem(11, "remove_001");
            event.setItem(18, "remove_1");
            event.setItem(19, "remove_01");
            event.setItem(20, "remove_001");
            OptionType option = ArmorStandTool.getPlayerCache(event.getPlayer()).getCurrentOption();

            if (option.equals(OptionType.POSITION)) {
                event.setItem(3, event.getItem("x_value", "%VALUE%", String.valueOf(stand.getLocation().getX())));
                event.setItem(12, event.getItem("y_value", "%VALUE%", String.valueOf(stand.getLocation().getY())));
                event.setItem(21, event.getItem("z_value", "%VALUE%", String.valueOf(stand.getLocation().getZ())));
            } else {
                event.setItem(3, event.getItem("x_pos", "%VALUE%", String.valueOf(getPose(option, "x", stand))));
                event.setItem(12, event.getItem("y_pos", "%VALUE%", String.valueOf(getPose(option, "y", stand))));
                event.setItem(21, event.getItem("z_pos", "%VALUE%", String.valueOf(getPose(option, "z", stand))));
            }

            event.setItem(4, "add_001");
            event.setItem(5, "add_01");
            event.setItem(6, "add_1");
            event.setItem(13, "add_001");
            event.setItem(14, "add_01");
            event.setItem(15, "add_1");
            event.setItem(22, "add_001");
            event.setItem(23, "add_01");
            event.setItem(24, "add_1");
            event.setItem(8, "free_edit");
            event.setItem(17, "free_edit");
            event.setItem(26, "free_edit");
            event.setItem(45, "back");

            if (ArmorStandTool.getPlayerCache(event.getPlayer()).getCurrentOption().equals(OptionType.POSITION)) {
                event.setItem(30, event.getItem("yaw", "%VALUE%", String.valueOf(stand.getLocation().getYaw())));
                event.setItem(27, "remove_1");
                event.setItem(28, "remove_01");
                event.setItem(29, "remove_001");
                event.setItem(31, "add_001");
                event.setItem(32, "add_01");
                event.setItem(33, "add_1");
                event.setItem(35, "free_edit");
            }else{
                for(int u = 27; u < 36; u++){
                    event.setItem(u, new ItemStack(Material.AIR));
                }
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (event.getTo().distance(event.getFrom()) > 0) {
            PlayerCache playerCache = ArmorStandTool.getPlayerCache(event.getPlayer());
            int mode = playerCache.getFreeEdit();
            if (mode != -1) {
                Player player = event.getPlayer();
                ArmorStand stand = ArmorStandTool.getPlayerCache(event.getPlayer()).getArmorStand();
                Location standStartPos = playerCache.getFreeEditStandPos().clone();
                Location playerPos = player.getLocation();
                Location startPos = playerCache.getFreeEditLoc().clone();
                double dis = startPos.toVector().length() - playerPos.toVector().length();
                double disAngle = -0.08;
                if (dis > 0) {
                    disAngle *= -1;
                }
                if (playerCache.hasLastDis()) {
                    if ((playerCache.getLastDis() < 0 && playerCache.getLastDis() < dis) || (playerCache.getLastDis() > 0 && playerCache.getLastDis() > dis)) {
                        disAngle *= -1;
                    }
                }
                playerCache.setLastDis(dis);
                switch (playerCache.getCurrentOption()) {
                    case POSITION:
                        switch (mode) {
                            case 0:
                                teleportStand(stand, standStartPos.add(dis / 2, 0, 0), player);
                                break;
                            case 1:
                                teleportStand(stand, standStartPos.add(0, dis / 2, 0), player);
                                break;
                            case 2:
                                teleportStand(stand, standStartPos.add(0, 0, dis / 2), player);
                                break;
                            case 3:
                                System.out.println(standStartPos.getYaw() + (float) dis * 20f);
                                standStartPos.setYaw(standStartPos.getYaw() + (float) dis * 20f);
                                teleportStand(stand, standStartPos, player);
                                break;
                        }
                        break;
                    case ROTATION_BODY:
                        switch (mode) {
                            case 0:
                                stand.setBodyPose(stand.getBodyPose().add(disAngle, 0, 0));
                                break;
                            case 1:
                                stand.setBodyPose(stand.getBodyPose().add(0, disAngle, 0));
                                break;
                            case 2:
                                stand.setBodyPose(stand.getBodyPose().add(0, 0, disAngle));
                                break;
                        }
                        break;
                    case ROTATION_HEAD:
                        switch (mode) {
                            case 0:
                                stand.setHeadPose(stand.getHeadPose().add(disAngle, 0, 0));
                                break;
                            case 1:
                                stand.setHeadPose(stand.getHeadPose().add(0, disAngle, 0));
                                break;
                            case 2:
                                stand.setHeadPose(stand.getHeadPose().add(0, 0, disAngle));
                                break;
                        }
                        break;
                    case ROTATION_LEFT_ARM:
                        switch (mode) {
                            case 0:
                                stand.setLeftArmPose(stand.getLeftArmPose().add(disAngle, 0, 0));
                                break;
                            case 1:
                                stand.setLeftArmPose(stand.getLeftArmPose().add(0, disAngle, 0));
                                break;
                            case 2:
                                stand.setLeftArmPose(stand.getLeftArmPose().add(0, 0, disAngle));
                                break;
                        }
                        break;
                    case ROTATION_RIGHT_ARM:
                        switch (mode) {
                            case 0:
                                stand.setRightArmPose(stand.getRightArmPose().add(disAngle, 0, 0));
                                break;
                            case 1:
                                stand.setRightArmPose(stand.getRightArmPose().add(0, disAngle, 0));
                                break;
                            case 2:
                                stand.setRightArmPose(stand.getRightArmPose().add(0, 0, disAngle));
                        }
                        break;
                    case ROTATION_LEFT_LEG:
                        switch (mode) {
                            case 0:
                                stand.setLeftLegPose(stand.getLeftLegPose().add(disAngle, 0, 0));
                                break;
                            case 1:
                                stand.setLeftLegPose(stand.getLeftLegPose().add(0, disAngle, 0));
                                break;
                            case 2:
                                stand.setLeftLegPose(stand.getLeftLegPose().add(0, 0, disAngle));
                                break;
                        }
                        break;
                    case ROTATION_RIGHT_LEG:
                        switch (mode) {
                            case 0:
                                stand.setRightLegPose(stand.getRightLegPose().add(disAngle, 0, 0));
                                break;
                            case 1:
                                stand.setRightLegPose(stand.getRightLegPose().add(0, disAngle, 0));
                                break;
                            case 2:
                                stand.setRightLegPose(stand.getRightLegPose().add(0, 0, disAngle));
                                break;
                        }
                }
            }
        }
    }

    @EventHandler
    public void onAction(GuiActionEvent event) {
        if (event.verify(this)) {
            ArmorStand stand = ArmorStandTool.getPlayerCache(event.getPlayer()).getArmorStand();
            Location currentLocation = stand.getLocation();

            String action = event.getAction();

            if (action.startsWith("remove_") || action.startsWith("add_")) {
                switch (ArmorStandTool.getPlayerCache(event.getPlayer()).getCurrentOption()) {
                    case POSITION:
                        if (event.getRawSlot() >= 0 && event.getRawSlot() < 8) {
                            teleportStand(stand, currentLocation.add(getAmount(action, event.getClickType().isShiftClick()), 0, 0), event.getPlayer());
                        }
                        if (event.getRawSlot() >= 9 && event.getRawSlot() < 17) {
                            teleportStand(stand, currentLocation.add(0, getAmount(action, event.getClickType().isShiftClick()), 0), event.getPlayer());
                        }
                        if (event.getRawSlot() >= 18 && event.getRawSlot() < 26) {
                            teleportStand(stand, currentLocation.add(0, 0, getAmount(action, event.getClickType().isShiftClick())), event.getPlayer());
                        }
                        if (event.getRawSlot() >= 27 && event.getRawSlot() < 35) {
                            currentLocation.setYaw(currentLocation.getYaw() + getAmount(action, event.getClickType().isShiftClick()));
                            teleportStand(stand, currentLocation, event.getPlayer());
                        }
                        break;
                    case ROTATION_BODY:
                        if (event.getRawSlot() >= 0 && event.getRawSlot() < 8) {
                            stand.setBodyPose(stand.getBodyPose().add(getAmount(event.getAction(), event.getClickType().isShiftClick()), 0, 0));
                        }
                        if (event.getRawSlot() >= 9 && event.getRawSlot() < 17) {
                            stand.setBodyPose(stand.getBodyPose().add(0, getAmount(event.getAction(), event.getClickType().isShiftClick()), 0));
                        }
                        if (event.getRawSlot() >= 18 && event.getRawSlot() < 26) {
                            stand.setBodyPose(stand.getBodyPose().add(0, 0, getAmount(event.getAction(), event.getClickType().isShiftClick())));
                        }
                        break;
                    case ROTATION_HEAD:
                        if (event.getRawSlot() >= 0 && event.getRawSlot() < 8) {
                            stand.setHeadPose(stand.getHeadPose().add(getAmount(event.getAction(), event.getClickType().isShiftClick()), 0, 0));
                        }
                        if (event.getRawSlot() >= 9 && event.getRawSlot() < 17) {
                            stand.setHeadPose(stand.getHeadPose().add(0, getAmount(event.getAction(), event.getClickType().isShiftClick()), 0));
                        }
                        if (event.getRawSlot() >= 18 && event.getRawSlot() < 26) {
                            stand.setHeadPose(stand.getHeadPose().add(0, 0, getAmount(event.getAction(), event.getClickType().isShiftClick())));
                        }
                        break;
                    case ROTATION_LEFT_ARM:
                        if (event.getRawSlot() >= 0 && event.getRawSlot() < 8) {
                            stand.setLeftArmPose(stand.getLeftArmPose().add(getAmount(event.getAction(), event.getClickType().isShiftClick()), 0, 0));
                        }
                        if (event.getRawSlot() >= 9 && event.getRawSlot() < 17) {
                            stand.setLeftArmPose(stand.getLeftArmPose().add(0, getAmount(event.getAction(), event.getClickType().isShiftClick()), 0));
                        }
                        if (event.getRawSlot() >= 18 && event.getRawSlot() < 26) {
                            stand.setLeftArmPose(stand.getLeftArmPose().add(0, 0, getAmount(event.getAction(), event.getClickType().isShiftClick())));
                        }
                        break;
                    case ROTATION_LEFT_LEG:
                        if (event.getRawSlot() >= 0 && event.getRawSlot() < 8) {
                            stand.setLeftLegPose(stand.getLeftLegPose().add(getAmount(event.getAction(), event.getClickType().isShiftClick()), 0, 0));
                        }
                        if (event.getRawSlot() >= 9 && event.getRawSlot() < 17) {
                            stand.setLeftLegPose(stand.getLeftLegPose().add(0, getAmount(event.getAction(), event.getClickType().isShiftClick()), 0));
                        }
                        if (event.getRawSlot() >= 18 && event.getRawSlot() < 26) {
                            stand.setLeftLegPose(stand.getLeftLegPose().add(0, 0, getAmount(event.getAction(), event.getClickType().isShiftClick())));
                        }
                        break;
                    case ROTATION_RIGHT_ARM:
                        if (event.getRawSlot() >= 0 && event.getRawSlot() < 8) {
                            stand.setRightArmPose(stand.getRightArmPose().add(getAmount(event.getAction(), event.getClickType().isShiftClick()), 0, 0));
                        }
                        if (event.getRawSlot() >= 9 && event.getRawSlot() < 17) {
                            stand.setRightArmPose(stand.getRightArmPose().add(0, getAmount(event.getAction(), event.getClickType().isShiftClick()), 0));
                        }
                        if (event.getRawSlot() >= 18 && event.getRawSlot() < 26) {
                            stand.setRightArmPose(stand.getRightArmPose().add(0, 0, getAmount(event.getAction(), event.getClickType().isShiftClick())));
                        }
                        break;
                    case ROTATION_RIGHT_LEG:
                        if (event.getRawSlot() >= 0 && event.getRawSlot() < 8) {
                            stand.setRightLegPose(stand.getRightLegPose().add(getAmount(event.getAction(), event.getClickType().isShiftClick()), 0, 0));
                        }
                        if (event.getRawSlot() >= 9 && event.getRawSlot() < 17) {
                            stand.setRightLegPose(stand.getRightLegPose().add(0, getAmount(event.getAction(), event.getClickType().isShiftClick()), 0));
                        }
                        if (event.getRawSlot() >= 18 && event.getRawSlot() < 26) {
                            stand.setRightLegPose(stand.getRightLegPose().add(0, 0, getAmount(event.getAction(), event.getClickType().isShiftClick())));
                        }
                        break;
                }
            } else if (event.getAction().equals("free_edit")) {
                PlayerCache playerCache = ArmorStandTool.getPlayerCache(event.getPlayer());
                switch (event.getRawSlot()) {
                    case 8:
                        playerCache.setFreeEdit(0);
                        break;
                    case 17:
                        playerCache.setFreeEdit(1);
                        break;
                    case 26:
                        playerCache.setFreeEdit(2);
                        break;
                    case 35:
                        playerCache.setFreeEdit(3);
                }
                if (playerCache.getFreeEdit() != -1) {
                    playerCache.setFreeEditLoc(event.getPlayer().getLocation().clone());
                    playerCache.setFreeEditStandPos(stand.getLocation().clone());
                    event.getGuiHandler().close();
                }

            } else if (event.getAction().endsWith("_pos")) {
                switch (ArmorStandTool.getPlayerCache(event.getPlayer()).getCurrentOption()) {
                    case ROTATION_RIGHT_LEG:
                        switch (action) {
                            case "x_pos":
                                stand.setRightLegPose(stand.getRightLegPose().setX(0));
                                break;
                            case "y_pos":
                                stand.setRightLegPose(stand.getRightLegPose().setY(0));
                                break;
                            case "z_pos":
                                stand.setRightLegPose(stand.getRightLegPose().setZ(0));
                        }
                        break;
                    case ROTATION_RIGHT_ARM:
                        switch (action) {
                            case "x_pos":
                                stand.setRightArmPose(stand.getRightArmPose().setX(0));
                                break;
                            case "y_pos":
                                stand.setRightArmPose(stand.getRightArmPose().setY(0));
                                break;
                            case "z_pos":
                                stand.setRightArmPose(stand.getRightArmPose().setZ(0));
                        }
                        break;
                    case ROTATION_LEFT_LEG:
                        switch (action) {
                            case "x_pos":
                                stand.setLeftLegPose(stand.getLeftLegPose().setX(0));
                                break;
                            case "y_pos":
                                stand.setLeftLegPose(stand.getLeftLegPose().setY(0));
                                break;
                            case "z_pos":
                                stand.setLeftLegPose(stand.getLeftLegPose().setZ(0));
                        }
                        break;
                    case ROTATION_LEFT_ARM:
                        switch (action) {
                            case "x_pos":
                                stand.setLeftArmPose(stand.getLeftArmPose().setX(0));
                                break;
                            case "y_pos":
                                stand.setLeftArmPose(stand.getLeftArmPose().setY(0));
                                break;
                            case "z_pos":
                                stand.setLeftArmPose(stand.getLeftArmPose().setZ(0));
                        }
                        break;
                    case ROTATION_HEAD:
                        switch (action) {
                            case "x_pos":
                                stand.setHeadPose(stand.getHeadPose().setX(0));
                                break;
                            case "y_pos":
                                stand.setHeadPose(stand.getHeadPose().setY(0));
                                break;
                            case "z_pos":
                                stand.setHeadPose(stand.getHeadPose().setZ(0));
                        }
                        break;
                    case ROTATION_BODY:
                        switch (action) {
                            case "x_pos":
                                stand.setBodyPose(stand.getBodyPose().setX(0));
                                break;
                            case "y_pos":
                                stand.setBodyPose(stand.getBodyPose().setY(0));
                                break;
                            case "z_pos":
                                stand.setBodyPose(stand.getBodyPose().setZ(0));
                        }
                }
            } else if (action.equals("yaw")) {
                stand.getLocation().setYaw(0);
            } else if (event.getAction().equals("back")) {
                event.getGuiHandler().openLastInv();
            }
            update(event.getGuiHandler());
        }
    }

    @EventHandler
    public void onClick(GuiClickEvent event) {
        if (event.verify(this)) {
            if (event.getClickType().isShiftClick()) {
                event.setCancelled(true);
            }
        }
    }

    public static float getAmount(String text, boolean shift) {
        float value = 1;
        switch (text.split("_")[1]) {
            case "001":
                value *= 0.01;
                break;
            case "01":
                value *= 0.1;
                break;
        }
        if (shift) {
            value *= 0.001;
        }
        if (text.startsWith("remove_")) {
            value *= -1;
        }
        return value;
    }

    private boolean teleportStand(ArmorStand stand, Location location, Player player) {
        if (WolfyUtilities.hasPlotSquared() && PSUtils.isPlotWorld(location.getWorld())) {
            if (PSUtils.hasPerm(player, location)) {
                stand.teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
                return true;
            }
            return false;
        } else if (WolfyUtilities.hasWorldGuard()) {
            return WGUtils.teleportEntity(stand, location, player);
        } else {
            stand.teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
            return true;
        }

    }

    public double getPose(OptionType optionType, String xyz, ArmorStand stand) {
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
