package com.example.mod3.service.serviceImpl;

import com.example.mod3.Dto.MessageDto;
import com.example.mod3.Dto.UserDto;
import com.example.mod3.Dto.mapping.UserDtoMapping;
import com.example.mod3.config.KafkaProducer;
import com.example.mod3.model.User;
import com.example.mod3.repository.UserRepository;
import com.example.mod3.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserDtoMapping userDtoMapping;
    private final KafkaProducer kafkaProducer;

    public UserServiceImpl(UserRepository userRepository, UserDtoMapping userDtoMapping, KafkaProducer kafkaProducer) {
        this.userRepository = userRepository;
        this.userDtoMapping = userDtoMapping;
        this.kafkaProducer = kafkaProducer;
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userDtoMapping::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return userDtoMapping.toDto(user);
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = userDtoMapping.toEntity(userDto); // Преобразуем DTO в Entity
        User savedUser = userRepository.save(user); // Сохраняем пользователя
        kafkaProducer.sendMessage(new MessageDto(savedUser.getEmail(), "CREATE"));
        return userDtoMapping.toDto(savedUser); // Преобразуем обратно в DTO и возвращаем
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        // Проверяем, существует ли пользователь
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // Обновляем данные пользователя
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setAge(userDto.getAge());

        User updatedUser = userRepository.save(user);
        return userDtoMapping.toDto(updatedUser);
    }
    @Override
        public void deleteUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            kafkaProducer.sendMessage(new MessageDto(user.get().getEmail(), "DELETE"));
            userRepository.deleteById(id);
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }
}
