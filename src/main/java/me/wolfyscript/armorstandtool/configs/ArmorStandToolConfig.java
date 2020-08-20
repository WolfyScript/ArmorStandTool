package me.wolfyscript.armorstandtool.configs;

import me.wolfyscript.armorstandtool.ArmorStandTool;
import me.wolfyscript.utilities.api.config.Config;
import me.wolfyscript.utilities.api.config.ConfigAPI;
import me.wolfyscript.utilities.api.config.JsonConfiguration;
import me.wolfyscript.utilities.libraries.com.fasterxml.jackson.annotation.JsonGetter;
import me.wolfyscript.utilities.libraries.com.fasterxml.jackson.annotation.JsonSetter;

public class ArmorStandToolConfig {

    private String lang;
    private boolean armorStandKnockback;

    @JsonGetter(value = "language")
    public String getLang(){
        return lang;
    }

    @JsonSetter(value = "language")
    public void setLang(String lang) {
        this.lang = lang;
    }

    @JsonGetter(value = "armorstand_knockback")
    public boolean isArmorStandKnockback() {
        return armorStandKnockback;
    }

    @JsonSetter(value = "armorstand_knockback")
    public void setArmorStandKnockback(boolean armorStandKnockback) {
        this.armorStandKnockback = armorStandKnockback;
    }


}
