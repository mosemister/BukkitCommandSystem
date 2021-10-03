package org.mose.command.arguments.id;

import org.bukkit.Material;

import java.util.Arrays;
import java.util.Collection;

/**
 * Gets a single BlockType from a single string argument
 */
public class MaterialArgument extends IdentifiableArgument<Material> {

    public MaterialArgument(String id) {
        super(id);
    }

    @Override
    public Collection<Material> getAll() {
        return Arrays.asList(Material.values());
    }
}
