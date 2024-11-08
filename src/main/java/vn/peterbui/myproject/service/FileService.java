package vn.peterbui.myproject.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileService {
    private final Logger logger = LoggerFactory.getLogger(FileService.class);
    @Value("${peterBui.upload-file.base-uri}")
    private String baseUri;

    public void createDirectory(String folder) throws URISyntaxException {
        URI uri = new URI(folder);
        Path path = Paths.get(uri);
        File tmpDir = new File(path.toString());
        if (!tmpDir.isDirectory()) {
            try {
                Files.createDirectory(tmpDir.toPath());
                logger.info(">>> CREATED DIRECTORY, Path = {}", tmpDir.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
                logger.info(">>> SKIP MAKING DIRECTORY, ALREADY EXISTS");
        }
    }

    public String storageFile(MultipartFile file, String folder) throws URISyntaxException, IOException {
        // Create a unique fileName
        String finalName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
        URI uri = new URI(baseUri + folder + "/" +  finalName);
        Path path = Paths.get(uri);
        try (InputStream inputStream = file.getInputStream()){
            Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
        }
        return finalName;
    }
}
