package org.mose.command.context;

import org.mose.command.CommandArgument;

public class CommandArgumentContext<T> {

    private final CommandArgument<T> argument;
    private int firstArgument;
    private String[] command;

    public CommandArgumentContext(CommandArgument<T> argument, int firstArgument, String... command) {
        this.argument = argument;
        this.firstArgument = firstArgument;
        this.command = command;
    }

    public CommandArgument<T> getArgument() {
        return this.argument;
    }

    public String[] getRemainingArguments() {
        int last = this.command.length;
        String[] ret = new String[last - this.firstArgument];
        System.arraycopy(this.command, this.firstArgument, ret, 0, ret.length);
        return ret;
    }

    public String getFocusArgument() {
        return this.command[this.firstArgument];
    }

    public int getFirstArgument() {
        return this.firstArgument;
    }

    public void setCommand(String... args) {
        this.command = args;
    }

    public void setStartArgument(int start) {
        this.firstArgument = start;
    }

}
