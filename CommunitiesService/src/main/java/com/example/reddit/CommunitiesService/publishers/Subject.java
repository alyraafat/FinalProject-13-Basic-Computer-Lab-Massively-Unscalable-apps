package com.example.reddit.CommunitiesService.publishers;


import com.example.reddit.CommunitiesService.events.CommunityMemberAddedEvent;
import com.example.reddit.CommunitiesService.listeners.Observer;

public interface Subject {

    void notifyObservers();
    void registerObserver(Observer observer);
    void removeObserver(Observer observer);
}
