package org.mose.command.arguments.simple;

import org.jetbrains.annotations.NotNull;
import org.mose.command.CommandArgument;
import org.mose.command.CommandArgumentResult;
import org.mose.command.context.CommandArgumentContext;
import org.mose.command.context.CommandContext;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class BooleanArgument implements CommandArgument<Boolean> {

    private final @NotNull String id;

    public BooleanArgument(@NotNull String id) {
        this.id = id;
    }

    @Override
    public @NotNull String getId() {
        return this.id;
    }

    @Override
    public @NotNull CommandArgumentResult<Boolean> parse(@NotNull CommandContext context, @NotNull CommandArgumentContext<Boolean> argument) throws IOException {
        String arg = context.getCommand()[argument.getFirstArgument()];
        if (arg.equals("true")) {
            return CommandArgumentResult.from(argument, true);
        }
        if (arg.equals("false")) {
            return CommandArgumentResult.from(argument, false);
        }
        throw new IOException("'" + arg + "' is not either 'true' or 'false'");
    }

    @Override
    public @NotNull Set<String> suggest(@NotNull CommandContext commandContext, @NotNull CommandArgumentContext<Boolean> argument) {
        String peek = commandContext.getCommand()[argument.getFirstArgument()];
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
