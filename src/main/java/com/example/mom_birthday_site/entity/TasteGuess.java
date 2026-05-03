package com.example.mom_birthday_site.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.sql.Time;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="taste_guess")
public class TasteGuess {

    @Id
    private Long id;

    private String guess;
    private LocalDateTime lastGuessTime;

    public TasteGuess( String guess, LocalDateTime lastGuess) {
        this.guess = guess;
        this.lastGuessTime = lastGuess;
    }
    public TasteGuess(Long id, String guess, LocalDateTime lastGuess) {
        this.id = id;
        this.guess = guess;
        this.lastGuessTime = lastGuess;
    }

    public String getGuess() {
        return guess;
    }

    public void setGuess(String guess) {
        this.guess = guess;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getLastGuessTime() {
        return lastGuessTime;
    }

    public void setLastGuessTime(LocalDateTime lastGuessTime) {
        this.lastGuessTime = lastGuessTime;
    }
}