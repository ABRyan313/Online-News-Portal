package com.nokshal.amarDesh.content.controller;

import com.nokshal.amarDesh.content.dto.ArticleDTO;
import com.nokshal.amarDesh.content.model.Article;
import com.nokshal.amarDesh.content.service.ArticleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public List<Article> getAllArticles() {
        return articleService.getAllArticles();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Article> getArticleById(@PathVariable Long id) {
        Optional<Article> article = articleService.getArticleById(id);
        if (article.isPresent() && "PUBLIC".equals(article.get().getStatus())) {
            return ResponseEntity.ok(article.get());
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PreAuthorize("hasRole('EDITOR') or hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<Article> createArticle(@Valid @RequestBody ArticleDTO articleDTO) {
        Article article = new Article();
        article.setTitle(articleDTO.getTitle());
        article.setContent(articleDTO.getContent());
        article.setAuthor(articleDTO.getAuthor());
        article.setCategory(articleDTO.getCategory());

        Article savedArticle = articleService.saveArticle(article);
        return ResponseEntity.ok(savedArticle);
    }

    @PreAuthorize("hasRole('EDITOR') or hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable Long id, @Valid @RequestBody ArticleDTO articleDTO) {
        Optional<Article> existingArticle = articleService.getArticleById(id);
        if (existingArticle.isPresent()) {
            Article article = existingArticle.get();
            article.setTitle(articleDTO.getTitle());
            article.setContent(articleDTO.getContent());
            article.setAuthor(articleDTO.getAuthor());
            article.setCategory(articleDTO.getCategory());
            return ResponseEntity.ok(articleService.saveArticle(article));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
