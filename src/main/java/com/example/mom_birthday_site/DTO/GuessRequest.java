package com.example.mom_birthday_site.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GuessRequest {
    private String guess;
    // Getters and Setters
    public String getGuess() { return guess; }
    public void setGuess(String guess) { this.guess = guess; }
}