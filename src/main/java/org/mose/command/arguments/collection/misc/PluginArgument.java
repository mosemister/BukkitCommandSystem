package org.mose.command.arguments.collection.misc;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.mose.command.arguments.operation.AnyArgument;

/**
 * Gives the user a chose of plugins
 */
public class PluginArgument extends AnyArgument<Plugin> {
    public PluginArgument(@NotNull String id) {
        super(
                id,
                Plugin::getName,
                (plugins, pluginName) -> plugins
                        .stream()
                        .filter(plugin -> plugin.getName().equals(pluginName))
                        .findAny()
                        .orElse(null),
                Bukkit.getPluginManager().getPlugins());
    }
}
