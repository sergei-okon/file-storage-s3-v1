package ua.com.sergeiokon.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.com.sergeiokon.repository.S3PropertyRepository;
import ua.com.sergeiokon.repository.entity.S3Property;

import static org.mockito.Mockito.*;

class S3PropertiesServiceTest {

    S3PropertyRepository s3PropertyRepositoryMock;
    S3PropertiesService s3PropertiesService;

    @BeforeEach
    void setUp() {
        s3PropertyRepositoryMock = mock(S3PropertyRepository.class);
        s3PropertiesService = new S3PropertiesService(s3PropertyRepositoryMock);
    }

    @Test
    void findAll() {
        s3PropertiesService.findAll();
        verify(s3PropertyRepositoryMock).findAll();
    }

    @Test
    void save() {
        S3Property s3Property = new S3Property();
        when(s3PropertyRepositoryMock.save(any())).thenReturn(s3Property);
        s3PropertiesService.save(s3Property);
        verify(s3PropertyRepositoryMock).save(any());
    }

    @Test
    void deleteById() {
        Long id = 1L;
        S3Property s3Property = new S3Property();
        when(s3PropertyRepositoryMock.findById(anyLong())).thenReturn(java.util.Optional.of(s3Property));
        s3PropertiesService.deleteById(id);
        verify(s3PropertyRepositoryMock).deleteById(id);
    }
}