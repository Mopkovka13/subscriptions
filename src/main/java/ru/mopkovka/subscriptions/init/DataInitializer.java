package ru.mopkovka.subscriptions.init;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.mopkovka.subscriptions.entity.Subscription;
import ru.mopkovka.subscriptions.repository.SubscriptionRepository;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final SubscriptionRepository subscriptionRepository;

    @Override
    public void run(String... args) throws Exception {
        if (subscriptionRepository.count() == 0) {
            List<Subscription> subscriptions = List.of(
                    new Subscription("YouTube Premium", "Подпишись не ленись на канал", new BigDecimal("9.99")),
                    new Subscription("VK Музыка", "Лайк поставь", new BigDecimal("19.99")),
                    new Subscription("Яндекс.Плюс", "Он выглядит словно палец", new BigDecimal("29.99")),
                    new Subscription("Netflix", "Который направлен вверх", new BigDecimal("29.99"))
            );

            subscriptionRepository.saveAll(subscriptions);
            log.info("Initial subscriptions loaded");
        }
    }
}
