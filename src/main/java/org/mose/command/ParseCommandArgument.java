package org.mose.command;

import org.jetbrains.annotations.NotNull;
import org.mose.command.context.ArgumentCommandContext;
import org.mose.command.context.ArgumentContext;
import org.mose.command.context.CommandArgumentContext;
import org.mose.command.context.CommandContext;
import org.mose.command.exception.ArgumentException;

import java.io.IOException;

/**
 * The basic interface for parsing a {@link CommandArgument}.
 * Some command arguments require getters for values whereby the getter requires {@link CommandContext} and {@link CommandArgumentContext} resulting in this
 * function being handy as a lamda
 *
 * @param <T> The returning class type
 */
public interface ParseCommandArgument<T> {

    /**
     * Parses the command argument
     *
     * @param context  The context of the command
     * @param argument The argument context from the command
     * @return A CommandArgumentResult of the argument
     * @throws IOException if the argument cannot be processed, then it will throw a IOException of what went wrong
     * @deprecated Use the ArgumentContext edition
     */
    @Deprecated(forRemoval = true)
    default @NotNull CommandArgumentResult<T> parse(@NotNull CommandContext context, @NotNull CommandArgumentContext<T> argument) throws IOException {
        try {
            return parse(context, new ArgumentCommandContext<>(argument.getArgument(), argument.getFirstArgument(), context.getCommand()));
        } catch (ArgumentException e) {
            throw new IOException(e);
        }
    }

    @NotNull CommandArgumentResult<T> parse(@NotNull CommandContext context, @NotNull ArgumentContext argument) throws ArgumentException;

}
