package ua.com.sergeiokon.service;

import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ua.com.sergeiokon.config.S3Config;
import ua.com.sergeiokon.model.dto.EventDto;
import ua.com.sergeiokon.model.dto.FileDto;
import ua.com.sergeiokon.model.dto.UserDto;
import ua.com.sergeiokon.repository.FileRepository;
import ua.com.sergeiokon.repository.entity.File;
import ua.com.sergeiokon.repository.entity.Operation;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    @Value("${aws.bucket}")
    private String bucket;

    private final FileRepository fileRepository;
    private final UserService userService;
    private final EventService eventService;
    private final S3Config s3Config;


    public List<FileDto> findAll() {
        return fileRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public FileDto findById(Long id) {
        File file = fileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("File with id " + id + " not found"));
        return convertToDto(file);
    }

    public FileDto save(FileDto fileDto) {
        File savedFile = fileRepository.save(convertToEntity(fileDto));
        return convertToDto(savedFile);
    }

    public ObjectListing getListFilesFromS3(String bucketName) {
        ObjectListing objectListing = s3Config.s3client().listObjects(bucketName);
        for (S3ObjectSummary os : objectListing.getObjectSummaries()) {
            log.info(os.getKey());
        }
        return objectListing;
    }

    public void uploadFile(MultipartFile uploadFile, Long userId) {
        UserDto userDto = userService.findById(userId);

        String originalFilename = uploadFile.getOriginalFilename();
        FileDto fileDto = new FileDto();
        fileDto.setFileName(originalFilename);
        fileDto.setBucket(bucket);
        String location = userDto.getEmail() + "/" + fileDto.getFileName();
        fileDto.setLocation(location);
        FileDto savedFile = this.save(fileDto);

        s3Config.s3client().putObject(bucket, location, convertMultiPartToFile(uploadFile));

        createEvent(savedFile, userDto, Operation.UPLOAD);
    }

    public void downloadFile(Long fileId, String path, Long userId) {
        UserDto userDto = userService.findById(userId);
        FileDto fileDto = this.findById(fileId);
        java.io.File fileToDownload = new java.io.File(path + fileDto.getFileName());
        if (fileToDownload.exists())
            FileUtils.deleteQuietly(fileToDownload);
        try {
            if (!fileToDownload.createNewFile()) {
                throw new IOException("Unable to create file with path " + path);
            } else {
                S3Object s3object = s3Config.s3client().getObject(fileDto.getBucket(), fileDto.getLocation());
                S3ObjectInputStream inputStream = s3object.getObjectContent();
                FileUtils.copyInputStreamToFile(inputStream, fileToDownload);

                createEvent(fileDto, userDto, Operation.DOWNLOAD);
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private java.io.File convertMultiPartToFile(MultipartFile file) {
        java.io.File convFile = new java.io.File(Objects.requireNonNull(file.getOriginalFilename()));
        try (FileOutputStream fos = new FileOutputStream(String.valueOf(convFile))) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return convFile;
    }

    private void createEvent(FileDto fileDto, UserDto userDto, Operation operation) {
        EventDto eventDto = new EventDto();
        eventDto.setFileId(fileDto.getId());
        eventDto.setUserId(userDto.getId());
        eventDto.setCreated(LocalDateTime.now());
        eventDto.setOperation(operation);
        eventService.save(eventDto);
    }

    private FileDto convertToDto(File file) {
        if (file == null) {
            throw new NullPointerException("File is null");
        }
        FileDto fileDto = new FileDto();
        fileDto.setId(file.getId());
        fileDto.setFileName(file.getFileName());
        fileDto.setLocation(file.getLocation());
        fileDto.setBucket(file.getBucket());
        return fileDto;
    }

    private File convertToEntity(FileDto fileDto) {
        if (fileDto == null) {
            throw new NullPointerException("FileDto is null");
        }
        File file = new File();
        file.setId(fileDto.getId());
        file.setFileName(fileDto.getFileName());
        file.setLocation(fileDto.getLocation());
        file.setBucket(fileDto.getBucket());
        return file;
    }
}

