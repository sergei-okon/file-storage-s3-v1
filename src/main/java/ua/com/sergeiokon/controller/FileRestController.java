package ua.com.sergeiokon.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.sergeiokon.model.dto.FileDto;
import ua.com.sergeiokon.service.FileService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/files")
public class FileRestController {

    private final FileService fileService;

    @GetMapping
    public ResponseEntity<List<FileDto>> getFiles() {
        return ResponseEntity.ok(fileService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FileDto> getFile(@PathVariable("id") Long id) {
        return ResponseEntity.ok(fileService.findById(id));
    }

    @PostMapping
    public ResponseEntity<FileDto> addFile(@RequestBody FileDto fileDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(fileService.save(fileDto));
    }

    @PutMapping
    public ResponseEntity<FileDto> updateFile(@RequestBody FileDto fileDto) {
        return ResponseEntity.ok(fileService.update(fileDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFile(@PathVariable("id") Long id) {
        fileService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(
            IllegalArgumentException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}