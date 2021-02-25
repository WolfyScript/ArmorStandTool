package me.wolfyscript.armorstandtool.data;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

public class PlayerCache {

    private OptionType currentOption;
    private ArmorStand armorStand;
    private FreeEditMode freeEdit;
    private Location freeEditLoc;
    private Location freeEditStandPos;
    private double lastDis;

    public PlayerCache(){
        this.freeEdit = FreeEditMode.NONE;
        this.currentOption = OptionType.NONE;
        lastDis = -9999d;
    }

    public boolean hasLastDis(){
        return lastDis > -9999d;
    }

    public double getLastDis() {
        return lastDis;
    }

    public void setLastDis(double lastDis) {
        this.lastDis = lastDis;
    }

    public Location getFreeEditStandPos() {
        return freeEditStandPos;
    }

    public void setFreeEditStandPos(Location freeEditStandPos) {
        this.freeEditStandPos = freeEditStandPos;
    }

    public void setFreeEditLoc(Location freeEditLoc) {
        this.freeEditLoc = freeEditLoc;
    }

    public Location getFreeEditLoc() {
        return freeEditLoc;
    }

    public void setFreeEdit(FreeEditMode freeEdit) {
        this.freeEdit = freeEdit;
    }

    public FreeEditMode getFreeEdit() {
        return freeEdit;
    }

    public void setArmorStand(ArmorStand armorStand) {
        this.armorStand = armorStand;
    }

    public ArmorStand getArmorStand() {
        return armorStand;
    }

    public OptionType getCurrentOption() {
        return currentOption;
    }

    public void setCurrentOption(OptionType currentOption) {
        this.currentOption = currentOption;
    }
}
