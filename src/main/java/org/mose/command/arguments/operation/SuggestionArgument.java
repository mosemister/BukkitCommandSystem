package org.mose.command.arguments.operation;

import org.jetbrains.annotations.NotNull;
import org.mose.command.CommandArgument;
import org.mose.command.CommandArgumentResult;
import org.mose.command.ParseCommandArgument;
import org.mose.command.context.ArgumentContext;
import org.mose.command.context.CommandContext;
import org.mose.command.exception.ArgumentException;

/**
 * This allows you to give extra suggestions to a command argument.
 *
 * @param <A> The return type
 */
public abstract class SuggestionArgument<A> implements CommandArgument<A> {

    protected final @NotNull ParseCommandArgument<A> argument;
    protected final @NotNull String id;

    public SuggestionArgument(@NotNull CommandArgument<A> argument) {
        this(argument.getId(), argument);
    }

    public SuggestionArgument(@NotNull String id, @NotNull ParseCommandArgument<A> argument) {
        this.argument = argument;
        this.id = id;
    }

    @Override
    public @NotNull String getId() {
        return this.id;
    }

    @Override
    public @NotNull CommandArgumentResult<A> parse(@NotNull CommandContext context, @NotNull ArgumentContext argument) throws ArgumentException {
        return this.argument.parse(context, argument);
    }
}
