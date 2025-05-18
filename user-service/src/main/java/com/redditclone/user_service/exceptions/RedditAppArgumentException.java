package com.redditclone.user_service.exceptions;

public class RedditAppArgumentException extends RuntimeException {
    public RedditAppArgumentException(String exceptionMessage) {
        super(exceptionMessage);
    }

    public RedditAppArgumentException() {
    }

}
