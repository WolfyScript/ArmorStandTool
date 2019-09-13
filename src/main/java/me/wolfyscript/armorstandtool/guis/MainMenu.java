package me.wolfyscript.armorstandtool.guis;

import me.wolfyscript.armorstandtool.ArmorStandTool;
import me.wolfyscript.armorstandtool.guis.buttons.EquipmentButton;
import me.wolfyscript.armorstandtool.guis.buttons.RotatePosSettingsButton;
import me.wolfyscript.armorstandtool.guis.buttons.ToggleSettingButton;
import me.wolfyscript.utilities.api.WolfyUtilities;
import me.wolfyscript.utilities.api.inventory.*;
import me.wolfyscript.utilities.api.inventory.button.ButtonState;
import me.wolfyscript.utilities.api.inventory.button.buttons.DummyButton;
import me.wolfyscript.utilities.api.inventory.button.buttons.ToggleButton;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class MainMenu extends GuiWindow {

    public MainMenu(InventoryAPI inventoryAPI) {
        super("main_menu", inventoryAPI, 54);
    }

    @Override
    public void onInit() {
        registerButton(new DummyButton("base_plate", new ButtonState("base_plate", Material.STONE_PRESSURE_PLATE)));
        registerButton(new DummyButton("arms", new ButtonState("arms", Material.STICK)));
        registerButton(new DummyButton("small_stand", new ButtonState("small_stand", Material.CLAY_BALL)));
        registerButton(new DummyButton("no_gravity", new ButtonState("no_gravity", Material.RAIL)));
        registerButton(new DummyButton("invisible", new ButtonState("invisible", Material.POTION)));
        registerButton(new DummyButton("display_name", new ButtonState("display_name", Material.NAME_TAG)));

        for(int i = 0; i < 6; i++){
            registerButton(new ToggleSettingButton(i));
        }

        registerButton(new RotatePosSettingsButton("rotation_left_arm", Material.STICK));
        registerButton(new RotatePosSettingsButton("rotation_left_leg", Material.STICK));
        registerButton(new RotatePosSettingsButton("rotation_right_arm", Material.STICK));
        registerButton(new RotatePosSettingsButton("rotation_right_leg", Material.STICK));
        registerButton(new RotatePosSettingsButton("rotation_body", Material.IRON_CHESTPLATE));
        registerButton(new RotatePosSettingsButton("rotation_head", Material.IRON_HELMET));
        registerButton(new RotatePosSettingsButton("position", Material.IRON_BLOCK));

        registerButton(new DummyButton("helmet", new ButtonState("helmet", Material.LEATHER_HELMET)));
        registerButton(new DummyButton("chestplate", new ButtonState("chestplate", Material.LEATHER_CHESTPLATE)));
        registerButton(new DummyButton("leggings", new ButtonState("leggings", Material.LEATHER_LEGGINGS)));
        registerButton(new DummyButton("boots", new ButtonState("boots", Material.LEATHER_BOOTS)));
        registerButton(new DummyButton("left_hand", new ButtonState("left_hand", Material.STICK)));
        registerButton(new DummyButton("right_hand", new ButtonState("right_hand", Material.STICK)));

        for(int i = 0; i < 6; i++){
            registerButton(new EquipmentButton(i));
        }
    }

    @EventHandler
    public void openInventory(GuiUpdateEvent event) {
        if (event.verify(this)) {
            ArmorStand stand = ArmorStandTool.getPlayerCache(event.getPlayer()).getArmorStand();
            ((ToggleButton)getButton("toggle_button_0")).setState(event.getGuiHandler(), stand.hasBasePlate());
            ((ToggleButton)getButton("toggle_button_1")).setState(event.getGuiHandler(), stand.hasArms());
            ((ToggleButton)getButton("toggle_button_2")).setState(event.getGuiHandler(), stand.isSmall());
            ((ToggleButton)getButton("toggle_button_3")).setState(event.getGuiHandler(), stand.hasGravity());
            ((ToggleButton)getButton("toggle_button_4")).setState(event.getGuiHandler(), !stand.isVisible());
            ((ToggleButton)getButton("toggle_button_5")).setState(event.getGuiHandler(), stand.isCustomNameVisible());

            event.setButton(0, "base_plate");
            event.setButton(9, "arms");
            event.setButton(18, "small_stand");
            event.setButton(27, "no_gravity");
            event.setButton(36, "invisible");
            event.setButton(45, "display_name");

            event.setButton(1, "toggle_button_0");
            event.setButton(10, "toggle_button_1");
            event.setButton(19, "toggle_button_2");
            event.setButton(28, "toggle_button_3");
            event.setButton(37, "toggle_button_4");
            event.setButton(46, "toggle_button_5");

            event.setButton(9 + 4, "rotation_head");
            event.setButton(2 * 9 + 3, "rotation_left_arm");
            event.setButton(2 * 9 + 4, "rotation_body");
            event.setButton(2 * 9 + 5, "rotation_right_arm");
            event.setButton(3 * 9 + 4, "position");
            event.setButton(4 * 9 + 3, "rotation_left_leg");
            event.setButton(4 * 9 + 5, "rotation_right_leg");

            event.setButton(7, "helmet");
            event.setButton(16, "chestplate");
            event.setButton(25, "leggings");
            event.setButton(34, "boots");
            event.setButton(43, "left_hand");
            event.setButton(52, "right_hand");

            event.setButton(8, "equipment_container_3");
            event.setButton(17, "equipment_container_2");
            event.setButton(26, "equipment_container_1");
            event.setButton(35, "equipment_container_0");
            event.setButton(44, "equipment_container_4");
            event.setButton(53, "equipment_container_5");
        }
    }

    public static void toggleStandSettings(int slot, Player player){
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
                    if(WolfyUtilities.hasPermission(player, "armorstandtool.edit.invisible")){
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
