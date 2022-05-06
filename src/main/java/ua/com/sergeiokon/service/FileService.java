package ua.com.sergeiokon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.com.sergeiokon.model.dto.FileDto;
import ua.com.sergeiokon.repository.FileRepository;
import ua.com.sergeiokon.repository.entity.File;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;

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

    public FileDto update(FileDto fileDto) {
        if (fileRepository.findById(fileDto.getId()).isPresent()) {
            File updatedFile = fileRepository.save(convertToEntity(fileDto));
            return convertToDto(updatedFile);
        } else {
            throw new IllegalArgumentException("File with id " + fileDto.getId() +
                    " not found. Unable to update file");
        }
    }

    public void deleteById(Long id) {
        if (fileRepository.findById(id).isPresent()) {
            fileRepository.deleteById(id);
        } else
            throw new IllegalArgumentException("File with id " + id + " not found. Unable to delete file");
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

