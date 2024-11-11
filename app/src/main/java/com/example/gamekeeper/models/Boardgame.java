package com.example.gamekeeper.models;

public class Boardgame {
    private int id;
    private String name;
    private byte[] photo;
    private String description;
    private int yearPublished;
    private String numberOfPlayers;
    private String time;
    private String genre;


    // Constructor
    public Boardgame(int id, String name, byte[] photo, String description, int yearPublished, String numberOfPlayers, String time, String genre) {
        this.id = id;
        this.name = name;
        this.photo = photo;
        this.description = description;
        this.yearPublished = yearPublished;
        this.numberOfPlayers = numberOfPlayers;
        this.time = time;
        this.genre = genre;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getYearPublished() {
        return yearPublished;
    }

    public void setYearPublished(int yearPublished) {
        this.yearPublished = yearPublished;
    }

    public String getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(String numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}

