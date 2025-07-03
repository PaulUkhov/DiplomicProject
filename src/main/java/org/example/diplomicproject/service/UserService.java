package org.example.diplomicproject.service;

import lombok.RequiredArgsConstructor;
import org.example.diplomicproject.domain.User;
import org.example.diplomicproject.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // Получить всех пользователей
    public List<User> findAll() {
        log.info("Получение всех пользователей");
        List<User> users = userRepository.findAll();
        log.debug("Найдено {} пользователей", users.size());
        return users;
    }

    // Найти пользователя по ID
    public Optional<User> findById(Long id) {
        log.info("Поиск пользователя по ID: {}", id);
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            log.debug("Пользователь найден: login = {}", user.get().getLogin());
        } else {
            log.warn("Пользователь с ID {} не найден", id);
        }
        return user;
    }

    // Найти пользователя по логину
    public Optional<User> findByLogin(String login) {
        log.info("Поиск пользователя по логину: {}", login);
        Optional<User> user = userRepository.findByLogin(login);
        if (user.isPresent()) {
            log.debug("Пользователь найден: ID = {}", user.get().getId());
        } else {
            log.warn("Пользователь с логином {} не найден", login);
        }
        return user;
    }

    // Зарегистрировать нового пользователя
    public User registerUser(User user) {
        log.info("Регистрация нового пользователя: login = {}", user.getLogin());

        if (userRepository.findByLogin(user.getLogin()).isPresent()) {
            log.warn("Регистрация не удалась — логин '{}' уже занят", user.getLogin());
            throw new RuntimeException("Пользователь с таким логином уже существует");
        }

        boolean emailExists = userRepository.findAll().stream()
                .anyMatch(u -> u.getEmail().equals(user.getEmail()));

        if (emailExists) {
            log.warn("Регистрация не удалась — email '{}' уже используется", user.getEmail());
            throw new RuntimeException("Пользователь с таким email уже существует");
        }

        User savedUser = userRepository.save(user);
        log.info("Пользователь успешно зарегистрирован: ID = {}, login = {}", savedUser.getId(), savedUser.getLogin());
        return savedUser;
    }

    // Обновить пользователя
    public User updateUser(User user) {
        log.info("Обновление пользователя: ID = {}", user.getId());

        if (user.getId() == null || !userRepository.existsById(user.getId())) {
            log.error("Обновление не удалось — пользователь с ID {} не найден", user.getId());
            throw new RuntimeException("Пользователь не найден");
        }

        User updatedUser = userRepository.save(user);
        log.debug("Пользователь обновлён: login = {}", updatedUser.getLogin());
        return updatedUser;
    }

    // Удалить пользователя по ID
    public void deleteById(Long id) {
        log.info("Удаление пользователя с ID: {}", id);

        if (!userRepository.existsById(id)) {
            log.error("Удаление не удалось — пользователь с ID {} не найден", id);
            throw new RuntimeException("Пользователь не найден");
        }

        userRepository.deleteById(id);
        log.info("Пользователь с ID {} успешно удалён", id);
    }
}
