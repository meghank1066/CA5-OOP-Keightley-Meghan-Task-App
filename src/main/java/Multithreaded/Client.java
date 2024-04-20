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
    final int SERVER_PORT = 8889;
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


            while (true) {
                displayMenu();
                Scanner sc = new Scanner(System.in);
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
//                        deleteTaskById();
                        break;
                    case "4":
//                        addedTask = insertTask();
//                        System.out.println("Added Task: " + addedTask);
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
        System.out.println("|        1. Display Entity by ID        |");
        System.out.println("|        2. Display Task by ID          |");
        System.out.println("|        3. Delete Task by ID           |");
        System.out.println("|        4. Add Task by ID              |");
        System.out.println("|        5. Update Task by ID           |");
        System.out.println("|        6. Filter by Status & Priority |");
        System.out.println("|        7. Convert List JSON String    |");
        System.out.println("|        8. Convert Task to JSON String |");


        System.out.println("=========================================");
    }

    private void displayAllEntities(PrintWriter out, BufferedReader in)throws IOException{
        out.println("get all");

        System.out.println("Response from server (All Entities):");
        String line;
        while((line = in.readLine()) != null){
            System.out.println(line);
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
}


