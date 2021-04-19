package com.cursor.onlineshop.services;

import com.cursor.onlineshop.dtos.GetFileDto;
import com.cursor.onlineshop.entities.FileData;
import com.cursor.onlineshop.exceptions.StorageException;
import com.cursor.onlineshop.repositories.FileRepo;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class FileService {

    private final FileRepo fileRepo;
    private final UserService userService;

    private static final Path FILE_DIRECTORY = Paths.get("src/main/resources/files");

    @PostConstruct
    public void init() throws IOException {
        Files.createDirectories(FILE_DIRECTORY);
    }

    public FileData createFile(final MultipartFile file) throws Exception {
        FileData newFileData = new FileData(
                store(file),
                StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename())),
                userService.getByUsername(
                        SecurityContextHolder.getContext().getAuthentication().getName()).getAccountId()
        );
        fileRepo.save(newFileData);
        return newFileData;
    }

    public GetFileDto getFile(final String fileId) throws FileNotFoundException {
        try {
            final Path filePath = FILE_DIRECTORY.resolve(fileId);
            Resource fileResource = new UrlResource(filePath.toUri());
            if (fileResource.exists() || fileResource.isReadable()) {
                return new GetFileDto(fileResource, fileRepo.getOne(fileId).getFileName());
            } else {
                throw new FileNotFoundException("Could not read fileId: " + fileId);
            }
        } catch (MalformedURLException e) {
            throw new FileNotFoundException("Could not read fileId: " + fileId);
        }
    }

    public String store(final MultipartFile file) throws Exception {
        if (file == null) {
            throw new Exception("File cannot be null.");
        }

        final String fileId = UUID.randomUUID().toString();

        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + fileId);
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(
                        inputStream,
                        FILE_DIRECTORY.resolve(fileId),
                        StandardCopyOption.REPLACE_EXISTING
                );
            }
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + fileId, e);
        }
        return fileId;
    }

    @Transactional
    public void deleteFile(String fileId) throws FileNotFoundException {
        Optional<FileData> fileOptional = fileRepo.findById(fileId);
        if (fileOptional.isEmpty()) {
            throw new FileNotFoundException("There is now file with id: " + fileId);
        }
        FileData fileData = fileOptional.get();
        Path fileToDeletePath = Paths.get(FILE_DIRECTORY.toString() + "/" + fileData.getFileId());
        try {
            Files.delete(fileToDeletePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        fileRepo.deleteById(fileId);
    }

    public List<FileData> getAll() {
        return fileRepo.findAll();
    }
}
