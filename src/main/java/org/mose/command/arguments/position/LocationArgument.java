package org.mose.command.arguments.position;

import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.mose.command.arguments.simple.number.DoubleArgument;

/**
 * LocationArgument provides a exact location based upon the user input
 */
public class LocationArgument extends PositionArgument<Double, Location> {

    public LocationArgument(@NotNull String id) {
        super(id, new DoubleArgument(""));
    }

    @Override
    public Location build(@NotNull World extent, @NotNull Double x, @NotNull Double y, @NotNull Double z) {
        return new Location(extent, x, y, z);
    }
}
