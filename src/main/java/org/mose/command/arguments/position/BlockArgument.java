package org.mose.command.arguments.position;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.mose.command.arguments.simple.number.IntegerArgument;

/**
 * BlockArgument provides a block location based upon the user input
 */
public class BlockArgument extends PositionArgument<Integer, Block> {

    public BlockArgument(@NotNull String id) {
        super(id, new IntegerArgument(""));
    }

    @Override
    public @NotNull Block build(@NotNull World extent, @NotNull Integer x, @NotNull Integer y, @NotNull Integer z) {
        return extent.getBlockAt(x, y, z);
    }
}
