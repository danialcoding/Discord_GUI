package project.Discord.client.gui.fxml.menu.home_menu.chat;

import project.Discord.client.GraphicalInterface;
import project.Discord.client.ResponseHandler;
import project.Discord.server.entity.Message;

import java.util.ArrayList;

public class MessageGetter implements Runnable {
    private GraphicalInterface graphicalInterface;

    private ChatController cc;

    private ArrayList<Message> messages;

    private ResponseHandler responseHandler;

    public MessageGetter(GraphicalInterface graphicalInterface,ChatController cc,ArrayList<Message> messages,ResponseHandler responseHandler) {
        this.graphicalInterface = graphicalInterface;
        this.cc = cc;
        this.responseHandler = responseHandler;
        if(messages != null) {
            this.messages = messages;
        }
        else {
            this.messages = new ArrayList<>();
        }

    }

    @Override
    public void run() {
        while (true) {
            /*ArrayList<Message> messages = graphicalInterface.getMessages();

            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            cc.updateMessage();

            if(messages != null) {
                if(messages.size() > this.messages.size()) {
                    System.out.println("yes............");
                    cc.setMessages(messages);

                    cc.updateMessage();

                    this.messages = messages;
                }
            }*/

            if(responseHandler.getResponse() != null && responseHandler.getResponse().getMessage() != null) {
                messages.add(responseHandler.getResponse().getMessage());

                //System.out.println(messages.get(messages.size()-1));

                cc.setMessages(messages);

                cc.updateMessage();
            }


        }
    }
}
