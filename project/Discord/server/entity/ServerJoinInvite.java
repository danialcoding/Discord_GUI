package project.Discord.server.entity;

import java.io.Serializable;

public class ServerJoinInvite implements Serializable {

    private User sender;

    private User getter;

    private DiscordServer invitedServer;

    public ServerJoinInvite(User sender, User getter,DiscordServer discordServer) {
        this.sender = sender;
        this.getter = getter;
        this.invitedServer = discordServer;
    }

    public User getSender() {
        return sender;
    }

    public User getGetter() {
        return getter;
    }

    public String toString() {

        return "You have Invited  to '"+invitedServer.toString()+"'  from : "+sender.getUserName();
    }


    public void setInvitedServer(DiscordServer invitedServer) {
        this.invitedServer = invitedServer;
    }

    public DiscordServer getInvitedServer() {
        return invitedServer;
    }

}
