package org.mose.command.arguments.operation;

import org.jetbrains.annotations.NotNull;
import org.mose.command.CommandArgument;
import org.mose.command.CommandArgumentResult;
import org.mose.command.context.ArgumentCommandContext;
import org.mose.command.context.ArgumentContext;
import org.mose.command.context.CommandArgumentContext;
import org.mose.command.context.CommandContext;
import org.mose.command.exception.ArgumentException;

import java.io.IOException;
import java.util.Collection;
import java.util.function.Function;

/**
 * Used for mapping one argument to another. This is useful when a argument depends upon another argument
 *
 * @param <T> The type being mapped to
 * @param <J> The type being mapped from
 */
public record MappedArgumentWrapper<T, J>(@NotNull CommandArgument<J> commandArgument,
                                          @NotNull Function<J, T> convert) implements CommandArgument<T> {

    @Override
    public @NotNull
    String getId() {
        return this.commandArgument.getId();
    }

    @Override
    public @NotNull
    CommandArgumentResult<T> parse(@NotNull CommandContext context, @NotNull ArgumentContext argument) throws ArgumentException {
        ArgumentCommandContext<J> argContext = new ArgumentCommandContext<>(this.commandArgument, argument.getArgumentIndex(), context.getCommand());
        CommandArgumentResult<J> entry = this.commandArgument.parse(context, argument);
        return new CommandArgumentResult<>(entry.getPosition(), this.convert.apply(entry.getValue()));
    }

    @Override
    public @NotNull
    Collection<String> suggest(@NotNull CommandContext context, @NotNull ArgumentContext argument) {
        ArgumentCommandContext<J> argContext = new ArgumentCommandContext<>(this.commandArgument, argument.getArgumentIndex(), context.getCommand());
        return this.commandArgument.suggest(context, argContext);
    }
}
