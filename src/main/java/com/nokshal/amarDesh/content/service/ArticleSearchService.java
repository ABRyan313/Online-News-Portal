package com.nokshal.amarDesh.content.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nokshal.amarDesh.content.elasticsearch.ArticleDocument;
import com.nokshal.amarDesh.content.elasticsearch.ArticleDocumentRepository;

@Service
public class ArticleSearchService {

    private final ArticleDocumentRepository articleDocumentRepository;

    public ArticleSearchService(ArticleDocumentRepository articleDocumentRepository) {
        this.articleDocumentRepository = articleDocumentRepository;
    }

    public ArticleDocument save(ArticleDocument articleDocument) {
        return articleDocumentRepository.save(articleDocument);
    }

    public List<ArticleDocument> searchByTitle(String title) {
        return articleDocumentRepository.findByTitleContaining(title);
    }

    public void deleteById(Long id) {
        articleDocumentRepository.deleteById(id);
    }
}

