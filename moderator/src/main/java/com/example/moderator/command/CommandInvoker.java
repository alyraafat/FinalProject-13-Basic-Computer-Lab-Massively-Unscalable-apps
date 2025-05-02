package com.example.moderator.command;

public class CommandInvoker {
    public <T> T executeCommand(Command<T> command) {
        return command.execute();
    }
}