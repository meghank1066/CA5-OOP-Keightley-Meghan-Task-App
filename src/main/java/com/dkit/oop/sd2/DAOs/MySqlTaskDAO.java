package com.dkit.oop.sd2.DAOs;

import com.dkit.oop.sd2.DTOs.Task;
import com.dkit.oop.sd2.Exceptions.DaoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;


public class MySqlTaskDAO extends MySqlDao implements TaskDaoInterface {

    /**
     * Meghan Keightley 9 Mar 2024
     */
    @Override
    public List<Task> getAllTasks() throws DaoException {
        List<Task> tasksList = new ArrayList<>();
        try (Connection connection = this.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM tasks");
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String status = resultSet.getString("status");
                String priority = resultSet.getString("priority");
                String description = resultSet.getString("description");
                java.sql.Date dueDate = resultSet.getDate("due_date");

                Task task = new Task(id, title, status, priority, description, dueDate);
                tasksList.add(task);
            }
        } catch (SQLException e) {
            throw new DaoException("Error in getAllTasks(): " + e.getMessage());
        }
        return tasksList;
    }

    /**
     * Meghan Keightley 9 Mar 2024
     */
    @Override
    public Task getTaskById(int id) throws DaoException {
        Task task = null;
        try (Connection connection = this.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM tasks WHERE id = ?");
        ) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String title = resultSet.getString("title");
                    String status = resultSet.getString("status");
                    String priority = resultSet.getString("priority");
                    String description = resultSet.getString("description");
                    java.sql.Date dueDate = resultSet.getDate("due_date");

                    task = new Task(id, title, status, priority, description, dueDate);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Error in getTaskById(): " + e.getMessage());
        }
        return task;
    }

    /**
     * Meghan Keightley 9 Mar 2024
     */
    @Override
    public Task deleteTaskById(int id) throws DaoException {
        Task deletedTask = null;
        try (Connection connection = this.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM tasks WHERE id = ?")) {

            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                // If a task was deleted, create a Task object with the deleted task_id
                deletedTask = new Task(id, null, null, null, null, null);
            }

        } catch (SQLException e) {
            throw new DaoException("Error in deleteTaskById(): " + e.getMessage());
        }
        return deletedTask;
    }


}