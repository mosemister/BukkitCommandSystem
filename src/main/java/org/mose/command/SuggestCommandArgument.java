package org.mose.command;

import org.jetbrains.annotations.NotNull;
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
     */
    @NotNull Collection<String> suggest(@NotNull CommandContext commandContext, @NotNull CommandArgumentContext<T> argument);

}
