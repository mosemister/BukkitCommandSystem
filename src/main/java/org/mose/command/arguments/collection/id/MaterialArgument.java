package org.mose.command.arguments.collection.id;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;

/**
 * Gets a single Material from a single string argument
 */
public class MaterialArgument extends IdentifiableArgument<Material> {

    public MaterialArgument(@NotNull String id, Predicate<Material> material){
        super(id, material);
    }

    public MaterialArgument(@NotNull String id) {
        super(id);
    }

    @Override
    public @NotNull Collection<Material> getAll() {
        return Arrays.asList(Material.values());
    }
}
