package project.Discord.server.clientmanagment;
import project.Discord.networkPortocol.Request;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RequestReceiver implements Runnable {

    private  RequestController requestController;

    private  ObjectInputStream in;

    public RequestReceiver(Socket socket) {

        try {
            in= new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            requestController = new RequestController(out,socket);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void run() {

        while (true) {

            try {
                Request request = (Request) in.readObject();

                requestController.setRequest(request);

                ExecutorService executorService = Executors.newCachedThreadPool();

                executorService.execute(requestController);

            }catch (EOFException  | SocketException e){

                break;
            }
            catch (IOException | ClassNotFoundException e) {

                e.printStackTrace();

            }

        }

    }


}


