package ua.com.sergeiokon.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.com.sergeiokon.repository.S3PropertyRepository;
import ua.com.sergeiokon.repository.entity.S3Property;

import java.util.List;

@Service
@RequiredArgsConstructor
public class S3PropertiesService {

    private final S3PropertyRepository s3PropertyRepository;

    public List<S3Property> findAll() {
        return s3PropertyRepository.findAll();
    }

    public S3Property save(S3Property s3property) {
        return s3PropertyRepository.save(s3property);
    }

    public void deleteById(Long id) {
        if (s3PropertyRepository.findById(id).isPresent()) {
            s3PropertyRepository.deleteById(id);
        } else
            throw new IllegalArgumentException("S3Properties with id: "
                    + id + " not found. Unable to delete S3Properties");
    }
}
