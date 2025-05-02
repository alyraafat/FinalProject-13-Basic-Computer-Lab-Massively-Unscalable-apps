package com.example.moderator.command;

public interface Command<T> {
    T execute();
}