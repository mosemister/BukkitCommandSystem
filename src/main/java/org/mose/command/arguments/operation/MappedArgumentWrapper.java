package org.mose.command.arguments.operation;

import org.jetbrains.annotations.NotNull;
import org.mose.command.CommandArgument;
import org.mose.command.CommandArgumentResult;
import org.mose.command.context.CommandArgumentContext;
import org.mose.command.context.CommandContext;

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

    /**
     * Constructor
     *
     * @param commandArgument The original command argument
     * @param convert         The function for mapping from one to another
     */
    public MappedArgumentWrapper(@NotNull CommandArgument<J> commandArgument, @NotNull Function<J, T> convert) {
        this.commandArgument = commandArgument;
        this.convert = convert;
    }

    @Override
    public @NotNull
    String getId() {
        return this.commandArgument.getId();
    }

    @Override
    public @NotNull
    CommandArgumentResult<T> parse(@NotNull CommandContext context, @NotNull CommandArgumentContext<T> argument) throws IOException {
        CommandArgumentContext<J> argContext = new CommandArgumentContext<>(this.commandArgument, argument.getFirstArgument(), context.getCommand());
        CommandArgumentResult<J> entry = this.commandArgument.parse(context, argContext);
        return new CommandArgumentResult<>(entry.getPosition(), this.convert.apply(entry.getValue()));
    }

    @Override
    public @NotNull
    Collection<String> suggest(@NotNull CommandContext context, @NotNull CommandArgumentContext<T> argument) {
        CommandArgumentContext<J> argContext = new CommandArgumentContext<>(this.commandArgument, argument.getFirstArgument(), context.getCommand());
        return this.commandArgument.suggest(context, argContext);
    }
}
