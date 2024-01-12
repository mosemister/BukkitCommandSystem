package org.mose.command.arguments.operation;

import org.jetbrains.annotations.NotNull;
import org.mose.command.CommandArgument;
import org.mose.command.CommandArgumentResult;
import org.mose.command.context.ArgumentCommandContext;
import org.mose.command.context.ArgumentContext;
import org.mose.command.context.CommandContext;
import org.mose.command.exception.ArgumentException;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The flat remaining argument is designed to get all the remaining arguments whereby the
 * argument return a list. This Flat version combines all the commands argument results into
 * a list for ease of use
 *
 * @param <T> The common type from all arguments
 */
public class FlatRemainingArgument<T> implements CommandArgument<List<T>> {

    private final @NotNull String id;
    private final @NotNull List<CommandArgument<? extends Collection<T>>> argument;

    @Deprecated
    public FlatRemainingArgument(@NotNull String ignored) {
        throw new RuntimeException("Flat Remaining Argument requires at least one argument");
    }

    /**
     * This one is mainly used with the {@link MappedArgumentWrapper}. This just wraps the
     * provided argument with the FlatRemainingArgument, maintaining the id of the provided
     * argument
     *
     * @param argument A single argument which returns a collection.
     */
    public FlatRemainingArgument(@NotNull CommandArgument<? extends Collection<T>> argument) {
        this(argument.getId(), argument);
    }

    /**
     * Constructor
     *
     * @param id       The Id of the command argument
     * @param argument The arguments to compare against in var-array form
     */
    @SafeVarargs
    public FlatRemainingArgument(@NotNull String id, @NotNull CommandArgument<? extends Collection<T>>... argument) {
        this(id, Arrays.asList(argument));
    }

    /**
     * Constructor
     *
     * @param id       The Id of the command argument
     * @param argument The argument to compare against in Collection
     */
    public FlatRemainingArgument(@NotNull String id, @NotNull Collection<CommandArgument<? extends Collection<T>>> argument) {
        if (argument.isEmpty()) {
            throw new IllegalArgumentException("Remaining Argument cannot have a argument of empty");
        }
        this.id = id;
        this.argument = new ArrayList<>(argument);
    }

    private @NotNull CommandArgumentResult<? extends Collection<T>> parseAny(@NotNull CommandContext context, int B) throws ArgumentException {
        ArgumentException e1 = null;
        for (int A = 0; A < this.argument.size(); A++) {
            try {
                return parse(context, B, this.argument.get(A));
            } catch (ArgumentException e) {
                if (A == 0) {
                    e1 = e;
                }
            }
        }
        if (e1 == null) {
            //shouldnt be possible
            throw new ArgumentException("Unknown error occurred");
        }
        throw e1;
    }

    private <R extends Collection<T>> @NotNull CommandArgumentResult<R> parse(@NotNull CommandContext context, int B, @NotNull CommandArgument<R> argument) throws ArgumentException {
        ArgumentCommandContext<R> argumentContext = new ArgumentCommandContext<>(argument, B, context.getCommand());
        return argument.parse(context, argumentContext);
    }

    @Override
    public @NotNull String getId() {
        return this.id;
    }

    @Override
    public @NotNull CommandArgumentResult<List<T>> parse(@NotNull CommandContext context, @NotNull ArgumentContext argument) throws ArgumentException {
        int A = argument.getArgumentIndex();
        List<T> list = new ArrayList<>();
        while (A < context.getCommand().length) {
            CommandArgumentResult<? extends Collection<T>> entry = parseAny(context, A);
            A = entry.getPosition();
            list.addAll(entry.getValue());
        }
        return new CommandArgumentResult<>(A, list);
    }

    @Override
    public @NotNull Set<String> suggest(@NotNull CommandContext context, @NotNull ArgumentContext argument) {
        int A = argument.getArgumentIndex();
        while (A < context.getCommand().length) {
            final int B = A;
            CommandArgumentResult<? extends Collection<T>> entry;
            try {
                entry = parseAny(context, A);
            } catch (ArgumentException e) {
                return this
                        .argument
                        .stream()
                        .flatMap(a -> suggest(context, B, a).stream())
                        .collect(Collectors.toSet());
            }
            A = entry.getPosition();
        }
        return Collections.emptySet();
    }

    private <R extends Collection<T>> @NotNull Collection<String> suggest(@NotNull CommandContext context, int A, @NotNull CommandArgument<R> arg) {
        ArgumentCommandContext<R> argumentContext = new ArgumentCommandContext<>(arg, A, context.getCommand());
        return arg.suggest(context, argumentContext);
    }
}
