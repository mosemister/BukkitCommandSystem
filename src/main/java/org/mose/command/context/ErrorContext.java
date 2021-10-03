package org.mose.command.context;

import org.mose.command.ArgumentCommand;
import org.mose.command.CommandArgument;

public class ErrorContext {

    private final ArgumentCommand command;
    private final int argumentFailedAt;
    private final CommandArgument<?> argument;
    private final String error;

    public ErrorContext(ArgumentCommand command, int argumentFailedAt, CommandArgument<?> argument, String error) {
        this.command = command;
        this.argumentFailedAt = argumentFailedAt;
        this.argument = argument;
        this.error = error;
    }

    public ArgumentCommand getCommand() {
        return command;
    }

    public int getArgumentFailedAt() {
        return argumentFailedAt;
    }

    public CommandArgument<?> getArgument() {
        return argument;
    }

    public String getError() {
        return error;
    }
}
