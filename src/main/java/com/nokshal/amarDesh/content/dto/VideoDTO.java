package com.nokshal.amarDesh.content.dto;

public class VideoDTO {
    private Long id;
    private String title;
    private String url;
    private String description;
    private String uploader;

    // Constructors
    public VideoDTO() {}

    public VideoDTO(Long id, String title, String url, String description, String uploader) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.description = description;
        this.uploader = uploader;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getUploader() { return uploader; }
    public void setUploader(String uploader) { this.uploader = uploader; }
}

