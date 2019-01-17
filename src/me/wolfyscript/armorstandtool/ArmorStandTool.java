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
import me.wolfyscript.utilities.api.inventory.GuiHandler;
import me.wolfyscript.utilities.api.inventory.InventoryAPI;
import me.wolfyscript.utilities.api.language.Language;
import me.wolfyscript.utilities.api.language.LanguageAPI;
import me.wolfyscript.utilities.api.utils.protection.PSUtils;
import me.wolfyscript.utilities.api.utils.protection.WGUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StringUtil;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ArmorStandTool extends JavaPlugin implements Listener {

    private static ArmorStandTool instance;
    private static WolfyUtilities wolfyUtilities;

    private static HashMap<String, ArmorStand> currentlyActive = new HashMap<>();
    private static HashMap<String, PlayerCache> playerCaches = new HashMap<>();

    public void onLoad() {
        instance = this;
    }

    public void onEnable() {
        wolfyUtilities = new WolfyUtilities(instance);
        ConfigAPI configAPI = wolfyUtilities.getConfigAPI();
        LanguageAPI languageAPI = wolfyUtilities.getLanguageAPI();
        InventoryAPI inventoryAPI = wolfyUtilities.getInventoryAPI();

        wolfyUtilities.setCHAT_PREFIX("§3[§7AST§3] §7");
        wolfyUtilities.setCONSOLE_PREFIX("[AST] ");

        configAPI.registerConfig(new Config(configAPI, "me/wolfyscript/armorstandtool/configs", getDataFolder().getPath(), "config"));
        configAPI.getConfig("config").loadDefaults();
        String chosenLang = configAPI.getConfig("config").getString("language");
        languageAPI.registerLanguage(new Language(chosenLang, new LangConfig(configAPI, "me/wolfyscript/armorstandtool/configs/lang", chosenLang), configAPI));

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

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInteract(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        PlayerCache playerCache = getPlayerCache(player);
        if (playerCache != null) {
            if (!playerCache.getCurrentOption().equals(OptionType.NONE) && playerCache.getFreeEdit() != -1) {
                event.setCancelled(true);
                wolfyUtilities.sendPlayerMessage(player, "$msg.free_edit.cancelled$");
                playerCache.setFreeEditLoc(null);
                playerCache.setFreeEdit(-1);
            }else if (!event.isCancelled() && event.getRightClicked() instanceof ArmorStand && (!currentlyActive.containsValue(event.getRightClicked()) || event.getRightClicked().equals(currentlyActive.get(player.getUniqueId().toString()))) && hasPerm(event.getRightClicked().getLocation(), player)) {
                if (player.isSneaking()) {
                    getPlayerCache(player).setArmorStand((ArmorStand) event.getRightClicked());
                    currentlyActive.put(player.getUniqueId().toString(), (ArmorStand) event.getRightClicked());
                    wolfyUtilities.getInventoryAPI().openGui(player);
                    event.setCancelled(true);
                }
            } else if(event.getRightClicked() instanceof ArmorStand && player.isSneaking()){
                wolfyUtilities.sendPlayerMessage(event.getPlayer(), "$msg.edit.open.cancelled$");
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event){
        Player player = (Player) event.getPlayer();
        if(wolfyUtilities.getInventoryAPI().hasGuiHandler(player)){
            GuiHandler guiHandler = wolfyUtilities.getInventoryAPI().getGuiHandler(player);
            PlayerCache playerCache = getPlayerCache(player);
            if(!guiHandler.isChatEventActive() && !guiHandler.isChangingInv() && playerCache.getFreeEdit() == -1){
                currentlyActive.put(player.getUniqueId().toString(), null);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
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
                }else if(wolfyUtilities.getConfigAPI().getConfig("config").getBoolean("block_armorstand-knockback")){
                    Location loc = event.getEntity().getLocation();
                    Bukkit.getScheduler().runTaskLater(this, () -> {
                        event.getEntity().setVelocity(new Vector(0,0,0));
                        event.getEntity().teleport(loc);
                    },1);
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
            if(args.length == 0){
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    wolfyUtilities.sendPlayerMessage(p, "~*~*~*~*&6[&3&lArmorStandTool&6]&7~*~*~*~*~");
                    wolfyUtilities.sendPlayerMessage(p, "");
                    wolfyUtilities.sendPlayerMessage(p, "        &n     by &b&n&lWolfyScript&7&n      ");
                    wolfyUtilities.sendPlayerMessage(p, "          ------------------");
                    wolfyUtilities.sendPlayerMessage(p, "");
                    wolfyUtilities.sendPlayerMessage(p, "               &nVersion:&r&b "+getDescription().getVersion());
                    wolfyUtilities.sendPlayerMessage(p, "");
                    wolfyUtilities.sendPlayerMessage(p, "~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~");
                }
            }else if(args[0].equalsIgnoreCase("reload")){
                if(sender.hasPermission("armorstandtool.reload")){
                    wolfyUtilities.getConfigAPI().loadConfigs();
                    String chosenLang = wolfyUtilities.getConfigAPI().getConfig("config").getString("language");
                    if(wolfyUtilities.getLanguageAPI().getActiveLanguage().getName().equals(chosenLang)){
                        wolfyUtilities.getLanguageAPI().getActiveLanguage().reloadKeys();
                    }else {
                        wolfyUtilities.getLanguageAPI().unregisterLanguages();
                        wolfyUtilities.getLanguageAPI().setActiveLanguage(new Language(chosenLang, new LangConfig(wolfyUtilities.getConfigAPI(), "me/wolfyscript/armorstandtool/configs/lang", chosenLang), wolfyUtilities.getConfigAPI()));
                    }
                    InventoryAPI inventoryAPI = wolfyUtilities.getInventoryAPI();
                    inventoryAPI.reset();
                    inventoryAPI.registerItem("none", "toggle_button_off", new ItemStack(Material.ROSE_RED));
                    inventoryAPI.registerItem("none", "toggle_button_on", new ItemStack(Material.LIME_DYE));

                    inventoryAPI.registerGuiWindow(new MainMenu(inventoryAPI));
                    inventoryAPI.registerGuiWindow(new SettingsGui(inventoryAPI));

                    inventoryAPI.setMainmenu("main_menu");

                    if(sender instanceof Player){
                        wolfyUtilities.sendPlayerMessage((Player) sender, "$msg.reload.complete$");
                    }else{
                        wolfyUtilities.sendConsoleMessage("$msg.reload.complete$");
                    }
                }
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

    private boolean hasPerm(Location location, Player player) {
        if(WolfyUtilities.hasPlotSquared() && PSUtils.isPlotWorld(location.getWorld())){
            if(PSUtils.hasPerm(player, location)){
                return true;
            }
        }else if (WolfyUtilities.hasWorldGuard()) {
            return WGUtils.hasPermBuild(location, player);
        }else{
            return true;
        }
        return false;
    }
}
