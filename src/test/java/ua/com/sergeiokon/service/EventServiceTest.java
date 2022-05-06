package ua.com.sergeiokon.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.com.sergeiokon.model.dto.EventDto;
import ua.com.sergeiokon.repository.EventRepository;
import ua.com.sergeiokon.repository.entity.Event;
import ua.com.sergeiokon.repository.entity.File;
import ua.com.sergeiokon.repository.entity.Operation;
import ua.com.sergeiokon.repository.entity.User;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class EventServiceTest {

    private EventRepository eventRepositoryMock;
    private EventService eventService;

    @BeforeEach
    void setUp() {
        eventRepositoryMock = mock(EventRepository.class);
        eventService = new EventService(eventRepositoryMock);
    }

    @Test
    void findAllSuccess() {
        eventService.findAll();
        verify(eventRepositoryMock).findAll();
    }

    @Test
    void findById_Success() {
        Long id = 1L;
        Event event = new Event();
        when(eventRepositoryMock.findById(anyLong())).thenReturn(java.util.Optional.of(event));
        eventService.findById(id);
        verify(eventRepositoryMock).findById(id);
    }

    @Test
    void save_Success() {
        Event event = new Event();
        event.setFile(new File());
        event.setUser(new User());
        event.setCreated(LocalDateTime.now());
        event.setOperation(Operation.UPLOAD);

        EventDto eventDto = new EventDto();
        eventDto.setFileId(1L);
        eventDto.setUserId(1L);
        eventDto.setCreated(LocalDateTime.now());
        eventDto.setOperation(Operation.UPLOAD);

        when(eventRepositoryMock.save(any())).thenReturn(event);
        eventService.save(eventDto);
        verify(eventRepositoryMock).save(any());
    }
}