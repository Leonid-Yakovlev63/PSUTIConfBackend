package ru.psuti.conf.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.psuti.conf.dto.request.CreateArticleDTO;
import ru.psuti.conf.entity.Article;
import ru.psuti.conf.service.ArticleService;

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

}
