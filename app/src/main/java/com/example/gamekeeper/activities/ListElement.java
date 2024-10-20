package com.example.gamekeeper.activities;

public class ListElement {
    private String name; // Nombre del juego
    private int id; // ID del juego

    // Constructor
    public ListElement(String name, int id) {
        this.name = name;
        this.id = id;
    }

    // Método para obtener el nombre
    public String getName() {
        return name;
    }

    // Método para obtener el ID
    public int getId() {
        return id;
    }
}

