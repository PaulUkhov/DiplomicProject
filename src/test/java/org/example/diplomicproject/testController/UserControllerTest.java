package org.example.diplomicproject.testController;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.diplomicproject.controller.UserController;
import org.example.diplomicproject.domain.User;
import org.example.diplomicproject.security.JwtAuthenticationFilter;
import org.example.diplomicproject.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class))

@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setLogin("john");
        user.setPassword("pass123");
    }

    @Test
    @WithMockUser
    void testGetAllUsers() throws Exception {
        Mockito.when(userService.findAll()).thenReturn(List.of(user));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].login").value("john"));
    }

    @Test
    @WithMockUser
    void testGetUserById_found() throws Exception {
        Mockito.when(userService.findById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login").value("john"));
    }

    @Test
    @WithMockUser
    void testGetUserById_notFound() throws Exception {
        Mockito.when(userService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void testGetUserByLogin_found() throws Exception {
        Mockito.when(userService.findByLogin("john")).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/users/by-login/john"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser
    void testGetUserByLogin_notFound() throws Exception {
        Mockito.when(userService.findByLogin("john")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/by-login/john"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void testRegisterUser_success() throws Exception {
        Mockito.when(userService.registerUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/users/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser
    void testRegisterUser_conflict() throws Exception {
        Mockito.when(userService.registerUser(any(User.class))).thenThrow(new RuntimeException());

        mockMvc.perform(post("/api/users/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockUser
    void testUpdateUser_success() throws Exception {
        Mockito.when(userService.updateUser(any(User.class))).thenReturn(user);

        mockMvc.perform(put("/api/users/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser
    void testUpdateUser_idMismatch() throws Exception {
        user.setId(2L);
        mockMvc.perform(put("/api/users/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void testUpdateUser_notFound() throws Exception {
        Mockito.when(userService.updateUser(any(User.class))).thenThrow(new RuntimeException());

        mockMvc.perform(put("/api/users/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void testDeleteUser_success() throws Exception {
        Mockito.doNothing().when(userService).deleteById(1L);

        mockMvc.perform(delete("/api/users/1").with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void testDeleteUser_notFound() throws Exception {
        Mockito.doThrow(new RuntimeException()).when(userService).deleteById(1L);

        mockMvc.perform(delete("/api/users/1").with(csrf()))
                .andExpect(status().isNotFound());
    }
}

