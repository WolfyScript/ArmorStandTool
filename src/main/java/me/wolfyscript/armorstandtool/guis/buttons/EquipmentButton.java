package me.wolfyscript.armorstandtool.guis.buttons;

import me.wolfyscript.armorstandtool.ArmorStandTool;
import me.wolfyscript.utilities.api.inventory.button.buttons.ItemInputButton;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;

public class EquipmentButton extends ItemInputButton {

    public EquipmentButton(int equipmentSlot) {
        super("equipment_container_" + equipmentSlot, Material.AIR, (guiHandler, player, inventory, i, inventoryClickEvent) -> {
            ArmorStand stand = ArmorStandTool.getPlayerCache(player).getArmorStand();
            Bukkit.getScheduler().runTaskLater(ArmorStandTool.getInstance(), () -> {
                ItemStack itemStack = inventory.getItem(i);
                switch (equipmentSlot) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                        ItemStack[] armorContents = stand.getEquipment().getArmorContents();
                        armorContents[equipmentSlot] = itemStack;
                        stand.getEquipment().setArmorContents(armorContents);
                        break;
                    case 4:
                        stand.getEquipment().setItemInOffHand(itemStack);
                        break;
                    case 5:
                        stand.getEquipment().setItemInMainHand(itemStack);
                }
            }, 1);
            return false;
        }, (hashMap, guiHandler, player, itemStack, i, b) -> {
            ArmorStand stand = ArmorStandTool.getPlayerCache(player).getArmorStand();
            switch (equipmentSlot) {
                case 0:
                case 1:
                case 2:
                case 3:
                    return stand.getEquipment().getArmorContents()[equipmentSlot];
                case 4:
                    return stand.getEquipment().getItemInOffHand();
                case 5:
                    return stand.getEquipment().getItemInMainHand();
            }
            return itemStack;
        });
    }
}
