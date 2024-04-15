package org.mose.command.builder;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.mose.command.ArgumentCommand;
import org.mose.command.CommandArgument;
import org.mose.command.context.CommandContext;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

class BuiltCommand implements ArgumentCommand {

    private final List<CommandArgument<?>> arguments = new LinkedList<>();
    private final String description;
    private final BiPredicate<CommandContext, String[]> executor;
    private final Predicate<CommandSender> hasPermission;
    private final String permissionNode;

    public BuiltCommand(@NotNull CommandBuilder builder) {
        this.description = Objects.requireNonNull(builder.getDescription(), "Description must be set");
        this.executor = Objects.requireNonNull(builder.getExecutor(), "Executor must be set");
        this.hasPermission = Objects.requireNonNullElseGet(builder.getPermissionSenderCheck(),
                () -> (source) -> getPermissionNode().map(source::hasPermission).orElse(false));
        this.permissionNode = builder.getPermissionNode();
        this.arguments.addAll(builder.getArguments());
    }

    @Override
    public @NotNull List<CommandArgument<?>> getArguments() {
        return this.arguments;
    }

    @Override
    public @NotNull String getDescription() {
        return this.description;
    }

    @Override
    public @NotNull Optional<String> getPermissionNode() {
        return Optional.ofNullable(this.permissionNode);
    }

    @Override
    public boolean run(CommandContext commandContext, String... args) {
        return this.executor.test(commandContext, args);
    }

    @Override
    public boolean hasPermission(CommandSender source) {
        return this.hasPermission.test(source);
    }
}
