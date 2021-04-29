package me.wolfyscript.armorstandtool.guis;

import me.wolfyscript.armorstandtool.data.ASTCache;
import me.wolfyscript.utilities.api.inventory.gui.GuiUpdate;
import me.wolfyscript.utilities.api.inventory.gui.GuiWindow;

public abstract class ASTGuiWindow extends GuiWindow<ASTCache> {

    protected ASTGuiWindow(ASTGUICluster cluster, String key, int size) {
        super(cluster, key, size, true);
    }

    @Override
    public void onUpdateAsync(GuiUpdate<ASTCache> guiUpdate) { }
}
