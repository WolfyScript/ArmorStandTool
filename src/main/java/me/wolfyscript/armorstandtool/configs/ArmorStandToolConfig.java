package me.wolfyscript.armorstandtool.configs;

import me.wolfyscript.armorstandtool.ArmorStandTool;
import me.wolfyscript.utilities.api.config.Config;
import me.wolfyscript.utilities.api.config.ConfigAPI;

public class ArmorStandToolConfig extends Config {

    public ArmorStandToolConfig(ConfigAPI configAPI) {
        super(configAPI, ArmorStandTool.getInstance().getDataFolder().getPath(), "main_config", "me/wolfyscript/armorstandtool/configs", "config", "yml", false);
    }

    public String getLang(){
        return getString("language");
    }

    public boolean blockArmorStandKnockback(){
        return getBoolean("block_armorstand-knockback");
    }
}
