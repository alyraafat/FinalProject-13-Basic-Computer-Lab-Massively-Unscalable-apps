package com.omarahmed.reddit.exception;

public class RedditAppException extends RuntimeException {
    public RedditAppException(String exceptionMessage) {
        super(exceptionMessage);
    }

    public RedditAppException() {
    }

}
