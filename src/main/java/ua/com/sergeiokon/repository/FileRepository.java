package ua.com.sergeiokon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.sergeiokon.repository.entity.File;

public interface FileRepository extends JpaRepository<File, Long> {

}
