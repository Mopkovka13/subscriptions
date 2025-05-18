package ru.mopkovka.subscriptions.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.mopkovka.subscriptions.entity.UserSubscription;

import java.util.List;

public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Long> {
    List<UserSubscription> findByUserId(Long userId);
    List<UserSubscription> findByUserIdAndIsActive(Long userId, Boolean isActive);
    @Query("SELECT us FROM UserSubscription us " +
            "WHERE us.subscription IN (" +
            "   SELECT sub.subscription FROM UserSubscription sub " +
            "   GROUP BY sub.subscription " +
            "   ORDER BY COUNT(sub.user) DESC" +
            ")")
    List<UserSubscription> getTop(Pageable pageable);
}
