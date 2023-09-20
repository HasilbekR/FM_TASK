package org.example.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BeanUtil {
    private static Connection connection;
    public static Connection getConnection(){
        if(connection == null){
            try{
                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection(
                        "jdbc:postgresql://localhost:5432/fm_task",
                        "postgres", "20229"
                );
            } catch (SQLException | ClassNotFoundException e){
                throw new RuntimeException(e);
            }
        }
        return connection;
    }
}
