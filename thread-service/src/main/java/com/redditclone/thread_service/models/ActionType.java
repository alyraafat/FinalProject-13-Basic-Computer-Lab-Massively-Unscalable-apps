package com.redditclone.thread_service.models;

public enum ActionType {
    POST("com.example.reddit.ThreadsService.models.PostManufacturer"),
    UPVOTE("com.example.reddit.ThreadsService.models.UpVoteManufacturer"),
    DOWNVOTE("com.example.reddit.ThreadsService.models.DownVoteManufacturer"),
    COMMENT("com.example.reddit.ThreadsService.models.CommentManufacturer");

    private final String className;

    ActionType(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }
}