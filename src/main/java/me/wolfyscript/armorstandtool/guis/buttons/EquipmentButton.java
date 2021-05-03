package me.wolfyscript.armorstandtool.guis.buttons;

import me.wolfyscript.armorstandtool.data.ASTCache;
import me.wolfyscript.utilities.api.inventory.gui.button.buttons.ActionButton;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;

public class EquipmentButton extends ActionButton<ASTCache> {

    public EquipmentButton(int equipmentSlot) {
        super("equipment_container_" + equipmentSlot, Material.AIR,
                (cache, guiHandler, player, inventory, i, inventoryInteractEvent) -> false,
                (cache, guiHandler, player, inventory, itemStack, i, inventoryInteractEvent) -> {
                    ArmorStand stand = cache.getArmorStand();
                    if(stand.getEquipment() != null){
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
                                break;
                            default:
                                //NONE
                        }
                    }
                },
                (hashMap, cache, guiHandler, player, guiInventory, itemStack, i, b) -> {
                    ArmorStand stand = cache.getArmorStand();
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
                        default:
                            return itemStack;
                    }
                }, null);
    }
}
