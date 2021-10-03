package org.mose.command.arguments.operation;

import org.jetbrains.annotations.NotNull;
import org.mose.command.CommandArgument;
import org.mose.command.CommandArgumentResult;
import org.mose.command.context.CommandArgumentContext;
import org.mose.command.context.CommandContext;

import java.io.IOException;
import java.util.Collection;
import java.util.function.Function;

public class MappedArgumentWrapper<T, J> implements CommandArgument<T> {

    private final @NotNull CommandArgument<J> commandArgument;
    private final @NotNull Function<J, T> convert;

    public MappedArgumentWrapper(@NotNull CommandArgument<J> commandArgument, @NotNull Function<J, T> convert) {
        this.commandArgument = commandArgument;
        this.convert = convert;
    }

    @Override
    public @NotNull String getId() {
        return this.commandArgument.getId();
    }

    @Override
    public @NotNull CommandArgumentResult<T> parse(@NotNull CommandContext context, @NotNull CommandArgumentContext<T> argument) throws IOException {
        CommandArgumentContext<J> argContext = new CommandArgumentContext<>(this.commandArgument, argument.getFirstArgument(), context.getCommand());
        CommandArgumentResult<J> entry = this.commandArgument.parse(context, argContext);
        return new CommandArgumentResult<>(entry.getPosition(), this.convert.apply(entry.getValue()));
    }

    @Override
    public @NotNull Collection<String> suggest(@NotNull CommandContext context, @NotNull CommandArgumentContext<T> argument) {
        CommandArgumentContext<J> argContext = new CommandArgumentContext<>(this.commandArgument, argument.getFirstArgument(), context.getCommand());
        return this.commandArgument.suggest(context, argContext);
    }
}
