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

public class BukkitCommandWrapper implements TabExecutor {

    public final Set<ArgumentCommand> commands = new HashSet<>();

    @Deprecated
    public BukkitCommandWrapper() {
        throw new RuntimeException("A ArgumentCommand needs to be specified");
    }

    public BukkitCommandWrapper(ArgumentCommand... commands) {
        this(Arrays.asList(commands));
    }

    public BukkitCommandWrapper(Collection<ArgumentCommand> commands) {
        this.commands.addAll(commands);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, String[] strings) {
        CommandContext commandContext = new CommandContext(commandSender, this.commands, strings);
        Optional<ArgumentCommand> opCommand = commandContext.getCompleteCommand();
        if (!opCommand.isPresent()) {
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
            commandSender.sendMessage(ChatColor.RED + " You do not have permission for that command. You require " + opCommand.get().getPermissionNode());
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
