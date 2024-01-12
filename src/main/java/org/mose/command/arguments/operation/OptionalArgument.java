package org.mose.command.arguments.operation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mose.command.CommandArgument;
import org.mose.command.CommandArgumentResult;
import org.mose.command.ParseCommandArgument;
import org.mose.command.context.ArgumentContext;
import org.mose.command.context.CommandContext;
import org.mose.command.exception.ArgumentException;

import java.util.Collection;
import java.util.Optional;

/**
 * The optional argument is designed to give the user a optional argument to give more detail,
 * this could be that the user needs to specify a online player, however this could be optional
 * if the user is already a player, whereby the optional value would be the user player.
 * <p>
 * One of the limits of Optional is that it must return a value, this can be {@link Optional#empty}
 *
 * @param <T> The type
 */
public class OptionalArgument<T> implements CommandArgument<T> {

    private final @NotNull CommandArgument<T> arg;
    private final @NotNull ParseCommandArgument<T> value;

    public OptionalArgument(@NotNull CommandArgument<T> arg) {
        this(arg, (T) null);
    }

    /**
     * Constructor
     *
     * @param arg   The command argument
     * @param value The raw value if failed
     */
    public OptionalArgument(@NotNull CommandArgument<T> arg, @Nullable T value) {
        this(arg, new WrappedParser<>(value));
    }

    /**
     * Constructor
     *
     * @param arg   The command argument
     * @param value the argument processor to use if the argument failed
     */
    public OptionalArgument(@NotNull CommandArgument<T> arg, @NotNull ParseCommandArgument<T> value) {
        this.arg = arg;
        this.value = value;
    }

    /**
     * Gets the original argument to compare
     *
     * @return The original argument
     */
    public @NotNull CommandArgument<T> getOriginalArgument() {
        return this.arg;
    }

    @Override
    public @NotNull String getId() {
        return this.arg.getId();
    }

    @Override
    public @NotNull CommandArgumentResult<T> parse(@NotNull CommandContext context,
                                                   @NotNull ArgumentContext argument) throws ArgumentException {
        if (context.getCommand().length == argument.getArgumentIndex()) {
            return CommandArgumentResult.from(argument, 0, this.value.parse(context, argument).getValue());
        }
        try {
            return this.arg.parse(context, argument);
        } catch (ArgumentException e) {
            return CommandArgumentResult.from(argument, 0, this.value.parse(context, argument).getValue());
        }
    }

    @Override
    public @NotNull Collection<String> suggest(@NotNull CommandContext commandContext,
                                               @NotNull ArgumentContext argument) {
        return this.arg.suggest(commandContext, argument);
    }

    @Override
    public @NotNull String getUsage() {
        String original = this.getOriginalArgument().getUsage();
        return "[" + original.substring(1, original.length() - 1) + "]";
    }

    /**
     * If the Optional not provided value is known, then you can use
     * a WrappedParser to pass the argument to the OptionalArgument
     * if needed
     *
     * @param <T> The type
     */
    public record WrappedParser<T>(@Nullable T value) implements ParseCommandArgument<T> {
        @Override
        public @NotNull
        CommandArgumentResult<T> parse(@NotNull CommandContext context, @NotNull ArgumentContext argument) {
            return CommandArgumentResult.from(argument, 0, this.value);
        }
    }
}
