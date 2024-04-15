package org.mose.command.builder;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mose.command.ArgumentCommand;
import org.mose.command.CommandArgument;
import org.mose.command.context.CommandContext;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class CommandBuilder {

    public interface CommandDetailSupplier {

        ArgumentCommand build(Supplier<ArgumentCommand> command, CommandBuilder builder);

    }

    private final @NotNull List<CommandArgument<?>> arguments = new LinkedList<>();
    private @Nullable String description;
    private @Nullable BiPredicate<CommandContext, String[]> executor;
    private @Nullable String permissionNode;
    private @Nullable Predicate<CommandSender> permissionSource;

    private CommandBuilder() {

    }

    public CommandBuilder addArguments(CommandArgument<?>... arguments) {
        this.arguments.addAll(List.of(arguments));
        return this;
    }

    public ArgumentCommand build() {
        return new BuiltCommand(this);
    }

    public List<CommandArgument<?>> getArguments() {
        return this.arguments;
    }

    public @Nullable String getDescription() {
        return this.description;
    }

    public CommandBuilder setDescription(@NotNull String description) {
        this.description = description;
        return this;
    }

    public @Nullable BiPredicate<CommandContext, String[]> getExecutor() {
        return this.executor;
    }

    public CommandBuilder setExecutor(BiPredicate<CommandContext, String[]> executor) {
        this.executor = executor;
        return this;
    }

    public @Nullable String getPermissionNode() {
        return this.permissionNode;
    }

    public CommandBuilder setPermissionNode(@Nullable String permissionNode) {
        this.permissionNode = permissionNode;
        return this;
    }

    public @Nullable Predicate<CommandSender> getPermissionSenderCheck() {
        return this.permissionSource;
    }

    public CommandBuilder setPermissionSenderCheck(@Nullable Predicate<CommandSender> sender) {
        this.permissionSource = sender;
        return this;
    }

    public static ArgumentCommand build(CommandDetailSupplier supplier) {
        var builder = new CommandBuilder();
        AtomicReference<ArgumentCommand> reference = new AtomicReference<>();
        Supplier<ArgumentCommand> supplierReference = reference::get;
        return supplier.build(supplierReference, builder);

    }

}
