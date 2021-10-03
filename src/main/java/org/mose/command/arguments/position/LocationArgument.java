package org.mose.command.arguments.position;

import org.bukkit.Location;
import org.bukkit.World;
import org.mose.command.arguments.simple.number.DoubleArgument;

public class LocationArgument extends PositionArgument<Double, Location> {

    public LocationArgument(String id) {
        super(id, new DoubleArgument(""));
    }

    @Override
    public Location build(World extent, Double x, Double y, Double z) {
        return new Location(extent, x, y, z);
    }
}
