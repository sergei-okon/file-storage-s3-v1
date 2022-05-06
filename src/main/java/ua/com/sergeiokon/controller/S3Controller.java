package ua.com.sergeiokon.controller;

import com.amazonaws.services.s3.model.ObjectListing;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.com.sergeiokon.s3.S3FileService;
import ua.com.sergeiokon.service.UserService;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/s3")
public class S3Controller {

    private final S3FileService s3FileService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<ObjectListing> getFiles(@RequestBody String bucketName) {
        return ResponseEntity.ok(s3FileService.getListFiles(bucketName));
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFileToS3(@RequestParam("file") MultipartFile file) {
        Long userId = userService.findByEmail(getCurrentUsername()).getId();
        s3FileService.uploadFile(file, userId);

        return ResponseEntity.ok("File uploaded successful");
    }

    @GetMapping("/download")
    public ResponseEntity<?> downloadFile(@RequestParam Long fileId,
                                          @RequestParam String path) {
        Long userId = userService.findByEmail(getCurrentUsername()).getId();
        s3FileService.downloadFile(fileId, path, userId);
        return ResponseEntity.ok("File downloaded successful");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(
            IllegalArgumentException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    public String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }
}