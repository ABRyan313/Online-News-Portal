package com.nokshal.amarDesh.content.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import java.util.List;

public interface ArticleDocumentRepository extends ElasticsearchRepository<ArticleDocument, Long> {
    List<ArticleDocument> findByTitleContaining(String title);
}

