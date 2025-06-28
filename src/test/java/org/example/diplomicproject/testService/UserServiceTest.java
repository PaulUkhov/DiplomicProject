package org.example.diplomicproject.testService;

import org.example.diplomicproject.domain.User;
import org.example.diplomicproject.repository.UserRepository;
import org.example.diplomicproject.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setLogin("testuser");
        user.setEmail("test@example.com");
    }

    @Test
    void testFindAll() {
        List<User> users = List.of(user);
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.findAll();

        assertEquals(1, result.size());
        assertEquals("testuser", result.get(0).getLogin());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = userService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().getLogin());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByLogin() {
        when(userRepository.findByLogin("testuser")).thenReturn(Optional.of(user));

        Optional<User> result = userService.findByLogin("testuser");

        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().getLogin());
        verify(userRepository, times(1)).findByLogin("testuser");
    }

    @Test
    void testRegisterUserSuccess() {
        when(userRepository.findByLogin(user.getLogin())).thenReturn(Optional.empty());
        when(userRepository.findAll()).thenReturn(Collections.emptyList());
        when(userRepository.save(user)).thenReturn(user);

        User registered = userService.registerUser(user);

        assertNotNull(registered);
        assertEquals("testuser", registered.getLogin());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testRegisterUserLoginExists() {
        when(userRepository.findByLogin(user.getLogin())).thenReturn(Optional.of(user));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.registerUser(user));
        assertEquals("Пользователь с таким логином уже существует", ex.getMessage());

        verify(userRepository, never()).save(any());
    }

    @Test
    void testRegisterUserEmailExists() {
        User otherUser = new User();
        otherUser.setEmail("test@example.com");

        when(userRepository.findByLogin(user.getLogin())).thenReturn(Optional.empty());
        when(userRepository.findAll()).thenReturn(List.of(otherUser));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.registerUser(user));
        assertEquals("Пользователь с таким email уже существует", ex.getMessage());

        verify(userRepository, never()).save(any());
    }

    @Test
    void testUpdateUserSuccess() {
        when(userRepository.existsById(user.getId())).thenReturn(true);
        when(userRepository.save(user)).thenReturn(user);

        User updated = userService.updateUser(user);

        assertNotNull(updated);
        assertEquals("testuser", updated.getLogin());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testUpdateUserNotFound() {
        when(userRepository.existsById(user.getId())).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.updateUser(user));
        assertEquals("Пользователь не найден", ex.getMessage());

        verify(userRepository, never()).save(any());
    }

    @Test
    void testDeleteByIdSuccess() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteById(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteByIdNotFound() {
        when(userRepository.existsById(1L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.deleteById(1L));
        assertEquals("Пользователь не найден", ex.getMessage());

        verify(userRepository, never()).deleteById(anyLong());
    }
}
