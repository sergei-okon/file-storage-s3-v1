package ua.com.sergeiokon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.com.sergeiokon.model.dto.UserDto;
import ua.com.sergeiokon.repository.UserRepository;
import ua.com.sergeiokon.repository.entity.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public UserDto findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + id + " not found"));
        return convertToDto(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User with   " + email + " not found"));
    }

    public UserDto save(UserDto userDto) {
        if (userDto != null) {
            if (userRepository.findByEmail(userDto.getEmail()).isEmpty()) {
                userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
                User savedUser = userRepository.save(convertToEntity(userDto));
                UserDto savedUserDto = convertToDto(savedUser);
                savedUserDto.setPassword(null);
                return savedUserDto;
            } else {
                throw new IllegalArgumentException("The user with this " + userDto.getEmail() + " is already registered");
            }
        }
        return null;
    }

    public UserDto update(UserDto userDto) {
        if (userRepository.findById(userDto.getId()).isPresent()) {
            User savedUser = userRepository.save(convertToEntity(userDto));
            return convertToDto(savedUser);
        } else {
            throw new IllegalArgumentException("User with id " + userDto.getId() + " not found");
        }
    }

    public void deleteById(Long id) {
        if (userRepository.findById(id).isPresent()) {
            userRepository.deleteById(id);
        } else
            throw new IllegalArgumentException("User with id " + id + " not found");
    }

    private UserDto convertToDto(User user) {
        if (user == null) {
            throw new NullPointerException("User is null");
        }
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setRole(user.getRole());
        userDto.setActive(user.isActive());
        userDto.setPassword(user.getPassword());
        return userDto;
    }

    private User convertToEntity(UserDto userDto) {
        if (userDto == null) {
            throw new NullPointerException("User is null");
        }
        User user = new User();
        user.setId(userDto.getId());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setRole(userDto.getRole());
        user.setActive(userDto.isActive());
        user.setPassword(userDto.getPassword());
        return user;
    }
}
