package me.wolfyscript.armorstandtool.guis;

import me.wolfyscript.armorstandtool.ArmorStandTool;
import me.wolfyscript.armorstandtool.data.FreeEditMode;
import me.wolfyscript.armorstandtool.data.PlayerCache;
import me.wolfyscript.utilities.api.inventory.gui.GuiHandler;
import me.wolfyscript.utilities.api.inventory.gui.GuiWindow;
import me.wolfyscript.utilities.api.inventory.gui.cache.CustomCache;
import me.wolfyscript.utilities.api.nms.inventory.GUIInventory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;

public abstract class ASTGuiWindow extends GuiWindow<CustomCache> {

    public ASTGuiWindow(ASTGUICluster cluster, String key, int size) {
        super(cluster, key, size, true);
    }

    @Override
    public boolean onClose(GuiHandler<CustomCache> guiHandler, GUIInventory<CustomCache> guiInventory, InventoryView transaction) {
        Player player = guiHandler.getPlayer();
        PlayerCache playerCache = ArmorStandTool.getPlayerCache(player);
        if (playerCache.getFreeEdit().equals(FreeEditMode.NONE)) {
            ArmorStandTool.currentlyActive.put(player.getUniqueId().toString(), null);
        }
        return super.onClose(guiHandler, guiInventory, transaction);
    }
}
