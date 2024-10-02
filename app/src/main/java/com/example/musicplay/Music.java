package com.example.musicplay;

public class Music {
    private String title;
    private String author;
    private String url;
    private String duration;

    // Construtores, getters y setters
    public Music(String title, String author, String url, String duration) {
        this.title = title;
        this.author = author;
        this.url = url;
        this.duration = duration;
    }

    // Getters
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getUrl() { return url; }
    public String getDuration() { return duration; }
}