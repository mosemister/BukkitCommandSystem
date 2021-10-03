package org.mose.command.arguments.simple;

import org.jetbrains.annotations.NotNull;
import org.mose.command.CommandArgument;
import org.mose.command.CommandArgumentResult;
import org.mose.command.context.CommandArgumentContext;
import org.mose.command.context.CommandContext;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

public class StringArgument implements CommandArgument<String> {

    private final @NotNull String id;

    public StringArgument(@NotNull String id) {
        this.id = id;
    }

    @Override
    public @NotNull String getId() {
        return this.id;
    }

    @Override
    public @NotNull CommandArgumentResult<String> parse(@NotNull CommandContext context, @NotNull CommandArgumentContext<String> argument) throws IOException {
        String text = context.getCommand()[argument.getFirstArgument()];
        return CommandArgumentResult.from(argument, text);

    }

    @Override
    public @NotNull Set<String> suggest(@NotNull CommandContext commandContext, @NotNull CommandArgumentContext<String> argument) {
        return Collections.emptySet();
    }
}
