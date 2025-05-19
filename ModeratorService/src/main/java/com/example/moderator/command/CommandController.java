package com.example.moderator.command;

import org.springframework.stereotype.Component;

@Component
public class CommandController<T> {

    private Command<T> command;

    public void setCommand(Command<T> command) {
        this.command = command;
    }

    public T executeCommand() {
        return command.execute();
    }
}