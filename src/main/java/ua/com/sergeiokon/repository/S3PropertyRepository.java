package ua.com.sergeiokon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.sergeiokon.repository.entity.S3Property;

import java.util.Optional;

public interface S3PropertyRepository extends JpaRepository<S3Property, Long> {

    Optional<S3Property> findByKey(String key);
}
