package org.mose.command;

import org.jetbrains.annotations.NotNull;
import org.mose.command.context.CommandArgumentContext;

/**
 * This tells the command system how the argument should be processed, including the result
 * object of the argument and how many string arguments to move ahead of.
 * <p>
 * Unless your needing raw access, use the static methods to create the object
 *
 * @param <T> The type of the returning object
 */
public record CommandArgumentResult<T>(int position, T value) {

	public int getPosition() {
		return this.position;
	}

	public T getValue() {
		return this.value;
	}

	/**
	 * This creates a CommandArgumentResult which processes the argument by 1
	 *
	 * @param argumentContext The argumentContext of the command argument in question
	 * @param value           The value of the argument
	 * @param <T>             The type of the value of the argument
	 * @return The CommandArgumentResult
	 */
	public static @NotNull <T> CommandArgumentResult<T> from(CommandArgumentContext<T> argumentContext, T value) {
		return from(argumentContext, 1, value);
	}

	/**
	 * This creates a CommandArgumentResult which processes the argument by the specified amount
	 *
	 * @param argumentContext The argumentContext of the command argument in question
	 * @param length          The amount to move the argument over
	 * @param value           The value of the argument
	 * @param <T>             The type of the value of the argument
	 * @return The CommandArgumentResult
	 */
	public static @NotNull <T> CommandArgumentResult<T> from(CommandArgumentContext<T> argumentContext, int length,
			T value) {
		return new CommandArgumentResult<>(argumentContext.getFirstArgument() + length, value);
	}


}
