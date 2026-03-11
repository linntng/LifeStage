package com.loop.cases.service;

import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserRetrieverService {
    @Value("${external.user.api.url}")
    private String externalUserApiUrl;

    WebClient webClient = WebClient.create();

    // Coupled to backend/lifestage version of UserDTO
    private static class UserDTO {
        // These other fields are unneeded
        // public String id;
        // public String username;
        // public List<Long> lifeEventIds;
        public String role;
    }

    public Mono<String> getUserRole(String userId, String accessToken) {
        return webClient.get()
                .uri(externalUserApiUrl + "/users/" + userId)
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(UserDTO.class)
                .map(userDTO -> {
                    return userDTO.role;
            });
    }
}
