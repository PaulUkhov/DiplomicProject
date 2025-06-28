package org.example.diplomicproject.security;

import lombok.RequiredArgsConstructor;
import org.example.diplomicproject.domain.Role;
import org.example.diplomicproject.domain.User;
import org.example.diplomicproject.repository.RoleRepository;
import org.example.diplomicproject.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserPasswordService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    public void registerUser(String login, String password) {
        String encryptedPassword = passwordEncoder.encode(password);
        User user = new User();
        user.setLogin(login);
        user.setPassword(encryptedPassword);

        // Получаем роль из БД
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Role not found"));

        user.getRoles().add(userRole);

        userRepository.save(user);
    }

}
