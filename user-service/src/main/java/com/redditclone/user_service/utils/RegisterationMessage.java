package com.redditclone.user_service.utils;


public class RegisterationMessage {

    private final String serviceUrl;
    private final String token;

    public RegisterationMessage(String serviceUrl, String token) {
        this.serviceUrl = serviceUrl;
        this.token = token;
    }

    public String toString() {
        System.out.println("serviceUrl = " + serviceUrl);
        return "Thank you for signing up to Reddit Replica. " +
                "Please click <a href=\""
                + serviceUrl
                + "/api/auth/activateAccount/"
                + token
                + "\">Activate your account</a>.";
    }
}
