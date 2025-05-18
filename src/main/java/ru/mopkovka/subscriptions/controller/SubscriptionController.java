package ru.mopkovka.subscriptions.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.mopkovka.subscriptions.dto.response.SubscriptionDto;
import ru.mopkovka.subscriptions.service.SubscriptionService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/subscriptions")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @GetMapping("/top")
    public ResponseEntity<List<SubscriptionDto>> getTopSubscriptions(
            @RequestParam(required = false, defaultValue = "3") Integer count
    ) {
        List<SubscriptionDto> subscriptions = subscriptionService.getTopSubscriptions(count);
        return ResponseEntity.ok(subscriptions);
    }
}
