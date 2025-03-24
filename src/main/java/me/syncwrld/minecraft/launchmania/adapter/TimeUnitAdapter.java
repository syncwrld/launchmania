package me.syncwrld.minecraft.launchmania.adapter;

import java.util.concurrent.TimeUnit;

public class TimeUnitAdapter {

    public TimeUnit resolve(String timeUnit) {
        return TimeUnit.valueOf(timeUnit.toUpperCase());
    }

    public String adapt(TimeUnit timeUnit) {
        return timeUnit.toString().toUpperCase();
    }

}
