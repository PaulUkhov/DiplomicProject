package org.example.diplomicproject.service;

import lombok.RequiredArgsConstructor;
import org.example.diplomicproject.domain.User;
import org.example.diplomicproject.repository.UserRepository;
import org.example.diplomicproject.security.CustomUserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

//Загрузить пользователя из базы данных и преобразовать
@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(username).
                orElseThrow(() -> new UsernameNotFoundException("username not found"));
        return new CustomUserDetails(user.getLogin(), user.getPassword(), user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList()));
    }

}