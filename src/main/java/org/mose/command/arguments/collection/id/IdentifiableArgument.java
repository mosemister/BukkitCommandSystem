package org.mose.command.arguments.collection.id;

import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.mose.command.CommandArgument;
import org.mose.command.CommandArgumentResult;
import org.mose.command.context.ArgumentContext;
import org.mose.command.context.CommandContext;
import org.mose.command.exception.ArgumentException;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Gets a single identifiable object from the collection provided by {@link #getAll()}
 *
 * @param <I> The return class type of the argument
 */
public abstract class IdentifiableArgument<I extends Keyed> implements CommandArgument<I> {

    private final @NotNull String id;
    private final @NotNull Predicate<I> predicate;

    public IdentifiableArgument(@NotNull String id) {
        this(id, (key) -> true);
    }

    public IdentifiableArgument(@NotNull String id, @NotNull Predicate<I> filter) {
        this.id = id;
        this.predicate = filter;
    }

    /**
     * Gets all possible values that the argument could be
     *
     * @return A collection of all possible values
     */
    public abstract @NotNull Collection<I> getAll();

    @Override
    public @NotNull String getId() {
        return this.id;
    }

    @Override
    public @NotNull CommandArgumentResult<I> parse(@NotNull CommandContext context, @NotNull ArgumentContext argument) throws ArgumentException {
        String id = argument.getFocusArgument();
        Optional<I> opIdent = this
                .getAll()
                .stream()
                .filter(this.predicate)
                .filter(a -> a.getKey().toString().equalsIgnoreCase(id))
                .findAny();
        if (opIdent.isEmpty()) {
            throw new ArgumentException("Invalid ID of '" + id + "'");
        }
        return CommandArgumentResult.from(argument, opIdent.get());
    }

    @Override
    public @NotNull Set<String> suggest(@NotNull CommandContext context, @NotNull ArgumentContext argument) {
        String id = argument.getFocusArgument();
        return this.getAll()
                .stream()
                .filter(this.predicate)
                .map(Keyed::getKey)
                .filter(a ->
                        a
                                .getKey()
                                .toLowerCase()
                                .startsWith(id.toLowerCase())
                                ||
                                a
                                        .toString()
                                        .toLowerCase()
                                        .startsWith(id.toLowerCase()))
                .map(NamespacedKey::toString)
                .collect(Collectors.toSet());
    }
}
