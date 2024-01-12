package org.mose.command.arguments.simple.text;

import org.jetbrains.annotations.NotNull;
import org.mose.command.CommandArgument;
import org.mose.command.CommandArgumentResult;
import org.mose.command.context.ArgumentContext;
import org.mose.command.context.CommandContext;

import java.util.Collections;
import java.util.Set;

/**
 * Provides a single string argument (no spaces) to the user
 */
public record StringArgument(@NotNull String id) implements CommandArgument<String> {

    @Override
    public @NotNull
    String getId() {
        return this.id;
    }

    @Override
    public @NotNull
    CommandArgumentResult<String> parse(@NotNull CommandContext context,
                                        @NotNull ArgumentContext argument) {
        String text = argument.getFocusArgument();
        return CommandArgumentResult.from(argument, text);

    }

    @Override
    public @NotNull
    Set<String> suggest(@NotNull CommandContext commandContext, @NotNull ArgumentContext argument) {
        return Collections.emptySet();
    }
}
