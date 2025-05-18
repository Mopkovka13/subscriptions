package ru.mopkovka.subscriptions.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.mopkovka.subscriptions.dto.response.SubscriptionDto;
import ru.mopkovka.subscriptions.entity.UserSubscription;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SubscriptionMapper {
    public static SubscriptionDto toDto(UserSubscription userSubscription) {
        return SubscriptionDto.builder()
                .startDate(userSubscription.getStartDate())
                .endDate(userSubscription.getEndDate())
                .price(userSubscription.getPrice())
                .name(userSubscription.getSubscription().getName())
                .description(userSubscription.getSubscription().getDescription())
                .build();
    }

    public static List<SubscriptionDto> toDtos(List<UserSubscription> userSubscriptions) {
        return userSubscriptions.stream()
                .map(SubscriptionMapper::toDto)
                .toList();
    }
}
