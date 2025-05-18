package ru.mopkovka.subscriptions.dto.request;

public record AddSubscriptionRequest(
        Long subscriptionId,
        Long countDays
) {
}
