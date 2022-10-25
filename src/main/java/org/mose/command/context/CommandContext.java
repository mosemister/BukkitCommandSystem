package org.mose.command.context;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.mose.command.ArgumentCommand;
import org.mose.command.CommandArgument;
import org.mose.command.CommandArgumentResult;
import org.mose.command.arguments.operation.OptionalArgument;

import java.io.IOException;
import java.util.*;

/**
 * The magic that is the CommandContext. Everything important about the command processing can be found
 * in this class.
 */
public class CommandContext {

	private final @NotNull String[] commands;
	private final @NotNull CommandSender source;
	private final @NotNull Set<ArgumentCommand> potentialCommands = new HashSet<>();

	/**
	 * @param source   The command source who is running the command
	 * @param commands The potential commands of the command context
	 * @param command  The string arguments that the source wrote
	 */
	public CommandContext(@NotNull CommandSender source, @NotNull Collection<ArgumentCommand> commands,
			String... command) {
		this.commands = command;
		this.potentialCommands.addAll(commands);
		this.source = source;
	}

	/**
	 * Gets the raw string arguments that the command source used
	 *
	 * @return A String array of the raw string arguments
	 */
	public @NotNull String[] getCommand() {
		return this.commands;
	}

	/**
	 * The source of the command
	 *
	 * @return The command sender
	 */
	public @NotNull CommandSender getSource() {
		return this.source;
	}

	/**
	 * Gets the suggestions for the next argument in the command.
	 * This is based upon the argument command provided as well as the raw
	 * string arguments. The suggestion will be to the last of the raw string argument
	 *
	 * @param command The command to target
	 * @return A list of suggestions for the current context and provided command
	 */
	public @NotNull Collection<String> getSuggestions(@NotNull ArgumentCommand command) {
		List<CommandArgument<?>> arguments = command.getArguments();
		int commandArgument = 0;
		List<OptionalArgument<?>> optionalArguments = new ArrayList<>();
		for (CommandArgument<?> arg : arguments) {
			if (this.commands.length == commandArgument) {
				if (arg instanceof OptionalArgument) {
					optionalArguments.add((OptionalArgument<?>) arg);
					continue;
				}
				return this.suggest(arg, commandArgument);
			}
			if (this.commands.length < commandArgument) {
				throw new IllegalArgumentException("Not enough provided arguments for value of that argument");
			}
			try {
				CommandArgumentResult<?> entry = this.parse(arg, commandArgument);
				if (commandArgument == entry.getPosition() && arg instanceof OptionalArgument) {
					optionalArguments.add((OptionalArgument<?>) arg);
				} else {
					optionalArguments.clear();
				}
				commandArgument = entry.getPosition();
			} catch (IOException e) {
				return this.suggest(arg, commandArgument);
			}
		}
		if (optionalArguments.isEmpty()) {
			return Collections.emptySet();
		}
		Set<String> ret = new HashSet<>();
		for (OptionalArgument<?> argument : optionalArguments) {
			ret.addAll(suggest(argument, commandArgument));
		}
		return ret;
	}

	/**
	 * Gets the argument value of the command argument provided
	 *
	 * @param command The command to target
	 * @param id      The command argument that should be used
	 * @param <T>     The expected type of argument (by providing the command argument, the type will be the same
     *              unless the argument is breaking the standard)
	 * @return The value of the argument
	 * @throws IllegalArgumentException If the provided id argument is not part of the command
	 * @throws IllegalStateException    Argument requested is asking for string requirements then what is provided
	 */
	public <T> T getArgument(@NotNull ArgumentCommand command, @NotNull CommandArgument<T> id) {
		return this.getArgument(command, id.getId());
	}

	/**
	 * Gets the argument value of the id provided
	 *
	 * @param command The command to target
	 * @param id      The id of the argument to get
	 * @param <T>     The expected type of argument
	 * @return The value of the argument
	 * @throws IllegalArgumentException If the provided id argument is not part of the command
	 * @throws IllegalStateException    Argument requested is asking for string requirements then what is provided
	 */
	public <T> T getArgument(@NotNull ArgumentCommand command, @NotNull String id) {
		List<CommandArgument<?>> arguments = command.getArguments();
		if (arguments.stream().noneMatch(a -> a.getId().equals(id))) {
			throw new IllegalArgumentException("Argument ID not found within command");
		}
		int commandArgument = 0;
		for (CommandArgument<?> arg : arguments) {
			if (this.commands.length == commandArgument && arg instanceof OptionalArgument) {
				if (arg.getId().equals(id)) {
					try {
						return (T) this.parse(arg, commandArgument).getValue();
					} catch (IOException ignored) {
					}
				}
				continue;
			}
			if (this.commands.length < commandArgument) {
				throw new IllegalStateException("Not enough provided arguments for value of that argument");
			}
			try {
				CommandArgumentResult<?> entry = this.parse(arg, commandArgument);
				commandArgument = entry.getPosition();
				if (arg.getId().equals(id)) {
					return (T) entry.getValue();
				}
			} catch (IOException e) {
				throw new IllegalArgumentException(e);
			}
		}
		throw new IllegalArgumentException("Argument ID of '" + id + "' not found within command");
	}

	/**
	 * If there is a issue with the command the user is attempting to parse, you can
	 * get all the errors with this function. The error is not specific to the command argument
	 *
	 * @return A set of all errors
	 */
	public @NotNull Set<ErrorContext> getErrors() {
		Set<ErrorContext> map = new HashSet<>();
		for (ArgumentCommand command : this.potentialCommands) {
			List<CommandArgument<?>> arguments = command.getArguments();
			int commandArgument = 0;
			for (CommandArgument<?> arg : arguments) {
				if (this.commands.length == commandArgument && arg instanceof OptionalArgument) {
					continue;
				}
				if (this.commands.length <= commandArgument) {
					ErrorContext context = new ErrorContext(command, commandArgument, arg, "Not enough arguments");
					map.add(context);
					break;
				}
				try {
					CommandArgumentResult<?> entry = this.parse(arg, commandArgument);
					commandArgument = entry.getPosition();
				} catch (IOException e) {
					ErrorContext context = new ErrorContext(command, commandArgument, arg, e.getMessage());
					map.add(context);
					break;
				}
			}
		}
		Set<ErrorContext> ret = new HashSet<>();
		int target = 0;

		for (ErrorContext context : map) {
			int failedAt = context.getArgumentFailedAt();
			if (failedAt > target) {
				target = failedAt;
				ret.clear();
			}
			if (failedAt == target) {
				ret.add(context);
			}
		}

		return ret;
	}

	/**
	 * Gets the command the user is targeting
	 *
	 * @return A single argument command, if none can be found then {@link Optional#empty()} will be used
	 */
	public @NotNull Optional<ArgumentCommand> getCompleteCommand() {
		return this.potentialCommands.stream().filter(command -> {
			List<CommandArgument<?>> arguments = command.getArguments();
			int commandArgument = 0;
			for (CommandArgument<?> arg : arguments) {
				if (this.commands.length == commandArgument && arg instanceof OptionalArgument) {
					continue;
				}
				if (this.commands.length <= commandArgument) {
					return false;
				}
				try {
					CommandArgumentResult<?> entry = this.parse(arg, commandArgument);
					commandArgument = entry.getPosition();
				} catch (IOException e) {
					return false;
				}
			}
			return this.commands.length == commandArgument;
		}).findAny();

	}

	/**
	 * Gets all potential commands from what the user has entered
	 *
	 * @return A set of all the potential commands
	 */
	public @NotNull Set<ArgumentCommand> getPotentialCommands() {
		Map<ArgumentCommand, Integer> map = new HashMap<>();
		this.potentialCommands.forEach(c -> {
			List<CommandArgument<?>> arguments = c.getArguments();
			int commandArgument = 0;
			int completeArguments = 0;
			for (CommandArgument<?> arg : arguments) {
				if (this.commands.length == commandArgument && arg instanceof OptionalArgument) {
					continue;
				}
				if (this.commands.length <= commandArgument) {
					map.put(c, completeArguments);
					return;
				}
				try {
					CommandArgumentResult<?> entry = this.parse(arg, commandArgument);
					if (commandArgument != entry.getPosition()) {
						commandArgument = entry.getPosition();
						completeArguments++;
					}
				} catch (IOException e) {
					map.put(c, completeArguments);
					return;
				}
			}
			map.put(c, completeArguments);
		});

		Set<ArgumentCommand> set = new HashSet<>();
		int current = 0;
		for (Map.Entry<ArgumentCommand, Integer> entry : map.entrySet()) {
			if (entry.getValue() > current) {
				current = entry.getValue();
				set.clear();
			}
			if (entry.getValue() == current) {
				set.add(entry.getKey());
			}
		}
		return set;
	}

	private <T> @NotNull CommandArgumentResult<T> parse(@NotNull CommandArgument<T> arg, int commandArgument) throws
			IOException {
		CommandArgumentContext<T> argContext = new CommandArgumentContext<>(arg, commandArgument, this.commands);
		return arg.parse(this, argContext);
	}

	private <T> @NotNull Collection<String> suggest(@NotNull CommandArgument<T> arg, int commandArgument) {
		if (this.commands.length <= commandArgument) {
			return Collections.emptySet();
		}
		return arg.suggest(this, new CommandArgumentContext<T>(arg, commandArgument, this.commands));
	}
}
