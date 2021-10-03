package org.mose.command.arguments.operation;

import org.jetbrains.annotations.NotNull;
import org.mose.command.CommandArgument;
import org.mose.command.CommandArgumentResult;
import org.mose.command.context.CommandArgumentContext;
import org.mose.command.context.CommandContext;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class FlatRemainingArgument<T> implements CommandArgument<List<T>> {

    private final @NotNull String id;
    private final @NotNull List<CommandArgument<? extends Collection<T>>> argument;

    @Deprecated
    public FlatRemainingArgument(@NotNull String ignored) {
        throw new RuntimeException("Flat Remaining Argument requires at least one argument");
    }

    public FlatRemainingArgument(@NotNull CommandArgument<? extends Collection<T>> argument) {
        this(argument.getId(), argument);
    }

    @SafeVarargs
    public FlatRemainingArgument(@NotNull String id, @NotNull CommandArgument<? extends Collection<T>>... argument) {
        this(id, Arrays.asList(argument));
    }

    public FlatRemainingArgument(@NotNull String id, @NotNull Collection<CommandArgument<? extends Collection<T>>> argument) {
        if (argument.isEmpty()) {
            throw new IllegalArgumentException("Remaining Argument cannot have a argument of empty");
        }
        this.id = id;
        this.argument = new ArrayList<>(argument);
    }

    private @NotNull CommandArgumentResult<? extends Collection<T>> parseAny(@NotNull CommandContext context, int B) throws IOException {
        IOException e1 = null;
        for (int A = 0; A < this.argument.size(); A++) {
            try {
                return parse(context, B, this.argument.get(A));
            } catch (IOException e) {
                if (A==0) {
                    e1 = e;
                }
            }
        }
        if (e1==null) {
            //shouldnt be possible
            throw new IOException("Unknown error occurred");
        }
        throw e1;
    }

    private <R extends Collection<T>> @NotNull CommandArgumentResult<R> parse(@NotNull CommandContext context, int B, @NotNull CommandArgument<R> argument) throws IOException {
        CommandArgumentContext<R> argumentContext = new CommandArgumentContext<>(argument, B, context.getCommand());
        return argument.parse(context, argumentContext);
    }

    @Override
    public @NotNull String getId() {
        return this.id;
    }

    @Override
    public @NotNull CommandArgumentResult<List<T>> parse(@NotNull CommandContext context, @NotNull CommandArgumentContext<List<T>> argument) throws IOException {
        int A = argument.getFirstArgument();
        List<T> list = new ArrayList<>();
        while (A < context.getCommand().length) {
            CommandArgumentResult<? extends Collection<T>> entry = parseAny(context, A);
            A = entry.getPosition();
            list.addAll(entry.getValue());
        }
        return new CommandArgumentResult<>(A, list);
    }

    @Override
    public @NotNull Set<String> suggest(@NotNull CommandContext context, @NotNull CommandArgumentContext<List<T>> argument) {
        int A = argument.getFirstArgument();
        while (A < context.getCommand().length) {
            final int B = A;
            CommandArgumentResult<? extends Collection<T>> entry;
            try {
                entry = parseAny(context, A);
            } catch (IOException e) {
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
        CommandArgumentContext<R> argumentContext = new CommandArgumentContext<>(arg, A, context.getCommand());
        return arg.suggest(context, argumentContext);
    }
}
