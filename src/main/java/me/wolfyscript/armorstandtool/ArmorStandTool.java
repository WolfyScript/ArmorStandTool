package me.wolfyscript.armorstandtool;

import me.wolfyscript.armorstandtool.commands.LocateArmorStandsCommand;
import me.wolfyscript.armorstandtool.configs.ArmorStandToolConfig;
import me.wolfyscript.armorstandtool.data.FreeEditMode;
import me.wolfyscript.armorstandtool.data.OptionType;
import me.wolfyscript.armorstandtool.data.PlayerCache;
import me.wolfyscript.armorstandtool.guis.ASTGUICluster;
import me.wolfyscript.armorstandtool.guis.MainMenu;
import me.wolfyscript.armorstandtool.guis.SettingsGui;
import me.wolfyscript.armorstandtool.listener.FreeEditListener;
import me.wolfyscript.utilities.api.WolfyUtilities;
import me.wolfyscript.utilities.api.chat.Chat;
import me.wolfyscript.utilities.api.inventory.gui.GuiCluster;
import me.wolfyscript.utilities.api.inventory.gui.InventoryAPI;
import me.wolfyscript.utilities.api.inventory.gui.cache.CustomCache;
import me.wolfyscript.utilities.api.language.Language;
import me.wolfyscript.utilities.api.language.LanguageAPI;
import me.wolfyscript.utilities.util.json.jackson.JacksonUtil;
import me.wolfyscript.utilities.util.protection.PSUtils;
import me.wolfyscript.utilities.util.protection.WGUtils;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class ArmorStandTool extends JavaPlugin implements Listener {

    private static ArmorStandTool instance;
    private static WolfyUtilities wolfyUtilities;
    private static ArmorStandToolConfig config;

    public static HashMap<String, ArmorStand> currentlyActive = new HashMap<>();
    private static HashMap<String, PlayerCache> playerCaches = new HashMap<>();

    public void onLoad() {
        instance = this;
    }

    public void onEnable() {
        wolfyUtilities = WolfyUtilities.get(instance);
        Chat chat = wolfyUtilities.getChat();
        chat.setIN_GAME_PREFIX("§3[§7AST§3] §7");
        chat.setCONSOLE_PREFIX("[AST] ");

        InventoryAPI<CustomCache> inventoryAPI = wolfyUtilities.getInventoryAPI(CustomCache.class);

        //Load Config
        try {
            saveResource("config.json", false);
            config = JacksonUtil.getObjectMapper().readValue(new File(getDataFolder(), "config.json"), ArmorStandToolConfig.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        loadLanguage();

        inventoryAPI.registerCluster(new ASTGUICluster(inventoryAPI, "main"));

        //Bukkit.getPluginCommand("locatestands").setExecutor(new LocateArmorStandsCommand());

        Bukkit.getPluginManager().registerEvents(this, instance);
        Bukkit.getPluginManager().registerEvents(new FreeEditListener(), instance);
        Metrics metrics = new Metrics(this, 5222);
    }

    public void onDisable() {
        try {
            JacksonUtil.getObjectWriter(true).writeValue(new File(getDataFolder(), "config.json"), config);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static ArmorStandTool getInstance() {
        return instance;
    }

    public void loadLanguage() {
        LanguageAPI languageAPI = wolfyUtilities.getLanguageAPI();
        String lang = config.getLang();
        saveResource("lang/en_US.json", true);
        saveResource("lang/de_DE.json", true);

        Language fallBackLanguage = new Language(this, "en_US");

        languageAPI.registerLanguage(fallBackLanguage);
        System.out.println("Loaded fallback language \"en_US\" v" + fallBackLanguage.getVersion() + " translated by " + String.join("", fallBackLanguage.getAuthors()));

        File file = new File(getDataFolder(), "lang/" + lang + ".json");
        if (file.exists()) {
            Language language = new Language(this, lang);
            languageAPI.registerLanguage(language);
            languageAPI.setActiveLanguage(language);
            System.out.println("Loaded active language \"" + lang + "\" v" + language.getVersion() + " translated by " + String.join("", language.getAuthors()));
        }
    }


    @EventHandler(priority = EventPriority.LOWEST)
    public void onInteract(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        PlayerCache playerCache = getPlayerCache(player);
        if (!playerCache.getCurrentOption().equals(OptionType.NONE) && !playerCache.getFreeEdit().equals(FreeEditMode.NONE)) {
            event.setCancelled(true);
            wolfyUtilities.getChat().sendMessages(player, "$msg.free_edit.cancelled$");
            playerCache.setFreeEditLoc(null);
            playerCache.setFreeEdit(FreeEditMode.NONE);
        } else if (!event.isCancelled() && event.getRightClicked() instanceof ArmorStand && (!currentlyActive.containsValue(event.getRightClicked()) || event.getRightClicked().equals(currentlyActive.get(player.getUniqueId().toString()))) && hasPerm(event.getRightClicked().getLocation(), player)) {
            if (player.isSneaking()) {
                getPlayerCache(player).setArmorStand((ArmorStand) event.getRightClicked());
                currentlyActive.put(player.getUniqueId().toString(), (ArmorStand) event.getRightClicked());
                wolfyUtilities.getInventoryAPI().openCluster(player, "main");
                event.setCancelled(true);
            }
        } else if (event.getRightClicked() instanceof ArmorStand && player.isSneaking()) {
            wolfyUtilities.getChat().sendMessages(player, "$msg.edit.open.cancelled$");
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof ArmorStand) {
            Player player = (Player) event.getDamager();
            PlayerCache playerCache = getPlayerCache(player);
            if (!playerCache.getCurrentOption().equals(OptionType.NONE) && !playerCache.getFreeEdit().equals(FreeEditMode.NONE)) {
                event.setCancelled(true);
                wolfyUtilities.getChat().sendMessage(player, "$msg.free_edit.cancelled$");
                playerCache.setFreeEditLoc(null);
                playerCache.setFreeEdit(FreeEditMode.NONE);
            } else if (!config.isArmorStandKnockback()) {
                Location loc = event.getEntity().getLocation();
                Bukkit.getScheduler().runTaskLater(this, () -> {
                    event.getEntity().setVelocity(new Vector(0, 0, 0));
                    event.getEntity().teleport(loc);
                }, 1);
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        PlayerCache playerCache = getPlayerCache(event.getPlayer());
        if (!playerCache.getCurrentOption().equals(OptionType.NONE) && !playerCache.getFreeEdit().equals(FreeEditMode.NONE)) {
            event.setCancelled(true);
            wolfyUtilities.getChat().sendMessage(event.getPlayer(), "$msg.free_edit.cancelled$");
            playerCache.setFreeEditLoc(null);
            playerCache.setFreeEdit(FreeEditMode.NONE);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("ast")) {
            if (args.length == 0) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    wolfyUtilities.getChat().sendMessages(p,
                            "~*~*~*~*&6[&3&lArmorStandTool&6]&7~*~*~*~*~",
                            "",
                            "        &n     by &b&n&lWolfyScript&7&n      ",
                            "",
                            "               &nVersion:&r&b " + getDescription().getVersion(),
                            "~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~");
                }
            } else if (args[0].equalsIgnoreCase("reload")) {
                if (sender.hasPermission("armorstandtool.reload")) {
                    try {
                        JacksonUtil.getObjectWriter(true).writeValue(new File(getDataFolder(), "config.json"), config);
                        config = JacksonUtil.getObjectMapper().readValue(new File(getDataFolder(), "config.json"), ArmorStandToolConfig.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    InventoryAPI<?> inventoryAPI = wolfyUtilities.getInventoryAPI();
                    inventoryAPI.reset();
                    loadLanguage();

                    inventoryAPI.reset();

                    Chat chat = wolfyUtilities.getChat();
                    if (sender instanceof Player) {
                        chat.sendMessage((Player) sender, "$msg.reload.complete$");
                    } else {
                        chat.sendConsoleMessage("$msg.reload.complete$");
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
        if (WolfyUtilities.hasPlotSquared() && PSUtils.isPlotWorld(location.getWorld())) {
            if (PSUtils.hasPerm(player, location)) {
                return true;
            }
        } else if (WolfyUtilities.hasWorldGuard()) {
            return WGUtils.hasPermBuild(location, player);
        } else {
            return true;
        }
        return false;
    }

    public static WolfyUtilities getAPI() {
        return wolfyUtilities;
    }
}
