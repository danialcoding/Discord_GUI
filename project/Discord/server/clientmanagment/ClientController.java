package project.Discord.server.clientmanagment;


import project.Discord.server.entity.*;
import project.Discord.server.observerpattern.Subscriber;
import project.Discord.networkPortocol.Response;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.random.RandomGenerator;


public class ClientController implements Subscriber {

    private User user;

    private DiscordServer server;

    private Chat chat;

    private Member member;

    private final ObjectOutputStream out;

    private Socket socket;

    public ClientController(ObjectOutputStream out,Socket socket) {
        this.out = out;
        this.socket = socket;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    /**
     * @@Author danial
     * @param user
     * set user and run status checker
     */
    public void setUserCheckStatus(User user) {
        this.user = user;

        this.user.setActive(true);
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public Chat getChat() {
        return chat;
    }

    public synchronized void  sendResponse(Response response){

        try {
            out.writeObject(response);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void addObserverToChat(){
        chat.subscribe(this);
    }

    public void removeObserverFromChat(){

        chat.unSubscribe(this);

    }




    public DiscordServer getServer() {
        return server;
    }



    public void getDiscordServerOfUser(){

        Response response = new Response();

        for (DiscordServer discordServer: user.getDiscordServers()) {

            response.addText(discordServer.toString());
        }

        sendResponse(response);
    }


    public void getFriendsOfUser(){
        int index = 1;
        Response response = new Response();

        for (User user : user.getFriends()) {
            response.addText(index + ". " + user.toString());
        }

        sendResponse(response);
    }

    public void getPrivateChats() {
        Response response = new Response();

        for (PrivateChat chat : user.getChats()) {
            response.addText(chat.toString());
        }

        sendResponse(response);
    }


    /**
     * @Author danial
     * get user friend request list
     */
    public void getUserfriendRequestList() {
        Response response = new Response();

        for (FriendRequest fr : user.getFriendRequests()) {
            response.addText(fr.toString());
        }

        sendResponse(response);
    }

    /**
     * @Author danial
     * close socket for close app
     */
    public void closeApp() {
        user.setActive(false);

        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Author danial
     * log out for user
     */
    public void logOut() {
        user.setActive(false);
    }


    public void selectPrivetChat(int index){
        chat = user.getPrivateChat(index);
    }

    public void enterPrivateChat(){
        addObserverToChat();
    }


    public void showValidServerPermissionsToAdd(int index){

        Role selected = server.getRoleByIndex(index);

        HashSet<ServerPermission> validServerPermissionsToAdd = selected.getServerPermissionsThatNotExist();

        Response response = new Response();

        for (ServerPermission SP:validServerPermissionsToAdd
             ) {
            response.addText(SP.name());
        }

        sendResponse(response);

    }

    public void showValidChannelPermissionsToAdd(int index) {

        Role selected = server.getRoleByIndex(index);

        HashSet<ChannelPermission> validChannelPermissionsToAdd = selected.getChannelPermissionsThatNotExist();

        Response response = new Response();

        for (ChannelPermission SP : validChannelPermissionsToAdd
        ) {
            response.addText(SP.name());
        }

        sendResponse(response);

    }

    public void addServerPermissionToRole(String serverPermission,int indexOfSelectedRole){

        Role selected = server.getRoleByIndex(indexOfSelectedRole);

        selected.addServerPermission(ServerPermission.valueOf(serverPermission));

        server.addServerPermission(ServerPermission.valueOf(serverPermission),selected);

        Response response =  new Response();

        response.addText("serverPermission successfully added");

        sendResponse(response);

    }

    public void addChannelPermissionToRole(String channelPermission,int indexOfSelectedRole){

        Role selected = server.getRoleByIndex(indexOfSelectedRole);

        selected.addChannelPermission(ChannelPermission.valueOf(channelPermission));

        server.addChannelPermission(ChannelPermission.valueOf(channelPermission),selected);

        Response response =  new Response();

        response.addText("channelPermission successfully added");

        sendResponse(response);


    }

//
//    @Override
//    public void update(Response response) {
//        String[] arr = response.getTexts().get(0).split(":");
//
//        if (!arr[0].equals(user.getUserName())) {
//            sendResponse(response);
//        }
//    }

    @Override
    public void update(Response response) {
        sendResponse(response);
    }


    public void setServer(String index){

        server = user.getDiscordServer(Integer.parseInt(index));

    }

    public void showRoles(){

        Response response = new Response();

        ArrayList<Role> showedRoles = server.getDefinedRoles();

        for (Role r:showedRoles
             ) {
            response.addText(r.toString());
        }

        sendResponse(response);


    }

    public void removeChannelPermission(String channelPermission,int indexOfSelectedRole){

        Role selected = server.getRoleByIndex(indexOfSelectedRole);

        selected.removeChannelPermission(ChannelPermission.valueOf(channelPermission));

        server.removeChannelPermission(ChannelPermission.valueOf(channelPermission),selected);

        Response response = new Response();

        response.addText("channelPermission successfully removed");

        sendResponse(response);


    }


    public void removeServerPermission(String serverPermission,int indexOfSelectedRole){

        Role selected = server.getRoleByIndex(indexOfSelectedRole);

        selected.removeServerPermission(ServerPermission.valueOf(serverPermission));

        server.removeServerPermission(ServerPermission.valueOf(serverPermission),selected);

        Response response = new Response();

        response.addText("serverPermission successfully removed");

        sendResponse(response);

    }


    public boolean checkDuplicateRole(String roleName){

        for (Role r:server.getDefinedRoles()
             ) {

            if(r.getName().equals(roleName))
                return true;
        }

        return false;
    }

    public void changeNameOfServer(String newName){

        server.setName(newName);

        Response response = new Response();

        response.addText("serverName changed successfully");

        sendResponse(response);

    }

    public void addRole(String roleName){

        Response response = new Response();

        if(checkDuplicateRole(roleName)){


            response.addText("this name already exist !");

            response.setFlag(Flag.NotSuccessful);


        }

        else {


            Role newRole = new Role(roleName);

            server.addRole(newRole);

            response.addText("role successfully added \n" +
                    "for customize and addMemberTo it select showRoles then edit role :)");

        }

        sendResponse(response);

    }

    public void setPhotoForServer(byte[] serverPhoto){

        server.setServerPhoto(serverPhoto);

        Response response = new Response();

        response.addText("photo successfully changed");

        sendResponse(response);

    }

    public void changeNameOfRole(int indexOfSelectedRole,String newName){

        Response response = new Response();

        if(checkDuplicateRole(newName)){

            response.addText("this name already exist !");

            response.setFlag(Flag.NotSuccessful);

        }

        else {

            Role selected = server.getRoleByIndex(indexOfSelectedRole);

            selected.setName(newName);

            response.addText("name of role changed successfully");

        }

        sendResponse(response);
    }

    public void showMembersBaseOnTheirRole(int indexOfRole){

        Response response = new Response();

        Role selected = server.getRoleByIndex(indexOfRole);

        for (Member m:server.getMembersOfRole(selected)
             ) {
            response.addText(m.getUserName());
        }

        sendResponse(response);

    }

    public void removeRoleFromMember(String username,int indexOfRole){


        Member m = server.getMemberByUserName(username);

        Role r = server.getRoleByIndex(indexOfRole);

        m.removeRole(r);

        Response response = new Response();

        response.addText("member removed successfully");

        sendResponse(response);

    }

    public void addRoleToMember(String username,int indexOfRole){

        Role selected = server.getRoleByIndex(indexOfRole);

        Member m = server.getMemberByUserName(username);

         m.addRole(selected);

         Response response = new Response();

         response.addText("role successfully added to member");

         sendResponse(response);

    }

    public void showServerPermissionOfRole(int index){

        Role selected = server.getRoleByIndex(index);

        Response response = new Response();

        for (ServerPermission SP:selected.getServerPermissions()
             ) {

            response.addText(SP.name());

        }

        sendResponse(response);

    }

    public void showChannelPermissionOfRole(int index){

        Role selected = server.getRoleByIndex(index);

        Response response = new Response();

        for (ChannelPermission CP:selected.getChannelPermissions()
        ) {
            response.addText(CP.name());
        }

        sendResponse(response);

    }



    public void deleteRole(int index){

            server.removeRole(index);

            Response response = new Response();

            response.addText("selected role removed ");

            sendResponse(response);
    }

    public void removeChannel(int index){

        server.removeChannel(index);

        Response response = new Response();

        response.addText("channel Removed successfully");

        sendResponse(response);

    }

    public void showChannels(){

        ArrayList<Channel> channels = server.getChannels();

        Response response = new Response();
        for (Channel c:channels
             ) {
            response.addText(c.toString());
        }

        sendResponse(response);


    }

    public void showMembers(){

        ArrayList<Member> activeMembers = server.getActiveMembers();

        Response response = new Response();

        response.addText("Online members :");

        if(activeMembers.size()==0){

            response.addText("List is empty");
        }

        for (Member m:activeMembers) {
            response.addText(m.toString());
        }

        ArrayList<Member> inActiveMembers = server.getInActiveMembers();

        response.addText("Offline members :");

        if(inActiveMembers.size()==0){

            response.addText("List is empty");
        }

        for (Member m:inActiveMembers) {
                response.addText(m.toString());
        }

        sendResponse(response);


    }

    public void setMember(){

        member = server.getMemberByUserName(user.getUserName());

        member.setActive(true);


    }

    public void showServerMenu(){

        Response response = new Response();
        if(member.isServerPermissionExist(ServerPermission.manageServer))
            response.addText("ManageServer");

        response.addText("Channels");
        response.addText("Members");

        if(member.isServerPermissionExist(ServerPermission.inviteToServer))
            response.addText("InviteServer");

        if(member.isServerPermissionExist(ServerPermission.kickMember))
            response.addText("KickMember");

        sendResponse(response);


    }


    public void setChannel(int index){

        chat=member.getChannelByIndex(index);

    }

    public void showChannelActions(){

        Response response = new Response();

        response.addText("Join channel");

        if(member.isServerPermissionExist(ServerPermission.manageChannel))
            response.addText("ManagePermissionsOfMember");

        sendResponse(response);

    }

    public void messageToChannel(String content){

        Message message = new Message(member.getUserName(),content);

        chat.sendMessage(message,user);

    }

    public void showManageServerActions(){

        Response response = new Response();

        response.addText("EditServerSettings");

        if(member.isServerPermissionExist(ServerPermission.manageRoles)){

            response.addText("ManageRoles");

        }

        if(member.isServerPermissionExist(ServerPermission.manageChannel)){

            response.addText("ManageChannels");

        }

        sendResponse(response);

    }

    public void joinChat(){

        chat.subscribe(this);

    }

    public void leaveChat(){

        chat.unSubscribe(this);

    }

    public Member getMember() {
        return member;
    }


    public void addChannelPermissionForSpecificChannel(String userName,String permission){

        Member m = server.getMemberByUserName(userName);

        Channel c = (Channel)chat;

        c.addPermission(m,ChannelPermission.valueOf(permission));

        Response response = new Response();

        response.addText("Channel permission added successfully");

        sendResponse(response);

    }


    public void removeChannelPermissionForSpecificChannel(String username,String permission){

        Member m = server.getMemberByUserName(username);

        Channel c = (Channel) chat;

        c.removePermission(m,ChannelPermission.valueOf(permission));

        Response response = new Response();

        response.addText("Channel permission removed successfully");

        sendResponse(response);

    }

    public void showSpecificChannelPermissionToAdd(String username){

        Member m = server.getMemberByUserName(username);

        Channel c = (Channel) chat;

        HashSet<ChannelPermission> CPS  = c.getDisablePermissions().get(m);

        Response response = new Response();

        for (ChannelPermission CP:CPS
             ) {
            response.addText(CP.name());
        }

        sendResponse(response);

    }


    public void deleteMessage(int index){

        chat.deleteMessage(index);

    }


    public void showSpecificChannelPermissionToRemove(String username){

        Member m = server.getMemberByUserName(username);

        Channel c = (Channel) chat;

        Response response = new Response();

        for (ChannelPermission CP: c.getChannelPermissionsThatAMemberHas(m)
             ) {

            response.addText(CP.name());

        }

        sendResponse(response);
    }

    public void sendMessageToChat(Message msg) {
        chat.sendMessage(msg,user);
    }

    public String sendNewMessageToFriend(Message msg) {
        Random random = new Random(System.currentTimeMillis());

        int code = random.nextInt();

        String address = user.getUserName() + code;

        chat.notifyForGetNewMessage(user,address);

        return address;
    }

    public void kickMember(String userName){

        Member member = server.getMemberByUserName(userName);

        server.remove(member);

        Response response = new Response();

        response.addText("member removed");

        sendResponse(response);

    }

    public void getMessageOfChannel(){

        Channel c= (Channel)chat;

        c.loadMessages(member);

    }

    public void checkForSendMessage(){

        Channel c = (Channel)chat;

        Response response = new Response();

        if(c.checkPermission(member,ChannelPermission.sendMessage)) {

            response.setFlag(Flag.Successful);

        }

        else{

            response.setFlag(Flag.NotSuccessful);


        }

        sendResponse(response);
    }

    public void checkManageMessage(){

        Channel c = (Channel) chat ;

        Response response = new Response();

        if(c.checkPermission(member,ChannelPermission.manageMessage)){


            response.setFlag((Flag.Successful));

        }

        else{

            response.setFlag(Flag.NotSuccessful);

        }

        sendResponse(response);

    }


}
