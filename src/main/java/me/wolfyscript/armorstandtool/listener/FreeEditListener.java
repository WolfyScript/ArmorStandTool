package me.wolfyscript.armorstandtool.listener;

import me.wolfyscript.armorstandtool.ArmorStandTool;
import me.wolfyscript.armorstandtool.data.FreeEditMode;
import me.wolfyscript.armorstandtool.data.OptionType;
import me.wolfyscript.armorstandtool.data.ASTCache;
import me.wolfyscript.armorstandtool.util.ArmorStandUtils;
import me.wolfyscript.utilities.api.WolfyUtilities;
import me.wolfyscript.utilities.api.inventory.gui.InventoryAPI;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class FreeEditListener implements Listener {

    private final ArmorStandTool ast;
    private final WolfyUtilities api;
    private final InventoryAPI<ASTCache> invAPI;

    public FreeEditListener(ArmorStandTool armorStandTool){
        this.ast = armorStandTool;
        this.api = armorStandTool.getAPI();
        this.invAPI = this.api.getInventoryAPI(ASTCache.class);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        ASTCache cache = invAPI.getGuiHandler(player).getCustomCache();
        ArmorStand stand = cache.getArmorStand();
        if (stand == null || !stand.isValid()) {
            cache.setFreeEdit(FreeEditMode.NONE);
            return;
        }
        FreeEditMode mode = cache.getFreeEdit();
        Location standLoc = stand.getLocation();

        if (event.getTo() != null) {
            if (mode.equals(FreeEditMode.RELOCATE_CURSOR)) {
                Block block = player.getTargetBlockExact(10);
                if (block != null) {
                    Location loc = block.getLocation().add(0.5, 1, 0.5);
                    standLoc.setX(loc.getX());
                    standLoc.setY(loc.getY());
                    standLoc.setZ(loc.getZ());
                    ArmorStandUtils.teleportStand(stand, standLoc, player);
                }
            } else if(event.getTo().distanceSquared(event.getFrom()) > 0) {
                if (mode.equals(FreeEditMode.RELOCATE_PLAYER)) {
                    if (player.isSneaking()) {
                        Location loc = player.getLocation();
                        standLoc.setX(loc.getX());
                        standLoc.setY(loc.getY());
                        standLoc.setZ(loc.getZ());
                    } else if (event.getTo().distanceSquared(standLoc) > 0.32) {
                        Block block = player.getLocation().getBlock();
                        standLoc.setX(block.getX() + 0.5);
                        standLoc.setY(block.getY());
                        standLoc.setZ(block.getZ() + 0.5);
                    }
                    ArmorStandUtils.teleportStand(stand, standLoc, player);
                } else if (!mode.equals(FreeEditMode.NONE)) {
                    Location standStartPos = cache.getFreeEditStandPos().clone();
                    Location playerPos = player.getLocation();
                    Location startPos = cache.getFreeEditLoc().clone();
                    double dis = startPos.toVector().length() - playerPos.toVector().length();
                    cache.setLastDis(dis);
                    if(cache.getCurrentOption().equals(OptionType.POSITION)){
                        switch (mode) {
                            case POS_X:
                                standStartPos.add(dis / 2, 0, 0);
                                break;
                            case POS_Y:
                                standStartPos.add(0, dis / 2, 0);
                                break;
                            case POS_Z:
                                standStartPos.add(0, 0, dis / 2);
                                break;
                            case YAW:
                                standStartPos.setYaw(standStartPos.getYaw() + (float) dis * 20f);
                                break;
                        }
                        ArmorStandUtils.teleportStand(stand, standStartPos, player);
                    }else{
                        double disAngle = -0.08;
                        if (dis > 0) {
                            disAngle *= -1;
                        }
                        if (cache.hasLastDis() && (cache.getLastDis() < 0 && cache.getLastDis() < dis) || (cache.getLastDis() > 0 && cache.getLastDis() > dis)) {
                            disAngle *= -1;
                        }
                        Vector vector = new Vector(mode == FreeEditMode.POS_X ? disAngle : 0, mode == FreeEditMode.POS_Y ? disAngle : 0, mode == FreeEditMode.YAW ? disAngle : 0);
                        switch (cache.getCurrentOption()) {
                            case ROTATION_BODY:
                                stand.setBodyPose(stand.getBodyPose().add(vector.getX(), vector.getY(), vector.getZ()));
                                break;
                            case ROTATION_HEAD:
                                stand.setHeadPose(stand.getHeadPose().add(vector.getX(), vector.getY(), vector.getZ()));
                                break;
                            case ROTATION_LEFT_ARM:
                                stand.setLeftArmPose(stand.getLeftArmPose().add(vector.getX(), vector.getY(), vector.getZ()));
                                break;
                            case ROTATION_RIGHT_ARM:
                                stand.setRightArmPose(stand.getRightArmPose().add(vector.getX(), vector.getY(), vector.getZ()));
                                break;
                            case ROTATION_LEFT_LEG:
                                stand.setLeftLegPose(stand.getLeftLegPose().add(vector.getX(), vector.getY(), vector.getZ()));
                                break;
                            case ROTATION_RIGHT_LEG:
                                stand.setRightLegPose(stand.getRightLegPose().add(vector.getX(), vector.getY(), vector.getZ()));
                        }
                    }
                }
            }
        }
    }

}
