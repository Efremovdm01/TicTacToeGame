package com.example.tictactoe;

public class Player {
    int Id;
    private String Name;
    int Score;

    public Player() {
    }

    public Player(int id,String name, int score) {
        Id = id;
        Name = name;
        Score = score;
    }


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getScore() {
        return Score;
    }

    public void setScore(int score) {
        Score = score;
    }
}
