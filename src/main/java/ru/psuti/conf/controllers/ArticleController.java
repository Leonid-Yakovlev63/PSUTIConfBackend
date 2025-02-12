package ru.psuti.conf.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.psuti.conf.entity.Article;
import ru.psuti.conf.service.ArticleService;

import java.io.IOException;

@RestController
@RequestMapping("/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @PostMapping
    public ResponseEntity<String> createArticle(@Valid @RequestBody Article article, @RequestParam("file") MultipartFile file) throws IOException {
        if (articleService.createArticle(article, file) != null){
            return new ResponseEntity<>("Article created successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Error while creating article", HttpStatus.BAD_REQUEST);
    }
}
