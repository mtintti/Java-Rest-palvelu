package com.game.visailu;

public class Participants {
    private String username;
    private int score;
    

    public Participants(String username) {
        this.username = username;

        this.score = 0;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getScore() {
        return score;
    }

    public void incrementScore() {
        this.score++;
    }

}
