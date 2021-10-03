package org.mose.command.arguments.operation;

import org.jetbrains.annotations.NotNull;
import org.mose.command.CommandArgument;
import org.mose.command.CommandArgumentResult;
import org.mose.command.context.CommandArgumentContext;
import org.mose.command.context.CommandContext;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class RemainingArgument<T> implements CommandArgument<List<T>> {

    private final @NotNull String id;
    private final @NotNull List<CommandArgument<T>> argument;

    @Deprecated
    public RemainingArgument(@NotNull String id) {
        throw new RuntimeException("Remaining Argument requires at least 1 argument");
    }

    public RemainingArgument(@NotNull CommandArgument<T> argument) {
        this(argument.getId(), argument);
    }

    @SafeVarargs
    public RemainingArgument(@NotNull String id, @NotNull CommandArgument<T>... argument) {
        this(id, Arrays.asList(argument));
    }

    public RemainingArgument(@NotNull String id, @NotNull Collection<CommandArgument<T>> argument) {
        if (argument.isEmpty()) {
            throw new IllegalArgumentException("Remaining Argument cannot have a argument of empty");
        }
        this.id = id;
        this.argument = new ArrayList<>(argument);
    }

    private @NotNull CommandArgumentResult<T> parseAny(@NotNull CommandContext context, int B) throws IOException {
        IOException e1 = null;
        for (int A = 0; A < this.argument.size(); A++) {
            try {
                CommandArgumentContext<T> argumentContext = new CommandArgumentContext<>(this.argument.get(A), B, context.getCommand());
                return this.argument.get(A).parse(context, argumentContext);
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

    @Override
    public @NotNull String getId() {
        return this.id;
    }

    @Override
    public @NotNull CommandArgumentResult<List<T>> parse(@NotNull CommandContext context, @NotNull CommandArgumentContext<List<T>> argument) throws IOException {
        int A = argument.getFirstArgument();
        List<T> list = new ArrayList<>();
        while (A < context.getCommand().length) {
            CommandArgumentResult<T> entry = parseAny(context, A);
            A = entry.getPosition();
            list.add(entry.getValue());
        }
        return new CommandArgumentResult<>(A, list);
    }

    @Override
    public @NotNull Collection<String> suggest(@NotNull CommandContext context, @NotNull CommandArgumentContext<List<T>> argument) {
        int A = argument.getFirstArgument();
        while (A < context.getCommand().length) {
            final int B = A;
            CommandArgumentResult<T> entry;
            try {
                entry = parseAny(context, A);
            } catch (IOException e) {
                return this.argument.stream().flatMap(a -> a.suggest(context, new CommandArgumentContext<>(a, B, context.getCommand())).stream()).collect(Collectors.toSet());
            }
            A = entry.getPosition();
        }
        return Collections.emptySet();
    }
}
