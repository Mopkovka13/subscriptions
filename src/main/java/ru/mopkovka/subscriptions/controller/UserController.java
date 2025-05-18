package ru.mopkovka.subscriptions.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mopkovka.subscriptions.dto.request.AddSubscriptionRequest;
import ru.mopkovka.subscriptions.dto.request.CreateUserRequest;
import ru.mopkovka.subscriptions.dto.request.UpdateUserRequest;
import ru.mopkovka.subscriptions.dto.response.SubscriptionDto;
import ru.mopkovka.subscriptions.dto.response.UserDto;
import ru.mopkovka.subscriptions.service.UserService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> createUser(
            @RequestBody CreateUserRequest request
    ) {
        UserDto createdUser = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(
            @PathVariable Long id
    ) {
        UserDto user = userService.getUser(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable Long id,
            @RequestBody UpdateUserRequest request
    ) {
        UserDto updatedUser = userService.updateUser(id, request);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id
    ) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("{userId}/subscriptions")
    public ResponseEntity<SubscriptionDto> addSubscription(
            @PathVariable Long userId,
            @RequestBody AddSubscriptionRequest request
    ) {
        SubscriptionDto subscription = userService.addSubscription(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(subscription);
    }

    @GetMapping("{userId}/subscriptions")
    public ResponseEntity<List<SubscriptionDto>> getUserSubscriptions(
            @PathVariable Long userId,
            @RequestParam(required = false, defaultValue = "true") Boolean activeOnly
    ) {
        List<SubscriptionDto> subscriptions = userService.getUserSubscriptions(userId, activeOnly);
        return ResponseEntity.ok(subscriptions);
    }

    @DeleteMapping("{userId}/subscriptions/{subscriptionId}")
    public ResponseEntity<Void> removeSubscription(
            @PathVariable Long userId,
            @PathVariable Long subscriptionId
    ) {
        userService.removeSubscription(userId, subscriptionId);
        return ResponseEntity.noContent().build();
    }
}
