package com.mracover.telegram_bot.service;

import com.mracover.telegram_bot.exception.userException.NoSuchUserException;
import com.mracover.telegram_bot.model.User;
import com.mracover.telegram_bot.repository.UserRepository;
import com.mracover.telegram_bot.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private final static User user = new User();
    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void prepare() {
        System.out.println("Before each: " + this);
        this.userRepository = Mockito.mock(UserRepository.class);
        this.userService = new UserServiceImpl(userRepository);
    }

    @Test
    void shouldAddUser() {
        Mockito.doReturn(user).when(userRepository).save(user);
        var answer = userService.addUser(user);
        assertNotNull(answer);
    }

    @Test
    void shouldGetAllUsers() {
        List<User> userList = new ArrayList<>();
        userList.add(user);
        Mockito.doReturn(userList).when(userRepository).findAll();
        var answer = userService.getAllUsers();
        assertFalse(answer.isEmpty());
    }

    @Test
    void shouldThrowNoSuchUserExceptionWhenGetAllUsers() {
        List<User> userList = new ArrayList<>();
        Mockito.doReturn(userList).when(userRepository).findAll();
        assertThrows(NoSuchUserException.class, () ->
                userService.getAllUsers());
    }

    @Test
    void shouldFindUserById() {
        Mockito.doReturn(Optional.of(new User())).when(userRepository).findById(Mockito.anyLong());
        var answer = userService.findUserById(Mockito.anyLong());
        assertNotNull(answer);
    }

    @Test
    void shouldThrowNoSuchUserExceptionWhenFindUserById() {
        Mockito.doReturn(Optional.empty()).when(userRepository).findById(Mockito.anyLong());
        assertThrows(NoSuchUserException.class, () ->
                userService.findUserById(Mockito.anyLong()));
    }

    @Test
    void shouldUpdateUser() {
        Mockito.doReturn(new User()).when(userRepository).save(user);
        var answer = userService.updateUser(user);
        assertNotNull(answer);
    }

    @Test
    void shouldThrowNoSuchUserExceptionWhenUpdateUser() {
        Mockito.doReturn(null).when(userRepository).save(user);
        assertThrows(NoSuchUserException.class, () ->
                userService.updateUser(user));
    }

    @Test
    void shouldDeleteUserById() {
        Mockito.doReturn(Optional.of(new User())).when(userRepository).findById(Mockito.anyLong());
        Mockito.doNothing().when(userRepository).deleteById(Mockito.anyLong());
        assertDoesNotThrow(() -> userService.deleteUserById(Mockito.anyLong()));
    }

    @Test
    void shouldDeleteUserByTelegramId() {
        Mockito.doReturn(user).when(userRepository).findUserByTelegramUserId(Mockito.anyLong());
        Mockito.doNothing().when(userRepository).deleteUserByTelegramUserId(Mockito.anyLong());
        assertDoesNotThrow(() -> userService.deleteUserByTelegramId(Mockito.anyLong()));
    }

    @Test
    void shouldFindUserByTelegramId() {
        Mockito.doReturn(user).when(userRepository).findUserByTelegramUserId(Mockito.anyLong());
        var findUser = userService.findUserByTelegramId(Mockito.anyLong());
        assertEquals(findUser, user);
    }
}