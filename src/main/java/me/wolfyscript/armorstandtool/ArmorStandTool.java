package me.wolfyscript.armorstandtool;

import me.wolfyscript.armorstandtool.configs.ArmorStandToolConfig;
import me.wolfyscript.armorstandtool.data.ASTCache;
import me.wolfyscript.armorstandtool.guis.ASTGUICluster;
import me.wolfyscript.armorstandtool.listener.FreeEditListener;
import me.wolfyscript.armorstandtool.listener.PlayerListener;
import me.wolfyscript.utilities.api.WolfyUtilities;
import me.wolfyscript.utilities.api.chat.Chat;
import me.wolfyscript.utilities.api.inventory.gui.InventoryAPI;
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
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ArmorStandTool extends JavaPlugin implements Listener {

    private static ArmorStandTool instance;
    private static final String CONFIG_FILE = "config.json";

    private ArmorStandToolConfig config;
    private final WolfyUtilities api;

    private final Map<UUID, ArmorStand> currentlyActive;

    public ArmorStandTool(){
        super();
        instance = this;
        this.currentlyActive = new HashMap<>();
        api = WolfyUtilities.get(instance, false);
        api.setInventoryAPI(new InventoryAPI<>(api.getPlugin(), api, ASTCache.class));
        Chat chat = api.getChat();
        chat.setInGamePrefix("§3[§7AST§3] §7");
    }

    public static ArmorStandTool inst(){
        return instance;
    }

    public WolfyUtilities getAPI() {
        return api;
    }

    @Override
    public void onEnable() {
        api.initialize();
        InventoryAPI<ASTCache> inventoryAPI = api.getInventoryAPI(ASTCache.class);

        //Load Config
        try {
            File file = new File(getDataFolder(), CONFIG_FILE);
            if(!file.exists()){
                saveResource(CONFIG_FILE, false);
            }
            config = JacksonUtil.getObjectMapper().readValue(file, ArmorStandToolConfig.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        loadLanguage();

        inventoryAPI.registerCluster(new ASTGUICluster(inventoryAPI, "main"));

        Bukkit.getPluginManager().registerEvents(this, instance);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(this), instance);
        Bukkit.getPluginManager().registerEvents(new FreeEditListener(this), instance);
        Metrics metrics = new Metrics(this, 5222);
    }

    @Override
    public void onDisable() {
        try {
            JacksonUtil.getObjectWriter(true).writeValue(new File(getDataFolder(), CONFIG_FILE), config);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadLanguage() {
        LanguageAPI languageAPI = api.getLanguageAPI();
        String lang = config.getLang();
        saveResource("lang/en_US.json", true);
        saveResource("lang/de_DE.json", true);

        Language fallBackLanguage = new Language(this, "en_US");

        languageAPI.registerLanguage(fallBackLanguage);
        api.getConsole().info("Loaded fallback language \"en_US\" v" + fallBackLanguage.getVersion() + " translated by " + String.join("", fallBackLanguage.getAuthors()));

        File file = new File(getDataFolder(), "lang/" + lang + ".json");
        if (file.exists()) {
            Language language = new Language(this, lang);
            languageAPI.registerLanguage(language);
            languageAPI.setActiveLanguage(language);
            api.getConsole().info("Loaded active language \"" + lang + "\" v" + language.getVersion() + " translated by " + String.join("", language.getAuthors()));
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("ast")) {
            if (args.length == 0) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    api.getChat().sendMessages(p,
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
                        JacksonUtil.getObjectWriter(true).writeValue(new File(getDataFolder(), CONFIG_FILE), config);
                        config = JacksonUtil.getObjectMapper().readValue(new File(getDataFolder(), CONFIG_FILE), ArmorStandToolConfig.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    InventoryAPI<?> inventoryAPI = api.getInventoryAPI();
                    inventoryAPI.reset();
                    loadLanguage();

                    inventoryAPI.reset();

                    Chat chat = api.getChat();
                    if (sender instanceof Player) {
                        chat.sendMessage((Player) sender, "$msg.reload.complete$");
                    } else {
                        api.getConsole().info("$msg.reload.complete$");
                    }
                }
            }
        }
        return true;
    }

    public boolean hasPerm(Location location, Player player) {
        if (WolfyUtilities.hasPlotSquared() && PSUtils.isPlotWorld(location.getWorld())) {
            return PSUtils.hasPerm(player, location);
        } else if (WolfyUtilities.hasWorldGuard()) {
            return WGUtils.hasPermBuild(location, player);
        } else {
            return true;
        }
    }

    public ArmorStandToolConfig getASTConfig() {
        return config;
    }

    public void setActive(Player player, ArmorStand armorStand){
        currentlyActive.put(player.getUniqueId(), armorStand);
    }

    public boolean isActive(ArmorStand stand) {
        return currentlyActive.containsValue(stand);
    }

    public ArmorStand getActive(Player player){
        return currentlyActive.get(player.getUniqueId());
    }
}
