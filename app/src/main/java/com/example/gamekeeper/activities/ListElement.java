package com.example.gamekeeper.activities;

public class ListElement {
    private String name;
    private int id;
    private byte[] image;

    public ListElement(String name, int id, byte[] image) {
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

    public byte[] getImage() {
        return image;
    }
}

