package org.mose.command.arguments.collection.id;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Gets a block group from a single string argument
 */
public class TagArgument extends IdentifiableArgument<Tag<Material>> {

    public TagArgument(@NotNull String id) {
        super(id);
    }

    @Override
    public @NotNull Collection<Tag<Material>> getAll() {
        return Stream
                .of(Tag.class.getFields())
                .filter(f -> f.getType().isAssignableFrom(Tag.class))
                .filter(f -> Modifier.isStatic(f.getModifiers()))
                .filter(f -> Modifier.isPublic(f.getModifiers()))
                .map(f -> {
                    try {
                        return (Tag<?>) f.get(null);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        //noinspection ReturnOfNull
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .filter(tag -> tag.getValues().isEmpty())
                .filter(tag -> tag.getValues().iterator().next() instanceof Material)
                .map(tag -> (Tag<Material>) tag)
                .collect(Collectors.toSet());
    }
}
