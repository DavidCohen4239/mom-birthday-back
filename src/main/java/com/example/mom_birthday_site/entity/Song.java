package com.example.mom_birthday_site.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String songName;
    private boolean isFound;

    public Song() {}

    public Song(String songName, boolean isFound) {
        this.songName = songName;
        this.isFound = isFound;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSongName() { return songName; }
    public void setSongName(String songName) { this.songName = songName; }
    public boolean isFound() { return isFound; }
    public void setFound(boolean found) { isFound = found; }
}