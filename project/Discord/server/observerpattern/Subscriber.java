package project.Discord.server.observerpattern;

import project.Discord.networkPortocol.Response;

public interface Subscriber {

    public void update(Response response);
}
