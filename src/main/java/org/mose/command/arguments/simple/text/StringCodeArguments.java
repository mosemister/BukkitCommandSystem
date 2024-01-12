package org.mose.command.arguments.simple.text;

import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;
import org.mose.command.CommandArgument;
import org.mose.command.CommandArgumentResult;
import org.mose.command.context.ArgumentContext;
import org.mose.command.context.CommandContext;
import org.mose.command.exception.ArgumentException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Formats a command argument result with {@link ChatColor} codes
 */
public class StringCodeArguments implements CommandArgument<String> {

    private final CommandArgument<String> originalArgument;
    private final Collection<ChatColor> formats;

    /**
     * Allows all {@link ChatColor} values
     *
     * @param originalArgument The original String argument
     */
    public StringCodeArguments(@NotNull CommandArgument<String> originalArgument) {
        this(originalArgument, ChatColor.values());
    }

    /**
     * Allows a specified set of {@link ChatColor} formats
     *
     * @param originalArgument The original String argument
     * @param formats          the select formats to choose from in var-array form
     */
    public StringCodeArguments(@NotNull CommandArgument<String> originalArgument, ChatColor... formats) {
        this(originalArgument, Arrays.asList(formats));
    }

    /**
     * Allows a specified set of {@link ChatColor} formats
     *
     * @param originalArgument The original String argument
     * @param formats          the select formats to choose from in collection form
     */
    public StringCodeArguments(@NotNull CommandArgument<String> originalArgument,
                               @NotNull Collection<ChatColor> formats) {
        this.formats = formats;
        this.originalArgument = originalArgument;
    }

    @Override
    public @NotNull String getId() {
        return this.originalArgument.getId();
    }

    @Override
    public @NotNull CommandArgumentResult<String> parse(@NotNull CommandContext context,
                                                        @NotNull ArgumentContext argument) throws ArgumentException {
        CommandArgumentResult<String> result = this.originalArgument.parse(context, argument);
        String formatted = ChatColor.translateAlternateColorCodes('&', result.getValue());
        return new CommandArgumentResult<>(result.getPosition(), formatted);
    }

    @Override
    public @NotNull Collection<String> suggest(@NotNull CommandContext commandContext,
                                               @NotNull ArgumentContext argument) {
        String first = argument.getFocusArgument();
        Collection<String> suggestions = new ArrayList<>(this.originalArgument.suggest(commandContext, argument));
        suggestions.addAll(formats.stream().map(code -> first + "&" + code.getChar()).toList());
        return suggestions;
    }
}
