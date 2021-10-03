package org.mose.command.arguments.operation;

import org.jetbrains.annotations.NotNull;
import org.mose.command.CommandArgument;
import org.mose.command.CommandArgumentResult;
import org.mose.command.ParseCommandArgument;
import org.mose.command.context.CommandArgumentContext;
import org.mose.command.context.CommandContext;

import java.io.IOException;
import java.util.Collection;

public class OptionalArgument<T> implements CommandArgument<T> {

    public static class WrappedParser<T> implements ParseCommandArgument<T> {

        private final @NotNull T value;

        public WrappedParser(@NotNull T value) {
            this.value = value;
        }

        @Override
        public @NotNull CommandArgumentResult<T> parse(@NotNull CommandContext context, @NotNull CommandArgumentContext<T> argument) {
            return CommandArgumentResult.from(argument, 0, this.value);
        }
    }

    private final @NotNull CommandArgument<T> arg;
    private final @NotNull ParseCommandArgument<T> value;

    public OptionalArgument(@NotNull CommandArgument<T> arg, @NotNull T value) {
        this(arg, new WrappedParser<>(value));
    }

    public OptionalArgument(@NotNull CommandArgument<T> arg, @NotNull ParseCommandArgument<T> value) {
        this.arg = arg;
        this.value = value;
    }

    public @NotNull CommandArgument<T> getOriginalArgument() {
        return this.arg;
    }

    @Override
    public @NotNull String getId() {
        return this.arg.getId();
    }

    @Override
    public @NotNull CommandArgumentResult<T> parse(@NotNull CommandContext context, @NotNull CommandArgumentContext<T> argument) throws IOException {
        if (context.getCommand().length==argument.getFirstArgument()) {
            return CommandArgumentResult.from(argument, 0, this.value.parse(context, argument).getValue());
        }
        try {
            return this.arg.parse(context, argument);
        } catch (IOException e) {
            return CommandArgumentResult.from(argument, 0, this.value.parse(context, argument).getValue());
        }
    }

    @Override
    public @NotNull Collection<String> suggest(@NotNull CommandContext commandContext, @NotNull CommandArgumentContext<T> argument) {
        return this.arg.suggest(commandContext, argument);
    }

    @Override
    public @NotNull String getUsage() {
        String original = this.getOriginalArgument().getUsage();
        return "[" + original.substring(1, original.length() - 1) + "]";
    }
}
