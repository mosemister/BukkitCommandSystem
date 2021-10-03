package org.mose.command.arguments.position;

import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.mose.command.CommandArgument;
import org.mose.command.CommandArgumentResult;
import org.mose.command.context.CommandArgumentContext;
import org.mose.command.context.CommandContext;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

public abstract class PositionArgument<N extends Number, P> implements CommandArgument<P> {

    private final @NotNull String id;
    private final @NotNull CommandArgument<N> positionArgument;

    public PositionArgument(@NotNull String id, @NotNull CommandArgument<N> argument) {
        this.id = id;
        this.positionArgument = argument;
    }

    public abstract P build(World extent, N x, N y, N z);

    @Override
    public @NotNull String getId() {
        return this.id;
    }

    @Override
    public @NotNull CommandArgumentResult<P> parse(@NotNull CommandContext context, @NotNull CommandArgumentContext<P> argument) throws IOException {
        int firstPosition = argument.getFirstArgument();
        WorldArgument worldArg = new WorldArgument("");
        CommandArgumentResult<World> extent = worldArg.parse(context, new CommandArgumentContext<>(worldArg, firstPosition, context.getCommand()));
        CommandArgumentResult<N> x = this.positionArgument.parse(context, new CommandArgumentContext<>(this.positionArgument, extent.getPosition() + 1, context.getCommand()));
        CommandArgumentResult<N> y = this.positionArgument.parse(context, new CommandArgumentContext<>(this.positionArgument, x.getPosition(), context.getCommand()));
        CommandArgumentResult<N> z = this.positionArgument.parse(context, new CommandArgumentContext<>(this.positionArgument, y.getPosition(), context.getCommand()));
        P pos = build(extent.getValue(), x.getValue(), y.getValue(), z.getValue());
        return new CommandArgumentResult<>(z.getPosition(), pos);
    }

    @Override
    public @NotNull Set<String> suggest(@NotNull CommandContext commandContext, @NotNull CommandArgumentContext<P> argument) {
        return Collections.emptySet();
    }
}
