package ru.mopkovka.subscriptions.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.mopkovka.subscriptions.dto.request.CreateUserRequest;
import ru.mopkovka.subscriptions.dto.response.UserDto;
import ru.mopkovka.subscriptions.entity.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {

    public static User toEntity(CreateUserRequest request) {
        return User.builder()
                .email(request.email())
                .lastName(request.lastName())
                .firstName(request.firstName())
                .build();
    }

    public static UserDto toDto(User entity) {
        return UserDto.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .build();
    }
}
