package me.wolfyscript.armorstandtool.guis;

import me.wolfyscript.armorstandtool.ArmorStandTool;
import me.wolfyscript.armorstandtool.guis.buttons.EquipmentButton;
import me.wolfyscript.armorstandtool.guis.buttons.RotatePosSettingsButton;
import me.wolfyscript.armorstandtool.guis.buttons.ToggleSettingButton;
import me.wolfyscript.utilities.api.WolfyUtilities;
import me.wolfyscript.utilities.api.inventory.GuiUpdate;
import me.wolfyscript.utilities.api.inventory.GuiWindow;
import me.wolfyscript.utilities.api.inventory.InventoryAPI;
import me.wolfyscript.utilities.api.inventory.button.buttons.DummyButton;
import me.wolfyscript.utilities.api.inventory.button.buttons.ToggleButton;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;

public class MainMenu extends GuiWindow {

    public MainMenu(InventoryAPI inventoryAPI) {
        super("main_menu", inventoryAPI, 54);
    }

    @Override
    public void onInit() {
        registerButton(new DummyButton("base_plate", Material.STONE_PRESSURE_PLATE));
        registerButton(new DummyButton("arms", Material.STICK));
        registerButton(new DummyButton("small_stand", Material.CLAY_BALL));
        registerButton(new DummyButton("no_gravity", Material.RAIL));
        registerButton(new DummyButton("invisible", Material.POTION));
        registerButton(new DummyButton("display_name", Material.NAME_TAG));

        for (int i = 0; i < 6; i++) {
            registerButton(new ToggleSettingButton(i));
        }

        registerButton(new RotatePosSettingsButton("rotation_left_arm", Material.STICK));
        registerButton(new RotatePosSettingsButton("rotation_left_leg", Material.STICK));
        registerButton(new RotatePosSettingsButton("rotation_right_arm", Material.STICK));
        registerButton(new RotatePosSettingsButton("rotation_right_leg", Material.STICK));
        registerButton(new RotatePosSettingsButton("rotation_body", Material.IRON_CHESTPLATE));
        registerButton(new RotatePosSettingsButton("rotation_head", Material.IRON_HELMET));
        registerButton(new RotatePosSettingsButton("position", Material.IRON_BLOCK));

        registerButton(new DummyButton("helmet", Material.LEATHER_HELMET));
        registerButton(new DummyButton("chestplate", Material.LEATHER_CHESTPLATE));
        registerButton(new DummyButton("leggings", Material.LEATHER_LEGGINGS));
        registerButton(new DummyButton("boots", Material.LEATHER_BOOTS));
        registerButton(new DummyButton("left_hand", Material.STICK));
        registerButton(new DummyButton("right_hand", Material.STICK));

        for (int i = 0; i < 6; i++) {
            registerButton(new EquipmentButton(i));
        }
    }

    @Override
    public void onUpdateAsync(GuiUpdate update) {
        Player player = update.getPlayer();
        ArmorStand stand = ArmorStandTool.getPlayerCache(update.getPlayer()).getArmorStand();
        ((ToggleButton) getButton("toggle_button_0")).setState(update.getGuiHandler(), stand.hasBasePlate());
        ((ToggleButton) getButton("toggle_button_1")).setState(update.getGuiHandler(), stand.hasArms());
        ((ToggleButton) getButton("toggle_button_2")).setState(update.getGuiHandler(), stand.isSmall());
        ((ToggleButton) getButton("toggle_button_3")).setState(update.getGuiHandler(), stand.hasGravity());
        ((ToggleButton) getButton("toggle_button_4")).setState(update.getGuiHandler(), !stand.isVisible());
        ((ToggleButton) getButton("toggle_button_5")).setState(update.getGuiHandler(), stand.isCustomNameVisible());

        List<String> allowedOptions = new ArrayList<>();
        if (player.hasPermission("armorstandtools.option.base_plate")) {
            allowedOptions.add("base_plate");
        }
        if (player.hasPermission("armorstandtools.option.arms")) {
            allowedOptions.add("arms");
        }
        if (player.hasPermission("armorstandtools.option.small_stand")) {
            allowedOptions.add("small_stand");
        }
        if (player.hasPermission("armorstandtools.option.no_gravity")) {
            allowedOptions.add("no_gravity");
        }
        if (player.hasPermission("armorstandtools.option.invisible")) {
            allowedOptions.add("invisible");
        }
        if (player.hasPermission("armorstandtools.option.display_name")) {
            allowedOptions.add("display_name");
        }

        int i = 0;
        for (String option : allowedOptions) {
            int j = i > 0 ? i / 9 : 0;
            update.setButton(i, option);
            update.setButton(i + 1, "toggle_button_" + j);
            i += 9;
        }

        update.setButton(9 + 4, "rotation_head");
        update.setButton(2 * 9 + 3, "rotation_left_arm");
        update.setButton(2 * 9 + 4, "rotation_body");
        update.setButton(2 * 9 + 5, "rotation_right_arm");
        update.setButton(3 * 9 + 4, "position");
        update.setButton(4 * 9 + 3, "rotation_left_leg");
        update.setButton(4 * 9 + 5, "rotation_right_leg");

        update.setButton(7, "helmet");
        update.setButton(16, "chestplate");
        update.setButton(25, "leggings");
        update.setButton(34, "boots");
        update.setButton(43, "left_hand");
        update.setButton(52, "right_hand");

        update.setButton(8, "equipment_container_3");
        update.setButton(17, "equipment_container_2");
        update.setButton(26, "equipment_container_1");
        update.setButton(35, "equipment_container_0");
        update.setButton(44, "equipment_container_4");
        update.setButton(53, "equipment_container_5");
    }

    public static void toggleStandSettings(int slot, Player player) {
        ArmorStand stand = ArmorStandTool.getPlayerCache(player).getArmorStand();
        switch (slot) {
            case 1:
                stand.setBasePlate(!stand.hasBasePlate());
                break;
            case 10:
                stand.setArms(!stand.hasArms());
                break;
            case 19:
                stand.setSmall(!stand.isSmall());
                break;
            case 28:
                stand.setGravity(!stand.hasGravity());
                break;
            case 37:
                if ((stand.getEquipment() != null && (!stand.getEquipment().getItemInMainHand().getType().equals(Material.AIR) || !stand.getEquipment().getItemInOffHand().getType().equals(Material.AIR))) || !stand.getHelmet().getType().equals(Material.AIR) || !stand.getChestplate().getType().equals(Material.AIR) || !stand.getLeggings().getType().equals(Material.AIR) || !stand.getBoots().getType().equals(Material.AIR)) {
                    if (WolfyUtilities.hasPermission(player, "armorstandtool.edit.invisible")) {
                        stand.setVisible(!stand.isVisible());
                    }
                }
                break;
            case 46:
                stand.setCustomNameVisible(!stand.isCustomNameVisible());
                break;
        }
    }

}
