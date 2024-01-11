package org.mose.command.context;

import java.util.stream.IntStream;

public class ArgumentContext {

    private final String[] rawCommand;
    private final int targetArgument;

    public ArgumentContext(int targetArgument, String... rawCommand){
        this.rawCommand = rawCommand;
        this.targetArgument = targetArgument;
    }

    public String getFocusArgument(){
        return this.rawCommand[this.targetArgument];
    }

    public int getArgumentIndex(){
        return this.targetArgument;
    }

    public String[] getRemainingArguments(){
        int remainingLength = this.rawCommand.length - this.targetArgument;
        return IntStream
                .range(0, remainingLength)
                .mapToObj(i -> this.rawCommand[this.targetArgument + i])
                .toArray(String[]::new);
    }

}
