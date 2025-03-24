package me.syncwrld.minecraft.launchmania.adapter;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.List;
import java.util.stream.Collectors;

public class LocationAdapter {

    public List<String> adapt(List<Location> locations) {
        return locations.stream().map(this::serialize).collect(Collectors.toList());
    }

    public List<Location> resolve(List<String> keys) {
        return keys.stream().map(this::deserialize).collect(Collectors.toList());
    }

    private String serialize(Location location) {
        return location.getWorld().getName() + ":" +
                location.getBlockX() + ":" + location.getBlockY() + ":" +
                location.getBlockZ();
    }

    private Location deserialize(String key) {
        String[] parts = key.split(":");
        return new Location(
                Bukkit.getWorld(parts[0]),
                Integer.parseInt(parts[1]),
                Integer.parseInt(parts[2]),
                Integer.parseInt(parts[3])
        );
    }

}
