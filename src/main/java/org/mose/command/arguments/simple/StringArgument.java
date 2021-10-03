package org.mose.command.arguments.simple;

import org.mose.command.CommandArgument;
import org.mose.command.CommandArgumentResult;
import org.mose.command.context.CommandArgumentContext;
import org.mose.command.context.CommandContext;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

public class StringArgument implements CommandArgument<String> {

    private final String id;

    public StringArgument(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public CommandArgumentResult<String> parse(CommandContext context, CommandArgumentContext<String> argument) throws IOException {
        String text = context.getCommand()[argument.getFirstArgument()];
        return CommandArgumentResult.from(argument, text);

    }

    @Override
    public Set<String> suggest(CommandContext commandContext, CommandArgumentContext<String> argument) {
        return Collections.emptySet();
    }
}
