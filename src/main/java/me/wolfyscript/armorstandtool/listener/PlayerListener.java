package me.wolfyscript.armorstandtool.listener;

import me.wolfyscript.armorstandtool.ArmorStandTool;
import me.wolfyscript.armorstandtool.data.FreeEditMode;
import me.wolfyscript.armorstandtool.data.OptionType;
import me.wolfyscript.armorstandtool.data.ASTCache;
import me.wolfyscript.utilities.api.WolfyUtilities;
import me.wolfyscript.utilities.api.inventory.gui.InventoryAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

public class PlayerListener implements Listener {

    private final ArmorStandTool ast;
    private final WolfyUtilities api;
    private final InventoryAPI<ASTCache> invAPI;

    public PlayerListener(ArmorStandTool armorStandTool){
        this.ast = armorStandTool;
        this.api = armorStandTool.getAPI();
        this.invAPI = this.api.getInventoryAPI(ASTCache.class);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInteract(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        ASTCache cache = api.getInventoryAPI(ASTCache.class).getGuiHandler(player).getCustomCache();
        if (!cache.getCurrentOption().equals(OptionType.NONE) && !cache.getFreeEdit().equals(FreeEditMode.NONE)) {
            event.setCancelled(true);
            api.getChat().sendMessages(player, "$msg.free_edit.cancelled$");
            cache.setFreeEditLoc(null);
            cache.setFreeEdit(FreeEditMode.NONE);
        } else if (!event.isCancelled() && event.getRightClicked() instanceof ArmorStand && (!ast.isActive((ArmorStand) event.getRightClicked()) || event.getRightClicked().equals(ast.getActive(player))) && ast.hasPerm(event.getRightClicked().getLocation(), player)) {
            if (player.isSneaking()) {
                invAPI.getGuiHandler(player).getCustomCache().setArmorStand((ArmorStand) event.getRightClicked());
                ast.setActive(player, (ArmorStand) event.getRightClicked());
                api.getInventoryAPI().openCluster(player, "main");
                event.setCancelled(true);
            }
        } else if (event.getRightClicked() instanceof ArmorStand && player.isSneaking()) {
            api.getChat().sendMessages(player, "$msg.edit.open.cancelled$");
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof ArmorStand) {
            Player player = (Player) event.getDamager();
            ASTCache cache = invAPI.getGuiHandler(player).getCustomCache();
            if (!cache.getCurrentOption().equals(OptionType.NONE) && !cache.getFreeEdit().equals(FreeEditMode.NONE)) {
                event.setCancelled(true);
                api.getChat().sendMessage(player, "$msg.free_edit.cancelled$");
                cache.setFreeEditLoc(null);
                cache.setFreeEdit(FreeEditMode.NONE);
            } else if (!ast.getASTConfig().isArmorStandKnockback()) {
                Location loc = event.getEntity().getLocation();
                Bukkit.getScheduler().runTaskLater(this.ast, () -> {
                    event.getEntity().setVelocity(new Vector(0, 0, 0));
                    event.getEntity().teleport(loc);
                }, 1);
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        ASTCache cache = invAPI.getGuiHandler(event.getPlayer()).getCustomCache();
        if (!cache.getCurrentOption().equals(OptionType.NONE) && !cache.getFreeEdit().equals(FreeEditMode.NONE)) {
            event.setCancelled(true);
            api.getChat().sendMessage(event.getPlayer(), "$msg.free_edit.cancelled$");
            cache.setFreeEditLoc(null);
            cache.setFreeEdit(FreeEditMode.NONE);
        }
    }


}
