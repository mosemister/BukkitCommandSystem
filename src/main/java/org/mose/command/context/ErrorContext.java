package org.mose.command.context;

import org.jetbrains.annotations.NotNull;
import org.mose.command.ArgumentCommand;
import org.mose.command.CommandArgument;

public record ErrorContext(@NotNull ArgumentCommand command, int argumentFailedAt,
                           @NotNull CommandArgument<?> argument, @NotNull String error) {

    public @NotNull ArgumentCommand getCommand() {
        return command;
    }

    public int getArgumentFailedAt() {
        return argumentFailedAt;
    }

    public @NotNull CommandArgument<?> getArgument() {
        return argument;
    }

    public @NotNull String getError() {
        return error;
    }
}
