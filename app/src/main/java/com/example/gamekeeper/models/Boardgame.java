package com.example.gamekeeper.models;
public class Boardgame {
    private int id;
    private String name;
    private String description;
    private int yearPublished;
    private String numberOfPlayers;
    private String time;
    private String photo;

    // Constructor
    public Boardgame(int id, String name, String description, int yearPublished,
                     String numberOfPlayers, String time, String photo) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.yearPublished = yearPublished;
        this.numberOfPlayers = numberOfPlayers;
        this.time = time;
        this.photo = photo;
    }

    // Getters y setters
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }


}
