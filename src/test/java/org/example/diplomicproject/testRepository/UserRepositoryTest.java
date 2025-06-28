package org.example.diplomicproject.testRepository;

import org.example.diplomicproject.domain.User;
import org.example.diplomicproject.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByLogin() {
        // Создаем и сохраняем пользователя
        User user = new User();
        user.setLogin("testlogin");
        user.setEmail("testlogin@example.com");
        // добавь другие обязательные поля, если есть
        user = userRepository.save(user);

        // Пытаемся найти пользователя по логину
        Optional<User> foundUser = userRepository.findByLogin("testlogin");

        // Проверяем, что пользователь найден и логин совпадает
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getLogin()).isEqualTo("testlogin");
    }
}

