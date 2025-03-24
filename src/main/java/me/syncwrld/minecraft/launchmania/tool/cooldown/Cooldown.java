package me.syncwrld.minecraft.launchmania.tool.cooldown;

import java.util.concurrent.TimeUnit;

public class Cooldown {
    private final long duration;
    private final TimeUnit timeUnit;
    private final long startTime;

    public Cooldown(long duration, TimeUnit timeUnit) {
        this.duration = duration;
        this.timeUnit = timeUnit;
        this.startTime = System.currentTimeMillis();
    }

    public long getRemainingTime() {
        long elapsedTime = System.currentTimeMillis() - startTime;
        long remaining = timeUnit.toMillis(duration) - elapsedTime;
        return Math.max(remaining, 0);
    }

    public boolean isExpired() {
        return getRemainingTime() <= 0;
    }
}