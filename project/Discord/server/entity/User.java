package project.Discord.server.entity;

import java.io.Serializable;
import java.util.ArrayList;


public class User implements Serializable {

    private String userName;

    private String passWord;

    private String email;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    private String phoneNumber;

    private String description;

    private boolean isActive;

    private Status status;

    private ArrayList<DiscordServer> discordServers;

    private ArrayList<User> friends;

    private ArrayList<FriendRequest> friendRequests;

    private ArrayList<User> blockUsers;

    private ArrayList<PrivateChat> chats;

    private ArrayList<Notification> notifications;

    private transient byte[] userPhoto;

    private Boolean havePhoto;

    private ArrayList<ServerJoinInvite> serverJoinInvites;


    public PrivateChat getPrivateChat(int index){
        return chats.get(index-1);
    }

    public void acceptServerInvite(int index){

        ServerJoinInvite SI  = serverJoinInvites.get(index-1);

        DiscordServer DS = SI.getInvitedServer();

        addDiscordServer(DS);

        DS.addMemberToServer(this);


    }

    public void addDiscordServer(DiscordServer Ds){

        discordServers.add(Ds);

    }

    public User(String userName, String passWord, String email, String phoneNumber) {
        this.userName = userName;
        this.passWord = passWord;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.friendRequests = new ArrayList<>();
        this.status = Status.NULL;
        this.description = "";
        this.notifications = new ArrayList<>();
        this.havePhoto = false;
        this.discordServers = new ArrayList<>();
        this.friends = new ArrayList<>();
        this.blockUsers = new ArrayList<>();
        this.chats = new ArrayList<>();
        this.serverJoinInvites = new ArrayList<>();
    }

    @Override
    public String toString(){

        return userName + "  " + status;

    }

    public ArrayList<User> getFriends() {
        return friends;
    }

    public ArrayList<DiscordServer> getDiscordServers( ) {
        return discordServers;
    }

    public String getUserName( ) {
        return userName;
    }

    public String getPassWord( ) {
        return passWord;
    }

    public String getEmail() {
        return email;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<PrivateChat> getChats() {
        return chats;
    }

    public ArrayList<FriendRequest> getFriendRequests() {
        return friendRequests;
    }

    public ArrayList<User> getBlockUsers() {
        return blockUsers;
    }

    public byte[] getUserPhoto() {
        return userPhoto;
    }

    public Boolean getActive() {
        return isActive;
    }

    public ArrayList<Notification> getNotifications() {
        return notifications;
    }

    public Boolean getHavePhoto() {
        return havePhoto;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setChats(ArrayList<PrivateChat> chats) {
        for (PrivateChat PC:chats) {

            PC.setMessages(PC.getSavedMessages());

        }

        this.chats = chats;
    }

    public void removeFromServer(DiscordServer server){

        discordServers.remove(server);

    }

    public void setHavePhoto(Boolean havePhoto) {
        this.havePhoto = havePhoto;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public void setUserPhoto(byte[] userPhoto) {
        this.userPhoto = userPhoto;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFriends(ArrayList<User> friends) {
        this.friends = friends;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public DiscordServer getDiscordServer(int index) {

        return discordServers.get(index-1);

    }

    public void addServerJoinInvite(ServerJoinInvite serverJoinInvite){

        serverJoinInvites.add(serverJoinInvite);

    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isActive() {
        return isActive;
    }

    public Status getStatus() {
        return status;
    }

    public ArrayList<ServerJoinInvite> getServerJoinInvites() {
        return serverJoinInvites;
    }

    public DiscordServer getServerJoinInviteByIndex(int index){

        return serverJoinInvites.get(index-1).getInvitedServer();

    }

    public void removeServerInviteByIndex(int index){

        serverJoinInvites.remove(index-1);

    }
}




