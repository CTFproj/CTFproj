package model;

public class Task {
    private int id;
    private String name;
    private String des;
    private String flag;
    private int score;
    private String category;

    public Task(int id, String name, String des, String flag, int score, String category) {
        this.id = id;
        this.name = name;
        this.des = des;
        this.flag = flag;
        this.score = score;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public String getDes() {
        return des;
    }

    public int getScore() {
        return score;
    }

    public String getFlag() {
        return flag;
    }
}

