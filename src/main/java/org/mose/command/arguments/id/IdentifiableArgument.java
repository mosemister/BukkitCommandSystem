package org.mose.command.arguments.id;

import org.bukkit.Keyed;
import org.mose.command.CommandArgument;
import org.mose.command.CommandArgumentResult;
import org.mose.command.context.CommandArgumentContext;
import org.mose.command.context.CommandContext;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Gets a single identifiable object from the collection provided by {@link #getAll()}
 *
 * @param <I> The return class type of the argument
 */
public abstract class IdentifiableArgument<I extends Keyed> implements CommandArgument<I> {

    private final String id;

    public IdentifiableArgument(String id) {
        this.id = id;
    }

    /**
     * Gets all possible values that the argument could be
     *
     * @return A collection of all possible values
     */
    public abstract Collection<I> getAll();

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public CommandArgumentResult<I> parse(CommandContext context, CommandArgumentContext<I> argument) throws IOException {
        String id = context.getCommand()[argument.getFirstArgument()];
        Optional<I> opIdent = this.getAll().stream().filter(a -> a.getKey().toString().equalsIgnoreCase(id)).findAny();
        if (!opIdent.isPresent()) {
            throw new IOException("Invalid ID of '" + id + "'");
        }
        return CommandArgumentResult.from(argument, opIdent.get());
    }

    @Override
    public Set<String> suggest(CommandContext context, CommandArgumentContext<I> argument) {
        String id = context.getCommand()[argument.getFirstArgument()];
        return this.getAll()
                .stream()
                .filter(a -> a.getKey().toString().toLowerCase().startsWith(id.toLowerCase()) || a.getKey().getKey().toLowerCase().startsWith(id.toLowerCase()))
                .map(a -> a.getKey().toString())
                .collect(Collectors.toSet());
    }
}
