package com.example.gamekeeper.models;

import java.util.Objects;

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        return obj instanceof ListElement && this.id == ((ListElement) obj).id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

