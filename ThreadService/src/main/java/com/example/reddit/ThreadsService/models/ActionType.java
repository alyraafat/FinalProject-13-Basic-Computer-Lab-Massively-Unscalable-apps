package com.example.reddit.ThreadsService.models;

public enum ActionType {
    POST("com.example.reddit.ThreadsService.models.PostLog"),
    UPVOTE("com.example.reddit.ThreadsService.models.UpVoteLog"),
    DOWNVOTE("com.example.reddit.ThreadsService.models.DownVoteLog"),
    COMMENT("com.example.reddit.ThreadsService.models.CommentLog");

    private final String className;

    ActionType(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }
}