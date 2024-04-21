package com.dkit.oop.sd2.DAOs;

import com.dkit.oop.sd2.DTOs.Task;
import com.dkit.oop.sd2.Exceptions.DaoException;
import com.dkit.oop.sd2.JSON.JsonConv;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public Task getTaskById(int id) throws DaoException {
        Task task = null;
        try (Connection connection = this.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM tasks WHERE id = ?")) {
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
     * Meghan Keightley 9 Mar 2024.
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


    /* Feature 4 - Insert new Task to Database */
    @Override
    public Task addTask(Task task) throws DaoException {

        String query = "INSERT INTO tasks (title, status, priority, description, due_date) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = this.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, task.getTitle());
            preparedStatement.setString(2, task.getStatus());
            preparedStatement.setString(3, task.getPriority());
            preparedStatement.setString(4, task.getDescription());
            preparedStatement.setDate(5, new Date(task.getDueDate().getTime()));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Error in insertData(): " + e.getMessage());
        }

        return null;
    }

    /**
     * Meghan Keightley 15 Mar 2024
     */
    @Override
    public Task updateTaskById(int id, Task updatedTask) throws DaoException {
        try (Connection connection = this.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE tasks SET title = ?, status = ?, priority = ?, description = ?, due_date = ? WHERE id = ?")) {

            preparedStatement.setString(1, updatedTask.getTitle());
            preparedStatement.setString(2, updatedTask.getStatus());
            preparedStatement.setString(3, updatedTask.getPriority());
            preparedStatement.setString(4, updatedTask.getDescription());
            preparedStatement.setDate(5, new Date(updatedTask.getDueDate().getTime()));
            preparedStatement.setInt(6, id);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                return updatedTask;
            }
        } catch (SQLException e) {
            throw new DaoException("Error updating task: " + e.getMessage());
        }
        return null;
    }

    /**
     * Meghan Keightley 9 Mar 2024
     */
    @Override
    public List<Task> FilteringTasks(Task filter) throws DaoException {
        List<Task> filteredTasks = new ArrayList<>();
        // Assuming that each attribute in the Task class represents a filtering criterion
        String query = "SELECT * FROM tasks WHERE status = ? AND priority = ?"; // Add more conditions as needed
        try (Connection connection = this.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, filter.getStatus());
            preparedStatement.setString(2, filter.getPriority());
            // Set other parameters based on additional attributes in the Task class
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String title = resultSet.getString("title");
                    String status = resultSet.getString("status");
                    String priority = resultSet.getString("priority");
                    String description = resultSet.getString("description");
                    Date dueDate = resultSet.getDate("due_date");
                    Task task = new Task(id, title, status, priority, description, dueDate);
                    filteredTasks.add(task);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Error in findTasksUsingFilter(): " + e.getMessage());
        }
        return filteredTasks;
    }


//     * Meghan Keightley 9 Mar 2024

//    //converting to json
    @Override
    public String JsonConversionOfTasks() {
        try {
            List<Task> ConvertAllTasks = getAllTasks();
            String json = JsonConv.TaskConversionToJson(ConvertAllTasks);
            System.out.println("Json display of tasks:");
            System.out.println(json);
            return json;
        } catch (DaoException e) {
            System.out.println("Error converting tasks to JSON: " + e.getMessage());
            return null;
        }
    }

    @Override
    public String JsonFormEntityByKey(int taskId) throws DaoException {
        Task task = getTaskById(taskId);
        List<Task> singleTaskList = new ArrayList<>();
        singleTaskList.add(task);
        return JsonConv.TaskConversionToJson(singleTaskList);
    }


//    @Override
//    public Task getEntityById(int id) throws DaoException {
//        Task task = null;
//        try (Connection connection = this.getConnection();
//             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM tasks WHERE id = ?");
//        ) {
//            preparedStatement.setInt(1, id);
//            try (ResultSet resultSet = preparedStatement.executeQuery()) {
//                if (resultSet.next()) {
//                    String title = resultSet.getString("title");
//                    String status = resultSet.getString("status");
//                    String priority = resultSet.getString("priority");
//                    String description = resultSet.getString("description");
//                    java.sql.Date dueDate = resultSet.getDate("due_date");
//
//                    task = new Task(id, title, status, priority, description, dueDate);
//                }
//            }
//        } catch (SQLException e) {
//            throw new DaoException("Error in getEntityById(): " + e.getMessage());
//        }
//        return task;
//    }

    @Override
    public Task getNewTaskById(int id) throws DaoException {
        Task task = null;
        try (Connection connection = this.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM tasks WHERE id = ?")) {
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

    @Override
    public List<Task> getnewAllTasks() throws DaoException {
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

    //add new task
    public Task addNewTask(Task task) throws DaoException {
        String query = "INSERT INTO tasks (title, status, priority, description, due_date) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = this.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, task.getTitle());
            preparedStatement.setString(2, task.getStatus());
            preparedStatement.setString(3, task.getPriority());
            preparedStatement.setString(4, task.getDescription());
            preparedStatement.setDate(5, new java.sql.Date(task.getDueDate().getTime()));

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected == 0) {
                throw new SQLException("Creating task failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    task.setTaskId(id); // Set the task ID generated by the database
                } else {
                    throw new SQLException("Creating task failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Error adding new task: " + e.getMessage());
        }

        return task;
    }


//feature 12
@Override
public boolean deleteNewTaskById(int id) throws DaoException {
    try (Connection connection = this.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM tasks WHERE id = ?")) {

        preparedStatement.setInt(1, id);
        int rowsAffected = preparedStatement.executeUpdate();

        // If rowsAffected > 0, it means a task was deleted successfully
        return rowsAffected > 0;

    } catch (SQLException e) {
        throw new DaoException("Error in deleteTaskById(): " + e.getMessage());
    }
}



    //end of file
}
