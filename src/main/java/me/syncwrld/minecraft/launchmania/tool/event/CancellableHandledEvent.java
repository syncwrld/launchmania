package me.syncwrld.minecraft.launchmania.tool.event;

import org.bukkit.event.Cancellable;

public class CancellableHandledEvent extends HandledEvent implements Cancellable {
	
	public boolean cancelled = false;
	
	@Override
	public boolean isCancelled() {
		return cancelled;
	}
	
	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
	
}
