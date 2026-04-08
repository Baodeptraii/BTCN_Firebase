package com.example.mung_tam_thang_tu;

public class Movie {
    private String id;
    private String title;
    private String description;
    private String posterUrl;
    private String genre;

    public Movie() {} // Required for Firestore

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getPosterUrl() { return posterUrl; }
    public void setPosterUrl(String posterUrl) { this.posterUrl = posterUrl; }
    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
}