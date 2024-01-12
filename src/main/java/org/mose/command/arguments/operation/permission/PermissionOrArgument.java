package org.mose.command.arguments.operation.permission;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mose.command.CommandArgument;
import org.mose.command.CommandArgumentResult;
import org.mose.command.ParseCommandArgument;
import org.mose.command.SuggestCommandArgument;
import org.mose.command.arguments.operation.BlankArgument;
import org.mose.command.context.ArgumentContext;
import org.mose.command.context.CommandContext;
import org.mose.command.exception.ArgumentException;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Predicate;

/**
 * Allows to provide a better argument to the source if they have the provided permission
 *
 * @param <T> The returning class type
 */
public class PermissionOrArgument<T> implements CommandArgument<T> {

    private final @NotNull String id;
    private final @NotNull Predicate<CommandSender> permission;
    private final @NotNull ParseCommandArgument<T> with;
    private final @NotNull ParseCommandArgument<T> or;

    public PermissionOrArgument(@NotNull String id, @NotNull Predicate<CommandSender> permission,
                                @NotNull ParseCommandArgument<T> with) {
        this(id, permission, with, (T) null);
    }

    public PermissionOrArgument(@NotNull String id, @NotNull Predicate<CommandSender> permission,
                                @NotNull ParseCommandArgument<T> with, @Nullable T value) {
        this(id, permission, with, new BlankArgument<>(id, value));
    }

    /**
     * @param id         The id of the command argument
     * @param permission the check for if the provided command source has permission. If needed this can be checked
     *                   for other boolean values such as if a player is part of a town
     * @param with       The command argument to use if the user has permission
     * @param or         The command argument to use if the user doesn't have permission
     */
    public PermissionOrArgument(@NotNull String id, @NotNull Predicate<CommandSender> permission,
                                @NotNull ParseCommandArgument<T> with, @NotNull ParseCommandArgument<T> or) {
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
    CommandArgumentResult<T> parse(@NotNull CommandContext context, @NotNull ArgumentContext argument) throws
            ArgumentException {
        if (this.permission.test(context.getSource())) {
            return this.with.parse(context, argument);
        }
        return this.or.parse(context, argument);
    }

    @Override
    public @NotNull
    Collection<String> suggest(@NotNull CommandContext context, @NotNull ArgumentContext argument) {
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
