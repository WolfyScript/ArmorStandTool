package me.wolfyscript.armorstandtool.guis;

import me.wolfyscript.armorstandtool.ArmorStandTool;
import me.wolfyscript.armorstandtool.data.OptionType;
import me.wolfyscript.armorstandtool.data.PlayerCache;
import me.wolfyscript.armorstandtool.guis.buttons.PoseResetButton;
import me.wolfyscript.armorstandtool.guis.buttons.ValueDisplayButton;
import me.wolfyscript.armorstandtool.guis.buttons.ValueEditButton;
import me.wolfyscript.utilities.api.WolfyUtilities;
import me.wolfyscript.utilities.api.inventory.*;
import me.wolfyscript.utilities.api.inventory.button.ButtonActionRender;
import me.wolfyscript.utilities.api.inventory.button.ButtonState;
import me.wolfyscript.utilities.api.inventory.button.buttons.ActionButton;
import me.wolfyscript.utilities.api.utils.protection.PSUtils;
import me.wolfyscript.utilities.api.utils.protection.WGUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class SettingsGui extends GuiWindow {

    public SettingsGui(InventoryAPI inventoryAPI) {
        super("settings", inventoryAPI, 54);
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

        registerButton(new ActionButton("yaw", new ButtonState("yaw", Material.YELLOW_DYE, new ButtonActionRender() {
            @Override
            public boolean run(GuiHandler guiHandler, Player player, Inventory inventory, int i, InventoryClickEvent inventoryClickEvent) {
                ArmorStand stand = ArmorStandTool.getPlayerCache(player).getArmorStand();
                Location loc = stand.getLocation();
                loc.setYaw(0);
                teleportStand(stand, loc, player);
                return true;
            }

            @Override
            public ItemStack render(HashMap<String, Object> hashMap, GuiHandler guiHandler, Player player, ItemStack itemStack, int i, boolean b) {
                hashMap.put("%value%", ArmorStandTool.getPlayerCache(player).getArmorStand().getLocation().getYaw());
                return itemStack;
            }
        })));

        registerButton(new ActionButton("free_edit", new ButtonState("free_edit", Material.CYAN_DYE, (guiHandler, player, inventory, i, inventoryClickEvent) -> {
            PlayerCache playerCache = ArmorStandTool.getPlayerCache(player);
            ArmorStand stand = playerCache.getArmorStand();
            switch (i) {
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
                playerCache.setFreeEditLoc(player.getLocation().clone());
                playerCache.setFreeEditStandPos(stand.getLocation().clone());
                guiHandler.close();
            }
            return true;
        })));

        registerButton(new ActionButton("back", new ButtonState("back", Material.MAGENTA_DYE, (guiHandler, player, inventory, i, inventoryClickEvent) -> {
            guiHandler.openPreviousInv();
            return true;
        })));
    }

    @EventHandler
    public void onInventoryOpen(GuiUpdateEvent event) {
        if (event.verify(this)) {

            event.setButton(0, "value_-1.0");
            event.setButton(1, "value_-0.1");
            event.setButton(2, "value_-0.01");
            event.setButton(9, "value_-1.0");
            event.setButton(10, "value_-0.1");
            event.setButton(11, "value_-0.01");
            event.setButton(18, "value_-1.0");
            event.setButton(19, "value_-0.1");
            event.setButton(20, "value_-0.01");

            if (ArmorStandTool.getPlayerCache(event.getPlayer()).getCurrentOption().equals(OptionType.POSITION)) {
                event.setButton(3, "loc_x");
                event.setButton(12, "loc_y");
                event.setButton(21, "loc_z");

                event.setButton(30, "yaw");
                event.setButton(27, "value_-1.0");
                event.setButton(28, "value_-0.1");
                event.setButton(29, "value_-0.01");

                event.setButton(31, "value_0.01");
                event.setButton(32, "value_0.1");
                event.setButton(33, "value_1.0");

                event.setButton(35, "free_edit");
            } else {
                event.setButton(3, "pose_x");
                event.setButton(12, "pose_y");
                event.setButton(21, "pose_z");

                for(int i = 27; i < 36; i++){
                    event.setItem(i, new ItemStack(Material.AIR));
                }
            }

            event.setButton(4, "value_0.01");
            event.setButton(5, "value_0.1");
            event.setButton(6, "value_1.0");
            event.setButton(13, "value_0.01");
            event.setButton(14, "value_0.1");
            event.setButton(15, "value_1.0");
            event.setButton(22, "value_0.01");
            event.setButton(23, "value_0.1");
            event.setButton(24, "value_1.0");
            event.setButton(8, "free_edit");
            event.setButton(17, "free_edit");
            event.setButton(26, "free_edit");
            event.setButton(45, "back");
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (event.getTo() != null && event.getTo().distance(event.getFrom()) > 0) {
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
