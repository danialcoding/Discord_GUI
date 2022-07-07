package project.Discord.server;

import project.Discord.server.clientmanagment.RequestReceiver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable  {

    private int port;

    private ServerSocket serverSocket;


    public Server(int port) {

        this.port = port;

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {

        ExecutorService executorService = Executors.newCachedThreadPool();

        while (true){

            try {
                Socket socket = serverSocket.accept();
                RequestReceiver requestReceiver = new RequestReceiver(socket);
                System.out.println("Connection accepted");
                executorService.execute(requestReceiver);

            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }
}
