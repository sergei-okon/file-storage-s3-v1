package ua.com.sergeiokon.model.dto;

import lombok.Data;

@Data
public class FileDto {

    private Long id;
    private String fileName;
    private String location;
    private String bucket;
}
