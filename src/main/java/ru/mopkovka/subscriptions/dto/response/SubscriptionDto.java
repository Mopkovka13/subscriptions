package ru.mopkovka.subscriptions.dto.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record SubscriptionDto(
    LocalDate startDate,
    LocalDate endDate,
    BigDecimal price,
    String name,
    String description
) {
}
