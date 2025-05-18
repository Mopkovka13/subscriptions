package ru.mopkovka.subscriptions.dto.response;

import lombok.Builder;

@Builder
public record UserDto(
    Long id,
    String firstName,
    String lastName,
    String email
) {
}
