package me.wolfyscript.armorstandtool.configs;

import me.wolfyscript.armorstandtool.ArmorStandTool;
import me.wolfyscript.utilities.api.config.Config;
import me.wolfyscript.utilities.api.config.ConfigAPI;
import me.wolfyscript.utilities.api.config.JsonConfiguration;

public class ArmorStandToolConfig extends JsonConfiguration {

    public ArmorStandToolConfig(ConfigAPI configAPI) {
        super(configAPI, ArmorStandTool.getInstance().getDataFolder().getPath(), "main_config", "me/wolfyscript/armorstandtool/configs", "config", false);
    }

    public String getLang(){
        return getString("language");
    }

    public boolean armorStandKnockback(){
        return getBoolean("armorstand_knockback");
    }
}
