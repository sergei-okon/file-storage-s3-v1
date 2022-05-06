package ua.com.sergeiokon.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventDto {

    private Long id;
    private Long userId;
    private Long fileId;
    private LocalDateTime created;
    private ua.com.sergeiokon.repository.entity.Operation Operation;
}
