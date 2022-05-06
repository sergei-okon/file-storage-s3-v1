package ua.com.sergeiokon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.sergeiokon.repository.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
