package me.syncwrld.minecraft.launchmania.tool.update;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.syncwrld.minecraft.launchmania.LaunchmaniaPlugin;
import me.syncwrld.minecraft.launchmania.tool.cooldown.CooldownManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author syncwrld
 * @version 1.0
 * <p>
 * 23/10/2024
 */
public class GitHubUpdateChecker implements Listener {

    private final LaunchmaniaPlugin plugin;
    private final String githubOwner;
    private final String githubRepo;
    private final long checkInterval;
    private final boolean logOnNoUpdate;
    private final Gson gson = new Gson();
    private final String currentVersion;
    private final CooldownManager<String> cooldownManager = new CooldownManager<>();

    private String latestVersion;
    private String releaseUrl;
    private boolean updateAvailable = false;

    public GitHubUpdateChecker(LaunchmaniaPlugin plugin, String githubOwner, String githubRepo,
                               long checkIntervalMinutes, boolean logOnNoUpdate) {
        this.plugin = plugin;
        this.githubOwner = githubOwner;
        this.githubRepo = githubRepo;
        this.checkInterval = checkIntervalMinutes * 60 * 20;
        this.logOnNoUpdate = logOnNoUpdate;
        this.currentVersion = plugin.getDescription().getVersion();
    }

    public void enable() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.runTaskAsynchronously(plugin, this::checkForUpdate);
        scheduler.runTaskTimerAsynchronously(plugin, this::checkForUpdate, checkInterval, checkInterval);

        plugin.platformLogger().info("Started update checker - checking every " + (checkInterval / (60 * 20)) + " minutes");
    }

    private void checkForUpdate() {
        try {
            HttpURLConnection connection = getUrlConnection();
            if (connection.getResponseCode() != 200) {
                plugin.platformLogger().warning("Failed to check for updates. Response code: " + connection.getResponseCode());
                return;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            JsonArray releases = gson.fromJson(reader.readLine(), JsonArray.class);
            reader.close();
            connection.disconnect();

            if (releases.size() <= 0) return;

            JsonObject latestRelease = releases.get(0).getAsJsonObject();
            latestVersion = latestRelease.get("tag_name").getAsString();
            releaseUrl = latestRelease.get("html_url").getAsString();

            if (compareVersions(cleanVersionString(currentVersion), cleanVersionString(latestVersion)) < 0) {
                updateAvailable = true;
                Bukkit.getScheduler().runTask(plugin, this::logUpdateInfo);
            } else {
                if (logOnNoUpdate) {
                    plugin.platformLogger().info("You are running the latest version: " + currentVersion);
                }
            }
        } catch (Exception e) {
            plugin.platformLogger().error("Failed while checking for updates", e);
        }
    }

    private void logUpdateInfo() {
        plugin.platformLogger().info(
                "",
                "New update available!",
                "Current version: " + currentVersion,
                "Latest version: " + latestVersion,
                "Download: " + releaseUrl,
                ""
        );
        notifyAdmins();
    }

    private HttpURLConnection getUrlConnection() throws IOException {
        URL url = new URL("https://api.github.com/repos/" + githubOwner + "/" + githubRepo + "/releases");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        connection.setRequestProperty("Accept", "application/vnd.github.v3+json");
        connection.setRequestProperty("User-Agent", plugin.getName() + "/" + plugin.getDescription().getVersion());
        return connection;
    }

    private void notifyAdmins() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("launchmania.admin") && !cooldownManager.hasCooldown(player.getName())) {
                player.sendMessage(new String[]{
                        "",
                        "§6[LaunchMania] §eA new update is available!",
                        "§7 ▸ §fCurrent version is §e" + currentVersion,
                        "§7 ▸ §fThe latest available version is §e" + latestVersion,
                        "§7 ▸ §fDownload it at this link: ",
                        "§7 ❙ §e" + releaseUrl.replace("-test", ""),
                        ""
                });
                cooldownManager.setCooldown(player.getName(), 30, TimeUnit.MINUTES);
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("launchmania.admin") && updateAvailable && !cooldownManager.hasCooldown(player.getName())) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> notifyAdmins(), 40L);
        }
    }

    private int compareVersions(String v1, String v2) {
        String[] parts1 = v1.split("\\.");
        String[] parts2 = v2.split("\\.");
        int length = Math.max(parts1.length, parts2.length);
        for (int i = 0; i < length; i++) {
            int p1 = i < parts1.length ? parseInt(parts1[i]) : 0;
            int p2 = i < parts2.length ? parseInt(parts2[i]) : 0;
            if (p1 != p2) return Integer.compare(p1, p2);
        }
        return 0;
    }

    private int parseInt(String s) {
        Matcher m = Pattern.compile("^\\d+").matcher(s);
        return m.find() ? Integer.parseInt(m.group()) : 0;
    }

    private String cleanVersionString(String version) {
        return version.startsWith("v") ? version.substring(1) : version;
    }

    public boolean isUpdateAvailable() {
        return updateAvailable;
    }

    public String getLatestVersion() {
        return latestVersion;
    }

    public String getReleaseUrl() {
        return releaseUrl;
    }
}