package com.nnk.springboot.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.domain.UserDto;

public class UserMapperTest {

    @Test
    public void testConvertUserDtoToUser() {
        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setUsername("user1");
        userDto.setFullname("User One");
        userDto.setRole("ADMIN");
        userDto.setPassword("password1");

        User user = UserMapper.convertUserDtoToUser(userDto);

        assertNotNull(user);
        assertEquals(userDto.getId(), user.getId());
        assertEquals(userDto.getUsername(), user.getUsername());
        assertEquals(userDto.getFullname(), user.getFullname());
        assertEquals(userDto.getRole(), user.getRole());
        assertEquals(userDto.getPassword(), user.getPassword());
    }

    @Test
    public void testConvertUserToUserDto() {
        User user = new User();
        user.setId(1);
        user.setUsername("user1");
        user.setFullname("User One");
        user.setRole("ADMIN");
        user.setPassword("password1");

        UserDto userDto = UserMapper.convertUserToUserDto(user);

        assertNotNull(userDto);
        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getUsername(), userDto.getUsername());
        assertEquals(user.getFullname(), userDto.getFullname());
        assertEquals(user.getRole(), userDto.getRole());
        assertEquals(user.getPassword(), userDto.getPassword());
    }

    @Test
    public void testConvertUserListToUserDtoList() {
        User user1 = new User();
        user1.setId(1);
        user1.setUsername("user1");
        

        User user2 = new User();
        user2.setId(2);
        user2.setUsername("user2");
       
        List<User> users = Arrays.asList(user1, user2);
        List<UserDto> userDtos = UserMapper.convertUserListToUserDtoList(users);

        assertNotNull(userDtos);
        assertEquals(2, userDtos.size());
        assertEquals(user1.getUsername(), userDtos.get(0).getUsername());
        assertEquals(user2.getUsername(), userDtos.get(1).getUsername());
    }
}
