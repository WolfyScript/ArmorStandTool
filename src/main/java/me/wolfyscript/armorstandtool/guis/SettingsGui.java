package me.wolfyscript.armorstandtool.guis;

import me.wolfyscript.armorstandtool.ArmorStandTool;
import me.wolfyscript.armorstandtool.data.FreeEditMode;
import me.wolfyscript.armorstandtool.data.OptionType;
import me.wolfyscript.armorstandtool.data.PlayerCache;
import me.wolfyscript.armorstandtool.guis.buttons.PoseResetButton;
import me.wolfyscript.armorstandtool.guis.buttons.ValueDisplayButton;
import me.wolfyscript.armorstandtool.guis.buttons.ValueEditButton;
import me.wolfyscript.armorstandtool.util.ArmorStandUtils;
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

public class SettingsGui extends ASTGuiWindow implements EventListener {

    public SettingsGui(ASTGUICluster cluster) {
        super(cluster, "settings", 54);
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
            ArmorStandUtils.teleportStand(stand, loc, player);
            return true;
        }, (hashMap, customCache, guiHandler, player, guiInventory, itemStack, i, b) -> {
            hashMap.put("%value%", ArmorStandTool.getPlayerCache(player).getArmorStand().getLocation().getYaw());
            return itemStack;
        }));

        registerButton(new ActionButton<>("relocate_cursor", Material.COMPASS, (customCache, guiHandler, player, guiInventory, i, inventoryInteractEvent) -> {
            ArmorStandTool.getPlayerCache(player).setFreeEdit(FreeEditMode.RELOCATE_CURSOR);
            guiHandler.close();
            return true;
        }));

        registerButton(new ActionButton<>("relocate_player", Material.LEAD, (customCache, guiHandler, player, guiInventory, i, inventoryInteractEvent) -> {
            ArmorStandTool.getPlayerCache(player).setFreeEdit(FreeEditMode.RELOCATE_PLAYER);
            guiHandler.close();
            return true;
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

            if (update.getPlayer().hasPermission("armorstandtools.option.relocate.cursor")) {
                update.setButton(53, "relocate_cursor");
            }
            if (update.getPlayer().hasPermission("armorstandtools.option.relocate.player")) {
                update.setButton(52, "relocate_player");
            }
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
}
