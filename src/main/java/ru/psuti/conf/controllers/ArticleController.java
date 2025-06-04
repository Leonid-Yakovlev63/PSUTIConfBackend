package ru.psuti.conf.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ru.psuti.conf.dto.request.CreateArticleDTO;
import ru.psuti.conf.dto.response.FileInfoDTO;
import ru.psuti.conf.entity.Article;
import ru.psuti.conf.entity.FileInfo;
import ru.psuti.conf.service.ArticleService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @PostMapping("/create")
    public ResponseEntity<String> createArticle(
            @RequestBody @Valid CreateArticleDTO createArticleDTO
    ) throws Exception {
        Optional<Article> optionalArticle = this.articleService.createArticle(createArticleDTO);
        if(optionalArticle.isPresent()) {
            return new ResponseEntity<String>("Article successfully created",HttpStatus.CREATED);
        }
        throw new Exception("Failed to create conference");
    }

    @PostMapping(value = "/{articleId}/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileInfoDTO> uploadFile(
            @PathVariable Long articleId,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            FileInfo uploadedFile = articleService.uploadArticleFile(articleId, file);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(FileInfoDTO.builder()
                            .id(uploadedFile.getId())
                            .size(uploadedFile.getSize())
                            .name(uploadedFile.getName())
                            .fileType(uploadedFile.getFileType())
                            .uploadDate(LocalDate.now())
                            .articleId(uploadedFile.getArticle().getId())
                            .build());
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to save file: " + e.getMessage()
            );
        }
    }
}
