package database;

public class DataBaseHelp {

    public static String UPDATE_TEAM_SQL = "UPDATE team SET score = score + :val WHERE id = :id";
    public static String SELECT_TEAM_SQL = "SELECT * FROM team WHERE id = :id";
    public static String SELECT_TASK_SQL = "SELECT * FROM task ORDER BY category";
    public static String SELECT_TASK_BY_ID_SQL = "SELECT * FROM task WHERE id = :id";
    public static String INSERT_SQL = "INSERT INTO solve_task(team_id, task_id) VALUES ( :team_id, :task_id)";
    public static String SELECT_TEAM_DESC_SQL = "SELECT * FROM team ORDER BY score DESC";
    public static String SELECT_NOT_SOLVE_SQL = "SELECT * FROM task EXCEPT (SELECT task.id, task.name, task.des, task.flag, task.score, task.category " +
            "FROM solve_task JOIN task ON solve_task.task_id = task.id WHERE team_id = :id)";

}
