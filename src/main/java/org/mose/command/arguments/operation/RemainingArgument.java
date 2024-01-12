package org.mose.command.arguments.operation;

import org.jetbrains.annotations.NotNull;
import org.mose.command.CommandArgument;
import org.mose.command.CommandArgumentResult;
import org.mose.command.arguments.simple.text.StringArgument;
import org.mose.command.context.ArgumentCommandContext;
import org.mose.command.context.ArgumentContext;
import org.mose.command.context.CommandContext;
import org.mose.command.exception.ArgumentException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Parses all the string arguments that have not be processed after the last successful parse.
 * This attempts the parse each string argument against a collection
 *
 * @param <T> The common type
 */
public class RemainingArgument<T> implements CommandArgument<List<T>> {

    private final @NotNull String id;
    private final @NotNull List<CommandArgument<T>> argument;

    /**
     * Do not use. provide command arguments
     *
     * @param id Ignore
     * @throws RuntimeException Tells you not to use it
     */
    @Deprecated
    public RemainingArgument(@NotNull String id) {
        throw new RuntimeException("Remaining Argument requires at least 1 argument");
    }

    /**
     * Used for wrapping a single argument as a Remaining argument. This can be great for
     * if the command requires a sentence where you use this constructor and pass a {@link StringArgument}
     *
     * @param argument The only argument to compare against
     */
    public RemainingArgument(@NotNull CommandArgument<T> argument) {
        this(argument.getId(), argument);
    }

    /**
     * Constructor
     *
     * @param id       The id of the argument
     * @param argument The arguments to compare against in var-array form
     */
    @SafeVarargs
    public RemainingArgument(@NotNull String id, @NotNull CommandArgument<T>... argument) {
        this(id, Arrays.asList(argument));
    }

    /**
     * Constructor
     *
     * @param id       The id of the argument
     * @param argument The arguments to compare against in collection form
     */
    public RemainingArgument(@NotNull String id, @NotNull Collection<CommandArgument<T>> argument) {
        if (argument.isEmpty()) {
            throw new IllegalArgumentException("Remaining Argument cannot have a argument of empty");
        }
        this.id = id;
        this.argument = new ArrayList<>(argument);
    }

    private @NotNull CommandArgumentResult<T> parseAny(@NotNull CommandContext context, int B) throws ArgumentException {
        ArgumentException e1 = null;
        for (int A = 0; A < this.argument.size(); A++) {
            try {
                ArgumentContext argumentContext = new ArgumentCommandContext<>(this.argument.get(A), B, context.getCommand());
                return this.argument.get(A).parse(context, argumentContext);
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

    @Override
    public @NotNull String getId() {
        return this.id;
    }

    @Override
    public @NotNull CommandArgumentResult<List<T>> parse(@NotNull CommandContext context, @NotNull ArgumentContext argument) throws ArgumentException {
        int A = argument.getArgumentIndex();
        List<T> list = new ArrayList<>();
        while (A < context.getCommand().length) {
            CommandArgumentResult<T> entry = parseAny(context, A);
            A = entry.getPosition();
            list.add(entry.getValue());
        }
        return new CommandArgumentResult<>(A, list);
    }

    @Override
    public @NotNull Collection<String> suggest(@NotNull CommandContext context, @NotNull ArgumentContext argument) {
        int A = argument.getArgumentIndex();
        while (A < context.getCommand().length) {
            final int B = A;
            CommandArgumentResult<T> entry;
            try {
                entry = parseAny(context, A);
            } catch (ArgumentException e) {
                return this.argument.stream().flatMap(a -> a.suggest(context, new ArgumentCommandContext<>(a, B, context.getCommand())).stream()).collect(Collectors.toSet());
            }
            A = entry.getPosition();
        }
        return Collections.emptySet();
    }
}
