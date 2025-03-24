package me.syncwrld.minecraft.launchmania.tool.event;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class HandledEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public <E extends HandledEvent> E call() {
        Bukkit.getPluginManager().callEvent(this);
        return (E) this;
    }

}
