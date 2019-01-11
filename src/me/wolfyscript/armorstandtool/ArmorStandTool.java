package me.wolfyscript.armorstandtool;

import com.griefcraft.lwc.LWC;
import me.wolfyscript.armorstandtool.data.OptionType;
import me.wolfyscript.armorstandtool.data.PlayerCache;
import me.wolfyscript.armorstandtool.guis.MainMenu;
import me.wolfyscript.armorstandtool.guis.SettingsGui;
import me.wolfyscript.utilities.api.WolfyUtilities;
import me.wolfyscript.utilities.api.config.Config;
import me.wolfyscript.utilities.api.config.ConfigAPI;
import me.wolfyscript.utilities.api.config.templates.LangConfig;
import me.wolfyscript.utilities.api.inventory.InventoryAPI;
import me.wolfyscript.utilities.api.language.Language;
import me.wolfyscript.utilities.api.language.LanguageAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class ArmorStandTool extends JavaPlugin implements Listener {

    private static ArmorStandTool instance;
    private static WolfyUtilities wolfyUtilities;

    private static HashMap<String, PlayerCache> playerCaches = new HashMap<>();

    public void onLoad() {
        instance = this;
    }

    public void onEnable() {
        wolfyUtilities = new WolfyUtilities(instance);
        ConfigAPI configAPI = wolfyUtilities.getConfigAPI();
        LanguageAPI languageAPI = wolfyUtilities.getLanguageAPI();
        InventoryAPI inventoryAPI = wolfyUtilities.getInventoryAPI();

        wolfyUtilities.setCHAT_PREFIX("§2[§6AST§2] §7");
        wolfyUtilities.setCONSOLE_PREFIX("[AST] ");

        configAPI.registerConfig(new Config(configAPI, "me/wolfyscript/armorstandtool/configs", getDataFolder().getPath(), "config"));
        languageAPI.registerLanguage(new Language("en_US", new LangConfig(configAPI, "me/wolfyscript/armorstandtool/configs/lang", "en_US"), configAPI));

        inventoryAPI.registerItem("none", "toggle_button_off", new ItemStack(Material.ROSE_RED));
        inventoryAPI.registerItem("none", "toggle_button_on", new ItemStack(Material.LIME_DYE));

        inventoryAPI.registerGuiWindow(new MainMenu(inventoryAPI));
        inventoryAPI.registerGuiWindow(new SettingsGui(inventoryAPI));

        inventoryAPI.setMainmenu("main_menu");

        Bukkit.getPluginManager().registerEvents(this, instance);

    }

    public void onDisable() {
        wolfyUtilities.getConfigAPI().saveConfigs();
    }


    public static ArmorStandTool getInstance() {
        return instance;
    }

    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent event) {
        PlayerCache playerCache = getPlayerCache(event.getPlayer());
        if (playerCache != null) {

            if (!playerCache.getCurrentOption().equals(OptionType.NONE) && playerCache.getFreeEdit() != -1) {
                event.setCancelled(true);
                wolfyUtilities.sendPlayerMessage(event.getPlayer(), "$msg.free_edit.cancelled$");
                playerCache.setFreeEditLoc(null);
                playerCache.setFreeEdit(-1);
            }else if (!event.isCancelled() && event.getRightClicked() instanceof ArmorStand && (!WolfyUtilities.hasPlotSquared() || PlotUtils.hasPerm(event.getPlayer(), event.getRightClicked().getLocation())) || (!WolfyUtilities.hasWorldGuard() || WGUtils.hasPermBuild(event.getRightClicked().getLocation(), event.getPlayer()))) {
                Player player = event.getPlayer();
                if (player.isSneaking()) {
                    getPlayerCache(player).setArmorStand((ArmorStand) event.getRightClicked());
                    wolfyUtilities.getInventoryAPI().openGui(player);
                    event.setCancelled(true);
                }
            } else {
                wolfyUtilities.sendPlayerMessage(event.getPlayer(), "$msg.edit.open.cancelled$");
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            PlayerCache playerCache = getPlayerCache(player);
            if (playerCache != null) {
                if (!playerCache.getCurrentOption().equals(OptionType.NONE) && playerCache.getFreeEdit() != -1) {
                    event.setCancelled(true);
                    wolfyUtilities.sendPlayerMessage(player, "$msg.free_edit.cancelled$");
                    playerCache.setFreeEditLoc(null);
                    playerCache.setFreeEdit(-1);
                }
            }
        }


    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        PlayerCache playerCache = getPlayerCache(event.getPlayer());
        if (playerCache != null) {
            if (!playerCache.getCurrentOption().equals(OptionType.NONE) && playerCache.getFreeEdit() != -1) {
                event.setCancelled(true);
                wolfyUtilities.sendPlayerMessage(event.getPlayer(), "$msg.free_edit.cancelled$");
                playerCache.setFreeEditLoc(null);
                playerCache.setFreeEdit(-1);
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("ast")) {
            if (sender instanceof Player) {

            }
        }

        return true;
    }

    public static HashMap<String, PlayerCache> getPlayerCaches() {
        return playerCaches;
    }

    public static PlayerCache getPlayerCache(Player player) {
        PlayerCache playerCache = getPlayerCaches().get(player.getUniqueId().toString());
        if (playerCache == null) {
            playerCache = new PlayerCache();
            getPlayerCaches().put(player.getUniqueId().toString(), playerCache);
        }
        return playerCache;
    }
}
