package Multithreaded;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import com.dkit.oop.sd2.DAOs.MySqlTaskDAO;
import com.dkit.oop.sd2.DAOs.TaskDaoInterface;
import com.dkit.oop.sd2.DTOs.Task;
import com.dkit.oop.sd2.Exceptions.DaoException;
import com.google.gson.Gson;

public class Server {
    final int SERVER_PORT_NUMBER = 8889;

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

            String requestType = in.readLine();
            if (requestType.startsWith("DISPLAY_BY_ID")) {
                int id = Integer.parseInt(requestType.substring(requestType.indexOf(":") + 1));
                displayEntityById(id, out);
            } else if (requestType.equals("get all")) {
                displayAllEntities(out);
            } else {
                out.println("Unsupported request type: " + requestType);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    private void displayAllEntities(PrintWriter out) {
        try {
            TaskDaoInterface taskDao = new MySqlTaskDAO();
            List<Task> tasks = taskDao.getAllTasks();
            Gson gson = new Gson();
            String jsonResponse = gson.toJson(tasks);
            out.println(jsonResponse);
        } catch (DaoException e) {
            out.println("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
}