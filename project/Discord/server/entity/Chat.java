package project.Discord.server.entity;
import project.Discord.server.clientmanagment.ClientController;
import project.Discord.server.observerpattern.Subscriber;
import project.Discord.server.observerpattern.Publisher;
import project.Discord.networkPortocol.Response;
import java.util.ArrayList;

public class Chat implements Publisher {


    private final ArrayList<Subscriber> subscribers;

    private ArrayList<Message> messages;


    public Chat() {
        this.messages = new ArrayList<>();
        subscribers = new ArrayList<>();
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    @Override
    public void notifySubscribers(Response response,User u) {
        for (Subscriber s: subscribers) {

            ClientController cc = (ClientController)s;

            if(!cc.getUser().equals(u))
                s.update(response);
        }
    }

    public void sendMessage(Message message,User user){
        messages.add(message);

        Response response = new Response();

        response.addText(message.toString());

        notifySubscribers(response,user);

    }

    public void deleteMessage(int index){

        messages.remove(index);

    }

    @Override
    public void subscribe(Subscriber O) {
        subscribers.add(O);
    }

    @Override
    public void unSubscribe(Subscriber O) {
        subscribers.remove(O);
    }

    public ArrayList<Subscriber> getSubscribers() {
        return subscribers;
    }

    public void notifySubscriber(Member member, Response response){

        Subscriber s = getSubscriber(member);

        if(s!=null)
            s.update(response);

    }



    public Subscriber getSubscriber(Member member){

        for (Subscriber s:getSubscribers()) {

            ClientController cc = (ClientController)s;

            if(cc.getMember()==member){

                return s;

            }

        }
        return null;

    }


}
