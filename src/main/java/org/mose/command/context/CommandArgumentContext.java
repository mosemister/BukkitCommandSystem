package org.mose.command.context;

import org.jetbrains.annotations.NotNull;
import org.mose.command.CommandArgument;

@Deprecated(forRemoval = true)
public class CommandArgumentContext<T> {

    private final @NotNull CommandArgument<T> argument;
    private int firstArgument;
    private @NotNull String[] command;

    public CommandArgumentContext(@NotNull CommandArgument<T> argument, int firstArgument, @NotNull String... command) {
        this.argument = argument;
        this.firstArgument = firstArgument;
        this.command = command;
    }

    public @NotNull CommandArgument<T> getArgument() {
        return this.argument;
    }

    public @NotNull String[] getRemainingArguments() {
        int last = this.command.length;
        String[] ret = new String[last - this.firstArgument];
        System.arraycopy(this.command, this.firstArgument, ret, 0, ret.length);
        return ret;
    }

    public @NotNull String getFocusArgument() {
        return this.command[this.firstArgument];
    }

    public int getFirstArgument() {
        return this.firstArgument;
    }

    public void setCommand(@NotNull String... args) {
        this.command = args;
    }

    public void setStartArgument(int start) {
        this.firstArgument = start;
    }

}
