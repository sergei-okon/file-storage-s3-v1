package ua.com.sergeiokon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.com.sergeiokon.model.dto.EventDto;
import ua.com.sergeiokon.repository.EventRepository;
import ua.com.sergeiokon.repository.entity.Event;
import ua.com.sergeiokon.repository.entity.File;
import ua.com.sergeiokon.repository.entity.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public List<EventDto> findAll() {
        return eventRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public EventDto findById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Event with id " + id + " not found"));
        return convertToDto(event);
    }

    public EventDto save(EventDto eventDto) {
        Event savedEvent = eventRepository.save(convertToEntity(eventDto));
        return convertToDto(savedEvent);
    }

    private EventDto convertToDto(Event event) {
        if (event == null) {
            throw new NullPointerException("Event is null");
        }
        EventDto eventDto = new EventDto();
        eventDto.setId(event.getId());
        if (event.getUser() == null) {
            eventDto.setUserId(null);
        } else {
            eventDto.setUserId(event.getUser().getId());
        }
        if (event.getFile() == null) {
            eventDto.setFileId(null);
        } else {
            eventDto.setFileId(event.getFile().getId());
        }
        eventDto.setCreated(event.getCreated());
        eventDto.setOperation(event.getOperation());
        return eventDto;
    }

    private Event convertToEntity(EventDto eventDto) {
        if (eventDto == null) {
            throw new NullPointerException("EventDto is null");
        }
        Event event = new Event();
        event.setId(eventDto.getId());
        if (eventDto.getUserId() == null) {
            event.setUser(null);
        } else {
            User user = new User();
            user.setId(eventDto.getUserId());
            event.setUser(user);
        }
        if (eventDto.getFileId() == null) {
            event.setFile(null);
        } else {
            File file = new File();
            file.setId(eventDto.getFileId());
            event.setFile(file);
        }
        event.setCreated(eventDto.getCreated());
        event.setOperation(eventDto.getOperation());
        return event;
    }
}
