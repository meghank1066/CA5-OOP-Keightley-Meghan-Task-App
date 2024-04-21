package Multithreaded;

import com.dkit.oop.sd2.DTOs.Task;
import com.dkit.oop.sd2.Exceptions.DaoException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
public class Client {
    final String SERVER_ADDRESS = "localhost";
    final int SERVER_PORT = 8888;
    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }

    public void start() {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Connected to server.");

            Scanner sc = new Scanner(System.in); // Create Scanner object outside the loop

            while (true) {
                displayMenu();
                System.out.println("Please Enter Your Choice");

                String choice = sc.nextLine();
                switch (choice) {
                    case "1":
                        displayEntityById(out, in, sc);
                        break;
                    case "2":
                        displayAllEntities(out, in);
                        break;
                    case "3":
                        addNewTask2(out, in, sc);
                        break;
                    case "4":
//                        deleteTaskById();
                        break;
                    case "5":
//                        updateTask();
                        break;
                    case "6":
//                        filterTasks();
                        break;
                    case "7":
//                        JsonConversionOfTasks();
                        break;
                    case "8":
//                        JsonFormEntityByKey();
                        break;
                    case "0":
                        System.out.println("Exiting...");
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 10.");
                }
            }
        } catch (IOException e) {
            System.err.println("Error in client: " + e.getMessage());
        }
    }



    private void displayMenu(){
        System.out.println("=========================================");
        System.out.println("|               Main Menu:              |");
        System.out.println("=========================================");
        System.out.println("|        1 (9). Display Tasks by ID     |");
        System.out.println("|        2.(10) Display All Tasks       |");
        System.out.println("|        3.(11) Add Task                |");
        System.out.println("|        4.(12) Delete Task             |");
        System.out.println("|        5.(13) Get Images list        |");



        System.out.println("=========================================");
    }

//    private void displayAllEntities(PrintWriter out, BufferedReader in)throws IOException{
//        out.println("get all");
//
//        System.out.println("Response from server (All Entities):");
//        String line;
//        while((line = in.readLine()) != null){
//            System.out.println(line);
//        }
//    }

//    private void displayAllEntities(PrintWriter out, BufferedReader in) throws IOException {
//        out.println("get all");
//
//        System.out.println("Response from server (All Entities):");
//        StringBuilder cleanedUpJson = new StringBuilder();
//        String line;
//        while ((line = in.readLine()) != null) {
//            cleanedUpJson.append(line.trim());
//        }
//
//        // Format the JSON entities cleanly
//        String[] parts = cleanedUpJson.toString().split("\\},");
//        cleanedUpJson = new StringBuilder();
//        for (int i = 0; i < parts.length; i++) {
//            if (i != 0) {
//                cleanedUpJson.append("},\n");
//            }
//            cleanedUpJson.append(parts[i].trim());
//        }
//
//        System.out.println("JSON representation of tasks:");
//        System.out.println(cleanedUpJson.toString());
//    }

    private void displayAllEntities(PrintWriter out, BufferedReader in) throws IOException {
        out.println("get all");

        System.out.println("Response from server (All Entities):");
        StringBuilder cleanedUpJson = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            cleanedUpJson.append(line.trim());
        }

        // Format the JSON entities cleanly
        String[] parts = cleanedUpJson.toString().split("\\},");
        cleanedUpJson = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            if (i != 0) {
                cleanedUpJson.append("},\n");
            }
            cleanedUpJson.append(parts[i].trim());
        }

        System.out.println("JSON representation of tasks:");
        System.out.println(cleanedUpJson.toString());

        // Wait for the server to close the connection
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void displayEntityById(PrintWriter out, BufferedReader in, Scanner sc) {
        try {
            System.out.println("Enter the ID of the entity you want to display:");
            int entityId = sc.nextInt();
            sc.nextLine(); // clear the buffer

            out.println("DISPLAY_BY_ID:" + entityId);
            String jsonData = in.readLine();
            System.out.println(jsonData);
            if (!"null".equals(jsonData)) {
                // Configure Gson to parse the date format "Mar 3, 2024"
                Gson gson = new GsonBuilder()
                        .setDateFormat("MMM d, yyyy") // This should match the date format from the server
                        .create();

                Task task = gson.fromJson(jsonData, Task.class);
                System.out.println("Due Date: " + task.getDueDate());
                // Optionally, display more details of the task
                displayTaskDetails(task);

            } else {
                System.out.println("Entity not found.");
            }
        } catch (Exception e) {
            System.err.println("Error communicating with server: " + e.getMessage());
        }
    }

    private void displayTaskDetails(Task task) {
        System.out.println("Entity details:");
        System.out.println("Task ID: " + task.getTaskId());
        System.out.println("Title: " + task.getTitle());
        System.out.println("Status: " + task.getStatus());
        System.out.println("Priority: " + task.getPriority());
        System.out.println("Description: " + task.getDescription());
        System.out.println("Due Date: " + task.getDueDate());
    }

    private void addNewTask2(PrintWriter out, BufferedReader in, Scanner sc) {
        try {
            Task newTask = new Task();

            // Input validation for title
            System.out.println("Enter Title:");
            String title = sc.nextLine().trim();
            while (title.isEmpty()) {
                System.out.println("Title cannot be empty. Please enter a valid title:");
                title = sc.nextLine().trim();
            }
            newTask.setTitle(title);

            // Input validation for status
            System.out.println("Enter Status:");
            String status = sc.nextLine().trim();
            while (!status.matches("[A-Za-z]+")) {
                System.out.println("Status must contain only letters. Please enter a valid status:");
                status = sc.nextLine().trim();
            }
            newTask.setStatus(status);

            // Input validation for priority
            System.out.println("Enter Priority:");
            String priority = sc.nextLine().trim();
            while (!priority.matches("[A-Za-z]+")) {
                System.out.println("Priority must contain only letters. Please enter a valid priority:");
                priority = sc.nextLine().trim();
            }
            newTask.setPriority(priority);

            // Input validation for description
            System.out.println("Enter Description:");
            String description = sc.nextLine().trim();
            while (description.isEmpty()) {
                System.out.println("Description cannot be empty. Please enter a valid description:");
                description = sc.nextLine().trim();
            }
            newTask.setDescription(description);

            // Input validation for due date
            System.out.println("Enter Due Date (yyyy-MM-dd):");
            String dueDateString = sc.nextLine().trim();
            while (!dueDateString.matches("\\d{4}-\\d{2}-\\d{2}")) {
                System.out.println("Invalid date format. Please enter the due date in the format yyyy-MM-dd:");
                dueDateString = sc.nextLine().trim();
            }
            newTask.setDueDate(java.sql.Date.valueOf(dueDateString));

            // Send the new task to the server to be added
            out.println("ADD_NEW_TASK:" + addNewTaskToServer(newTask, out, in));
            System.out.println("New task added successfully.");
        } catch (Exception e) {
            System.err.println("Error adding new task: " + e.getMessage());
        }
    }

    private String addNewTaskToServer(Task newTask, PrintWriter out, BufferedReader in) throws IOException {
        // Convert the new task to JSON
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        String jsonTask = gson.toJson(newTask);

        // Send the JSON string to the server
        out.println(jsonTask);

        // Wait for response from the server
        return in.readLine();
    }


}





