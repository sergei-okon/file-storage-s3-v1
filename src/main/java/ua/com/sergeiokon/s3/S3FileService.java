package ua.com.sergeiokon.s3;

import com.amazonaws.services.s3.AmazonS3;
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
import ua.com.sergeiokon.model.dto.EventDto;
import ua.com.sergeiokon.model.dto.FileDto;
import ua.com.sergeiokon.model.dto.UserDto;
import ua.com.sergeiokon.repository.entity.Operation;
import ua.com.sergeiokon.service.EventService;
import ua.com.sergeiokon.service.FileService;
import ua.com.sergeiokon.service.UserService;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;


@Service
@Slf4j
@RequiredArgsConstructor
public class S3FileService {

    private final FileService fileService;
    private final UserService userService;
    private final EventService eventService;
    private final S3Connector s3Connector;

    @Value("${aws.bucket}")
    private String bucket;

    public ObjectListing getListFiles(String bucketName) {
        AmazonS3 s3client = s3Connector.getS3client();

        ObjectListing objectListing = s3client.listObjects(bucketName);
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
        FileDto savedFile = fileService.save(fileDto);

        AmazonS3 s3client = s3Connector.getS3client();
        s3client.putObject(bucket, location, convertMultiPartToFile(uploadFile));

        createEvent(savedFile, userDto, Operation.UPLOAD);
    }

    public void downloadFile(Long fileId, String path, Long userId) {
        UserDto userDto = userService.findById(userId);
        FileDto fileDto = fileService.findById(fileId);
        java.io.File fileToDownload = new java.io.File(path + fileDto.getFileName());
        if (fileToDownload.exists())
            FileUtils.deleteQuietly(fileToDownload);
        try {
            if (!fileToDownload.createNewFile()) {
                throw new IOException("Unable to create file with path " + path);
            } else {
                AmazonS3 s3client = s3Connector.getS3client();
                S3Object s3object = s3client.getObject(fileDto.getBucket(), fileDto.getLocation());
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
}


