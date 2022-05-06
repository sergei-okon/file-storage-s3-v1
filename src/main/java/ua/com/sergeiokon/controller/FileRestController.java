package ua.com.sergeiokon.controller;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectListing;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.com.sergeiokon.model.dto.FileDto;
import ua.com.sergeiokon.service.FileService;
import ua.com.sergeiokon.service.UserService;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/files")
public class FileRestController {

    private final UserService userService;
    private final FileService fileService;

    @GetMapping
    public ResponseEntity<List<FileDto>> getFiles() {
        return ResponseEntity.ok(fileService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FileDto> getFile(@PathVariable("id") Long id) {
        return ResponseEntity.ok(fileService.findById(id));
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFileToS3(@RequestParam("file") MultipartFile file) {
        Long userId = userService.findByEmail(getCurrentUsername()).getId();
        fileService.uploadFile(file, userId);

        return ResponseEntity.ok("File uploaded successful");
    }

    @GetMapping("/download")
    public ResponseEntity<?> downloadFile(@RequestParam Long fileId,
                                          @RequestParam String path) {
        Long userId = userService.findByEmail(getCurrentUsername()).getId();
        fileService.downloadFile(fileId, path, userId);
        return ResponseEntity.ok("File downloaded successful");
    }

    @GetMapping("/s3")
    public ResponseEntity<ObjectListing> getFilesFromS3(@RequestBody String bucketName) {
        return ResponseEntity.ok(fileService.getListFilesFromS3(bucketName));
    }

    private String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(
            IllegalArgumentException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(AmazonS3Exception.class)
    public ResponseEntity<?> handleIllegalArgumentException(
            AmazonS3Exception ex) {
        log.error(ex.getMessage());
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}