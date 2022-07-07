package project.Discord.server.observerpattern;

import project.Discord.server.entity.Member;
import project.Discord.networkPortocol.Response;
import project.Discord.server.entity.User;

public interface Publisher {

    public void notifySubscribers(Response response, User user);

    public void subscribe(Subscriber s);

    public void unSubscribe(Subscriber s);

    public Subscriber getSubscriber(Member member);

    public void notifySubscriber(Member member,Response response);
}
