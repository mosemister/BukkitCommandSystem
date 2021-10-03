package org.mose.command;

import org.jetbrains.annotations.NotNull;
import org.mose.command.context.CommandArgumentContext;
import org.mose.command.context.CommandContext;

import java.util.Collection;

public interface SuggestCommandArgument<T> {

    @NotNull Collection<String> suggest(@NotNull CommandContext commandContext, @NotNull CommandArgumentContext<T> argument);

}
