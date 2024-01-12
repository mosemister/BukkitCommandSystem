package org.mose.command.arguments.simple.number;

import org.jetbrains.annotations.NotNull;
import org.mose.command.CommandArgument;
import org.mose.command.CommandArgumentResult;
import org.mose.command.context.ArgumentContext;
import org.mose.command.context.CommandContext;
import org.mose.command.exception.ArgumentException;

import java.util.Collections;
import java.util.Set;

/**
 * Gets a single double value from user input. The user can provide a whole number
 * or double value
 */
public record DoubleArgument(@NotNull String id) implements CommandArgument<Double> {

    @Override
    public @NotNull
    String getId() {
        return this.id;
    }

    @Override
    public @NotNull
    CommandArgumentResult<Double> parse(@NotNull CommandContext context, @NotNull ArgumentContext argument) throws ArgumentException {
        try {
            return CommandArgumentResult.from(argument, Double.parseDouble(context.getCommand()[argument.getArgumentIndex()]));
        } catch (NumberFormatException e) {
            throw new ArgumentException("'" + context.getCommand()[argument.getArgumentIndex()] + "' is not a number");
        }
    }

    @Override
    public @NotNull
    Set<String> suggest(@NotNull CommandContext commandContext, @NotNull ArgumentContext argument) {
        return Collections.emptySet();
    }
}
