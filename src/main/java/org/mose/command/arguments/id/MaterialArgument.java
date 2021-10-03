package org.mose.command.arguments.id;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;

/**
 * Gets a single BlockType from a single string argument
 */
public class MaterialArgument extends IdentifiableArgument<Material> {

    public MaterialArgument(@NotNull String id) {
        super(id);
    }

    @Override
    public @NotNull Collection<Material> getAll() {
        return Arrays.asList(Material.values());
    }
}
