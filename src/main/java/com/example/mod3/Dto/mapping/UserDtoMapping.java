package com.example.mod3.Dto.mapping;

import com.example.mod3.Dto.UserDto;
import com.example.mod3.model.User;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.stereotype.Component;

@Component
public class UserDtoMapping {

    public UserDto toDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setAge(user.getAge());
        return userDto;
    }

    public User toEntity(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setAge(userDto.getAge());
        return user;
    }
}
