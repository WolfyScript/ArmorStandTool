package me.wolfyscript.armorstandtool.guis;

import me.wolfyscript.armorstandtool.ArmorStandTool;
import me.wolfyscript.armorstandtool.data.ASTCache;
import me.wolfyscript.armorstandtool.guis.buttons.EquipmentButton;
import me.wolfyscript.armorstandtool.guis.buttons.RotatePosSettingsButton;
import me.wolfyscript.armorstandtool.guis.buttons.ToggleSettingButton;
import me.wolfyscript.utilities.api.inventory.gui.GuiUpdate;
import me.wolfyscript.utilities.api.inventory.gui.button.buttons.DummyButton;
import me.wolfyscript.utilities.util.inventory.ItemUtils;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;

import java.util.ArrayList;
import java.util.List;

public class MainMenu extends ASTGuiWindow {

    private static final String BASE_PLATE = "base_plate";
    private static final String ARMS = "arms";
    private static final String SMALL_STAND = "small_stand";
    private static final String NO_GRAVITY = "no_gravity";
    private static final String INVISIBLE = "invisible";
    private static final String DISPLAY_NAME = "display_name";

    private static final String HELMET = "helmet";
    private static final String CHESTPLATE = "chestplate";
    private static final String LEGGINGS = "leggings";
    private static final String BOOTS = "boots";
    private static final String LEFT_HAND = "left_hand";
    private static final String RIGHT_HAND = "right_hand";

    private static final String ROT_LEFT_ARM = "rotation_left_arm";
    private static final String ROT_RIGHT_ARM = "rotation_right_arm";
    private static final String ROT_LEFT_LEG = "rotation_left_leg";
    private static final String ROT_RIGHT_LEG = "rotation_right_leg";
    private static final String ROT_BODY = "rotation_body";
    private static final String ROT_HEAD = "rotation_head";
    private static final String POSITION = "position";

    private static final String EQUIP_CONTAINER = "equipment_container_";

    public MainMenu(ASTGUICluster cluster) {
        super(cluster, "main_menu", 54);
    }

    @Override
    public void onInit() {
        registerButton(new DummyButton<>(BASE_PLATE, Material.STONE_PRESSURE_PLATE));
        registerButton(new DummyButton<>(ARMS, Material.STICK));
        registerButton(new DummyButton<>(SMALL_STAND, Material.CLAY_BALL));
        registerButton(new DummyButton<>(NO_GRAVITY, Material.RAIL));
        registerButton(new DummyButton<>(INVISIBLE, Material.POTION));
        registerButton(new DummyButton<>(DISPLAY_NAME, Material.NAME_TAG));

        registerButton(new ToggleSettingButton(0, ArmorStand::hasBasePlate));
        registerButton(new ToggleSettingButton(1, ArmorStand::hasArms));
        registerButton(new ToggleSettingButton(2, ArmorStand::isSmall));
        registerButton(new ToggleSettingButton(3, ArmorStand::hasGravity));
        registerButton(new ToggleSettingButton(4, stand -> !stand.isVisible()));
        registerButton(new ToggleSettingButton(5, ArmorStand::isCustomNameVisible));

        registerButton(new RotatePosSettingsButton(ROT_LEFT_ARM, Material.STICK));
        registerButton(new RotatePosSettingsButton(ROT_LEFT_LEG, Material.STICK));
        registerButton(new RotatePosSettingsButton(ROT_RIGHT_ARM, Material.STICK));
        registerButton(new RotatePosSettingsButton(ROT_RIGHT_LEG, Material.STICK));
        registerButton(new RotatePosSettingsButton(ROT_BODY, Material.IRON_CHESTPLATE));
        registerButton(new RotatePosSettingsButton(ROT_HEAD, Material.IRON_HELMET));
        registerButton(new RotatePosSettingsButton(POSITION, Material.IRON_BLOCK));

        registerButton(new DummyButton<>(HELMET, Material.LEATHER_HELMET));
        registerButton(new DummyButton<>(CHESTPLATE, Material.LEATHER_CHESTPLATE));
        registerButton(new DummyButton<>(LEGGINGS, Material.LEATHER_LEGGINGS));
        registerButton(new DummyButton<>(BOOTS, Material.LEATHER_BOOTS));
        registerButton(new DummyButton<>(LEFT_HAND, Material.STICK));
        registerButton(new DummyButton<>(RIGHT_HAND, Material.STICK));

        for (int i = 0; i < 6; i++) {
            registerButton(new EquipmentButton(i));
        }
    }

    @Override
    public void onUpdateSync(GuiUpdate<ASTCache> update) {
        Player player = update.getPlayer();

        List<String> allowedOptions = new ArrayList<>();
        if (player.hasPermission("armorstandtools.option.base_plate")) {
            allowedOptions.add(BASE_PLATE);
        }
        if (player.hasPermission("armorstandtools.option.arms")) {
            allowedOptions.add(ARMS);
        }
        if (player.hasPermission("armorstandtools.option.small_stand")) {
            allowedOptions.add(SMALL_STAND);
        }
        if (player.hasPermission("armorstandtools.option.no_gravity")) {
            allowedOptions.add(NO_GRAVITY);
        }
        if (player.hasPermission("armorstandtools.option.invisible")) {
            allowedOptions.add(INVISIBLE);
        }
        if (player.hasPermission("armorstandtools.option.display_name")) {
            allowedOptions.add(DISPLAY_NAME);
        }

        int i = 0;
        for (String option : allowedOptions) {
            int j = i > 0 ? i / 9 : 0;
            update.setButton(i, option);
            update.setButton(i + 1, "toggle_button_" + j);
            i += 9;
        }

        update.setButton(9 + 4, ROT_HEAD);
        update.setButton(2 * 9 + 3, ROT_LEFT_ARM);
        update.setButton(2 * 9 + 4, ROT_BODY);
        update.setButton(2 * 9 + 5, ROT_RIGHT_ARM);
        update.setButton(3 * 9 + 4, POSITION);
        update.setButton(4 * 9 + 3, ROT_LEFT_LEG);
        update.setButton(4 * 9 + 5, ROT_RIGHT_LEG);

        update.setButton(7, HELMET);
        update.setButton(16, CHESTPLATE);
        update.setButton(25, LEGGINGS);
        update.setButton(34, BOOTS);
        update.setButton(43, LEFT_HAND);
        update.setButton(52, RIGHT_HAND);

        update.setButton(8, EQUIP_CONTAINER + "3");
        update.setButton(17, EQUIP_CONTAINER + "2");
        update.setButton(26, EQUIP_CONTAINER + "1");
        update.setButton(35, EQUIP_CONTAINER + "0");
        update.setButton(44, EQUIP_CONTAINER + "4");
        update.setButton(53, EQUIP_CONTAINER + "5");
    }

    public static void toggleStandSettings(int slot, Player player, ASTCache cache) {
        ArmorStand stand = cache.getArmorStand();
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
                if (ArmorStandTool.inst().getAPI().getPermissions().hasPermission(player, "armorstandtool.edit.invisible") && stand.getEquipment() != null) {
                    EntityEquipment equip = stand.getEquipment();
                    if((!ItemUtils.isAirOrNull(equip.getItemInMainHand()) || !ItemUtils.isAirOrNull(equip.getItemInOffHand()) || !ItemUtils.isAirOrNull(equip.getHelmet()) || !ItemUtils.isAirOrNull(equip.getChestplate()) || !ItemUtils.isAirOrNull(equip.getLeggings()) || !ItemUtils.isAirOrNull(equip.getBoots()))) {
                        stand.setVisible(!stand.isVisible());
                    }
                }
                break;
            case 46:
                stand.setCustomNameVisible(!stand.isCustomNameVisible());
                break;
            default:
                //None
        }
    }

}
