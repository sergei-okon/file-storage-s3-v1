package ua.com.sergeiokon.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.sergeiokon.repository.entity.S3Property;
import ua.com.sergeiokon.service.S3PropertiesService;

import javax.validation.Valid;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/admin/property")
public class AdminRestController {

    private final S3PropertiesService s3PropertiesService;

    @GetMapping
    public ResponseEntity<List<S3Property>> getProperty() {
        return ResponseEntity.ok(s3PropertiesService.findAll());
    }

    @PostMapping
    public ResponseEntity<S3Property> addProperty(@Valid @RequestBody S3Property s3Property) {
        S3Property savedS3Property = s3PropertiesService.save(s3Property);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedS3Property);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProperty(@PathVariable("id") Long id) {
        s3PropertiesService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<?> handleIllegalArgumentException(
            EmptyResultDataAccessException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<?> handleIllegalArgumentException(
            SQLIntegrityConstraintViolationException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}