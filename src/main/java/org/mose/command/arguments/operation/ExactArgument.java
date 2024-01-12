package org.mose.command.arguments.operation;

import org.jetbrains.annotations.NotNull;
import org.mose.command.CommandArgument;
import org.mose.command.CommandArgumentResult;
import org.mose.command.context.ArgumentContext;
import org.mose.command.context.CommandContext;
import org.mose.command.exception.ArgumentException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The exact argument is used as a string argument where there is only one single possible
 * argument. This is useful for child commands
 */
public class ExactArgument implements CommandArgument<String> {

    private final @NotNull String id;
    private final @NotNull String[] lookup;
    private final boolean caseSens;

    /**
     * A quick way to use the provided id as the exact argument without being case sensitive
     *
     * @param id The Id and lookup of the command
     */
    public ExactArgument(@NotNull String id) {
        this(id, false, id);
    }

    /**
     * This tells developers that a lookup should be defined
     *
     * @param id       ignored
     * @param caseSens ignored
     * @throws RuntimeException lookup needs to be defined
     */
    @Deprecated
    public ExactArgument(@NotNull String id, boolean caseSens) {
        throw new RuntimeException("Lookup should be defined");
    }

    /**
     * If you need to have more then one exact match or you need to make it case sensitive,
     * then use this constructor
     *
     * @param id       The Id of the command
     * @param caseSens If the check should be case sensitive
     * @param lookup   The possible lookups as a var array
     * @throws IllegalArgumentException if no lookup are provided
     */
    public ExactArgument(@NotNull String id, boolean caseSens, @NotNull String... lookup) {
        if (lookup.length == 0) {
            throw new IllegalArgumentException("Lookup cannot be []");
        }
        this.id = id;
        this.lookup = lookup;
        this.caseSens = caseSens;
    }

    /**
     * Gets all possible lookups
     *
     * @return Gets all possible lookups as a array
     */
    public @NotNull String[] getLookup() {
        return this.lookup;
    }

    @Override
    public @NotNull String getId() {
        return this.id;
    }

    private boolean anyMatch(@NotNull String arg) {
        return Arrays.stream(this.lookup)
                .anyMatch(a -> (this.caseSens && a.equals(arg)) || (!this.caseSens && a.equalsIgnoreCase(arg)));
    }

    @Override
    public @NotNull CommandArgumentResult<String> parse(@NotNull CommandContext context, @NotNull ArgumentContext argument) throws ArgumentException {
        String arg = argument.getFocusArgument();
        if (anyMatch(arg)) {
            return CommandArgumentResult.from(argument, arg);
        }
        throw new ArgumentException("Unknown argument of '" + arg + "'");
    }

    @Override
    public @NotNull Set<String> suggest(@NotNull CommandContext context, @NotNull ArgumentContext argument) {
        String arg = "";
        if (context.getCommand().length > argument.getArgumentIndex()) {
            arg = argument.getFocusArgument();
        }
        Set<String> args = new HashSet<>();
        for (String look : this.lookup) {
            if (look.toLowerCase().startsWith(arg.toLowerCase())) {
                args.add(look);
            }
        }
        return args;
    }

    @Override
    public @NotNull String getUsage() {
        return "<" + Stream.of(this.lookup).map(t -> "\"" + t + "\"").collect(Collectors.joining()) + ">";
    }
}
