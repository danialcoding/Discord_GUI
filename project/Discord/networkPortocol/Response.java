package project.Discord.networkPortocol;

import project.Discord.server.Server;
import project.Discord.server.clientmanagment.Flag;
import project.Discord.server.entity.DiscordServer;
import project.Discord.server.entity.FriendRequest;
import project.Discord.server.entity.PrivateChat;
import project.Discord.server.entity.User;

import java.io.Serializable;
import java.util.ArrayList;

public class Response implements Serializable {

    private ArrayList<String> texts;

    private Flag flag ;

    private byte[] file;

    private ArrayList<PrivateChat> privateChats;

    private ArrayList<User> users;

    private ArrayList<DiscordServer> servers;

    private User user;

    private ArrayList<FriendRequest> friendRequests;

    private FriendRequest friendRequest;

    public Response() {
        texts = new ArrayList<>();
    }

    public Flag getFlag() {
        return flag;
    }

    public ArrayList<String> getTexts( ) {
        return texts;
    }

    public byte[] getFile() {
        return file;
    }

    public ArrayList<PrivateChat> getPrivateChats() {
        return privateChats;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public ArrayList<DiscordServer> getServers() {
        return servers;
    }

    public User getUser() {
        return user;
    }

    public ArrayList<FriendRequest> getFriendRequests() {
        return friendRequests;
    }

    public FriendRequest getFriendRequest() {
        return friendRequest;
    }

    public void setFriendRequest(FriendRequest friendRequest) {
        this.friendRequest = friendRequest;
    }

    public void setFriendRequests(ArrayList<FriendRequest> friendRequests) {
        this.friendRequests = friendRequests;
    }

    public void setPrivateChats(ArrayList<PrivateChat> privateChats) {
        this.privateChats = privateChats;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public void setServers(ArrayList<DiscordServer> servers) {
        this.servers = servers;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public void setFlag(Flag flag) {
        this.flag = flag;
    }

    public void setTexts(ArrayList<String> texts) {
        this.texts = texts;
    }



    public void addText(String text){
        texts.add(text);
    }


}
