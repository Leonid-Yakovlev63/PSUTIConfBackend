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
import java.util.UUID;

@Service
public class ImageFileInfoService {

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private ImageFileInfoRepository imageFileInfoRepository;

    private static final String PNG_EXTENSION = "png";

    private static final String JPG_EXTENSION = "jpg";

    private static final String JPEG_EXTENSION = "jpeg";

    private static final String FILE_UPLOAD_DIR = "uploads/public/photos/";

    public Optional<ImageFileInfo> getImageFileInfoById(Long id) {
        return imageFileInfoRepository.findById(id);
    }

    public ImageFileInfo saveImageFileInfo(ImageFileInfo imageFileInfo, MultipartFile multipartFile) throws IOException {
        String originalFileName = multipartFile.getOriginalFilename();

        if (originalFileName == null || !originalFileName.contains(".")) {
            throw new IllegalArgumentException("Invalid file format");
        }
        String lowerCaseFileName = originalFileName.toLowerCase();
        if (!lowerCaseFileName.endsWith(PNG_EXTENSION) && !lowerCaseFileName.endsWith(JPG_EXTENSION) && !lowerCaseFileName.endsWith(JPEG_EXTENSION)) {
            throw new IllegalArgumentException("Unsupported file format. Acceptable formats: png, jpg, jpeg");
        }

        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String newFileName = UUID.randomUUID() + fileExtension;
        imageFileInfo.setName(newFileName);
        Path path = Paths.get(FILE_UPLOAD_DIR + newFileName);

        Files.copy(multipartFile.getInputStream(), path);

        if (!Files.exists(path)) {
            throw new IOException("File was not saved successfully");
        }
        return imageFileInfoRepository.save(imageFileInfo);
    }
}