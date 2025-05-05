package com.example.reddit.CommunitiesService.clients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.reddit.CommunitiesService.models.CommunityThread;

import java.util.Optional;
import java.util.UUID;

@Component
public class ThreadClient {
    private final RestTemplate rest;
    private final String baseUrl = "http://threads-service/api/threads";

    @Autowired
    public ThreadClient(RestTemplate restTemplate) {
        this.rest = restTemplate;
    }

    /** Fetch a single thread by ID, returning empty if 404 */
    public Optional<CommunityThread> getById(UUID threadId) {
        try {
            CommunityThread dto = rest.getForObject(
                    baseUrl + "/{id}",
                    CommunityThread.class,
                    threadId
            );
            return Optional.ofNullable(dto);
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                return Optional.empty();
            }
            throw ex;  // rethrow 5xx or other errors
        }
    }
}
