package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.yandex.practicum.filmorate.util.TestModel.getValidUser;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FriendshipDaoTest {

    private final FriendshipDao friendshipDao;
    private final UserDao userDao;

    @Test
    @Order(1)
    public void testAdd() {
        User user1 = getValidUser();
        User user2 = getValidUser();
        User user3 = getValidUser();
        userDao.save(user1);
        userDao.save(user2);
        userDao.save(user3);

        friendshipDao.add(1L, 2L);

        Friendship friendship = Friendship.builder()
                .userId(1L)
                .friendId(2L)
                .status(true)
                .build();
        List<Friendship> friendships = friendshipDao.findFriendshipsByUserId(1L);

        assertThat(friendships)
                .isNotNull()
                .isNotEmpty()
                .contains(friendship);
    }

    @Test
    @Order(2)
    public void testFindFriendshipsByUserId() {
        friendshipDao.add(1L, 3L);

        List<Friendship> friendships = friendshipDao.findFriendshipsByUserId(1L);

        assertThat(friendships)
                .isNotNull()
                .isNotEmpty()
                .hasSize(2);
    }


    @Test
    @Order(3)
    public void testDelete() {
        friendshipDao.delete(1L, 2L);
        friendshipDao.delete(1L, 3L);

        List<Friendship> friendships = friendshipDao.findFriendshipsByUserId(1L);

        assertThat(friendships).isEmpty();
    }

}
