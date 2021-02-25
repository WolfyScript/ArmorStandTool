package me.wolfyscript.armorstandtool.guis;

import me.wolfyscript.utilities.api.inventory.gui.GuiCluster;
import me.wolfyscript.utilities.api.inventory.gui.InventoryAPI;
import me.wolfyscript.utilities.api.inventory.gui.cache.CustomCache;

public class ASTGUICluster extends GuiCluster<CustomCache> {

    public ASTGUICluster(InventoryAPI<CustomCache> inventoryAPI, String id) {
        super(inventoryAPI, id);
    }

    @Override
    public void onInit() {
        registerGuiWindow(new MainMenu(this));
        registerGuiWindow(new SettingsGui(this));
    }
}
