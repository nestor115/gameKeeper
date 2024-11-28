package com.example.gamekeeper.models;

public class ListElement {
    private String name;
    private int id;
    private String image;

    public ListElement(String name, int id, String image) {
        this.name = name;
        this.id = id;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getImage() {
        return image;
    }
}

