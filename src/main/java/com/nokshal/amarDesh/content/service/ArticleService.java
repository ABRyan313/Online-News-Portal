package com.nokshal.amarDesh.content.service;

import com.nokshal.amarDesh.content.model.Article;
import com.nokshal.amarDesh.content.elasticsearch.ArticleDocument;
import com.nokshal.amarDesh.content.elasticsearch.ArticleDocumentRepository;
import com.nokshal.amarDesh.content.repository.ArticleRepository;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    private static final Logger logger = LoggerFactory.getLogger(ArticleService.class);

    private final ArticleRepository articleRepository;
    private final ElasticsearchRestTemplate elasticsearchRestTemplate;
    private final ArticleDocumentRepository articleDocumentRepository;

    public ArticleService(ArticleRepository articleRepository,
                          ElasticsearchRestTemplate elasticsearchRestTemplate,
                          ArticleDocumentRepository articleDocumentRepository) {
        this.articleRepository = articleRepository;
        this.elasticsearchRestTemplate = elasticsearchRestTemplate;
        this.articleDocumentRepository = articleDocumentRepository;
    }

    @Cacheable("articles")
    public List<Article> getAllArticles() {
        logger.info("Fetching all articles");
        return articleRepository.findAll();
    }

    @Cacheable(value = "articles", key = "#id")
    public Optional<Article> getArticleById(Long id) {
        logger.info("Fetching article with ID: {}", id);
        return articleRepository.findById(id);
    }

    @Transactional
    @PreAuthorize("hasRole('EDITOR') or hasRole('ADMIN')")
    public Article saveArticle(Article article) {
        logger.info("Saving article with title: {}", article.getTitle());
        Article savedArticle = articleRepository.save(article);

        // Index in Elasticsearch
        ArticleDocument articleDocument = new ArticleDocument(savedArticle);
        articleDocumentRepository.save(articleDocument);

        return savedArticle;
    }

    @Transactional
    public void deleteArticle(Long id) {
        logger.info("Deleting article with ID: {}", id);
        articleRepository.deleteById(id);
        articleDocumentRepository.deleteById(id);
    }

    public List<Article> getArticlesByCategory(String category) {
        logger.info("Fetching articles from category: {}", category);
        return articleRepository.findByCategory(category);
    }

    // Full-text search with highlighting
    public List<ArticleDocument> searchWithHighlighting(String keyword) {
        HighlightBuilder highlightBuilder = new HighlightBuilder()
                .field("content")
                .preTags("<em>")
                .postTags("</em>");

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.multiMatchQuery(keyword, "title", "content"))
                .withHighlightBuilder(highlightBuilder)
                .build();

        SearchHits<ArticleDocument> searchHits = elasticsearchRestTemplate.search(searchQuery, ArticleDocument.class);

        return searchHits.stream()
                .map(hit -> {
                    ArticleDocument article = hit.getContent();
                    String highlightedContent = String.join("...", hit.getHighlightFields().get("content"));
                    article.setContent(highlightedContent);
                    return article;
                })
                .collect(Collectors.toList());
    }

    // Suggestive search implementation
    public List<String> getSuggestions(String query) {
        NativeSearchQuery suggestQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.prefixQuery("title", query))
                .build();

        SearchHits<ArticleDocument> searchHits = elasticsearchRestTemplate.search(suggestQuery, ArticleDocument.class);
        return searchHits.stream()
                .map(hit -> hit.getContent().getTitle())
                .collect(Collectors.toList());
    }

    // Advanced search with filters and fuzzy search
    public List<ArticleDocument> advancedSearch(String keyword, String category, LocalDate startDate, LocalDate endDate) {
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.boolQuery()
                        .must(QueryBuilders.matchQuery("content", keyword))
                        .filter(QueryBuilders.termQuery("category", category))
                        .filter(QueryBuilders.rangeQuery("publishedDate").gte(startDate).lte(endDate)))
                .build();

        SearchHits<ArticleDocument> searchHits = elasticsearchRestTemplate.search(searchQuery, ArticleDocument.class);

        return searchHits.stream()
                .map(hit -> hit.getContent())
                .collect(Collectors.toList());
    }
}
