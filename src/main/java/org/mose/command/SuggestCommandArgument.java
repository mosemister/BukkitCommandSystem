package org.mose.command;

import org.jetbrains.annotations.NotNull;
import org.mose.command.context.ArgumentCommandContext;
import org.mose.command.context.ArgumentContext;
import org.mose.command.context.CommandArgumentContext;
import org.mose.command.context.CommandContext;

import java.util.Collection;

public interface SuggestCommandArgument<T> {

    /**
     * Gets the suggestions for this command
     *
     * @param commandContext The context of the command
     * @param argument       The context of the argument in the command
     * @return The resulting value
     * @deprecated use ArgumentContext edition
     */
    @Deprecated(forRemoval = true)
    default @NotNull Collection<String> suggest(@NotNull CommandContext commandContext, @NotNull CommandArgumentContext<T> argument) {
        return suggest(commandContext, new ArgumentCommandContext<>(argument.getArgument(), argument.getFirstArgument(), commandContext.getCommand()));
    }

    default @NotNull Collection<String> suggest(@NotNull CommandContext commandContext, @NotNull ArgumentContext argument) {
        if (argument instanceof ArgumentCommandContext<?> context) {
            return suggest(commandContext, new CommandArgumentContext<>((CommandArgument<T>) context.getArgument(), context.getArgumentIndex(), commandContext.getCommand()));
        }
        throw new RuntimeException("Argument has not been updated to support ArgumentContext");
    }

}
