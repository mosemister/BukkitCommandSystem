package org.mose.command.arguments.position;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.mose.command.arguments.simple.number.IntegerArgument;

public class BlockArgument extends PositionArgument<Integer, Block> {

    public BlockArgument(String id) {
        super(id, new IntegerArgument(""));
    }

    @Override
    public Block build(World extent, Integer x, Integer y, Integer z) {
        return extent.getBlockAt(x, y, z);
    }
}
