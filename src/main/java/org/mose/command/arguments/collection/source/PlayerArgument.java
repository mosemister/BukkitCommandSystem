package org.mose.command.arguments.collection.source;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.mose.command.CommandArgument;
import org.mose.command.CommandArgumentResult;
import org.mose.command.context.CommandArgumentContext;
import org.mose.command.context.CommandContext;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Provides a online user argument to the user
 */
public record PlayerArgument(@NotNull String id) implements CommandArgument<Player> {

    public PlayerArgument(@NotNull String id) {
        this.id = id;
    }

    @Override
    public @NotNull
    String getId() {
        return this.id;
    }

    @Override
    public @NotNull
    CommandArgumentResult<Player> parse(@NotNull CommandContext context, @NotNull CommandArgumentContext<Player> argument) throws IOException {
        String command = context.getCommand()[argument.getFirstArgument()];
        Optional<? extends Player> opPlayer = Bukkit
                .getOnlinePlayers()
                .stream()
                .filter(p -> p.getName().equalsIgnoreCase(command))
                .findFirst();
        if (opPlayer.isEmpty()) {
            throw new IOException("Player is not online");
        }
        return CommandArgumentResult.from(argument, opPlayer.get());

    }

    @Override
    public @NotNull
    Set<String> suggest(@NotNull CommandContext commandContext, @NotNull CommandArgumentContext<Player> argument) {
        String command = commandContext.getCommand()[argument.getFirstArgument()];
        return Bukkit
                .getOnlinePlayers()
                .stream()
                .map(HumanEntity::getName)
                .filter(p -> p.toLowerCase().startsWith(command.toLowerCase()))
                .collect(Collectors.toSet());
    }
}
