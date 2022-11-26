package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FriendshipDao;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.service.FriendshipService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendshipServiceImpl implements FriendshipService {

    private final FriendshipDao friendshipDao;

    @Override
    public List<Friendship> getAllFriendships(Long userId) {
        return friendshipDao.findFriendshipsByUserId(userId);
    }

    @Override
    public void addFriendship(Long userId, Long friendId) {
        friendshipDao.add(userId, friendId);
    }

    @Override
    public void deleteFriendship(Long userId, Long friendId) {
        friendshipDao.delete(userId, friendId);
    }
}
