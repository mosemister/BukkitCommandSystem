package org.mose.command.arguments.source;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.mose.command.CommandArgument;
import org.mose.command.CommandArgumentResult;
import org.mose.command.context.CommandArgumentContext;
import org.mose.command.context.CommandContext;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class PlayerArgument implements CommandArgument<Player> {

    private final String id;

    public PlayerArgument(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public CommandArgumentResult<Player> parse(CommandContext context, CommandArgumentContext<Player> argument) throws IOException {
        String command = context.getCommand()[argument.getFirstArgument()];
        Optional<? extends Player> opPlayer = Bukkit
                .getOnlinePlayers()
                .stream()
                .filter(p -> p.getName().equalsIgnoreCase(command))
                .findFirst();
        if (!opPlayer.isPresent()) {
            throw new IOException("Player is not online");
        }
        return CommandArgumentResult.from(argument, opPlayer.get());

    }

    @Override
    public Set<String> suggest(CommandContext commandContext, CommandArgumentContext<Player> argument) {
        String command = commandContext.getCommand()[argument.getFirstArgument()];
        return Bukkit
                .getOnlinePlayers()
                .stream()
                .map(HumanEntity::getName)
                .filter(p -> p.toLowerCase().startsWith(command.toLowerCase()))
                .collect(Collectors.toSet());
    }
}
