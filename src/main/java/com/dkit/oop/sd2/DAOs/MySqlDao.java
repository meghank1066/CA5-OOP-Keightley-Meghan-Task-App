package com.dkit.oop.sd2.DAOs;
/** MySqlDao -
 * - implements functionality that is common to all MySQL DAOs
 * - i.e. getConection() and freeConnection()
 * All MySQL DAOs will extend (inherit from) this class in order to
 * gain the connection functionality, thus avoiding inclusion
 * of this code in every DAO class.
 *
 */

import java.sql.*;

import com.dkit.oop.sd2.DTOs.Task;
import com.dkit.oop.sd2.Exceptions.DaoException;
import java.sql.Statement;

public class MySqlDao {
    public Connection getConnection() throws DaoException {
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/task_db";
        String username = "root";
        String password = "";
        Connection connection = null;

        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            System.out.println("Failed to find driver class " + e.getMessage());
            System.exit(1);
        } catch (SQLException e) {
            System.out.println("Connection failed " + e.getMessage());
            System.exit(2);
        }
        return connection;
    }


    public void freeConnection(Connection connection) throws DaoException {
        try {
            if (connection != null) {
                connection.close();
                connection = null;
            }
        } catch (SQLException e) {
            System.out.println("Failed to free connection: " + e.getMessage());
            System.exit(1);
        }


    }

//commit
    /**
     * Meghan Keightley 9 Mar 2024
     */
    public Task createTask(String title, String status, String priority,
                           String description, Date dueDate) throws DaoException {
        Task task = null;
        int insertReturnCode = 0;
        int last_insert_id = 0;

        String queryInsert = "INSERT INTO tasks (title, status, priority, description, due_date) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = this.getConnection();
             PreparedStatement ps = connection.prepareStatement(queryInsert, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, title);
            ps.setString(2, status);
            ps.setString(3, priority);
            ps.setString(4, description);
            ps.setDate(5, dueDate);
            insertReturnCode = ps.executeUpdate();

            ResultSet generatedKeyResultSet = ps.getGeneratedKeys();

            if (generatedKeyResultSet.next()) {
                last_insert_id = generatedKeyResultSet.getInt(1);
            }
            System.out.println("Last Inserted record's ID: " + last_insert_id);

        } catch (SQLException e) {
            throw new DaoException("createTask() " + e.getMessage());
        }

        String querySelect = "SELECT * FROM tasks WHERE task_id = ?";

        try (Connection connection = this.getConnection();
             PreparedStatement ps = connection.prepareStatement(querySelect)) {
            if (insertReturnCode == 1) {

                ps.setInt(1, last_insert_id);
                ResultSet resultSet = ps.executeQuery();

                if (resultSet.next()) {
                    int taskId = resultSet.getInt("task_id");
                    String taskTitle = resultSet.getString("title");
                    String taskStatus = resultSet.getString("status");
                    String taskPriority = resultSet.getString("priority");
                    String taskDescription = resultSet.getString("description");
                    Date taskDueDate = resultSet.getDate("due_date");

                    task = new Task(taskId, taskTitle, taskStatus, taskPriority, taskDescription, taskDueDate);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("createTask() " + e.getMessage());
        }

        return task;
    }
}