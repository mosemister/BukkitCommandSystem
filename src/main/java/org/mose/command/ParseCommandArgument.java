package org.mose.command;

import org.mose.command.context.CommandArgumentContext;
import org.mose.command.context.CommandContext;

import java.io.IOException;

/**
 * The basic interface for parsing a {@link CommandArgument}.
 * Some command arguments require getters for values whereby the getter requires {@link CommandContext} and {@link CommandArgumentContext} resulting in this function being handy as a lamda
 *
 * @param <T> The returning class type
 */
public interface ParseCommandArgument<T> {

    CommandArgumentResult<T> parse(CommandContext context, CommandArgumentContext<T> argument) throws IOException;

}
