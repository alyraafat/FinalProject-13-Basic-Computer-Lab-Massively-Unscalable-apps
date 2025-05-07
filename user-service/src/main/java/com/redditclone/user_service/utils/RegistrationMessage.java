package com.redditclone.user_service.utils;


public class RegistrationMessage {

    private final String serviceUrl;
    private final String token;

    public RegistrationMessage(String serviceUrl, String token) {
        this.serviceUrl = serviceUrl;
        this.token = token;
    }

    public String toString() {
        return "Thank you for signing up to Reddit Replica. " +
                "Please click <a href=\""
                + serviceUrl
                + "/public/activateAccount/"
                + token
                + "\">Activate your account</a>.";
    }
}
