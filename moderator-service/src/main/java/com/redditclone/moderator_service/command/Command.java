package com.redditclone.moderator_service.command;

public interface Command<T> {
    T execute();
}