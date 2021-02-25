package me.wolfyscript.armorstandtool.guis;

import me.wolfyscript.armorstandtool.ArmorStandTool;
import me.wolfyscript.armorstandtool.data.FreeEditMode;
import me.wolfyscript.armorstandtool.data.OptionType;
import me.wolfyscript.armorstandtool.data.PlayerCache;
import me.wolfyscript.armorstandtool.guis.buttons.PoseResetButton;
import me.wolfyscript.armorstandtool.guis.buttons.ValueDisplayButton;
import me.wolfyscript.armorstandtool.guis.buttons.ValueEditButton;
import me.wolfyscript.utilities.api.WolfyUtilities;
import me.wolfyscript.utilities.api.inventory.gui.GuiUpdate;
import me.wolfyscript.utilities.api.inventory.gui.GuiWindow;
import me.wolfyscript.utilities.api.inventory.gui.InventoryAPI;
import me.wolfyscript.utilities.api.inventory.gui.button.buttons.ActionButton;
import me.wolfyscript.utilities.api.inventory.gui.cache.CustomCache;
import me.wolfyscript.utilities.util.protection.PSUtils;
import me.wolfyscript.utilities.util.protection.WGUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zoglin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

import java.util.EventListener;

public class SettingsGui extends GuiWindow<CustomCache> implements EventListener {

    public SettingsGui(ASTGUICluster cluster) {
        super(cluster, "settings", 54, true);
    }

    @Override
    public void onInit() {
        registerButton(new ValueEditButton(0.01f, Material.GREEN_DYE));
        registerButton(new ValueEditButton(0.1f, Material.GREEN_DYE));
        registerButton(new ValueEditButton(1.0f, Material.GREEN_DYE));

        registerButton(new ValueEditButton(-0.01f, Material.RED_DYE));
        registerButton(new ValueEditButton(-0.1f, Material.RED_DYE));
        registerButton(new ValueEditButton(-1.0f, Material.RED_DYE));

        registerButton(new ValueDisplayButton("x"));
        registerButton(new ValueDisplayButton("y"));
        registerButton(new ValueDisplayButton("z"));

        registerButton(new PoseResetButton("x"));
        registerButton(new PoseResetButton("y"));
        registerButton(new PoseResetButton("z"));

        registerButton(new ActionButton<>("yaw", Material.YELLOW_DYE,(customCache, guiHandler, player, guiInventory, i, inventoryInteractEvent) -> {
            ArmorStand stand = ArmorStandTool.getPlayerCache(player).getArmorStand();
            Location loc = stand.getLocation();
            loc.setYaw(0);
            teleportStand(stand, loc, player);
            return true;
        }, (hashMap, customCache, guiHandler, player, guiInventory, itemStack, i, b) -> {
            hashMap.put("%value%", ArmorStandTool.getPlayerCache(player).getArmorStand().getLocation().getYaw());
            return itemStack;
        }));

        registerButton(new ActionButton<>("free_edit", Material.CYAN_DYE,(customCache, guiHandler, player, guiInventory, i, inventoryInteractEvent) -> {
            PlayerCache playerCache = ArmorStandTool.getPlayerCache(player);
            ArmorStand stand = playerCache.getArmorStand();
            switch (i) {
                case 8:
                    playerCache.setFreeEdit(FreeEditMode.POS_X);
                    break;
                case 17:
                    playerCache.setFreeEdit(FreeEditMode.POS_Y);
                    break;
                case 26:
                    playerCache.setFreeEdit(FreeEditMode.POS_Z);
                    break;
                case 35:
                    playerCache.setFreeEdit(FreeEditMode.YAW);
            }
            if (!playerCache.getFreeEdit().equals(FreeEditMode.NONE)) {
                playerCache.setFreeEditLoc(player.getLocation().clone());
                playerCache.setFreeEditStandPos(stand.getLocation().clone());
                guiHandler.close();
            }
            return true;
        }));

        registerButton(new ActionButton<>("back", Material.MAGENTA_DYE,(customCache, guiHandler, player, guiInventory, i, inventoryInteractEvent) -> {
            guiHandler.openPreviousWindow();
            return true;
        }));
    }

    @Override
    public void onUpdateSync(GuiUpdate<CustomCache> update) {
        update.setButton(0, "value_-1.0");
        update.setButton(1, "value_-0.1");
        update.setButton(2, "value_-0.01");
        update.setButton(9, "value_-1.0");
        update.setButton(10, "value_-0.1");
        update.setButton(11, "value_-0.01");
        update.setButton(18, "value_-1.0");
        update.setButton(19, "value_-0.1");
        update.setButton(20, "value_-0.01");

        if (ArmorStandTool.getPlayerCache(update.getPlayer()).getCurrentOption().equals(OptionType.POSITION)) {
            update.setButton(3, "loc_x");
            update.setButton(12, "loc_y");
            update.setButton(21, "loc_z");

            update.setButton(30, "yaw");
            update.setButton(27, "value_-1.0");
            update.setButton(28, "value_-0.1");
            update.setButton(29, "value_-0.01");

            update.setButton(31, "value_0.01");
            update.setButton(32, "value_0.1");
            update.setButton(33, "value_1.0");

            update.setButton(35, "free_edit");
        } else {
            update.setButton(3, "pose_x");
            update.setButton(12, "pose_y");
            update.setButton(21, "pose_z");

            for (int i = 27; i < 36; i++) {
                update.setItem(i, new ItemStack(Material.AIR));
            }
        }

        update.setButton(4, "value_0.01");
        update.setButton(5, "value_0.1");
        update.setButton(6, "value_1.0");
        update.setButton(13, "value_0.01");
        update.setButton(14, "value_0.1");
        update.setButton(15, "value_1.0");
        update.setButton(22, "value_0.01");
        update.setButton(23, "value_0.1");
        update.setButton(24, "value_1.0");
        update.setButton(8, "free_edit");
        update.setButton(17, "free_edit");
        update.setButton(26, "free_edit");
        update.setButton(45, "back");
    }

    @Override
    public void onUpdateAsync(GuiUpdate<CustomCache> guiUpdate) { }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (event.getTo() != null && event.getTo().distance(event.getFrom()) > 0) {
            PlayerCache playerCache = ArmorStandTool.getPlayerCache(event.getPlayer());
            FreeEditMode mode = playerCache.getFreeEdit();
            if (!mode.equals(FreeEditMode.NONE)) {
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
                            case POS_X:
                                teleportStand(stand, standStartPos.add(dis / 2, 0, 0), player);
                                break;
                            case POS_Y:
                                teleportStand(stand, standStartPos.add(0, dis / 2, 0), player);
                                break;
                            case POS_Z:
                                teleportStand(stand, standStartPos.add(0, 0, dis / 2), player);
                                break;
                            case YAW:
                                standStartPos.setYaw(standStartPos.getYaw() + (float) dis * 20f);
                                teleportStand(stand, standStartPos, player);
                                break;
                        }
                        break;
                    case ROTATION_BODY:
                        switch (mode) {
                            case POS_X:
                                stand.setBodyPose(stand.getBodyPose().add(disAngle, 0, 0));
                                break;
                            case POS_Y:
                                stand.setBodyPose(stand.getBodyPose().add(0, disAngle, 0));
                                break;
                            case YAW:
                                stand.setBodyPose(stand.getBodyPose().add(0, 0, disAngle));
                                break;
                        }
                        break;
                    case ROTATION_HEAD:
                        switch (mode) {
                            case POS_X:
                                stand.setHeadPose(stand.getHeadPose().add(disAngle, 0, 0));
                                break;
                            case POS_Y:
                                stand.setHeadPose(stand.getHeadPose().add(0, disAngle, 0));
                                break;
                            case YAW:
                                stand.setHeadPose(stand.getHeadPose().add(0, 0, disAngle));
                                break;
                        }
                        break;
                    case ROTATION_LEFT_ARM:
                        switch (mode) {
                            case POS_X:
                                stand.setLeftArmPose(stand.getLeftArmPose().add(disAngle, 0, 0));
                                break;
                            case POS_Y:
                                stand.setLeftArmPose(stand.getLeftArmPose().add(0, disAngle, 0));
                                break;
                            case YAW:
                                stand.setLeftArmPose(stand.getLeftArmPose().add(0, 0, disAngle));
                                break;
                        }
                        break;
                    case ROTATION_RIGHT_ARM:
                        switch (mode) {
                            case POS_X:
                                stand.setRightArmPose(stand.getRightArmPose().add(disAngle, 0, 0));
                                break;
                            case POS_Y:
                                stand.setRightArmPose(stand.getRightArmPose().add(0, disAngle, 0));
                                break;
                            case YAW:
                                stand.setRightArmPose(stand.getRightArmPose().add(0, 0, disAngle));
                        }
                        break;
                    case ROTATION_LEFT_LEG:
                        switch (mode) {
                            case POS_X:
                                stand.setLeftLegPose(stand.getLeftLegPose().add(disAngle, 0, 0));
                                break;
                            case POS_Y:
                                stand.setLeftLegPose(stand.getLeftLegPose().add(0, disAngle, 0));
                                break;
                            case YAW:
                                stand.setLeftLegPose(stand.getLeftLegPose().add(0, 0, disAngle));
                                break;
                        }
                        break;
                    case ROTATION_RIGHT_LEG:
                        switch (mode) {
                            case POS_X:
                                stand.setRightLegPose(stand.getRightLegPose().add(disAngle, 0, 0));
                                break;
                            case POS_Y:
                                stand.setRightLegPose(stand.getRightLegPose().add(0, disAngle, 0));
                                break;
                            case YAW:
                                stand.setRightLegPose(stand.getRightLegPose().add(0, 0, disAngle));
                                break;
                        }
                }
            }
        }
    }

    public static boolean teleportStand(ArmorStand stand, Location location, Player player) {
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
}
