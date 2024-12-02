package com.nokshal.amarDesh.content.elasticsearch;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

@Document(indexName = "articles")
public class ArticleDocument {

    @Id
    private Long id;

    @Field(type = FieldType.Text, analyzer = "standard", searchAnalyzer = "standard")
    private String title;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String content;

    @Field(type = FieldType.Keyword)
    private String category;

    @Field(type = FieldType.Date)
    private LocalDateTime publishedDate;

    @Field(type = FieldType.Keyword)
    private String status;

    public ArticleDocument() {}

    public ArticleDocument(Long id, String title, String content, String category, LocalDateTime publishedDate, String status) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.category = category;
        this.publishedDate = publishedDate;
        this.status = status;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public LocalDateTime getPublishedDate() {
		return publishedDate;
	}

	public void setPublishedDate(LocalDateTime publishedDate) {
		this.publishedDate = publishedDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

  
}
