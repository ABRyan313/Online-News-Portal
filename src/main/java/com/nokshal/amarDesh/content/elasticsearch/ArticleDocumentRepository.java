package com.nokshal.amarDesh.content.elasticsearch;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ArticleDocumentRepository extends ElasticsearchRepository<ArticleDocument, Long> {
    List<ArticleDocument> findByTitleContaining(String title);
}

