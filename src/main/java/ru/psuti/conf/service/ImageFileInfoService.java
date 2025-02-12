package ru.psuti.conf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.psuti.conf.entity.FileType;
import ru.psuti.conf.entity.ImageFileInfo;
import ru.psuti.conf.repository.ImageFileInfoRepository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
public class ImageFileInfoService {

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private ImageFileInfoRepository imageFileInfoRepository;

    private static final String FILE_UPLOAD_DIR = "src/main/resources/static/public/photos/";

    public Optional<ImageFileInfo> getImageFileInfoById(Long id) {
        return imageFileInfoRepository.findById(id);
    }

    public ImageFileInfo saveImageFileInfo(ImageFileInfo imageFileInfo, MultipartFile multipartFile) throws IOException {
        String fileName = multipartFile.getOriginalFilename();
        if (fileName == null || !fileName.contains(".")) {
            throw new IllegalArgumentException("Invalid file format");
        }

        Path path = Paths.get(FILE_UPLOAD_DIR + fileName);

        Files.copy(multipartFile.getInputStream(), path);

        if (!Files.exists(path)) {
            throw new IOException("File was not saved successfully");
        }
        return imageFileInfoRepository.save(imageFileInfo);
    }
}