package com.redditclone.communities_service.publishers;


import com.redditclone.communities_service.listeners.Observer;

public interface Subject {

    void notifyObservers();
    void registerObserver(Observer observer);
    void removeObserver(Observer observer);
}
