package project.Discord.server.entity;

import java.io.Serializable;

public class FriendRequest implements Serializable {
    private User sender;

    private User getter;

    public FriendRequest(User sender, User getter) {
        this.sender = sender;
        this.getter = getter;
    }

    public User getSender() {
        return sender;
    }

    public User getGetter() {
        return getter;
    }


    /**
     * @Author danial
     * @return String
     * return string to show friend request
     */
    public String toString() {
        return "Request from " + sender.getUserName();
    }
}