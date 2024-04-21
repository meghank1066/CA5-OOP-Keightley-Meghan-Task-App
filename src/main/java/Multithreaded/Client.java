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

            Scanner sc = new Scanner(System.in);

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
                        deleteNewTaskById(out, in, sc);
                        break;
                    case "5":
                        // Request the list of image file names from the server
                        requestImageList(out);

                        // Receive and process the list of image file names from the server
                        try {
                            String[] imageList = receiveImageList(in);
                            System.out.println("List of images:");
                            for (int i = 0; i < imageList.length; i++) {
                                System.out.println((i+1) + ". " + imageList[i]);
                            }

                            // Prompt the user to select an image
                            System.out.println("Please enter the index of the image you want to download:");
                            int selectedIndex = Integer.parseInt(sc.nextLine());

                            // After receiving the user's selected index
                            if (selectedIndex < 1 || selectedIndex > imageList.length) {
                                System.out.println("Invalid index. Please select a valid index.");
                            } else {
                                // Send request for the selected image
                                String selectedImage = imageList[selectedIndex - 1];
                                requestImage(selectedImage, out);

                                // Receive and save the selected image
                                receiveImage(selectedImage, socket.getInputStream());
                                System.out.println("Image downloaded successfully.");
                            }
                        } catch (IOException e) {
                            System.err.println("Error receiving image list: " + e.getMessage());
                        }
                        break;
                    case "14":
                        System.out.println("exiting task app + Notifying server...");
                        out.println("QUITTING");
                        try {
                            out.close();
                            in.close();
                            consoleInput.close();
                            socket.close();
                        } catch (IOException e) {
                            System.err.println("Sorry there was an error closing resources: " + e.getMessage());
                        }
                        System.exit(0);
                        break;
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
        System.out.println("|        3.(11) Add Task         !      |");
        System.out.println("|        4.(12) Delete Task      !      |");
        System.out.println("|        5.(13) Get Images list    !    |");
        System.out.println("|        5.(14) Quit                    |");



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

    private void deleteNewTaskById(PrintWriter out, BufferedReader in, Scanner sc) {
        try {
            System.out.println("Enter the ID of the task you want to delete:");
            int taskId = sc.nextInt();
            sc.nextLine(); //empties the buffer :3

            out.println("DELETE_TASK:" + taskId); //send the task id to the server
            String response = in.readLine(); //get the response from the server
            System.out.println(response);
        } catch (Exception e) {
            System.err.println("Error communicating with server: " + e.getMessage());
        }
    }

    private void requestImageList(PrintWriter out) {
        out.println("GET_IMAGE_LIST");
    }

    private String[] receiveImageList(BufferedReader in) throws IOException {
        Gson gson = new Gson();
        String jsonImageList = in.readLine();
        return gson.fromJson(jsonImageList, String[].class);
    }

    private void requestImage(String fileName, PrintWriter out) {
        out.println("REQUEST_IMAGE:" + fileName);
    }

    private static void receiveImage(String fileName, InputStream inputStream) throws IOException {
        int bytes;
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);
        byte[] buffer = new byte[4 * 1024];
        while ((bytes = inputStream.read(buffer)) != -1) {
            fileOutputStream.write(buffer, 0, bytes);
        }
        fileOutputStream.close();
    }
}








