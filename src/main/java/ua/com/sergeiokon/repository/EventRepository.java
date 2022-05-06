package ua.com.sergeiokon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.sergeiokon.repository.entity.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
}
