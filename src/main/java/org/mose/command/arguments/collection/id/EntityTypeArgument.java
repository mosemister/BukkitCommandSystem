package org.mose.command.arguments.collection.id;

import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.EnumSet;
import java.util.function.Predicate;

public class EntityTypeArgument extends IdentifiableArgument<EntityType> {

    public EntityTypeArgument(@NotNull String id) {
        super(id);
    }

    public EntityTypeArgument(@NotNull String id, @NotNull Predicate<EntityType> filter) {
        super(id, filter);
    }

    @Override
    public @NotNull Collection<EntityType> getAll() {
        return EnumSet.allOf(EntityType.class);
    }
}
