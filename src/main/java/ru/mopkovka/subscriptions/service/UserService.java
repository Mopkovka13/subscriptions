package ru.mopkovka.subscriptions.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mopkovka.subscriptions.dto.request.AddSubscriptionRequest;
import ru.mopkovka.subscriptions.dto.request.CreateUserRequest;
import ru.mopkovka.subscriptions.dto.request.UpdateUserRequest;
import ru.mopkovka.subscriptions.dto.response.SubscriptionDto;
import ru.mopkovka.subscriptions.dto.response.UserDto;
import ru.mopkovka.subscriptions.entity.Subscription;
import ru.mopkovka.subscriptions.entity.User;
import ru.mopkovka.subscriptions.entity.UserSubscription;
import ru.mopkovka.subscriptions.exception.NotFoundException;
import ru.mopkovka.subscriptions.exception.ServiceException;
import ru.mopkovka.subscriptions.mapper.SubscriptionMapper;
import ru.mopkovka.subscriptions.mapper.UserMapper;
import ru.mopkovka.subscriptions.repository.SubscriptionRepository;
import ru.mopkovka.subscriptions.repository.UserRepository;
import ru.mopkovka.subscriptions.repository.UserSubscriptionRepository;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;

    @Transactional
    public UserDto createUser(
            CreateUserRequest request
    ) {
        log.info("Создание пользователя с email: {}", request.email());

        User user = UserMapper.toEntity(request);

        if (userRepository.existByEmail(request.email())) {
            throw new ServiceException("Пользовать с таким email уже существует.");
        }

        try {
            User savedUser = userRepository.save(user);
            log.debug("Пользователь создан с идентификатором: {}", savedUser.getId());

            return UserMapper.toDto(savedUser);
        } catch (Exception ex) {
            log.error("Ошибка создания пользователя: {}", ex.getMessage());
            throw new ServiceException("Ошибка создания пользователя");
        }
    }

    @Transactional(readOnly = true)
    public UserDto getUser(
            Long id
    ) {
        log.info("Получение пользователя с ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        return UserMapper.toDto(user);
    }

    public UserDto updateUser(
            Long id,
            UpdateUserRequest request
    ) {
        log.info("Обновление пользователя с ID: {}", id);

        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

            if (request.email() != null && !request.email().equals(user.getEmail())) {
                if (userRepository.existByEmail(request.email())) {
                    throw new ServiceException("Email уже используется другим пользователем");
                }
                user.setEmail(request.email());
            }

            if (request.firstName() != null) {
                user.setFirstName(request.firstName());
            }

            if (request.lastName() != null) {
                user.setLastName(request.lastName());
            }

            User updatedUser = userRepository.save(user);
            log.debug("Пользователь обновлён: {}", id);

            return UserMapper.toDto(updatedUser);
        } catch (Exception ex) {
            log.error("Ошибка обновления пользователя: {}", ex.getMessage());
            throw new ServiceException("Ошибка обновления пользователя");
        }
    }

    @Transactional
    public void deleteUser(Long id) {
        log.info("Удаление пользователя с ID: {}", id);
        try {
            if (!userRepository.existsById(id)) {
                throw new NotFoundException("Пользователь не найден");
            }
            userRepository.deleteById(id);
            log.debug("Пользователь удален: {}", id);
        } catch (Exception ex) {
            log.error("Ошибка удаления пользователя: {}", ex.getMessage());
            throw new ServiceException("Ошибка удаления пользователя");
        }
    }

    @Transactional
    public SubscriptionDto addSubscription(
            Long userId,
            AddSubscriptionRequest request
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException("Пользователь не найден"));

        Subscription subscription = subscriptionRepository.findById(request.subscriptionId())
                .orElseThrow(() -> new ServiceException("Подписка не найдена"));

        try {
            LocalDate currentDate = LocalDate.now(Clock.systemUTC());
            LocalDate endDate = currentDate.plusDays(request.countDays());

            UserSubscription userSubscription = UserSubscription.builder()
                    .user(user)
                    .subscription(subscription)
                    .price(subscription.getActualPrice())
                    .startDate(currentDate)
                    .endDate(endDate)
                    .isActive(true)
                    .build();

            UserSubscription savedSubscription = userSubscriptionRepository.save(userSubscription);
            log.debug("Подписка добавлена: {}", savedSubscription.getId());

            return SubscriptionMapper.toDto(savedSubscription);
        } catch (Exception ex) {
            log.error("Ошибка добавления подписки: {}", ex.getMessage());
            throw new ServiceException("Ошибка добавления подписки");
        }
    }

    @Transactional(readOnly = true)
    public List<SubscriptionDto> getUserSubscriptions(
            Long userId,
            Boolean activeOnly
    ) {
        log.info("Получение подписок пользователя ID: {}, activeOnly: {}", userId, activeOnly);

        try {
            List<UserSubscription> subscriptions;
            if (Boolean.TRUE.equals(activeOnly)) {
                subscriptions = userSubscriptionRepository.findByUserIdAndIsActive(userId, true);
            } else {
                subscriptions = userSubscriptionRepository.findByUserId(userId);
            }
            return SubscriptionMapper.toDtos(subscriptions);
        } catch (Exception ex) {
            log.error("Ошибка получения подписок: {}", ex.getMessage());
            throw new ServiceException("Ошибка получения подписок");
        }
    }

    @Transactional
    public void removeSubscription(
            Long userId,
            Long subscriptionId
    ) {
        log.info("Удаление подписки ID: {} для пользователя ID: {}", subscriptionId, userId);

        UserSubscription userSubscription = userSubscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new NotFoundException("подписка не найдена"));

        try {
            if (!userSubscription.getIsActive()) {
                log.warn("Подписка уже деактивирована");
                return;
            }
            userSubscription.setIsActive(false);

            userSubscriptionRepository.save(userSubscription);
            log.debug("Подписка деактивирована: {}", subscriptionId);
        } catch (Exception ex) {
            log.error("Ошибка удаления подписки: {}", ex.getMessage());
            throw new ServiceException("Ошибка удаления подписки");
        }
    }
}
