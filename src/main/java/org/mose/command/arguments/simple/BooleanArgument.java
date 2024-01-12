package org.mose.command.arguments.simple;

import org.jetbrains.annotations.NotNull;
import org.mose.command.CommandArgument;
import org.mose.command.CommandArgumentResult;
import org.mose.command.context.ArgumentContext;
import org.mose.command.context.CommandContext;
import org.mose.command.exception.ArgumentException;

import java.util.HashSet;
import java.util.Set;

/**
 * Provides a boolean argument to the user. This suggests both true and false
 */
public record BooleanArgument(@NotNull String id) implements CommandArgument<Boolean> {

    @Override
    public @NotNull
    String getId() {
        return this.id;
    }

    @Override
    public @NotNull
    CommandArgumentResult<Boolean> parse(@NotNull CommandContext context, @NotNull ArgumentContext argument) throws ArgumentException {
        String arg = argument.getFocusArgument();
        if (arg.equals("true")) {
            return CommandArgumentResult.from(argument, true);
        }
        if (arg.equals("false")) {
            return CommandArgumentResult.from(argument, false);
        }
        throw new ArgumentException("'" + arg + "' is not either 'true' or 'false'");
    }

    @Override
    public @NotNull
    Set<String> suggest(@NotNull CommandContext commandContext, @NotNull ArgumentContext argument) {
        String peek = argument.getFocusArgument();
        Set<String> list = new HashSet<>();
        if ("true".startsWith(peek.toLowerCase())) {
            list.add("true");
        }
        if ("false".startsWith(peek.toLowerCase())) {
            list.add("false");
        }
        return list;
    }
}
