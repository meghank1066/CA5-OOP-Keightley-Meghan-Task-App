package com.dkit.oop.sd2.TaskObjects;

import com.dkit.oop.sd2.DAOs.MySqlTaskDAO;
import com.dkit.oop.sd2.DAOs.TaskDaoInterface;
import com.dkit.oop.sd2.DTOs.Task;
import com.dkit.oop.sd2.Exceptions.DaoException;
import com.dkit.oop.sd2.JSON.JsonConv;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

//meghan

public class TaskApp {

    public static void main(String[] args) {
        TaskDaoInterface taskDao = new MySqlTaskDAO();
        TaskApp taskApp = new TaskApp(taskDao);

        while (true) {
            taskApp.displayMenu();
            taskApp.handleMenu();
        }
    }

    Scanner sc = new Scanner(System.in);
    private TaskDaoInterface taskDao;

    public TaskApp(TaskDaoInterface taskDao) {
        this.taskDao = taskDao;
    }

    public void displayMenu() {
        System.out.println("1. Display All Tasks"); // m
        System.out.println("2. Display Task by ID"); // m
        System.out.println("3. Delete Task by ID"); // m
        System.out.println("4. Add Task by ID"); // m
        System.out.println("5. Update Task by ID"); // m
        System.out.println("6. Filter by Status & Priority"); // m
        System.out.println("7. Convert List of Entities to a JSON String "); // m

        System.out.println("0. Exit");
    }

    public void handleMenu() {

        String choice = sc.nextLine();
        Task addedTask = null;

        switch (choice) {
            case "1":
                displayAllTasks();
                break;
            case "2":
                displayTaskById();
                break;
            case "3":
                deleteTaskById();
                break;
            case "4":
                addedTask = insertTask();
                System.out.println("Added Task: " + addedTask);
                break;
            case "5":
                updateTask();
                break;
            case "6":
                filterTasks();
                break;
            case "7":
                JsonConversionOfTasks();
                break;
            case "0":
                System.out.println("Exiting...");
                System.exit(0);
            default:
                System.out.println("Invalid choice. Please enter a number between 1 and 4.");
        }
    }

    /**
     * Meghan Keightley 9 Mar 2024.
     */
    private void displayAllTasks() {
        try {
            List<Task> allTasks = taskDao.getAllTasks();
            System.out.println("All Tasks:");
            displayTasks(allTasks);
        } catch (DaoException e) {
            System.out.println("Error retrieving tasks: " + e.getMessage());
        }
    }


    /**
     * Meghan Keightley 9 Mar 2024
     */
    private void displayTaskById() {
        try {
            System.out.print("Enter Task ID: ");
            int Id = Integer.parseInt(sc.nextLine());

            Task task = taskDao.getTaskById(Id);
            if (task != null) {
                System.out.println("Task by ID " + Id + ": " + task);
            } else {
                System.out.println("Task with ID " + Id + " not found.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number for Task ID.");
        } catch (DaoException e) {
            System.out.println("Error retrieving task: " + e.getMessage());
        }
    }

    /**
     * Meghan Keightley 9 Mar 2024
     *
     */
    private void displayTasks(List<Task> tasks) {
        for (Task task : tasks) {
            System.out.println(task);
        }
    }

    /**
     * Meghan Keightley 9 Mar 2024
     */
    private void deleteTaskById() {
        try {
            System.out.print("Enter Task ID to delete: ");
            int taskId = Integer.parseInt(sc.nextLine());

            Task deletedTask = taskDao.deleteTaskById(taskId);
            if (deletedTask != null) {
                System.out.println("Task with ID " + taskId + " deleted.");
            } else {
                System.out.println("Task with ID " + taskId + " not found or could not be deleted.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number for Task ID.");
        } catch (DaoException e) {
            System.out.println("Error deleting task: " + e.getMessage());
        }
    }


    /*Feature 4 - inserting Task */
    private Task insertTask() {

        System.out.println("Task Title:");
        String title = sc.nextLine();

        String status;
        do {
            System.out.println("Task Status (DONE, PROGRESS, OPEN):");
            status = sc.nextLine().toUpperCase(); // Convert input to uppercase for case-insensitivity
            if (!status.equals("DONE") && !status.equals("PROGRESS") && !status.equals("OPEN")) {
                System.out.println("Invalid status. Please enter one of: DONE, PROGRESS, OPEN");
            }
        } while (!status.equals("DONE") && !status.equals("PROGRESS") && !status.equals("OPEN"));


        String priority;
        System.out.println("Task Priority (CRITICAL, HIGH, MEDIUM, LOW, MIN):");
        while (true) {
            priority = sc.nextLine();
            if (priority.equalsIgnoreCase("CRITICAL") || priority.equalsIgnoreCase("HIGH") ||
                    priority.equalsIgnoreCase("MEDIUM") || priority.equalsIgnoreCase("LOW") ||
                    priority.equalsIgnoreCase("MIN")) {
                break;
            }
            System.out.println("Invalid priority. Please enter one of: CRITICAL, HIGH, MEDIUM, LOW, MIN");
        }


        System.out.println("Task Description:");
        String description = sc.nextLine();

        /* Change to allow user to insert Date
         * will need to use a Parser */
        String dueDateStr = null;
        Date due_date = null;


        // Prompt user for date until a valid format is provided
        while (due_date == null) {
            System.out.println("Task Due Date (YYYY-MM-DD):");
            dueDateStr = sc.nextLine();

            try {
                // Attempt to parse the due date string
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                due_date = sdf.parse(dueDateStr);
            } catch (ParseException e) {
                // If parsing fails, inform the user and prompt for input again
                System.out.println("Invalid date format. Please enter the date in the format YYYY-MM-DD.");
            }
        }

        Task newTask = new Task(title, status, priority, description, due_date);


        try {
            taskDao.addTask(newTask);
            System.out.println("Task inserted successfully!");
        } catch (DaoException e) {
            System.out.println("Error inserting task: " + e.getMessage());
        }

        return newTask;
    }


    /**
     * Meghan Keightley 9 Mar 2024
     */
    /*Feature 5 - Updating Task */
    private Task updateTask() {
        try {
            System.out.print("Enter Task ID to update: ");
            int taskId = Integer.parseInt(sc.nextLine());

            Task existingTask = taskDao.getTaskById(taskId);
            if (existingTask != null) {
                // Prompt the user to enter updated task details
                System.out.println("Enter updated task details:");

                // Get updated task details from user input
                Task updatedTask = insertTask();
                updatedTask.setTaskId(taskId);

                // Update the task in the database
                Task updated = taskDao.updateTaskById(taskId, updatedTask);
                return updated;
            } else {
                System.out.println("Task with ID " + taskId + " not found.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number for Task ID.");
        } catch (DaoException e) {
            System.out.println("Error updating task: " + e.getMessage());
        }
        return null;
    }

    /**
     * Meghan Keightley 9 Mar 2024
     */
    private void filterTasks() {
        Task filter = new Task();

        System.out.println("Enter Filter Criteria:");
        System.out.print("Task Status (DONE, PROGRESS, OPEN): ");
        filter.setStatus(sc.nextLine().toUpperCase());
        System.out.print("Task Priority (CRITICAL, HIGH, MEDIUM, LOW, MIN): ");
        filter.setPriority(sc.nextLine());

        try {
            List<Task> filteredTasks = taskDao.FilteringTasks(filter);
            if (!filteredTasks.isEmpty()) {
                System.out.println("Filtered Tasks:");
                displayTasks(filteredTasks);
            } else {
                System.out.println("No tasks found matching the filter criteria.");
            }
        } catch (DaoException e) {
            System.out.println("Error filtering tasks: " + e.getMessage());
        }
    }


    private void JsonConversionOfTasks() {
        try {
            List<Task> ConvertAllTasks = taskDao.getAllTasks();
            String json = JsonConv.TaskConversionToJson(ConvertAllTasks);
            System.out.println("JSON representation of tasks:");
            System.out.println(json);
        } catch (DaoException e) {
            System.out.println("Error converting tasks to JSON: " + e.getMessage());
        }
    }
}
