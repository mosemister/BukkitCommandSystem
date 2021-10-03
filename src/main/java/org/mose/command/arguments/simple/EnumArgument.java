package org.mose.command.arguments.simple;

import org.jetbrains.annotations.NotNull;
import org.mose.command.CommandArgument;
import org.mose.command.CommandArgumentResult;
import org.mose.command.context.CommandArgumentContext;
import org.mose.command.context.CommandContext;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EnumArgument<E extends Enum<?>> implements CommandArgument<E> {

    private final @NotNull String id;
    private final @NotNull Class<E> clazz;

    public EnumArgument(@NotNull String id, @NotNull Class<E> clazz) {
        this.id = id;
        this.clazz = clazz;
    }

    private @NotNull E[] getValues() throws NoSuchFieldException, IllegalAccessException {
        Field f = this.clazz.getDeclaredField("$VALUES");
        f.setAccessible(true);
        Object o = f.get(null);
        return (E[]) o;
    }

    @Override
    public @NotNull String getId() {
        return this.id;
    }

    @Override
    public @NotNull CommandArgumentResult<E> parse(@NotNull CommandContext context, @NotNull CommandArgumentContext<E> argument) throws IOException {
        String next = context.getCommand()[argument.getFirstArgument()];
        try {
            Optional<E> opValue = Stream.of(this.getValues()).filter(n -> n.name().equalsIgnoreCase(next)).findFirst();
            if (opValue.isPresent()) {
                return CommandArgumentResult.from(argument, opValue.get());
            }
            throw new IOException("Unknown value of '" + next + "' in argument " + this.getUsage());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IOException(e);
        }
    }

    @Override
    public @NotNull Set<String> suggest(@NotNull CommandContext commandContext, @NotNull CommandArgumentContext<E> argument) {
        String peek = commandContext.getCommand()[argument.getFirstArgument()];
        try {
            return Stream.of(this.getValues()).map(e -> e.name()).filter(n -> n.startsWith(peek.toUpperCase())).collect(Collectors.toSet());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return Collections.emptySet();
        }
    }
}
