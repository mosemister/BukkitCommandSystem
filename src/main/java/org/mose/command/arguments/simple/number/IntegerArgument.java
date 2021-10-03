package org.mose.command.arguments.simple.number;

import org.mose.command.CommandArgument;
import org.mose.command.CommandArgumentResult;
import org.mose.command.context.CommandArgumentContext;
import org.mose.command.context.CommandContext;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

public class IntegerArgument implements CommandArgument<Integer> {

    private final String id;

    public IntegerArgument(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public CommandArgumentResult<Integer> parse(CommandContext context, CommandArgumentContext<Integer> argument) throws IOException {
        try {
            return CommandArgumentResult.from(argument, Integer.parseInt(context.getCommand()[argument.getFirstArgument()]));
        } catch (NumberFormatException e) {
            throw new IOException("'" + context.getCommand()[argument.getFirstArgument()] + "' is not a number");
        }
    }

    @Override
    public Set<String> suggest(CommandContext commandContext, CommandArgumentContext<Integer> argument) {
        return Collections.emptySet();
    }
}
