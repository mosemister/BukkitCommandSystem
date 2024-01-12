package org.mose.command.arguments.position;

import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.mose.command.CommandArgument;
import org.mose.command.CommandArgumentResult;
import org.mose.command.context.ArgumentCommandContext;
import org.mose.command.context.ArgumentContext;
import org.mose.command.context.CommandContext;
import org.mose.command.exception.ArgumentException;

import java.util.Collections;
import java.util.Set;

/**
 * Common interface for all position based arguments (aka, x, y, z, world)
 *
 * @param <N> The number type of the xyz
 * @param <P> The returning type of argument
 */
public abstract class PositionArgument<N extends Number, P> implements CommandArgument<P> {

    private final @NotNull String id;
    private final @NotNull CommandArgument<N> positionArgument;

    public PositionArgument(@NotNull String id, @NotNull CommandArgument<N> argument) {
        this.id = id;
        this.positionArgument = argument;
    }

    /**
     * Builds the argument object based upon the input
     *
     * @param extent The world the user entered
     * @param x      The x position the user entered
     * @param y      The y position the user entered
     * @param z      The z position the user entered
     * @return The return object
     */
    public abstract P build(@NotNull World extent, @NotNull N x, @NotNull N y, @NotNull N z);

    @Override
    public @NotNull String getId() {
        return this.id;
    }

    @Override
    public @NotNull CommandArgumentResult<P> parse(@NotNull CommandContext context, @NotNull ArgumentContext argument) throws ArgumentException {
        int firstPosition = argument.getArgumentIndex();
        WorldArgument worldArg = new WorldArgument("");
        CommandArgumentResult<World> extent = worldArg.parse(context, new ArgumentCommandContext<>(worldArg, firstPosition, context.getCommand()));
        CommandArgumentResult<N> x = this.positionArgument.parse(context, new ArgumentCommandContext<>(this.positionArgument, extent.getPosition() + 1, context.getCommand()));
        CommandArgumentResult<N> y = this.positionArgument.parse(context, new ArgumentCommandContext<>(this.positionArgument, x.getPosition(), context.getCommand()));
        CommandArgumentResult<N> z = this.positionArgument.parse(context, new ArgumentCommandContext<>(this.positionArgument, y.getPosition(), context.getCommand()));
        P pos = build(extent.getValue(), x.getValue(), y.getValue(), z.getValue());
        return new CommandArgumentResult<>(z.getPosition(), pos);
    }

    @Override
    public @NotNull Set<String> suggest(@NotNull CommandContext commandContext, @NotNull ArgumentContext argument) {
        return Collections.emptySet();
    }
}
