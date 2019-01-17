package me.wolfyscript.armorstandtool.guis;

import me.wolfyscript.armorstandtool.ArmorStandTool;
import me.wolfyscript.armorstandtool.data.OptionType;
import me.wolfyscript.armorstandtool.data.PlayerCache;
import me.wolfyscript.utilities.api.inventory.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.awt.*;
import java.util.HashMap;

public class MainMenu extends GuiWindow {

    private int[] equipmentSlots = new int[]{8, 17, 26, 35, 44, 53};

    public MainMenu(InventoryAPI inventoryAPI) {
        super("main_menu", inventoryAPI, 54);
    }

    @Override
    public void onInit() {
        createItem("base_plate", Material.STONE_PRESSURE_PLATE);
        createItem("arms", Material.STICK);
        createItem("small_stand", Material.CLAY_BALL);
        createItem("no_gravity", Material.RAIL);
        createItem("invisible", Material.POTION);
        createItem("display_name", Material.NAME_TAG);
        createItem("rotation_left_arm", Material.STICK);
        createItem("rotation_left_leg", Material.STICK);
        createItem("rotation_right_arm", Material.STICK);
        createItem("rotation_right_leg", Material.STICK);
        createItem("rotation_body", Material.IRON_CHESTPLATE);
        createItem("rotation_head", Material.IRON_HELMET);
        createItem("position", Material.IRON_BLOCK);
        createItem("helmet", Material.LEATHER_HELMET);
        createItem("chestplate", Material.LEATHER_CHESTPLATE);
        createItem("leggings", Material.LEATHER_LEGGINGS);
        createItem("boots", Material.LEATHER_BOOTS);
        createItem("left_hand", Material.STICK);
        createItem("right_hand", Material.STICK);
    }

    @EventHandler
    public void openInventory(GuiUpdateEvent event) {
        if (event.verify(this)) {
            ArmorStand stand = ArmorStandTool.getPlayerCache(event.getPlayer()).getArmorStand();
            event.setItem(0, "base_plate");
            event.setItem(1, stand.hasBasePlate() ? "toggle_button_on" : "toggle_button_off", true);
            event.setItem(9, "arms");
            event.setItem(10, stand.hasArms() ? "toggle_button_on" : "toggle_button_off", true);
            event.setItem(18, "small_stand");
            event.setItem(19, stand.isSmall() ? "toggle_button_on" : "toggle_button_off", true);
            event.setItem(27, "no_gravity");
            event.setItem(28, stand.hasGravity() ? "toggle_button_on" : "toggle_button_off", true);
            event.setItem(36, "invisible");
            event.setItem(37, !stand.isVisible() ? "toggle_button_on" : "toggle_button_off", true);
            event.setItem(45, "display_name");
            event.setItem(46, stand.isCustomNameVisible() ? "toggle_button_on" : "toggle_button_off", true);
            event.setItem(9 + 4, "rotation_head");
            event.setItem(2 * 9 + 3, "rotation_left_arm");
            event.setItem(2 * 9 + 4, "rotation_body");
            event.setItem(2 * 9 + 5, "rotation_right_arm");
            event.setItem(3 * 9 + 4, "position");
            event.setItem(4 * 9 + 3, "rotation_left_leg");
            event.setItem(4 * 9 + 5, "rotation_right_leg");
            event.setItem(7, "helmet");
            event.setItem(16, "chestplate");
            event.setItem(25, "leggings");
            event.setItem(34, "boots");
            event.setItem(43, "left_hand");
            event.setItem(52, "right_hand");

            event.setItem(8, stand.getHelmet());
            event.setItem(17, stand.getChestplate());
            event.setItem(26, stand.getLeggings());
            event.setItem(35, stand.getBoots());
            event.setItem(44, stand.getEquipment().getItemInOffHand());
            event.setItem(53, stand.getEquipment().getItemInMainHand());

        }
    }

    @EventHandler
    public void onAction(GuiActionEvent event) {
        if (event.verify(this)) {
            if (event.getAction().startsWith("rotation_") || event.getAction().equals("position")) {
                ArmorStandTool.getPlayerCache(event.getPlayer()).setCurrentOption(OptionType.valueOf(event.getAction().toUpperCase()));
                event.getGuiHandler().changeToInv("settings");
            } else if (event.getAction().startsWith("toggle_button_")) {
                ArmorStand stand = ArmorStandTool.getPlayerCache(event.getPlayer()).getArmorStand();
                switch(event.getClickedSlot()){
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
                        if(!stand.getHelmet().getType().equals(Material.AIR) || !stand.getChestplate().getType().equals(Material.AIR) || !stand.getLeggings().getType().equals(Material.AIR) || !stand.getBoots().getType().equals(Material.AIR)){
                            stand.setVisible(!stand.isVisible());
                        }
                        break;
                    case 46:
                        stand.setCustomNameVisible(!stand.isCustomNameVisible());
                        break;
                }
                update(event.getGuiHandler());
            }
        }
    }

    @EventHandler
    public void onClick(GuiClickEvent event) {
        if (event.verify(this)) {
            ArmorStand stand = ArmorStandTool.getPlayerCache(event.getPlayer()).getArmorStand();
            if(event.getClickedInventory().equals(event.getPlayer().getOpenInventory().getTopInventory())){
                int slot = event.getRawSlot();
                if(slot != 8 && slot != 17 && slot != 26 && slot != 35 && slot != 44 && slot != 53){
                    event.setCancelled(true);
                }
                Bukkit.getScheduler().runTaskLater(event.getWolfyUtilities().getPlugin(), () ->{
                    stand.setHelmet(event.getClickedInventory().getItem(8));
                    stand.setChestplate(event.getClickedInventory().getItem(17));
                    stand.setLeggings(event.getClickedInventory().getItem(26));
                    stand.setBoots(event.getClickedInventory().getItem(35));
                    stand.getEquipment().setItemInOffHand(event.getClickedInventory().getItem(44));
                    stand.getEquipment().setItemInMainHand(event.getClickedInventory().getItem(53));
                }, 1);
            }else{
                if(event.getClickType().isShiftClick()){
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onDrag(GuiItemDragEvent event){
        if(event.verify(this)){
            ArmorStand stand = ArmorStandTool.getPlayerCache(event.getPlayer()).getArmorStand();
            Inventory inventory = event.getView().getTopInventory();
            Bukkit.getScheduler().runTaskLater(event.getWolfyUtilities().getPlugin(), () ->{
                stand.setHelmet(event.getView().getItem(8));
                stand.setChestplate(inventory.getItem(17));
                stand.setLeggings(inventory.getItem(26));
                stand.setBoots(inventory.getItem(35));
                stand.getEquipment().setItemInOffHand(inventory.getItem(44));
                stand.getEquipment().setItemInMainHand(inventory.getItem(53));
            }, 1);
        }
    }


}
