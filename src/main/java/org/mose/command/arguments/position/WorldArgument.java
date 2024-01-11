package org.mose.command.arguments.position;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.CommandBlock;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.mose.command.CommandArgument;
import org.mose.command.CommandArgumentResult;
import org.mose.command.context.CommandArgumentContext;
import org.mose.command.context.CommandContext;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Gets a world from a single argument, if the source of the command is locatable then this command
 * becomes optional whereby if the string argument is not provided then it will use the world the source is in.
 */
public record WorldArgument(@NotNull String id) implements CommandArgument<World> {

    @Override
    public @NotNull
    String getId() {
        return this.id;
    }

    @Override
    public @NotNull
    CommandArgumentResult<World> parse(@NotNull CommandContext context, @NotNull CommandArgumentContext<World> argument) throws IOException {
        String worldName = context.getCommand()[argument.getFirstArgument()];
        World world = Bukkit.getWorld(worldName);
        if (world!=null) {
            return CommandArgumentResult.from(argument, world);
        }
        if (context.getSource() instanceof Player) {
            World playerWorld = ((Entity) context.getSource()).getWorld();
            return CommandArgumentResult.from(argument, 0, playerWorld);
        }
        if (context.getSource() instanceof CommandBlock) {
            World blockWorld = ((BlockState) context.getSource()).getWorld();
            return CommandArgumentResult.from(argument, 0, blockWorld);
        }
        throw new IOException("Unknown world name of '" + worldName + "'");
    }

    @Override
    public @NotNull
    Set<String> suggest(@NotNull CommandContext commandContext, @NotNull CommandArgumentContext<World> argument) {
        String worldPeek = commandContext.getCommand()[argument.getFirstArgument()];
        return Bukkit
                .getWorlds()
                .stream()
                .map(World::getName)
                .filter(w -> w.toLowerCase().startsWith(worldPeek))
                .collect(Collectors.toSet());
    }
}
