package org.mose.command.arguments.simple.number;

import org.jetbrains.annotations.NotNull;
import org.mose.command.CommandArgument;
import org.mose.command.CommandArgumentResult;
import org.mose.command.context.CommandArgumentContext;
import org.mose.command.context.CommandContext;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

/**
 * Gets a single int value from user input.
 */
public record IntegerArgument(@NotNull String id) implements CommandArgument<Integer> {

    @Override
    public @NotNull
    String getId() {
        return this.id;
    }

    @Override
    public @NotNull
    CommandArgumentResult<Integer> parse(@NotNull CommandContext context, @NotNull CommandArgumentContext<Integer> argument) throws IOException {
        try {
            return CommandArgumentResult.from(argument, Integer.parseInt(context.getCommand()[argument.getFirstArgument()]));
        } catch (NumberFormatException e) {
            throw new IOException("'" + context.getCommand()[argument.getFirstArgument()] + "' is not a number");
        }
    }

    @Override
    public @NotNull
    Set<String> suggest(@NotNull CommandContext commandContext, @NotNull CommandArgumentContext<Integer> argument) {
        return Collections.emptySet();
    }
}
