package com.nokshal.amarDesh.content.service;

import com.nokshal.amarDesh.content.elasticsearch.ArticleDocument;
import com.nokshal.amarDesh.content.elasticsearch.ArticleDocumentRepository;
import com.nokshal.amarDesh.content.model.Article;
import com.nokshal.amarDesh.content.repository.ArticleRepository;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.GetRequest;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    private static final Logger logger = LoggerFactory.getLogger(ArticleService.class);

    private final ArticleRepository articleRepository;
    private final ElasticsearchClient elasticsearchClient;
    private final ArticleDocumentRepository articleDocumentRepository;

    public ArticleService(ArticleRepository articleRepository,
                          ElasticsearchClient elasticsearchClient,
                          ArticleDocumentRepository articleDocumentRepository) {
        this.articleRepository = articleRepository;
        this.elasticsearchClient = elasticsearchClient;
        this.articleDocumentRepository = articleDocumentRepository;
    }

    public Article saveArticle(Article article) {
        logger.info("Saving article with title: {}", article.getTitle());
        Article savedArticle = articleRepository.save(article);

        // Index the document in Elasticsearch
        ArticleDocument articleDocument = new ArticleDocument(
                savedArticle.getId(),
                savedArticle.getTitle(),
                savedArticle.getContent(),
                savedArticle.getCategory(),
                savedArticle.getPublishedDate(),
                savedArticle.getStatus().name()
        );
        articleDocumentRepository.save(articleDocument);

        return savedArticle;
    }

    public List<ArticleDocument> searchArticles(String keyword) {
        try {
            SearchRequest searchRequest = new SearchRequest.Builder()
                    .index("articles")
                    .query(q -> q
                            .multiMatch(m -> m
                                    .fields("title", "content")
                                    .query(keyword)))
                    .build();

            SearchResponse<ArticleDocument> response = elasticsearchClient.search(searchRequest, ArticleDocument.class);

            return response.hits().hits().stream()
                    .map(Hit::source)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error occurred while searching articles in Elasticsearch", e);
            return List.of();
        }
    }

    public List<Article> getAllArticles() {
        logger.info("Fetching all articles from the database.");
        List<Article> articles = articleRepository.findAll();

        // Synchronize Elasticsearch index
        for (Article article : articles) {
            if (!articleDocumentRepository.existsById(article.getId())) {
                ArticleDocument articleDocument = new ArticleDocument(
                        article.getId(),
                        article.getTitle(),
                        article.getContent(),
                        article.getCategory(),
                        article.getPublishedDate(),
                        article.getStatus().toString()
                );
                articleDocumentRepository.save(articleDocument);
                logger.info("Indexed article with ID: {} into Elasticsearch.", article.getId());
            }
        }

        return articles;
    }

    public Optional<Article> getArticleById(Long id) {
        logger.info("Fetching article with ID: {}", id);

        // First, fetch from the database
        Optional<Article> articleOptional = articleRepository.findById(id);

        if (articleOptional.isPresent()) {
            // Check if the article exists in Elasticsearch
            try {
                GetRequest request = new GetRequest.Builder()
                        .index("articles")
                        .id(id.toString())
                        .build();

                GetResponse<ArticleDocument> response = elasticsearchClient.get(request, ArticleDocument.class);

                if (!response.found()) {
                    // Index in Elasticsearch if not present
                    Article article = articleOptional.get();
                    ArticleDocument articleDocument = new ArticleDocument(
                            article.getId(),
                            article.getTitle(),
                            article.getContent(),
                            article.getCategory(),
                            article.getPublishedDate(),
                            article.getStatus().toString()
                    );
                    articleDocumentRepository.save(articleDocument);
                    logger.info("Indexed article with ID: {} into Elasticsearch.", id);
                }
            } catch (Exception e) {
                logger.error("Error occurred while checking article in Elasticsearch", e);
            }
        } else {
            logger.warn("No article found with ID: {}", id);
        }

        return articleOptional;
    }
}
