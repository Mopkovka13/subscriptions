package ru.mopkovka.subscriptions.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.mopkovka.subscriptions.dto.response.SubscriptionDto;
import ru.mopkovka.subscriptions.entity.UserSubscription;
import ru.mopkovka.subscriptions.exception.ServiceException;
import ru.mopkovka.subscriptions.mapper.SubscriptionMapper;
import ru.mopkovka.subscriptions.repository.UserSubscriptionRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final UserSubscriptionRepository userSubscriptionRepository;

    public List<SubscriptionDto> getTopSubscriptions(Integer count) {
        log.info("Получение топа популярных подписок");

        try {
            PageRequest pageRequest = PageRequest.of(0, count);
            List<UserSubscription> subscriptions = userSubscriptionRepository.getTop(pageRequest);

            return SubscriptionMapper.toDtos(subscriptions);
        } catch (Exception ex) {
            log.error("Ошибка получения топа популярных подписок: {}", ex.getMessage());
            throw new ServiceException("Ошибка получения топа популярных подписок");
        }
    }
}
