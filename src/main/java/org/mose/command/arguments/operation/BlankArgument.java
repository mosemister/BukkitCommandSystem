package org.mose.command.arguments.operation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mose.command.CommandArgument;
import org.mose.command.CommandArgumentResult;
import org.mose.command.context.ArgumentContext;
import org.mose.command.context.CommandContext;

import java.util.Collection;
import java.util.Collections;

public class BlankArgument<T> implements CommandArgument<T> {

    private final @NotNull String id;
    private final @Nullable T value;

    public BlankArgument(@NotNull String id) {
        this(id, null);
    }

    public BlankArgument(@NotNull String id, @Nullable T value) {
        this.id = id;
        this.value = value;
    }

    @Override
    public @NotNull String getId() {
        return this.id;
    }

    @Override
    public @NotNull CommandArgumentResult<T> parse(@NotNull CommandContext context,
                                                   @NotNull ArgumentContext argument) {
        return CommandArgumentResult.from(argument, 0, this.value);
    }

    @Override
    public @NotNull Collection<String> suggest(@NotNull CommandContext commandContext,
                                               @NotNull ArgumentContext argument) {
        return Collections.emptySet();
    }
}
