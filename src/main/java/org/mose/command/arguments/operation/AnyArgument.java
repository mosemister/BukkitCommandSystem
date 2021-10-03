package org.mose.command.arguments.operation;

import org.jetbrains.annotations.NotNull;
import org.mose.command.CommandArgument;
import org.mose.command.CommandArgumentResult;
import org.mose.command.context.CommandArgumentContext;
import org.mose.command.context.CommandContext;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AnyArgument<A> implements CommandArgument<A> {

    private final @NotNull String id;
    private final @NotNull Function<A, String> toString;
    private final @NotNull BiFunction<Collection<A>, String, A> fromString;
    private final @NotNull BiFunction<CommandContext, CommandArgumentContext<A>, Collection<A>> supply;

    @SafeVarargs
    public AnyArgument(@NotNull String id, @NotNull Function<A, String> toString, @NotNull BiFunction<Collection<A>, String, A> fromString, A... array) {
        this(id, toString, fromString, Arrays.asList(array));
    }

    public AnyArgument(@NotNull String id, @NotNull Function<A, String> toString, @NotNull BiFunction<Collection<A>, String, A> fromString, @NotNull Collection<A> collection) {
        this(id, toString, fromString, (c, a) -> collection);
    }

    public AnyArgument(@NotNull String id, @NotNull Function<A, String> toString, @NotNull BiFunction<Collection<A>, String, A> fromString, @NotNull BiFunction<CommandContext, CommandArgumentContext<A>, @NotNull Collection<A>> supply) {
        this.id = id;
        this.toString = toString;
        this.fromString = fromString;
        this.supply = supply;
    }

    @Override
    public @NotNull String getId() {
        return this.id;
    }

    @Override
    public @NotNull CommandArgumentResult<A> parse(@NotNull CommandContext context, @NotNull CommandArgumentContext<A> argument) throws IOException {
        String arg = context.getCommand()[argument.getFirstArgument()];
        A result = this.fromString.apply(this.supply.apply(context, argument), arg);
        if (result==null) {
            throw new IOException("Unknown value of " + arg);
        }
        return CommandArgumentResult.from(argument, 0, result);
    }

    @Override
    public @NotNull Set<String> suggest(@NotNull CommandContext context, @NotNull CommandArgumentContext<A> argument) {
        String arg = context.getCommand()[argument.getFirstArgument()];
        return this.supply.apply(context, argument).stream().map(toString).filter(v -> v.toLowerCase().startsWith(arg)).collect(Collectors.toSet());
    }
}
