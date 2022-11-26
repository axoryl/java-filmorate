package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Friendship {

    private Long userId;

    private Long friendId;

    private boolean status;
}
