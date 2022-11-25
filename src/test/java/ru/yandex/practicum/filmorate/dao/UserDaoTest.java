package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.yandex.practicum.filmorate.util.TestModel.getValidUser;

@SpringBootTest
@AutoConfigureTestDatabase
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDaoTest {

    private final UserDao userDao;

    @Test
    @Order(1)
    public void testSave() {
        User user = userDao.save(getValidUser());
        User response = userDao.findById(user.getId());

        assertThat(response)
                .isNotNull()
                .isEqualTo(user);
    }

    @Test
    @Order(2)
    public void testUpdate() {
        User user = userDao.findById(1L);
        user.setName("Updated");
        userDao.update(user);

        User response = userDao.findById(user.getId());

        assertThat(response)
                .isNotNull()
                .hasFieldOrPropertyWithValue("name", "Updated");
    }

    @Test
    @Order(3)
    public void testFindAll() {
        User user = getValidUser();
        userDao.save(user);

        List<User> users = userDao.findAll();

        assertThat(users)
                .isNotNull()
                .isNotEmpty()
                .hasSize(2);
    }

    @Test
    @Order(4)
    public void testFindById() {
        User user = userDao.findById(1L);

        assertThat(user)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    @Order(5)
    public void testDeleteById() {
        userDao.deleteById(1L);

        List<User> users = userDao.findAll();

        assertThat(users)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        User user = users.get(0);

        assertThat(user.getId()).isEqualTo(2L);
    }
}
