package model;

public class SolvedTask {
    private int team_id;
    private int task_id;

    public SolvedTask(int team_id, int task_id) {
        this.team_id = team_id;
        this.task_id = task_id;
    }

    public int getTeam_id() {
        return team_id;
    }

    public int getTask_id() {
        return task_id;
    }
}
