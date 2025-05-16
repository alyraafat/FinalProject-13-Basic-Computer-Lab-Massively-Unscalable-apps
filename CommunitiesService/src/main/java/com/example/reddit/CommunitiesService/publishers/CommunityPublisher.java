package com.example.reddit.CommunitiesService.publishers;
import com.example.reddit.CommunitiesService.events.CommunityMemberAddedEvent;
import com.example.reddit.CommunitiesService.listeners.Observer;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class CommunityPublisher implements Subject {
    private  CommunityMemberAddedEvent event;
    private  List<Observer> observers;

    public CommunityPublisher() {
        this.observers = new ArrayList<>();
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(event);
        }
    }

    public void setMember(CommunityMemberAddedEvent event) {
        this.event = event;
        MemberAdded();
    }

    private void MemberAdded() {
        notifyObservers();
    }

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }
}
