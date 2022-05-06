package ua.com.sergeiokon.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import ua.com.sergeiokon.model.dto.UserDto;
import ua.com.sergeiokon.repository.UserRepository;
import ua.com.sergeiokon.repository.entity.Role;
import ua.com.sergeiokon.repository.entity.User;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class UserServiceTest {

    UserRepository userRepositoryMock;
    PasswordEncoder passwordEncoderMock;
    UserService userService;

    @BeforeEach
    void setUp() {
        userRepositoryMock = mock(UserRepository.class);
        passwordEncoderMock = mock(PasswordEncoder.class);
        userService = new UserService(userRepositoryMock, passwordEncoderMock);
    }

    @Test
    void findAllSuccess() {
        userService.findAll();
        verify(userRepositoryMock).findAll();
    }

    @Test
    void findById_Success() {
        Long id = 1L;
        User user = new User();
        when(userRepositoryMock.findById(anyLong())).thenReturn(java.util.Optional.of(user));

        userService.findById(id);
        verify(userRepositoryMock).findById(id);
    }

    @Test
    void findByEmail() {
        String email = "a@gmail.com";
        User user = new User();
        when(userRepositoryMock.findByEmail(anyString())).thenReturn(java.util.Optional.of(user));

        userService.findByEmail(email);
        verify(userRepositoryMock).findByEmail(email);
    }

    @Test
    void save_Success() {
        UserDto userDto = createUserDto();
        User user = createUser();

        when(userRepositoryMock.findById(anyLong())).thenReturn(java.util.Optional.of(user));
        when(userRepositoryMock.save(any())).thenReturn(user);
        when(passwordEncoderMock.encode(anyString())).thenReturn("111");
        userService.save(userDto);
        verify(userRepositoryMock).save(any());
    }

    @Test
    void deleteById_Success() {
        Long id = 1L;
        User user = createUser();
        when(userRepositoryMock.findById(anyLong())).thenReturn(java.util.Optional.of(user));

        userService.deleteById(id);
        verify(userRepositoryMock).deleteById(id);
    }

    @Test
    void update_Success() {
        User user = createUser();
        UserDto userDto = createUserDto();

        when(userRepositoryMock.findById(anyLong())).thenReturn(java.util.Optional.of(user));
        when(userRepositoryMock.save(any())).thenReturn(user);
        userService.update(userDto);
        verify(userRepositoryMock).save(any());
    }

    private User createUser() {
        User user = new User();
        user.setId(1L);
        user.setName("Oleg");
        user.setEmail("oleg@gmail.com");
        user.setPassword("111");
        user.setRole(Role.ADMIN);
        user.setActive(true);
        user.setEvents(new ArrayList<>());
        return user;
    }

    private UserDto createUserDto() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("Oleg");
        userDto.setEmail("oleg@gmail.com");
        userDto.setPassword("111");
        userDto.setRole(Role.ADMIN);
        userDto.setActive(true);
        userDto.setEventsDto(new ArrayList<>());
        return userDto;
    }
}