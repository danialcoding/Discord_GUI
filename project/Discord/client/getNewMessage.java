package project.Discord.client;

import project.Discord.networkPortocol.Response;

public class getNewMessage implements Runnable {
    private ResponseHandler responseHandler;

    private GraphicalInterface graphicalInterface;

    public getNewMessage(ResponseHandler responseHandler,GraphicalInterface graphicalInterface) {
        this.responseHandler = responseHandler;
        this.graphicalInterface = graphicalInterface;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Response response = responseHandler.getResponse();

            if(response.getAddress() != null) {
                System.out.println("getting message................");
                graphicalInterface.getNewMessageFromFriend(response.getAddress());
            }
        }
    }
}
