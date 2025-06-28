package org.example.diplomicproject.service;

import lombok.RequiredArgsConstructor;
import org.example.diplomicproject.domain.User;
import org.example.diplomicproject.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // Получить всех пользователей
    public List<User> findAll() {
        return userRepository.findAll();
    }

    // Найти пользователя по ID
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    // Найти пользователя по логину
    public Optional<User> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    // Зарегистрировать нового пользователя
    public User registerUser(User user) {
        // Проверяем, что логин и email уникальны
        if (userRepository.findByLogin(user.getLogin()).isPresent()) {
            throw new RuntimeException("Пользователь с таким логином уже существует");
        }
        if (userRepository.findAll().stream().anyMatch(u -> u.getEmail().equals(user.getEmail()))) {
            throw new RuntimeException("Пользователь с таким email уже существует");
        }
        // Здесь можно добавить шифрование пароля, проверку и т.п.
        return userRepository.save(user);
    }

    // Обновить пользователя
    public User updateUser(User user) {
        if (user.getId() == null || !userRepository.existsById(user.getId())) {
            throw new RuntimeException("Пользователь не найден");
        }
        return userRepository.save(user);
    }

    // Удалить пользователя по ID
    public void deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Пользователь не найден");
        }
        userRepository.deleteById(id);
    }
}
