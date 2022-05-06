package ua.com.sergeiokon.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.com.sergeiokon.config.S3Config;
import ua.com.sergeiokon.model.dto.FileDto;
import ua.com.sergeiokon.repository.FileRepository;
import ua.com.sergeiokon.repository.entity.File;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class FileServiceTest {

    private FileRepository fileRepositoryMock;
    FileService fileService;

    @BeforeEach
    void setUp() {
        fileRepositoryMock = mock(FileRepository.class);
        UserService userServiceMock = mock(UserService.class);
        EventService eventServiceMock = mock(EventService.class);
        S3Config s3ConfigMock = mock(S3Config.class);

        fileService = new FileService(fileRepositoryMock, userServiceMock, eventServiceMock, s3ConfigMock);
    }

    @Test
    void findAllSuccess() {
        fileService.findAll();
        verify(fileRepositoryMock).findAll();
    }

    @Test
    void findById_Success() {
        Long id = 1L;
        File file = new File();
        when(fileRepositoryMock.findById(anyLong())).thenReturn(java.util.Optional.of(file));
        fileService.findById(id);
        verify(fileRepositoryMock).findById(id);
    }

    @Test
    void save_Success() {
        File file = createFile();
        FileDto fileDto = createFileDto();
        when(fileRepositoryMock.save(any())).thenReturn(file);
        fileService.save(fileDto);
        verify(fileRepositoryMock).save(any());
    }

    private File createFile() {
        File file = new File();
        file.setId(1L);
        file.setFileName("java.doc");
        file.setBucket("develop");
        file.setLocation("c://doc//");
        return file;
    }

    private FileDto createFileDto() {
        FileDto fileDto = new FileDto();
        fileDto.setId(1L);
        fileDto.setFileName("java.doc");
        fileDto.setBucket("develop");
        fileDto.setLocation("c://doc//");
        return fileDto;
    }
}