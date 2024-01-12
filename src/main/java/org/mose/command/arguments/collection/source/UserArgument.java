package org.mose.command.arguments.collection.source;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.mose.command.CommandArgument;
import org.mose.command.CommandArgumentResult;
import org.mose.command.context.ArgumentContext;
import org.mose.command.context.CommandContext;
import org.mose.command.exception.ArgumentException;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Provides a chose of OfflinePlayers to the user
 */
public class UserArgument implements CommandArgument<OfflinePlayer> {

    private final String id;
    private final Predicate<OfflinePlayer> filter;

    public UserArgument(String id, Predicate<OfflinePlayer> filter) {
        this.id = id;
        this.filter = filter;
    }

    @Override
    public @NotNull String getId() {
        return this.id;
    }

    @Override
    public @NotNull CommandArgumentResult<OfflinePlayer> parse(@NotNull CommandContext context, @NotNull ArgumentContext argument) throws ArgumentException {
        String command = argument.getFocusArgument();
        return Arrays
                .stream(Bukkit.getOfflinePlayers())
                .filter(offlinePlayer -> offlinePlayer.getName() != null)
                .filter(offlinePlayer -> offlinePlayer.getName().equalsIgnoreCase(command))
                .findAny()
                .map(offlinePlayer -> CommandArgumentResult.from(argument, offlinePlayer))
                .orElseThrow(() -> new ArgumentException("Unknown user"));
    }

    @Override
    public @NotNull Collection<String> suggest(@NotNull CommandContext commandContext, @NotNull ArgumentContext argument) {
        String command = argument.getFocusArgument();
        return Arrays
                .stream(Bukkit.getOfflinePlayers())
                .filter(this.filter)
                .map(OfflinePlayer::getName)
                .filter(Objects::nonNull)
                .filter(name -> name.toLowerCase().startsWith(command))
                .sorted()
                .collect(Collectors.toList());
    }
}
