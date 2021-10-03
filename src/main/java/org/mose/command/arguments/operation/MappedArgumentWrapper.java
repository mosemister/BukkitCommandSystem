package org.mose.command.arguments.operation;

import org.mose.command.CommandArgument;
import org.mose.command.CommandArgumentResult;
import org.mose.command.context.CommandArgumentContext;
import org.mose.command.context.CommandContext;

import java.io.IOException;
import java.util.Collection;
import java.util.function.Function;

public class MappedArgumentWrapper<T, J> implements CommandArgument<T> {

    private final CommandArgument<J> commandArgument;
    private final Function<J, T> convert;

    public MappedArgumentWrapper(CommandArgument<J> commandArgument, Function<J, T> convert) {
        this.commandArgument = commandArgument;
        this.convert = convert;
    }

    @Override
    public String getId() {
        return this.commandArgument.getId();
    }

    @Override
    public CommandArgumentResult<T> parse(CommandContext context, CommandArgumentContext<T> argument) throws IOException {
        CommandArgumentContext<J> argContext = new CommandArgumentContext<>(this.commandArgument, argument.getFirstArgument(), context.getCommand());
        CommandArgumentResult<J> entry = this.commandArgument.parse(context, argContext);
        return new CommandArgumentResult<>(entry.getPosition(), this.convert.apply(entry.getValue()));
    }

    @Override
    public Collection<String> suggest(CommandContext context, CommandArgumentContext<T> argument) {
        CommandArgumentContext<J> argContext = new CommandArgumentContext<>(this.commandArgument, argument.getFirstArgument(), context.getCommand());
        return this.commandArgument.suggest(context, argContext);
    }
}
