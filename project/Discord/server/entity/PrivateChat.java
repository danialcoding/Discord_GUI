package project.Discord.server.entity;


import java.io.Serializable;
import java.util.ArrayList;

public class PrivateChat extends Chat implements Serializable {
    private User userChatting;

    private boolean isBlocked;

    private boolean isFriend;

    private User user1;

    private User user2;

    private ArrayList<Message> savedMessages;

    public PrivateChat() {
        super();
        savedMessages = new ArrayList<>();
    }

    public ArrayList<Message> getSavedMessages() {
        return savedMessages;
    }

    public void saveMessages(){
        savedMessages = getMessages();
    }

    public User getUser1() {
        return user1;
    }

    public User getUser2() {
        return user2;
    }

    public boolean getIsBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }

    public void setUserChatting(User userChatting) {
        this.userChatting = userChatting;
    }

}
