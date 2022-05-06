package ua.com.sergeiokon.model.dto;

import lombok.Data;
import ua.com.sergeiokon.repository.entity.Role;

import java.util.List;

@Data
public class UserDto {

    private Long id;
    private String name;
    private String email;
    private String password;
    private Role role;
    private boolean active;
    private List<EventDto> eventsDto;
}

