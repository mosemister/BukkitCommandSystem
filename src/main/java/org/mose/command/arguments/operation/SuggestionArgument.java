package org.mose.command.arguments.operation;

import org.mose.command.CommandArgument;
import org.mose.command.CommandArgumentResult;
import org.mose.command.ParseCommandArgument;
import org.mose.command.context.CommandArgumentContext;
import org.mose.command.context.CommandContext;

import java.io.IOException;

public abstract class SuggestionArgument<A> implements CommandArgument<A> {

    protected final ParseCommandArgument<A> argument;
    protected final String id;

    public SuggestionArgument(CommandArgument<A> argument) {
        this(argument.getId(), argument);
    }

    public SuggestionArgument(String id, ParseCommandArgument<A> argument) {
        this.argument = argument;
        this.id = id;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public CommandArgumentResult<A> parse(CommandContext context, CommandArgumentContext<A> argument) throws IOException {
        return this.argument.parse(context, argument);
    }
}
