package com.cursor.onlineshop.controllers;

import com.cursor.onlineshop.entities.FileData;
import com.cursor.onlineshop.services.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/media")
@RequiredArgsConstructor
@Secured("ROLE_ADMIN")
public class FileController {

    private final FileService fileService;

    @PostMapping()
    public ResponseEntity<FileData> create(@RequestParam("file") MultipartFile file) throws Exception {
        final FileData newFileData = fileService.createFile(file);
        return ResponseEntity
                .created(URI.create("media/" + newFileData.getFileId()))
                .body(newFileData);
    }

    @GetMapping(value = "/{mediaId}", produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Resource> getFile(@PathVariable("mediaId") String mediaId) throws FileNotFoundException {
        return ResponseEntity.ok(fileService.getFile(mediaId).getFile());
    }

    @DeleteMapping(value = "/{mediaId}")
    public ResponseEntity<Void> deleteFile(@PathVariable("mediaId") String mediaId) throws FileNotFoundException {
        fileService.deleteFile(mediaId);
        return ResponseEntity.ok().build();
    }

    // 1. Add the following params here: limit (10 by default), offset (0 by default) and sort (by category name by default)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<FileData>> getAll() {
        return ResponseEntity.ok(fileService.getAll());
    }
}
