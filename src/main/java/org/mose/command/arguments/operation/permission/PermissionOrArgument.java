package org.mose.command.arguments.operation.permission;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.mose.command.CommandArgument;
import org.mose.command.CommandArgumentResult;
import org.mose.command.ParseCommandArgument;
import org.mose.command.SuggestCommandArgument;
import org.mose.command.context.CommandArgumentContext;
import org.mose.command.context.CommandContext;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Predicate;

/**
 * Allows to provide a better argument to the source if they have the provided permission
 *
 * @param <T> The returning class type
 */
public record PermissionOrArgument<T>(@NotNull String id,
                                      @NotNull Predicate<CommandSender> permission,
                                      @NotNull ParseCommandArgument<T> with,
                                      @NotNull ParseCommandArgument<T> or) implements CommandArgument<T> {

    /**
     * @param id         The id of the command argument
     * @param permission the check for if the provided command source has permission. If needed this can be checked for other boolean values such as if a player is part of a town
     * @param with       The command argument to use if the user has permission
     * @param or         The command argument to use if the user doesn't have permission
     */
    public PermissionOrArgument(@NotNull String id, @NotNull Predicate<CommandSender> permission, @NotNull ParseCommandArgument<T> with, @NotNull ParseCommandArgument<T> or) {
        this.id = id;
        this.permission = permission;
        this.with = with;
        this.or = or;
    }

    @Override
    public @NotNull
    String getId() {
        return this.id;
    }

    @Override
    public @NotNull
    CommandArgumentResult<T> parse(@NotNull CommandContext context, @NotNull CommandArgumentContext<T> argument) throws IOException {
        if (this.permission.test(context.getSource())) {
            return this.with.parse(context, argument);
        }
        return this.or.parse(context, argument);
    }

    @Override
    public @NotNull
    Collection<String> suggest(@NotNull CommandContext context, @NotNull CommandArgumentContext<T> argument) {
        if (this.permission.test(context.getSource())) {
            if (this.with instanceof SuggestCommandArgument) {
                return ((SuggestCommandArgument<T>) this.with).suggest(context, argument);
            }
        }
        if (this.or instanceof SuggestCommandArgument) {
            return ((SuggestCommandArgument<T>) this.or).suggest(context, argument);
        }
        return Collections.emptySet();
    }
}
