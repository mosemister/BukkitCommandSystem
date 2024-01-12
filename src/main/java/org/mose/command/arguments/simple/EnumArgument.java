package org.mose.command.arguments.simple;

import org.jetbrains.annotations.NotNull;
import org.mose.command.CommandArgument;
import org.mose.command.CommandArgumentResult;
import org.mose.command.context.ArgumentContext;
import org.mose.command.context.CommandContext;
import org.mose.command.exception.ArgumentException;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Provides a chose of values from enum class
 *
 * @param <E> The enum type
 */
public record EnumArgument<E extends Enum<?>>(@NotNull String id,
                                              @NotNull Class<E> clazz) implements CommandArgument<E> {

    private @NotNull
    E[] getValues() throws NoSuchFieldException, IllegalAccessException {
        Field f = this.clazz.getDeclaredField("$VALUES");
        f.setAccessible(true);
        Object o = f.get(null);
        return (E[]) o;
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
        try {
            Optional<E> opValue = Stream.of(this.getValues()).filter(n -> n.name().equalsIgnoreCase(next)).findFirst();
            if (opValue.isPresent()) {
                return CommandArgumentResult.from(argument, opValue.get());
            }
            throw new ArgumentException("Unknown value of '" + next + "' in argument " + this.getUsage());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new ArgumentException(e);
        }
    }

    @Override
    public @NotNull
    Set<String> suggest(@NotNull CommandContext commandContext, @NotNull ArgumentContext argument) {
        String peek = argument.getFocusArgument();
        try {
            return Stream.of(this.getValues()).map(e -> e.name()).filter(n -> n.startsWith(peek.toUpperCase())).collect(Collectors.toSet());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return Collections.emptySet();
        }
    }
}
