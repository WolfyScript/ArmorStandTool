package me.wolfyscript.armorstandtool.guis;

import me.wolfyscript.armorstandtool.data.ASTCache;
import me.wolfyscript.utilities.api.inventory.gui.GuiCluster;
import me.wolfyscript.utilities.api.inventory.gui.InventoryAPI;

public class ASTGUICluster extends GuiCluster<ASTCache> {

    public ASTGUICluster(InventoryAPI<ASTCache> inventoryAPI, String id) {
        super(inventoryAPI, id);
    }

    @Override
    public void onInit() {
        registerGuiWindow(new MainMenu(this));
        registerGuiWindow(new SettingsGui(this));
    }
}
