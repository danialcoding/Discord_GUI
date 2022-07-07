package project.Discord.client;

import project.Discord.client.gui.RunGUI;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client implements Runnable{

    private final int port;

    private String serverIP;

    private static Socket socket;

    private ObjectInputStream in;

    private ObjectOutputStream out;

    private static ExecutorService executorService;

    private ResponseHandler responseHandler;

    private ConsoleInterface consoleInterface;

    private GraphicalInterface graphicalInterface;

    public Client(int port, String serverIP) {

        this.port = port;

        this.serverIP = serverIP;

        try {
            socket = new Socket(serverIP,port);

            out = new ObjectOutputStream(socket.getOutputStream());

            in = new ObjectInputStream(socket.getInputStream());

            responseHandler = new ResponseHandler(in);

            //consoleInterface = new ConsoleInterface(out, responseHandler);

            graphicalInterface = new GraphicalInterface(out,responseHandler);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public GraphicalInterface getGraphicalInterface() {
        return graphicalInterface;
    }

    public static void exit()  {

        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    @Override
    public void run() {
        Thread thread = new Thread(responseHandler);

        thread.start();

        //consoleInterface.loginMenu();
    }
}
