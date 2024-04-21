package Multithreaded;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import com.dkit.oop.sd2.DAOs.MySqlTaskDAO;
import com.dkit.oop.sd2.DAOs.TaskDaoInterface;
import com.dkit.oop.sd2.DTOs.Task;
import com.dkit.oop.sd2.Exceptions.DaoException;
import com.google.gson.Gson;

public class Server {
    final int SERVER_PORT_NUMBER = 8888;

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT_NUMBER)) {
            System.out.println("Server has started.");
            int clientNumber = 0;

            while (true) {
                System.out.println("Server: Listening/waiting for connections on port ..." + SERVER_PORT_NUMBER);
                Socket clientSocket = serverSocket.accept();
                clientNumber++;
                System.out.println("Server: Client " + clientNumber + " has connected.");

                // Create a new ClientHandler thread for the client
                Thread t = new Thread(new ClientHandler(clientSocket, clientNumber));
                t.start();
                System.out.println("Server: ClientHandler started in thread " + t.getName() + " for client " + clientNumber + ".");
            }
        } catch (IOException ex) {
            System.out.println(ex);
        }

        System.out.println("Server: Server exiting, Goodbye!");
    }
}

class ClientHandler implements Runnable {
    private Socket clientSocket;
    private int clientNumber;

    public ClientHandler(Socket clientSocket, int clientNumber) {
        this.clientSocket = clientSocket;
        this.clientNumber = clientNumber;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String requestType;
            while ((requestType = in.readLine()) != null) {
                if (requestType.equals("QUITTING")) {
                    System.out.println("client " + clientNumber + " has disconnected.");
                    break;
                }

                if (requestType.startsWith("DISPLAY_BY_ID")) {
                    int id = Integer.parseInt(requestType.substring(requestType.indexOf(":") + 1));
                    displayEntityById(id, out);
                } else if (requestType.equals("get all")) {
                    displayAllEntities(out);
                } else if (requestType.equals("ADD_NEW_TASK")) {
                    addNewTask2(in, out);
                } else if (requestType.startsWith("DELETE_TASK")) {
                    int taskIdToDelete = Integer.parseInt(requestType.substring(requestType.indexOf(":") + 1));
                    deleteNewTaskById(taskIdToDelete, out);
                } else if (requestType.equals("GET_IMAGE_LIST")) {
                    sendImageList(out);
                } else {
                    out.println("Sorry, this is an unsupported request type: " + requestType);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Feature 9: Display Task by ID
    private void displayEntityById(int id, PrintWriter out) {
        try {
            TaskDaoInterface taskDao = new MySqlTaskDAO();
            Task task = taskDao.getNewTaskById(id);
            Gson gson = new Gson();
            String jsonResponse = (task != null) ? gson.toJson(task) : "{\"error\": \"Entity not found\"}";
            out.println(jsonResponse);
        } catch (DaoException e) {
            out.println("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    //Feature 10: Display All Tasks
    private void displayAllEntities(PrintWriter out) {
        try {
            TaskDaoInterface taskDao = new MySqlTaskDAO();
            List<Task> tasks = taskDao.getAllTasks(); // Update to use getAllTasks method
            Gson gson = new Gson();
            String jsonResponse = gson.toJson(tasks);
            out.println(jsonResponse);
        } catch (DaoException e) {
            out.println("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    // Feature 11: Add New Task
    private void addNewTask2(BufferedReader in, PrintWriter out) {
        try {
            String json = in.readLine();
            Gson gson = new Gson();
            Task task = gson.fromJson(json, Task.class);
            TaskDaoInterface taskDao = new MySqlTaskDAO();
            Task newTask = taskDao.addNewTask(task);

            // After adding the new task, retrieve all tasks from the database
            List<Task> allTasks = taskDao.getnewAllTasks();
            String jsonResponse = gson.toJson(allTasks);
            out.println(jsonResponse);
        } catch (DaoException | IOException e) {
            out.println("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    //Feature 12: Delete Task
    private void deleteNewTaskById(int taskId, PrintWriter out) {
        try {
            TaskDaoInterface taskDao = new MySqlTaskDAO();
            boolean deletionSuccessful = taskDao.deleteNewTaskById(taskId);
            if (deletionSuccessful) {
                out.println("Task deleted successfully.");
            } else {
                out.println("Task with ID " + taskId + " not found.");
            }
        } catch (DaoException e) {
            out.println("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    private void sendImageList(PrintWriter out) {
        try {
            File folder = new File("images");

            // Check if the folder exists and is a directory
            if (folder.exists() && folder.isDirectory()) {
                String[] imageFiles = folder.list();
                if (imageFiles != null && imageFiles.length > 0) {
                    Gson gson = new Gson();
                    String jsonResponse = gson.toJson(imageFiles);
                    out.println(jsonResponse);
                    return; // Exit the method after sending the response
                }
            }
            // If the folder is empty or does not exist
            out.println("{\"error\": \"No images found\"}");
        } catch (Exception e) {
            // Handle any exception that might occur during file reading
            e.printStackTrace();
            out.println("{\"error\": \"Error reading image files\"}");
        }
    }



    // Implement the sendImageFile method to send the selected image file to the client
    private void sendImageFile(String fileName, Socket clientSocket) throws IOException {
        File file = new File("images/" + fileName);
        byte[] buffer = new byte[4096];
        FileInputStream fileInputStream = new FileInputStream(file);
        OutputStream outputStream = clientSocket.getOutputStream();

        int bytesRead;
        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        fileInputStream.close();
        outputStream.close();
    }

}




