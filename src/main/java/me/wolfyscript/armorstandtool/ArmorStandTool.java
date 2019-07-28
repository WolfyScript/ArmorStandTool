package me.wolfyscript.armorstandtool;

import me.wolfyscript.armorstandtool.configs.ArmorStandToolConfig;
import me.wolfyscript.armorstandtool.data.OptionType;
import me.wolfyscript.armorstandtool.data.PlayerCache;
import me.wolfyscript.armorstandtool.guis.MainMenu;
import me.wolfyscript.armorstandtool.guis.SettingsGui;
import me.wolfyscript.utilities.api.WolfyUtilities;
import me.wolfyscript.utilities.api.config.Config;
import me.wolfyscript.utilities.api.config.ConfigAPI;
import me.wolfyscript.utilities.api.config.templates.LangConfiguration;
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
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class ArmorStandTool extends JavaPlugin implements Listener {

    private static ArmorStandTool instance;
    private static WolfyUtilities wolfyUtilities;
    private static ArmorStandToolConfig config;

    private static HashMap<String, ArmorStand> currentlyActive = new HashMap<>();
    private static HashMap<String, PlayerCache> playerCaches = new HashMap<>();

    private static boolean enabled = false;

    public void onLoad() {
        instance = this;
    }

    public void onEnable() {
        if(Bukkit.getPluginManager().getPlugin("WolfyUtilities") != null && !Bukkit.getPluginManager().getPlugin("WolfyUtilities").getDescription().getVersion().equals("0.1.0.0")){
            enabled = true;
            wolfyUtilities = new WolfyUtilities(instance);
            ConfigAPI configAPI = wolfyUtilities.getConfigAPI();
            LanguageAPI languageAPI = wolfyUtilities.getLanguageAPI();
            InventoryAPI inventoryAPI = wolfyUtilities.getInventoryAPI();

            wolfyUtilities.setCHAT_PREFIX("§3[§7AST§3] §7");
            wolfyUtilities.setCONSOLE_PREFIX("[AST] ");

            config = new ArmorStandToolConfig(configAPI);
            configAPI.registerConfig(config);
            config.loadDefaults();

            String lang = config.getLang();
            Config langConf;
            if (ArmorStandTool.getInstance().getResource("me/wolfyscript/armorstandtool/configs/lang/" + lang + ".yml") != null) {
                langConf = new LangConfiguration(configAPI, lang, "me/wolfyscript/armorstandtool/configs/lang", lang, "yml", false);
            } else {
                langConf = new LangConfiguration(configAPI, "en_US", "me/wolfyscript/armorstandtool/configs/lang", lang, "yml", false);
            }
            langConf.loadDefaults();
            languageAPI.registerLanguage(new Language(lang, langConf, configAPI));

            inventoryAPI.registerItem("none", "toggle_button_off", new ItemStack(Material.ROSE_RED));
            inventoryAPI.registerItem("none", "toggle_button_on", new ItemStack(Material.LIME_DYE));

            inventoryAPI.registerGuiWindow(new MainMenu(inventoryAPI));
            inventoryAPI.registerGuiWindow(new SettingsGui(inventoryAPI));

            inventoryAPI.setMainmenu("main_menu");

            Bukkit.getPluginManager().registerEvents(this, instance);

        }else{
            Bukkit.getConsoleSender().sendMessage("You need to have the LATEST WolfyUtilities in order to run this plugin!");
            Bukkit.getConsoleSender().sendMessage("Download here: https://www.spigotmc.org/resources/wolfyutilities.64124/");
            Bukkit.getPluginManager().disablePlugin(this);
        }
        Metrics metrics = new Metrics(this);
    }

    public void onDisable() {
        if(enabled){
            wolfyUtilities.getConfigAPI().saveConfigs();
        }
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
        if (event.getDamager() instanceof Player && event.getEntity() instanceof ArmorStand) {
            Player player = (Player) event.getDamager();
            PlayerCache playerCache = getPlayerCache(player);
            if (playerCache != null) {
                if (!playerCache.getCurrentOption().equals(OptionType.NONE) && playerCache.getFreeEdit() != -1) {
                    event.setCancelled(true);
                    wolfyUtilities.sendPlayerMessage(player, "$msg.free_edit.cancelled$");
                    playerCache.setFreeEditLoc(null);
                    playerCache.setFreeEdit(-1);
                }else if(config.blockArmorStandKnockback()){
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
                    String lang = config.getLang();

                    if(wolfyUtilities.getLanguageAPI().getActiveLanguage().getName().equals(lang)){
                        wolfyUtilities.getLanguageAPI().getActiveLanguage().reloadKeys();
                    }else {
                        wolfyUtilities.getLanguageAPI().unregisterLanguages();
                        Config langConf;
                        if (ArmorStandTool.getInstance().getResource("me/wolfyscript/armorstandtool/configs/lang/" + lang + ".yml") != null) {
                            langConf = new LangConfiguration(wolfyUtilities.getConfigAPI(), lang, "me/wolfyscript/armorstandtool/configs/lang", lang, "yml", false);
                        } else {
                            langConf = new LangConfiguration(wolfyUtilities.getConfigAPI(), "en_US", "me/wolfyscript/armorstandtool/configs/lang", lang, "yml", false);
                        }
                        langConf.loadDefaults();
                        wolfyUtilities.getLanguageAPI().setActiveLanguage(new Language(lang, langConf, wolfyUtilities.getConfigAPI()));
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

    public static WolfyUtilities getAPI() {
        return wolfyUtilities;
    }
}
