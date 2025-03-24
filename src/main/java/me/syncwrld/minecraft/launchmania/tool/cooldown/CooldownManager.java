package me.syncwrld.minecraft.launchmania.tool.cooldown;

import me.syncwrld.minecraft.launchmania.tool.time.TimeFormatter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class CooldownManager<K> {
	private final Map<K, Cooldown> cooldowns = new ConcurrentHashMap<>();
	
	public void setCooldown(K key, long duration, TimeUnit timeUnit) {
		cooldowns.put(key, new Cooldown(duration, timeUnit));
	}
	
	private Cooldown getCooldown(K key) {
		return cooldowns.get(key);
	}
	
	public boolean hasCooldown(K key) {
		Cooldown cooldown = getCooldown(key);
		return cooldown != null && !cooldown.isExpired();
	}
	
	public long getRemainingTime(K key) {
		Cooldown cooldown = getCooldown(key);
		return cooldown != null ? cooldown.getRemainingTime() : 0;
	}
	
	public String getRemainingTimeFormatted(K key) {
		Cooldown cooldown = getCooldown(key);
		return cooldown != null ? TimeFormatter.formatDuration(cooldown.getRemainingTime()) : "0s";
	}
	
	public void clearCooldown(K key) {
		cooldowns.remove(key);
	}
	
	public void cleanup() {
		cooldowns.entrySet().removeIf(entry -> entry.getValue().isExpired());
	}
}