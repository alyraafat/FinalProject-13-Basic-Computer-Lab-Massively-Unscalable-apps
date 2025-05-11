package com.example.miniapp.exceptions;

public class RedditAppException extends RuntimeException {
    public RedditAppException(String exceptionMessage) {
        super(exceptionMessage);
    }

    public RedditAppException() {
    }

}
