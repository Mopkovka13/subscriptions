package ru.mopkovka.subscriptions.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mopkovka.subscriptions.entity.Subscription;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
}
