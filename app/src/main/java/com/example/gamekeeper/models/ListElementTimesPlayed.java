package com.example.gamekeeper.models;

public class ListElementTimesPlayed extends ListElement {

    public int timesPlayed;

    public ListElementTimesPlayed(String name, int id, String image, int timesPlayed) {
        super(name, id, image);
        this.timesPlayed = timesPlayed;
    }

    public int getTimesPlayed() {
        return this.timesPlayed;

    }

}
