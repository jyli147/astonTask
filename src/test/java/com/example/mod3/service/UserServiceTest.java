package com.example.mod3.service;

import com.example.mod3.Dto.UserDto;
import com.example.mod3.Dto.mapping.UserDtoMapping;
import com.example.mod3.model.User;
import com.example.mod3.repository.UserRepository;
import com.example.mod3.service.serviceImpl.UserServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserDtoMapping userDtoMapping;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllUsers() {
        // Подготовка
        User user1 = new User();
        user1.setId(1L);
        user1.setName("Alice");
        user1.setEmail("alice@example.com");
        user1.setAge(25);

        User user2 = new User();
        user2.setId(2L);
        user2.setName("Bob");
        user2.setEmail("bob@example.com");
        user2.setAge(30);

        UserDto userDto1 = new UserDto();
        userDto1.setId(1L);
        userDto1.setName("Alice");
        userDto1.setEmail("alice@example.com");
        userDto1.setAge(25);

        UserDto userDto2 = new UserDto();
        userDto2.setId(2L);
        userDto2.setName("Bob");
        userDto2.setEmail("bob@example.com");
        userDto2.setAge(30);


        List<User> users = Arrays.asList(user1, user2);
        List<UserDto> userDtos = Arrays.asList(userDto1, userDto2);

        when(userRepository.findAll()).thenReturn(users);
        when(userDtoMapping.toDto(user1)).thenReturn(userDtos.get(0));
        when(userDtoMapping.toDto(user2)).thenReturn(userDtos.get(1));

        // Действие
        List<UserDto> result = userService.getAllUsers();

        // Проверка
        assertEquals(2, result.size());
        assertEquals("Alice", result.get(0).getName());
        assertEquals("Bob", result.get(1).getName());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testGetUserById_UserExists() {
        // Подготовка
        User user = new User();
        user.setId(1L);
        user.setName("Alice");
        user.setEmail("alice@example.com");
        user.setAge(25);

        UserDto userDto = new UserDto(1L, "Alice", "alice@example.com", 25);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userDtoMapping.toDto(user)).thenReturn(userDto);

        // Действие
        UserDto result = userService.getUserById(1L);

        // Проверка
        assertNotNull(result);
        assertEquals("Alice", result.getName());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetUserById_UserNotFound() {
        // Подготовка
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Проверка
        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.getUserById(1L);
        });

        assertEquals("User not found with id: 1", exception.getMessage());
    }

    @Test
    public void testCreateUser() {
        // Подготовка
        UserDto userDto = new UserDto(null, "Alice", "alice@example.com", 25);
        User user = new User();
        user.setId(1L);
        user.setName("Alice");
        user.setEmail("alice@example.com");
        user.setAge(25);

        when(userDtoMapping.toEntity(userDto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userDtoMapping.toDto(user)).thenReturn(userDto);

        // Действие
        UserDto result = userService.createUser(userDto);

        // Проверка
        assertNotNull(result);
        assertEquals("Alice", result.getName());
        verify(userDtoMapping, times(1)).toEntity(userDto);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testUpdateUser_UserExists() {
        // Подготовка
        UserDto userDto = new UserDto(null, "Alice Updated", "alice.updated@example.com", 26);
        User user = new User();
        user.setId(1L);
        user.setName("Alice");
        user.setEmail("alice@example.com");
        user.setAge(25);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(userDtoMapping.toDto(user)).thenReturn(userDto);

        // Действие
        UserDto result = userService.updateUser(1L, userDto);

        // Проверка
        assertNotNull(result);
        assertEquals("Alice Updated", result.getName());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testUpdateUser_UserNotFound() {
        // Подготовка
        UserDto userDto = new UserDto(null, "Alice Updated", "alice.updated@example.com", 26);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Проверка
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            userService.updateUser(1L, userDto);
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    public void testDeleteUser_UserExists() {
        // Подготовка
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(true);

        // Действие
        userService.deleteUser(userId);

        // Проверка
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    public void testDeleteUser_UserNotFound() {
        // Подготовка
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(false);

        // Проверка
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            userService.deleteUser(userId);
        });

        assertEquals("User not found", exception.getMessage());
    }
}
