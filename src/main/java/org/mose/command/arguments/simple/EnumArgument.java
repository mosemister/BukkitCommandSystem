package org.mose.command.arguments.simple;

import org.jetbrains.annotations.NotNull;
import org.mose.command.CommandArgument;
import org.mose.command.CommandArgumentResult;
import org.mose.command.context.ArgumentContext;
import org.mose.command.context.CommandContext;
import org.mose.command.exception.ArgumentException;

import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Provides a chose of values from enum class
 *
 * @param <E> The enum type
 */
public record EnumArgument<E extends Enum<E>>(@NotNull String id,
                                              @NotNull EnumSet<E> enumSet) implements CommandArgument<E> {

    public EnumArgument(String id, Class<E> enumClass) {
        this(id, EnumSet.allOf(enumClass));
    }

    @Override
    public @NotNull
    String getId() {
        return this.id;
    }

    @Override
    public @NotNull
    CommandArgumentResult<E> parse(@NotNull CommandContext context, @NotNull ArgumentContext argument) throws ArgumentException {
        String next = argument.getFocusArgument();
        Optional<E> opValue = enumSet.stream().filter(n -> n.name().equalsIgnoreCase(next)).findFirst();
        if (opValue.isPresent()) {
            return CommandArgumentResult.from(argument, opValue.get());
        }
        throw new ArgumentException("Unknown value of '" + next + "' in argument " + this.getUsage());
    }

    @Override
    public @NotNull Set<String> suggest(@NotNull CommandContext commandContext, @NotNull ArgumentContext argument) {
        String peek = argument.getFocusArgument();
        return enumSet.stream().map(Enum::name).filter(n -> n.startsWith(peek.toUpperCase())).collect(Collectors.toSet());
    }
}
