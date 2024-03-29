package org.mose.command.arguments.simple.text;

import org.jetbrains.annotations.NotNull;
import org.mose.command.CommandArgument;
import org.mose.command.CommandArgumentResult;
import org.mose.command.context.ArgumentContext;
import org.mose.command.context.CommandContext;
import org.mose.command.exception.ArgumentException;

import java.util.Arrays;
import java.util.Collection;

public record StringBetweenArgument(@NotNull String id, char betweenChar) implements CommandArgument<String> {

    @Override
    public @NotNull
    String getId() {
        return this.id;
    }

    @Override
    public @NotNull
    CommandArgumentResult<String> parse(@NotNull CommandContext context, @NotNull ArgumentContext argument) throws ArgumentException {
        String[] commands = context.getCommand();
        int start = argument.getArgumentIndex();
        Integer end = null;
        if (!commands[start].startsWith(this.betweenChar + "")) {
            throw new ArgumentException("String needs to start with " + this.betweenChar);
        }
        for (int i = start; i < commands.length; i++) {
            String arg = commands[i];
            if (arg.endsWith("\\" + this.betweenChar)) {
                continue;
            }
            if (arg.endsWith(this.betweenChar + "")) {
                end = i;
                break;
            }
        }
        if (end == null) {
            throw new ArgumentException("Cannot find end of argument. Argument must end with " + this.betweenChar);
        }
        StringBuilder builder = null;
        for (int i = start; i < end; i++) {
            if (builder == null) {
                builder = new StringBuilder(commands[i]);
            } else {
                builder.append(" ").append(commands[i]);
            }
        }

        if (builder == null) {
            throw new ArgumentException("Builder was null. Report as issue");
        }

        String ret = builder.toString();
        return CommandArgumentResult.from(argument, end - start, ret.substring(1, ret.length() - 1));
    }

    @Override
    public @NotNull
    Collection<String> suggest(@NotNull CommandContext commandContext, @NotNull ArgumentContext argument) {
        String first = argument.getFocusArgument();
        return Arrays.asList(first + '"', first + "\\\"");
    }
}
