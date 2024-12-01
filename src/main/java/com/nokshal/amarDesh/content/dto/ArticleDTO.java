package com.nokshal.amarDesh.content.dto;




import com.nokshal.amarDesh.auth.model.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ArticleDTO {
    private Long id;

    @NotBlank(message = "Title is mandatory")
    @Size(min = 5, max = 100, message = "Title must be between 5 and 100 characters")
    private String title;

    @NotBlank(message = "Content cannot be empty")
    private String content;

    private User author;
    
    @NotBlank(message = "Category is required.")
    @Size(max = 50, message = "Category must not exceed 50 characters.")
    private String category;

    // Constructors
    public ArticleDTO() {}

    public ArticleDTO(Long id, String title, String content, User author, String category) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.category = category;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public User getAuthor() { return author; }
    public void setAuthor(User author) { this.author = author; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}
