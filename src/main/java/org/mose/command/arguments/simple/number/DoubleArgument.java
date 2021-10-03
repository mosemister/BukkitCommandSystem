package org.mose.command.arguments.simple.number;

import org.jetbrains.annotations.NotNull;
import org.mose.command.CommandArgument;
import org.mose.command.CommandArgumentResult;
import org.mose.command.context.CommandArgumentContext;
import org.mose.command.context.CommandContext;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

public class DoubleArgument implements CommandArgument<Double> {

    private final @NotNull String id;

    public DoubleArgument(@NotNull String id) {
        this.id = id;
    }

    @Override
    public @NotNull String getId() {
        return this.id;
    }

    @Override
    public @NotNull CommandArgumentResult<Double> parse(@NotNull CommandContext context, @NotNull CommandArgumentContext<Double> argument) throws IOException {
        try {
            return CommandArgumentResult.from(argument, Double.parseDouble(context.getCommand()[argument.getFirstArgument()]));
        } catch (NumberFormatException e) {
            throw new IOException("'" + context.getCommand()[argument.getFirstArgument()] + "' is not a number");
        }
    }

    @Override
    public @NotNull Set<String> suggest(@NotNull CommandContext commandContext, @NotNull CommandArgumentContext<Double> argument) {
        return Collections.emptySet();
    }
}
