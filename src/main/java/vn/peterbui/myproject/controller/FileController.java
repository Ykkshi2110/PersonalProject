package vn.peterbui.myproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import vn.peterbui.myproject.convert.annotation.ApiMessage;
import vn.peterbui.myproject.domain.response.file.ResUploadFileDTO;
import vn.peterbui.myproject.exception.StorageException;
import vn.peterbui.myproject.service.FileService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;
    @Value("${peterBui.upload-file.base-uri}")
    private String baseUri;

    @PostMapping("/files")
    @ApiMessage("Upload single file")
    public ResponseEntity<ResUploadFileDTO> upload(@RequestParam(name = "file", required = false) MultipartFile file, @RequestParam("folder") String folder) throws URISyntaxException, IOException, StorageException {
        // validate a file
        if (file == null || file.isEmpty()) {
            throw new StorageException("File is empty. Please enter a valid file.");
        }
        String fileName = file.getOriginalFilename();
        List<String> allowedExtensions = Arrays.asList("pdf", "jpg", "jpeg", "png", "doc", "docx");
        boolean isValid = allowedExtensions.stream().anyMatch(item -> {
            assert fileName != null;
            return fileName.toLowerCase().endsWith(item);
        });
        if(!isValid) {
          throw new StorageException("Invalid file extension, only accepts " + allowedExtensions);
        }
        // create a directory if not exists
        this.fileService.createDirectory(baseUri + folder);
        // storage file
        String uploadFile = this.fileService.storageFile(file, folder);
        ResUploadFileDTO uploadFileDTO = new ResUploadFileDTO();
        uploadFileDTO.setFileName(uploadFile);
        uploadFileDTO.setUploadedAt(Instant.now());
        return ResponseEntity
                .ok()
                .body(uploadFileDTO);
    }
}
