//package com.example.reddit.CommunitiesService.clients;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Component;
//import org.springframework.web.client.HttpClientErrorException;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.Optional;
//import java.util.UUID;
//
//@Component
//public class ThreadClient {
//    private final RestTemplate rest;
//    private final String baseUrl = "http://threads-service/api/threads";
//
//    @Autowired
//    public ThreadClient(RestTemplate restTemplate) {
//        this.rest = restTemplate;
//    }
//
//    /** Fetch a single thread by ID, returning empty if 404 */
//    public Optional<ThreadModel> getById(UUID threadId) {
//        try {
//            ThreadModel dto = rest.getForObject(
//                    baseUrl + "/{id}",
//                    Thread.class,
//                    threadId
//            );
//            return Optional.ofNullable(dto);
//        } catch (HttpClientErrorException ex) {
//            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
//                return Optional.empty();
//            }
//            throw ex;  // rethrow 5xx or other errors
//        }
//    }
//}
