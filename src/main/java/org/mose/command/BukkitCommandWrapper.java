package org.mose.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.mose.command.context.CommandContext;
import org.mose.command.context.ErrorContext;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A wrapper to convert the Bukkits registered command into this command system.
 * When registering commands, use this.
 */
public class BukkitCommandWrapper implements TabExecutor {

    public final Set<ArgumentCommand> commands = new HashSet<>();

    /**
     * Do not use
     *
     * @throws RuntimeException requires at least 1 CommandArgument
     */
    @Deprecated
    public BukkitCommandWrapper() {
        throw new RuntimeException("A ArgumentCommand needs to be specified");
    }

    /**
     * Constructor
     *
     * @param commands The possible commands for this command
     */
    public BukkitCommandWrapper(ArgumentCommand... commands) {
        this(Arrays.asList(commands));
    }

    /**
     * Constructor
     *
     * @param commands The possible commands for this command
     */
    public BukkitCommandWrapper(Collection<ArgumentCommand> commands) {
        this.commands.addAll(commands);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, String[] strings) {
        CommandContext commandContext = new CommandContext(commandSender, this.commands, strings);
        Optional<ArgumentCommand> opCommand = commandContext.getCompleteCommand();
        if (opCommand.isEmpty()) {
            Set<ErrorContext> errors = commandContext.getErrors();
            if (!errors.isEmpty()) {
                ErrorContext error = errors.iterator().next();
                commandSender.sendMessage(ChatColor.RED + error.getError());
                errors
                        .parallelStream()
                        .map(e -> e.getArgument().getUsage())
                        .collect(Collectors.toSet())
                        .forEach(e -> commandSender.sendMessage(ChatColor.RED + e));
            } else {
                commandSender.sendMessage(ChatColor.RED + "Unknown error");
            }
            return true;
        }
        if (!opCommand.get().hasPermission(commandSender)) {
            commandSender.sendMessage(ChatColor.RED + " You do not have permission for that command. You require " + opCommand.get().getPermissionNode().orElse("unknown"));
            return true;
        }
        return opCommand.get().run(commandContext, strings);
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, String[] strings) {
        CommandContext commandContext = new CommandContext(commandSender, this.commands, strings);
        Set<ArgumentCommand> commands = commandContext.getPotentialCommands();
        TreeSet<String> tab = new TreeSet<>();
        commands.forEach(c -> {
            if (!c.hasPermission(commandSender)) {
                return;
            }
            tab.addAll(commandContext.getSuggestions(c));
        });
        return new ArrayList<>(tab);
    }
}
