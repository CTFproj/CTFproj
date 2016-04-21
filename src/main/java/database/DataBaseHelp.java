package database;

import org.sql2o.Sql2o;

public class DataBaseHelp{
    public static Sql2o sql2o = new Sql2o("jdbc:postgresql://localhost:5432/CTF","postgres","Pass1234");

    //SQL-request
    public static String UPDATE_SQL = "UPDATE team SET score = score + :val WHERE id = :id";
    public static String SELECT_SQL = "SELECT * FROM team WHERE id = :id";
}
