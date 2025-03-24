package me.syncwrld.minecraft.launchmania;

import com.github.fierioziy.particlenativeapi.api.ParticleNativeAPI;
import com.github.fierioziy.particlenativeapi.core.ParticleNativeCore;
import com.google.common.base.Stopwatch;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import me.syncwrld.minecraft.launchmania.adapter.LocationAdapter;
import me.syncwrld.minecraft.launchmania.adapter.TimeUnitAdapter;
import me.syncwrld.minecraft.launchmania.cache.BuilderCache;
import me.syncwrld.minecraft.launchmania.cache.LauncherCache;
import me.syncwrld.minecraft.launchmania.command.impl.BuildModeCommand;
import me.syncwrld.minecraft.launchmania.command.impl.LaunchmaniaCommand;
import me.syncwrld.minecraft.launchmania.config.SettingsHolder;
import me.syncwrld.minecraft.launchmania.listener.impl.BuildListener;
import me.syncwrld.minecraft.launchmania.listener.impl.PreventionListener;
import me.syncwrld.minecraft.launchmania.task.impl.PlayerLocationVerifierTask;
import me.syncwrld.minecraft.launchmania.task.impl.VisualEffectDisplayTask;
import me.syncwrld.minecraft.launchmania.tool.config.YAML;
import me.syncwrld.minecraft.launchmania.tool.logging.BukkitLogger;
import me.syncwrld.minecraft.launchmania.tool.update.GitHubUpdateChecker;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Getter(AccessLevel.PUBLIC)
@Accessors(fluent = true)
public class LaunchmaniaPlugin extends JavaPlugin {

    /*
    Utilities
     */
    private final BukkitLogger platformLogger = new BukkitLogger(this);
    private final GitHubUpdateChecker updateChecker = new GitHubUpdateChecker(
            this,
            "syncwrld",
            "launchmania",
            30,
            true
    );

    /*
    Caches and holders
     */
    private final LauncherCache launcherCache = new LauncherCache();
    private final SettingsHolder settingsHolder = new SettingsHolder();

    /*
    Global tasks
     */
    private final VisualEffectDisplayTask visualEffectTask = new VisualEffectDisplayTask(this, launcherCache);

    /*
    APIs
     */
    private ParticleNativeAPI particleNativeAPI;

    /*
    Data file
     */
    private YAML locationsYaml;

    @SneakyThrows
    @Override
    public void onLoad() {
        platformLogger.info("Starting loading phase.");

        final Stopwatch stopwatch = Stopwatch.createStarted();
        this.saveDefaultConfig();
        this.locationsYaml = new YAML("locations", this);
        settingsHolder.load(this.getConfig());

        platformLogger.info("Loading phase completed. &b(took " + stopwatch.stop() + ")");
    }

    @Override
    public void onEnable() {
        updateChecker.enable();

        platformLogger.info("Now starting the enable phase.");
        final Stopwatch stopwatch = Stopwatch.createStarted();
        final BuilderCache builderCache = new BuilderCache();

        this.particleNativeAPI = ParticleNativeCore.loadAPI(this);
		
		/*
		Commands
		 */
        new LaunchmaniaCommand(this).register();
        new BuildModeCommand(this, builderCache).register();
		
		/*
		Listeners
		 */
        new BuildListener(this, builderCache, launcherCache, settingsHolder).register();
        new PreventionListener(this, launcherCache).register();
		
		/*
		Runnables
		 */
        new PlayerLocationVerifierTask(this, launcherCache).startRepeating(0, 300, TimeUnit.MILLISECONDS);
        effectTaskSetup();
		
		/*
		Metrics and rest of stuff
		 */
        new Metrics(this, 25212);

        loadLocations(launcherCache);
        platformLogger.info("Enable phase completed. &b(took " + stopwatch.stop() + ")");
    }

    private void effectTaskSetup() {
        if (!getConfig().getBoolean("visual-effect.enable")) {
            visualEffectTask.cancel();
            return;
        }

        if (getConfig().getBoolean("visual-effect.enable") && !(visualEffectTask().running())) {
            final String updateInterval = getConfig().getString("visual-effect.update-interval");
            final String[] parts = updateInterval.split(":");

            visualEffectTask.startRepeating(0, Long.parseLong(parts[0]), new TimeUnitAdapter().resolve(parts[1]));
        }
    }

    @Override
    public void onDisable() {
        launcherCache.save(locationsYaml);
        platformLogger.info("Saved " + launcherCache.keys().size() + " locations.");
    }

    private void loadLocations(LauncherCache launcherCache) {
        final LocationAdapter locationAdapter = new LocationAdapter();
        final List<String> locations = locationsYaml.getStringList("launchers");

        locationAdapter.resolve(locations).forEach(launcherCache::insert);
        platformLogger.info("Loaded " + launcherCache.keys().size() + " locations.");
    }

    public void reload() {
        reloadConfig();
        settingsHolder.load(this.getConfig());

        effectTaskSetup();
    }

}