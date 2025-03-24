package me.syncwrld.minecraft.launchmania.task;

import me.syncwrld.minecraft.launchmania.LaunchmaniaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.TimeUnit;

public abstract class AbstractRunnable extends BukkitRunnable {
	
	private final LaunchmaniaPlugin plugin;
	private BukkitTask task;
	
	private boolean running = false;
	
	public AbstractRunnable(LaunchmaniaPlugin plugin) {
		this.plugin = plugin;
	}
	
	public void startRepeating(long delay, long duration, TimeUnit timeUnit) {
		this.task = runTaskTimer(plugin, delay, timeUnit.toSeconds(duration) * 20);
		this.running = true;
	}
	
	public void runDelayed(long delay) {
		this.task = this.runTaskLater(plugin, delay);
		this.running = true;
		
		Bukkit.getScheduler().runTaskLater(plugin, () -> running = false, delay);
	}
	
	public void runOnce() {
		this.running = true;
		this.task = this.runTask(plugin);
		Bukkit.getScheduler().runTask(plugin, () -> running = false);
	}
	
	public void cancel() {
		try {
			this.task.cancel();
		} catch (Exception ignored) {}
	}
	
	public LaunchmaniaPlugin plugin() {
		return plugin;
	}
	
	public abstract void tick();
	
	@Override
	public void run() {
		tick();
	}
	
	public boolean running() {
		return this.running;
	}
	
}
