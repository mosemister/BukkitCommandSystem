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

/**
 * AnyArgument is used for if you have a collection of possible acceptable arguments
 * with one of those arguments being passed to the command system
 *
 * @param <A> The type of the common object
 */
public class AnyArgument<A> implements CommandArgument<A> {

    private final @NotNull String id;
    private final @NotNull Function<A, String> toString;
    private final @NotNull BiFunction<Collection<A>, String, A> fromString;
    private final @NotNull BiFunction<CommandContext, CommandArgumentContext<A>, Collection<A>> supply;

    /**
     * Constructor
     *
     * @param id         The id of the command
     * @param toString   The conversion from the object to string that the user enters
     * @param fromString The conversion from string to the object. Return null if not present
     * @param array      The array of objects
     */
    @SafeVarargs
    public AnyArgument(@NotNull String id, @NotNull Function<A, String> toString, @NotNull BiFunction<Collection<A>, String, A> fromString, A... array) {
        this(id, toString, fromString, Arrays.asList(array));
    }

    /**
     * Constructor
     *
     * @param id         The id of the command
     * @param toString   The conversion from the object to string that the user enters
     * @param fromString The conversion from string to the object, return null if not present
     * @param collection The array of objects
     */
    public AnyArgument(@NotNull String id, @NotNull Function<A, String> toString, @NotNull BiFunction<Collection<A>, String, A> fromString, @NotNull Collection<A> collection) {
        this(id, toString, fromString, (c, a) -> collection);
    }

    /**
     * Constructor
     *
     * @param id         The id of the command
     * @param toString   The conversion from the object to string that the user enters
     * @param fromString The conversion from string to the object
     * @param supply     A BiFunction that supplies the command argument for the collection to be created just in time
     */
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
        return CommandArgumentResult.from(argument, result);
    }

    @Override
    public @NotNull Set<String> suggest(@NotNull CommandContext context, @NotNull CommandArgumentContext<A> argument) {
        String arg = context.getCommand()[argument.getFirstArgument()];
        return this.supply.apply(context, argument).stream().map(toString).filter(v -> v.toLowerCase().startsWith(arg)).collect(Collectors.toSet());
    }

    public static AnyArgument<String> buildStringList(String id, BiFunction<CommandContext, CommandArgumentContext<String>, Collection<String>> function){
        return new AnyArgument<>(id, v -> v, (collection, value) -> collection.stream().anyMatch(v -> v.equalsIgnoreCase(value)) ? value : null, function);
    }

    public static <A> AnyArgument<A> buildMappedToString(String id, BiFunction<CommandContext, CommandArgumentContext<A>, Collection<A>> function){
        return buildMapped(id, Object::toString, function);
    }

    public static <A> AnyArgument<A> buildMapped(String id, Function<A, String> toString, BiFunction<CommandContext, CommandArgumentContext<A>, Collection<A>> function){
        return new AnyArgument<>(id, toString, (collection, value) -> collection.stream().filter(v -> toString.apply(v).equals(value)).findFirst().orElse(null), function);
    }
}
