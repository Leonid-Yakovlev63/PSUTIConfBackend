package ru.psuti.conf.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.psuti.conf.entity.Article;
import ru.psuti.conf.entity.FileInfo;
import ru.psuti.conf.entity.FileType;
import ru.psuti.conf.repository.ArticleRepository;
import ru.psuti.conf.repository.FileRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ArticleService {

    private ArticleRepository articleRepository;

    private FileRepository fileRepository;

    private static final String WORD_EXTENSION = "docx";

    private static final String PDF_EXTENSION = "pdf";

    private static final String FILE_UPLOAD_DIR = "src/main/resources/static/articles/";

    public Article createArticle(Article article, MultipartFile file) throws IOException {

        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.contains(".")) {
            throw new IllegalArgumentException("Invalid file format");
        }

        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();

        FileType fileType = null;
        switch (fileExtension) {
            case WORD_EXTENSION:
                fileType = FileType.WORD;
                break;
            case PDF_EXTENSION:
                fileType = FileType.PDF;
                break;
            default:
                throw new IllegalArgumentException("Unsupported file format");
        }

        FileInfo fileInfo = FileInfo.builder()
                .name(fileName)
                .size(file.getSize())
                .fileType(fileType)
                .build();

        Path path = Paths.get(FILE_UPLOAD_DIR + fileName);

        Files.copy(file.getInputStream(), path);

        if (!Files.exists(path)) {
            throw new IOException("File was not saved successfully");
        }

        fileRepository.save(fileInfo);

        article.setFileInfo(fileInfo);
        return articleRepository.save(article);
    }
}
