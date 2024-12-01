package com.nokshal.amarDesh.content.repository;

import com.nokshal.amarDesh.content.model.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    List<Article> findByCategory(String category);
    List<Article> findByAuthorId(Long authorId);
    List<Article> findByStatus(String status);

    // Advanced full-text search using PostgreSQL's to_tsvector for dynamic keyword search
    @Query("SELECT a FROM Article a WHERE " +
            "(:category IS NULL OR a.category = :category) AND " +
            "(:startDate IS NULL OR a.publishedDate >= :startDate) AND " +
            "(:endDate IS NULL OR a.publishedDate <= :endDate) AND " +
            "(to_tsvector('english', a.title || ' ' || a.content) @@ plainto_tsquery('english', :query))")
    List<Article> fullTextSearch(
        @Param("query") String query,
        @Param("category") String category,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );

    // Pagination and sorting-enabled method
    @Query("SELECT a FROM Article a WHERE " +
            "(to_tsvector('english', a.title || ' ' || a.content) @@ plainto_tsquery('english', :query))")
    Page<Article> searchWithPagination(
        @Param("query") String query,
        Pageable pageable
    );
}
