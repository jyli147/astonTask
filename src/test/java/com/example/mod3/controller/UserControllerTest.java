package com.example.mod3.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.mod3.Dto.UserDto;
import com.example.mod3.service.serviceImpl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class UserControllerTest {

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testGetAllUsers() throws Exception {
        UserDto user1 = new UserDto();
        user1.setId(1L);
        user1.setName("Alice");
        user1.setEmail("alica@com.ru");
        user1.setAge(40);

        UserDto user2 = new UserDto();
        user2.setId(2L);
        user2.setName("Bob");
        user2.setEmail("Bob@com.ru");
        user2.setAge(40);
                List<UserDto> users = Arrays.asList(user1, user2);

        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Alice"))
                .andExpect(jsonPath("$[1].name").value("Bob"));
    }

    @Test
    public void testGetUserById() throws Exception {
        UserDto user = new UserDto();
        user.setId(1L);
        user.setName("Alice");
        user.setEmail("Alice@com.ru");
        user.setAge(40);

        when(userService.getUserById(anyLong())).thenReturn(user);

        mockMvc.perform(get("/api/v1/user/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Alice"));
    }

    @Test
    public void testCreateUser() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("Jyl");
        userDto.setEmail("jyl@com.ru");
        userDto.setAge(40);

        UserDto createdUser = new UserDto();
        userDto.setId(1L);
        userDto.setName("jylia");
        userDto.setEmail("jylia@com.ru");
        userDto.setAge(40);

        when(userService.createUser(any(UserDto.class))).thenReturn(createdUser);

        mockMvc.perform(post("/api/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"jylia\",\"email\":\"jylia@com.ru\",\"age\":40}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/user/1"));
    }

    @Test
    public void testUpdateUser() throws Exception {
        UserDto updatedUser = new UserDto();
        updatedUser.setId(1L);
        updatedUser.setName("jylia");
        updatedUser.setEmail("jylua@com.ru");
        updatedUser.setAge(40);


        when(userService.updateUser(anyLong(), any(UserDto.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/api/v1/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"jylia\",\"email\":\"jylua@com.ru\",\"age\":40}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("jylia"));
    }

    @Test
    public void testDeleteUser() throws Exception {
        doNothing().when(userService).deleteUser(anyLong());

        mockMvc.perform(delete("/api/v1/user/1"))
                .andExpect(status().isNoContent());
    }
}
