package project.Discord.server.clientmanagment;

import project.Discord.server.entity.Message;
import project.Discord.server.entity.User;

import java.util.ArrayList;

public class test implements Runnable {

    private ClientController user;

    public test(ClientController user) {
        this.user = user;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("\n\n\nuser:  " + user.getUser().getUserName());
            System.out.println(user.getChat().getMessages());
        }
    }
}
