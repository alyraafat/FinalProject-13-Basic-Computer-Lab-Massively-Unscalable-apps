package com.redditclone.user_service.exceptions;

public class RedditAppException extends RuntimeException {
    public RedditAppException(String exceptionMessage) {
        super(exceptionMessage);
    }

    public RedditAppException() {
    }

}
