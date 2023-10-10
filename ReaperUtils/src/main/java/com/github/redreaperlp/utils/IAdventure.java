package com.github.redreaperlp.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class IAdventure extends JavaPlugin {
    private final Component prefix;

    protected IAdventure(Component prefix) {
        this.prefix = prefix;
    }

    public Component getPrefix() {
        return prefix;
    }
}
