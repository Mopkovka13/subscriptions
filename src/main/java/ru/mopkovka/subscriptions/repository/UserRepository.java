package ru.mopkovka.subscriptions.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.mopkovka.subscriptions.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT COUNT(u) > 0 " +
            "FROM User u " +
            "WHERE u.email = :email")
    public boolean existByEmail(@Param("email") String email);
}
