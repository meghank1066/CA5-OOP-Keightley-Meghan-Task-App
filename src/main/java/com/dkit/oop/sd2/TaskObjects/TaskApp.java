package com.dkit.oop.sd2.TaskObjects;

import com.dkit.oop.sd2.DAOs.MySqlTaskDAO;
import com.dkit.oop.sd2.DAOs.TaskDaoInterface;
import com.dkit.oop.sd2.DTOs.Task;
import com.dkit.oop.sd2.Exceptions.DaoException;
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
        System.out.println("1. Display All Tasks");
        System.out.println("2. Display Task by ID");
        System.out.println("3. Delete Task by ID");
        System.out.println("4. Exit");
    }

    public void handleMenu() {

        String choice = sc.nextLine();

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
     * @param tasks
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
}


