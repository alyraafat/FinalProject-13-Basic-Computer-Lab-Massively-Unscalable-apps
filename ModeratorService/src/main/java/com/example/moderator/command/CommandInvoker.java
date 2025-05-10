package com.example.moderator.command;

import org.springframework.stereotype.Component;

@Component
public class CommandInvoker {
    public <T> T execute(Command<T> command) {
        return command.execute();
    }
}