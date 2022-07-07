package project.Discord.server;

import project.Discord.server.clientmanagment.ClientDataManagement;

public class ServerApplication {


    public static void main(String[] args) {

        ClientDataManagement.loadInformation();

        Server server = new Server(8000);

        Thread thread = new Thread(server);

        thread.start();

    }




}
