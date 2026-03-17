package com.loop.cases.client;

import com.loop.cases.dto.LifestageUserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "lifestage")
public interface LifestageClient {

    @GetMapping("/users/{id}")
    LifestageUserDTO getUserById(
        @PathVariable("id") String id,
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader
    );

    @PostMapping("/users/{userId}/policies")
    LifestageUserDTO addPolicyToUser(
        @PathVariable("userId") String userId,
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
        @RequestBody Long policyId
    );
}