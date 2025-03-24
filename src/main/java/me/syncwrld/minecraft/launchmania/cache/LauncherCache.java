package me.syncwrld.minecraft.launchmania.cache;

import me.syncwrld.minecraft.launchmania.tool.config.YAML;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LauncherCache {

    private final Set<Location> locations = new HashSet<>();
    private final Set<String> locationKeys = new HashSet<>();

    public void insert(Location location) {
        locationKeys.add(toKey(location));
        locations.add(location);
    }

    public boolean contains(Location location) {
        return locationKeys.contains(toKey(location));
    }

    public void remove(Location location) {
        locationKeys.remove(toKey(location));
        locations.remove(location);
    }

    public String toKey(Location loc) {
        return loc.getWorld().getName() + ":" + loc.getBlockX() + ":" + loc.getBlockY() + ":" + loc.getBlockZ();
    }

    public Set<String> keys() {
        return locationKeys;
    }

    public Set<Location> values() {
        return locations;
    }

    public void save(YAML yaml) {
        List<String> listLocations = new ArrayList<>(this.locationKeys);
        yaml.set("launchers", listLocations);
        yaml.save();
        yaml.reload();
    }

}
