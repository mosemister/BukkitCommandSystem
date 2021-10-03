package org.mose.command;

import org.mose.command.context.CommandArgumentContext;
import org.mose.command.context.CommandContext;

import java.util.Collection;

public interface SuggestCommandArgument<T> {

    Collection<String> suggest(CommandContext commandContext, CommandArgumentContext<T> argument);

}
