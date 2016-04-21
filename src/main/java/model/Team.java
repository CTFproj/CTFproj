package model;

public class Team {
    private int id;
    private String name;
    private String pass;
    private int score;

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getPass() {
        return pass;
    }
    public int getScore() {
        return score;
    }
    public void addScore(int num) {
        score = getScore() + num;
    }
}
