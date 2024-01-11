package org.mose.command.context;

import org.jetbrains.annotations.NotNull;
import org.mose.command.CommandArgument;

public class ArgumentCommandContext<T> extends ArgumentContext {

    private final @NotNull CommandArgument<T> argument;

    public ArgumentCommandContext(@NotNull CommandArgument<T> argument, int targetArgument, String... rawCommand) {
        super(targetArgument, rawCommand);
        this.argument = argument;
    }

    public @NotNull CommandArgument<T> getArgument() {
        return this.argument;
    }
}
