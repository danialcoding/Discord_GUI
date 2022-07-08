package project.Discord.server.clientmanagment;

import project.Discord.server.entity.*;
import project.Discord.networkPortocol.Response;
import project.Discord.server.observerpattern.Subscriber;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ClientServices {

    private final ClientDataManagement clientDataManagement;

    private final ClientController controller;

    public ClientServices(ClientDataManagement clientDataManagement, ClientController controller) {
        this.clientDataManagement = clientDataManagement;
        this.controller = controller;
    }

    /**
     * @Author danial
     * get user Object
     */
    public void getUserObject() {
        Response response = new Response();

        response.setUser(controller.getUser());

        controller.sendResponse(response);
    }

    /**
     * @Author danial
     * get Friends Object
     */
    public void getUserFriendsObject() {
        Response response = new Response();

        response.setUsers(controller.getUser().getFriends());

        controller.sendResponse(response);
    }

    /**
     * @Author danial
     * get user servers Object
     */
    public void getUserServersObject() {
        Response response = new Response();

        response.setServers(controller.getUser().getDiscordServers());

        controller.sendResponse(response);
    }

    /**
     * @Author danial
     * get user servers Object
     */
    public void getUserPrivateChatObject() {
        Response response = new Response();

        response.setPrivateChats(controller.getUser().getChats());

        controller.sendResponse(response);
    }

    /**
     * @Author danial
     * pin message in channel
     */
    public void showPinMessages() {
        Response response = new Response();

        ArrayList<Message> pinMessages = ((Channel)controller.getChat()).getPinMessages();

        for (Message pinM : pinMessages) {
            response.addText(pinM.toStringInChannel());
        }

        controller.sendResponse(response);
    }

    /**
     * @Author danial
     * @param indexOfMessage
     * pin message in channel
     */
    public void pinMessage(int indexOfMessage) {
        Message message = controller.getChat().getMessages().get(indexOfMessage);

        ((Channel) controller.getChat()).getPinMessages().add(message);

        Response response = new Response();

        response.addText("Message successfully pined.");

        controller.sendResponse(response);
    }


    /**
     * @Author danial
     * @param indexOfFriend
     * get friend Photo
     */
    public void getFriendPhoto(int indexOfFriend) {
        User friend = controller.getUser().getFriends().get(indexOfFriend);

        Response response = new Response();

        if(friend.getHavePhoto()) {
            response.setFile(friend.getUserPhoto());

            response.setFlag(Flag.Successful);
        }
        else {
            response.setFlag(Flag.NotSuccessful);
        }

        controller.sendResponse(response);
    }

    /**
     * @Author danial
     * @param indexOfServer
     * get Server Photo
     */
    public void getServerPhoto(int indexOfServer) {
        DiscordServer server = controller.getUser().getDiscordServers().get(indexOfServer);

        Response response = new Response();

        response.setFile(server.getServerPhoto());

        controller.sendResponse(response);
    }

    /**
     * @Author danial
     * send file message to channel
     */
    public void sendFileMessageToChannel(String msgText,String urlString) {

        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Response response = new Response();

        Chat chat = controller.getChat();

        Channel channel = (Channel) chat;

        for(Subscriber sb : channel.getSubscribers()) {
            ClientController newcc = (ClientController) sb;

            sendNotification("New file message","You have new file message in " +
                            channel.getName() + " channel.",newcc.getUser().getUserName());
        }

        response.setFlag(Flag.Successful);

        controller.sendResponse(response);

        FileMessage msg = new FileMessage(controller.getUser().getUserName(),msgText,url);

        msg.setDownloadCode(channel.getMessages().size());

        controller.sendMessageToChat(msg);
    }

    /**
     * @Author danial
     * close socket for close app
     */
    public void closeApp() {
        controller.closeApp();
    }

    /**
     * @Author danial
     * log for user
     */
    public void logOut() {
        controller.logOut();
    }

    /**
     * @Author danial
     * show friends with status
     */
    public void showFriendListWithStatus() {
        Response response = new Response();

        ArrayList<User> friends = controller.getUser().getFriends();

        for (User user : friends) {
            if(user.getStatus() == Status.NULL) {
                if (user.getActive()) {
                    response.addText(user.getUserName() + " (Status: ONLINE)");
                }
                else {
                    response.addText(user.getUserName() + " (Status: OFFLINE)");
                }
            }
            else {
                if(!user.getActive()) {
                    response.addText(user.getUserName() + " (Status: OFFLINE)");
                }
                else {
                    response.addText(user.getUserName() + " (Status: " + user.getStatus() + ")");
                }
            }
        }

        controller.sendResponse(response);
    }

    /**
     * @Author danial
     * get friends username
     */
    public void getFriendsUserName() {
        Response response = new Response();

        ArrayList<User> friends = controller.getUser().getFriends();

        for (User user : friends) {
            response.addText(user.getUserName());
        }

        controller.sendResponse(response);
    }

    /**
     * @Author danial
     * get friends status
     */
    public void getFriendsStatus() {
        Response response = new Response();

        ArrayList<User> friends = controller.getUser().getFriends();

        for (User user : friends) {
            if(user.getStatus() == Status.NULL) {
                if (user.getActive()) {
                    response.addText("ONLINE");
                }
                else {
                    response.addText("OFFLINE");
                }
            }
            else {
                if(!user.getActive()) {
                    response.addText("OFFLINE");
                }
                else {
                    response.addText("" + user.getStatus());
                }
            }
        }

        controller.sendResponse(response);
    }

    /**
     * @Author danial
     * show user notifications
     */
    public void showNotifications() {
        Response response = new Response();

        ArrayList<Notification> notifications = controller.getUser().getNotifications();

        if(notifications.size() != 0) {
            for (Notification notif : notifications) {
                response.addText(notif.toString());
            }
        }
        else {
            response.addText("List is empty.");
        }

        controller.sendResponse(response);
    }

    /**
     * @Author danial
     * show user notifications
     */
    public void sendNotification(String topic,String text,String targetUserUserName) {
        User targetUser = searchUser(targetUserUserName);

        Notification notification = new Notification(topic,text);

        targetUser.getNotifications().add(notification);
    }

    /**
     * @Author danial
     * @param  oldPass
     * check user Old Password
     */
    public void checkOldPassword(String oldPass) {
        Response response = new Response();

        String userPass = controller.getUser().getPassWord();

        if(userPass.equals(oldPass)) {
            response.setFlag(Flag.Successful);

            response.addText("Old password is correct.");
        }
        else {
            response.setFlag(Flag.NotSuccessful);

            response.addText("Old password isn't correct.");
        }

        controller.sendResponse(response);
    }

    /**
     * @Author danial
     * @param  indexOfFriend
     * unblock Friend with index
     */
    public void unblockFriend(int indexOfFriend) {
        Response response = new Response();

        User user = controller.getUser();

        User friend = controller.getUser().getFriends().get(indexOfFriend);

        ArrayList<PrivateChat> chats = controller.getUser().getChats();

        for (PrivateChat chat : chats) {
            if(chat.getUser1().getUserName().equals(friend.getUserName())  || chat.getUser2().getUserName().equals(friend.getUserName())) {
                chat.setBlocked(false);
            }
        }

        user.getBlockUsers().remove(friend);

        response.addText("User successfully unblocked.");

        response.setFlag(Flag.NotSuccessful);

        controller.sendResponse(response);
    }

    /**
     * @Author danial
     * @param  indexOfFriend
     * @return Boolean
     * check friend With User Block List
     */
    public void checkFriendwithuserblocklist(int indexOfFriend) {
        Response response = new Response();

        ArrayList<User> blockUsers = controller.getUser().getBlockUsers();

        User friend = searchUser(controller.getUser().getFriends().get(indexOfFriend).getUserName());

        for (User user : blockUsers) {
            if(user.getUserName().equals(friend.getUserName()) ) {
                response.setFlag(Flag.Successful);

                controller.sendResponse(response);
                return;
            }
        }

        response.setFlag(Flag.NotSuccessful);

        controller.sendResponse(response);

    }

    /**
     * @Author danial
     * @param  serverName
     * create discord server and add to user server list
     */
    public void createNewServer(String serverName,byte[] serverPhoto) {
        Response response = new Response();

        DiscordServer newServer = new DiscordServer(serverName,serverPhoto);

        newServer.addOwner(controller.getUser());

        controller.getUser().getDiscordServers().add(newServer);

        response.addText("Server successfully created.");

        controller.sendResponse(response);
    }

    /**
     * @Author danial
     * @param  photo
     * set Profile Photo for User
     */
    public void setUserProfilePhoto(byte[] photo) {
        controller.getUser().setUserPhoto(photo);

        controller.getUser().setHavePhoto(true);

        Response response = new Response();

        response.addText("Profile photo successfully added.");

        controller.sendResponse(response);
    }

    public void setPhotoForServer(byte[] photo){

        controller.setPhotoForServer(photo);
    }

    /**
     * @Author danial
     * @param  index
     * block chat with a friend
     */
    public void blockUser(int index) {
        Response response = new Response();

        User user = controller.getUser();

        User friend = user.getFriends().get(index);

        ArrayList<PrivateChat> chats = user.getChats();

        for (PrivateChat chat : chats) {
            if(chat.getUser1().getUserName().equals(friend.getUserName()) || chat.getUser2().getUserName().equals(friend.getUserName())) {
                chat.setBlocked(true);
            }
        }

        user.getBlockUsers().add(friend);

        response.addText("User successfully blocked.");

        controller.sendResponse(response);
    }

    /**
     * @Author danial
     * @param  userName
     * send Friend Request
     */
    public void sendFriendRequest(String userName) {
        User friend = searchUser(userName);

        Response response = new Response();

        if(friend == null) {
            response.addText("User not found!");

            response.setFlag(Flag.NotSuccessful);

            controller.sendResponse(response);
            return;
        }

        if(friend.getUserName().equals(controller.getUser().getUserName())) {
            response.addText("You can't send friend request for own!");

            response.setFlag(Flag.NotSuccessful);

            controller.sendResponse(response);
            return;
        }

        if(checkFriendExistInUserFriends(friend)) {
            response.addText("You are already friends with that user!");

            response.setFlag(Flag.NotSuccessful);

            controller.sendResponse(response);
            return;
        }

        if(checkFriendExistInUserFriendRequestsgui(friend)) {
            response.addText("You have already sent a friend request to this user!");

            response.setFlag(Flag.NotSuccessful);

            controller.sendResponse(response);
            return;
        }

        sendNotification("New friend request","New friend request from " +
                controller.getUser().getUserName() + " added to your friend request list.",friend.getUserName());

        FriendRequest friendRequest = new FriendRequest(controller.getUser(),friend);

        friend.getFriendRequests().add(friendRequest);

        controller.getUser().getFriendRequests().add(friendRequest);

        response.addText("Friend request successfully send.");

        response.setFlag(Flag.Successful);

        controller.sendResponse(response);
    }

    /**
     * @Author danial
     * @param  friend
     * @return Boolean
     * check Friend Exist In User Friend Requests
     */
    public Boolean checkFriendExistInUserFriendRequests(User friend) {
        ArrayList<FriendRequest> friendRequests = friend.getFriendRequests();

        for (FriendRequest fr : friendRequests) {
            if(fr.getGetter().getUserName().equals(friend.getUserName())) {
                return true;
            }
        }

        return false;
    }

    /**
     * @Author danial
     * @param  friend
     * @return Boolean
     * check Friend Exist In User Friend Requests
     */
    public Boolean checkFriendExistInUserFriendRequestsgui(User friend) {
        ArrayList<FriendRequest> friendRequests = controller.getUser().getFriendRequests();

        int index = 0;
        for (FriendRequest fr : friendRequests) {
            if(fr.getGetter().getUserName().equals(friend.getUserName())) {
                return true;
            }
            ++index;
        }

        return false;
    }

    /**
     * @Author danial
     * @param  status
     * change user status
     */
    public void changeUserStatus(String status) {
        Response response = new Response();

        switch (status) {
            case "ONLINE" -> controller.getUser().setStatus(Status.ONLINE);
            case "IDLE" -> controller.getUser().setStatus(Status.IDLE);
            case "DO_NOT_DISTURB" -> controller.getUser().setStatus(Status.DO_NOT_DISTURB);
            case "INVISIBLE" -> controller.getUser().setStatus(Status.INVISIBLE);
        }

        response.addText("Your status successfully changed.");

        controller.sendResponse(response);
    }

    /**
     * @Author danial
     * send friend request in response
     */
    public void showFriendRequests() {
        Response response = new Response();

        ArrayList<FriendRequest> friendRequests = controller.getUser().getFriendRequests();

        if(friendRequests.size() != 0) {
            for (FriendRequest fr : friendRequests) {
                response.addText(fr.toString());
            }

            controller.sendResponse(response);
        }
        else {
            response.addText("");

            controller.sendResponse(response);
        }

    }

    public void searchAndRemovefriendRequest(User Friend,User user) {
        ArrayList<FriendRequest> friendRequests =  Friend.getFriendRequests();

        int index = 0;

        for (FriendRequest fr : friendRequests) {
            if(fr.getSender().getUserName().equals(user.getUserName()) || fr.getGetter().getUserName().equals(user.getUserName())) {
                Friend.getFriendRequests().remove(index);
                return;
            }
            ++index;
        }
    }

    /**
     * @Author danial
     * @param  index
     * @param  action
     * get index and action of user for a friend request and ipdate friend request list and if
     * user accept add friend request sender to user friend
     */
    public void updateFriendRequestsList(int index,String action) {
        Response response = new Response();

        User user = controller.getUser();

        FriendRequest friendRequest = user.getFriendRequests().get(index);

        User friend;

        if (friendRequest.getSender().getUserName().equals(user.getUserName())) {
            friend = friendRequest.getGetter();
        }
        else {
            friend = friendRequest.getSender();
        }

        switch (action) {
            case "accept" -> {
                sendNotification("Friend request accepted","Friend request accepted from " +
                        controller.getUser().getUserName() + ".",friend.getUserName());

                controller.getUser().getFriendRequests().remove(index);

                searchAndRemovefriendRequest(friend,user);

                controller.getUser().getFriends().add(friend);

                friend.getFriends().add(controller.getUser());

                response.addText("User successfully added to your friends.");
            }

            case "reject" -> {
                sendNotification("Friend request rejected","Friend request rejected from " +
                        controller.getUser().getUserName() + ".",friend.getUserName());

                controller.getUser().getFriendRequests().remove(index);

                searchAndRemovefriendRequest(friend,user);

                response.addText("User successfully removed from your friend requests.");
            }
        }
        controller.sendResponse(response);
    }

    /**
     * @Author danial
     * get all friend request object
     */
    public void getFriendRequestObject() {
        ArrayList<FriendRequest> friendRequests = controller.getUser().getFriendRequests();

        Response response = new Response();

        response.setFriendRequests(friendRequests);

        if(friendRequests.size() != 0) {
            response.setFriendRequest(friendRequests.get(friendRequests.size() - 1));
        }

        controller.sendResponse(response);
    }


    public void showChannelPermissionsOfRole(int index){

        controller.showChannelPermissionOfRole(index);

    }

    public void showServerPermissionsOfRole(int index){

        controller.showServerPermissionOfRole(index);

    }

    public void showValidServerPermissionsToAdd(int index){

        controller.showValidServerPermissionsToAdd(index);

    }


    public void showValidChannelPermissionsToAdd(int index){

        controller.showValidChannelPermissionsToAdd(index);

    }

    public void addServerPermission(String serverPermission,int index){

        controller.addServerPermissionToRole(serverPermission,index);

    }

    /**
     * @Author danial
     * @param  index
     * get index of friend and send profile response
     */
    public void showProfileFriendByIndex(int index) {
        User user = controller.getUser();

        User friend = user.getFriends().get(index);

        Response response = new Response();

        if (checkUserBlockedByFriend(friend)) {
            response.addText("This user blocked you.");

            response.setFlag(Flag.NotSuccessful);

            controller.sendResponse(response);

            return;
        }

        response.setFlag(Flag.Successful);

        response.addText("Username: " + friend.getUserName());

        response.addText("Discription: " + friend.getDescription());

        response.setFile(friend.getUserPhoto());

        controller.sendResponse(response);
    }

    /**
     * @Author danial
     * @param  friend
     * @return boolean
     * check user block list with friend and return boolean
     */
    public Boolean checkUserBlockedByFriend(User friend) {
        User user = controller.getUser();

        ArrayList<User> blockUsers = friend.getBlockUsers();

        for (User blockUser : blockUsers) {
            if (user.getUserName().equals(blockUser.getUserName())) {
                return true;
            }
        }

        return false;
    }

    /**
     * @Author danial
     * @param  userName
     * @return User
     * get username and return user
     */
    public User searchUser(String userName) {
        ArrayList<User> users = clientDataManagement.getUsers();

        for (User user : users) {
            if (user.getUserName().equals(userName)) {
                return user;
            }
        }

        return null;
    }

    /**
     * @Author danial
     * @param  downloadCode
     * get download code and send url for do
     */
    public void downloadFileFromChannel(int downloadCode) {
        User user = controller.getUser();

        Channel channel = (Channel) controller.getChat();

        Response response = new Response();

        if(downloadCode < channel.getMessages().size() && channel.getMessages().get(downloadCode) instanceof  FileMessage) {
            FileMessage fm = (FileMessage) channel.getMessages().get(downloadCode);

            URL url = fm.getUrl();

            response.addText(String.valueOf(url));

            response.setFlag(Flag.Successful);

            controller.sendResponse(response);
        }
        else {
            response.addText("Download code isn't correct.");

            response.setFlag(Flag.NotSuccessful);

            controller.sendResponse(response);
        }
    }

    /**
     * @Author danial
     * @param  downloadCode
     * get download code and send url for do
     */
    public void downloadFile(int downloadCode) {
        User user = controller.getUser();

        PrivateChat pChat = (PrivateChat) controller.getChat();

        Response response = new Response();

        if(downloadCode < pChat.getMessages().size() && pChat.getMessages().get(downloadCode) instanceof  FileMessage) {
            FileMessage fm = (FileMessage) pChat.getMessages().get(downloadCode);

            URL url = fm.getUrl();

            response.addText(String.valueOf(url));

            response.setFlag(Flag.Successful);

            controller.sendResponse(response);
        }
        else {
            response.addText("Download code isn't correct.");

            response.setFlag(Flag.NotSuccessful);

            controller.sendResponse(response);
        }
    }

    /**
     * @Author danial
     * @param  friend
     * @return Boolean
     * get user and check Friend Exist In User Friends
     */
    public Boolean checkFriendExistInUserFriends(User friend) {
        ArrayList<User> friends = controller.getUser().getFriends();

        for (User u : friends) {
            if(u.getUserName().equals(friend.getUserName())) {
                return true;
            }
        }

        return false;
    }

    /**
     * @Author danial
     * @param userName
     * open a private message with other users
     */
    public void openPrivateMessageWithOtherUser(String userName) {
        Response response = new Response();

        User user = controller.getUser();

        User friend = searchUser(userName);

        if(friend == null) {
            response.addText("User not found!");

            response.setFlag(Flag.NotSuccessful);

            controller.sendResponse(response);
            return;
        }

        if(friend.getUserName().equals(user.getUserName())) {
            response.addText("You can't open private chat with own!");

            response.setFlag(Flag.NotSuccessful);

            controller.sendResponse(response);
            return;
        }

        ArrayList<PrivateChat> pChats =  user.getChats();

        Boolean checkExist = false;

        PrivateChat newChat = new PrivateChat();

        for (PrivateChat privateChat : pChats) {
            if(privateChat.getUser1().getUserName().equals(friend.getUserName()) || privateChat.getUser2().getUserName().equals(friend.getUserName())) {
                newChat = privateChat;

                checkExist = true;

                break;
            }
        }

        if(checkExist) {
            response.addText("You have already a private chat with this user.");

            response.setFlag(Flag.NotSuccessful);

            controller.sendResponse(response);

            return;
        }
        else {
            sendNotification("New private chat","New private chat from " +
                    user.getUserName() + " added to your private chat list.",friend.getUserName());

            PrivateChat newPrivateChat = new PrivateChat();

            newPrivateChat.setUser1(user);

            newPrivateChat.setUser2(friend);

            friend.getChats().add(newPrivateChat);

            user.getChats().add(newPrivateChat);

            controller.setChat(newPrivateChat);

            controller.enterPrivateChat();

            response.setFlag(Flag.Successful);

            controller.sendResponse(response);
        }
    }

    /**
     * @Author danial
     * @param indexOfFriend
     * check exist private chat and create or join to chat
     */
    public void checkExistPrivateChat(int indexOfFriend) {
        Response response = new Response();

        User user = controller.getUser();

        User friend = user.getFriends().get(indexOfFriend);

        ArrayList<PrivateChat> pChats =  user.getChats();

        Boolean checkExist = false;

        PrivateChat selectedChat = new PrivateChat();

        for (PrivateChat privateChat : pChats) {
            if(privateChat.getUser1().getUserName().equals(friend.getUserName())  || privateChat.getUser2().getUserName().equals(friend.getUserName())) {
                selectedChat = privateChat;
                checkExist = true;
                break;
            }
        }

        if(checkExist) {
            int indexOfArray = 0;
            for (Message msg : selectedChat.getMessages()) {
                if(msg instanceof FileMessage) {
                    FileMessage fm = (FileMessage) msg;

                    fm.setDownloadCode(indexOfArray);

                    response.addText(fm.toString());
                }
                else {
                    response.addText(msg.toString());
                }
                ++indexOfArray;
            }

            controller.setChat(selectedChat);

            controller.enterPrivateChat();

            response.setFlag(Flag.Successful);

            controller.sendResponse(response);
        }
        else {
            PrivateChat newPrivateChat = new PrivateChat();

            newPrivateChat.setUser1(user);

            newPrivateChat.setUser2(friend);

            friend.getChats().add(newPrivateChat);

            user.getChats().add(newPrivateChat);

            controller.setChat(newPrivateChat);

            controller.enterPrivateChat();

            response.setFlag(Flag.Successful);

            controller.sendResponse(response);
        }
    }

    /**
     * @Author danial
     * add user to private chat subscriber and send chat messages for user
     */
    public void joinToPrivateChat(int index) {
        Response response = new Response();

        User user = controller.getUser();

        PrivateChat chat = user.getChats().get(index);

        PrivateChat newchat = new PrivateChat();

        controller.setChat(chat);

        controller.enterPrivateChat();

        int indexOfArray = 0;
        for (Message msg : chat.getMessages()) {
            if(msg instanceof FileMessage) {
                FileMessage fm = (FileMessage) msg;

                fm.setDownloadCode(indexOfArray);

                response.addText(fm.toString());
            }
            else {
                response.addText(msg.toString());
            }
            ++indexOfArray;
        }

        response.setFlag(Flag.Successful);

        controller.sendResponse(response);
    }

    public void kickMember(String username){

        controller.kickMember(username);

    }

    /**
     * @Author danial
     * @param msgText
     * get msg text and send to chat
     */
    public void sendMessageToPrivateChat(String msgText) {
        Response response = new Response();

        Chat chat = controller.getChat();

        PrivateChat pChat = new PrivateChat();

        pChat = (PrivateChat) chat;

        if(pChat.getIsBlocked()) {
            response.setFlag(Flag.NotSuccessful);

            response.addText("You can't send message in this private chat.");

            controller.sendResponse(response);

            return;
        }

        User friend = null;

        if(pChat.getUser1().getUserName().equals(controller.getUser().getUserName())) {
            friend = pChat.getUser2();
        }
        else if(pChat.getUser2().getUserName().equals(controller.getUser().getUserName())) {
            friend = pChat.getUser1();
        }

        sendNotification("New private message","You have new private message from " +
                controller.getUser().getUserName() + ".",friend.getUserName());

        response.setFlag(Flag.Successful);

        controller.sendResponse(response);

        Message msg = new Message(controller.getUser().getUserName(),msgText);

        controller.sendMessageToChat(msg);
    }

    /**
     * @Author danial
     * @param msgText
     * get msg text and send to chat
     */
    public void sendFileMessageToPrivateChat(String msgText,String urlString) {

        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Response response = new Response();

        Chat chat = controller.getChat();

        PrivateChat pChat = new PrivateChat();

        pChat = (PrivateChat) chat;

        if(pChat.getIsBlocked()) {
            response.setFlag(Flag.NotSuccessful);

            response.addText("You can't send message in this private chat.");

            controller.sendResponse(response);

            return;
        }

        User friend = null;

        if(pChat.getUser1().getUserName().equals(controller.getUser().getUserName())) {
            friend = pChat.getUser2();
        }
        else if(pChat.getUser2().getUserName().equals(controller.getUser().getUserName())) {
            friend = pChat.getUser1();
        }

        sendNotification("New private file message","You have new private file message from " +
                controller.getUser().getUserName() + ".",friend.getUserName());

        response.setFlag(Flag.Successful);

        controller.sendResponse(response);

        FileMessage msg = new FileMessage(controller.getUser().getUserName(),msgText,url);

        msg.setDownloadCode(pChat.getMessages().size());

        controller.sendMessageToChat(msg);
    }

    /**
     * @Author danial
     * exit user from chat subscribers
     */
    public void exitFromChat() {

        PrivateChat pChat = (PrivateChat) controller.getChat();

        User user1 = pChat.getUser1();

        User user2 = pChat.getUser2();

        int indexOfPrivateChat1 = serachPrivateChat(pChat,user1);

        int indexOfPrivateChat2 = serachPrivateChat(pChat,user2);

        user1.getChats().set(indexOfPrivateChat1,pChat);

        user2.getChats().set(indexOfPrivateChat2,pChat);

        controller.removeObserverFromChat();

        controller.setChat(null);
    }

    /**
     * @Author danial
     * @param pChat,user
     * search private chat and get index
     */
    public int serachPrivateChat(PrivateChat pChat,User user) {
        ArrayList<PrivateChat> privateChats = user.getChats();

        int indexOfPrivateChat = 0;

        for (PrivateChat privateChat : privateChats) {
            if(privateChat == pChat) {
                return indexOfPrivateChat;
            }
            indexOfPrivateChat++;
        }

        return -1;
    }


    /**
     * @Author danial
     * show private chats for a user
     */
    public void getUserPrivateChats() {
        Response response = new Response();

        User user = controller.getUser();

        ArrayList<PrivateChat> chats = user.getChats();

        for (PrivateChat chat : chats) {
            User user1 = chat.getUser1();
            User user2 = chat.getUser2();

            if(user1.getUserName().equals(user.getUserName())) {
                response.addText(user2.getUserName());
            }
            else if(user2.getUserName().equals(user.getUserName())) {
                response.addText(user1.getUserName());
            }
        }

        controller.sendResponse(response);
    }


    /**
     * @Author danial
     * @param  index
     * get index of friend and remove it
     */
    public void removeFriend(int index) {
        Response response = new Response();

        User user = controller.getUser();

        ArrayList<User> friendFriendList = user.getFriends().get(index).getFriends();

        int indexOfuser = 0;

        for (User u : friendFriendList) {
            if(u.getUserName().equals(user.getUserName())) {
                break;
            }
            ++indexOfuser;
        }

        friendFriendList.remove(indexOfuser);

        user.getFriends().remove(index);

        response.addText("Friend successfully removed.");

        controller.sendResponse(response);
    }

    /**
     * @Author danial
     * @param  mail
     * set new mail for user
     */
    public void changeMail(String mail) {
        Response response = new Response();

        User user = controller.getUser();

        user.setEmail(mail);

        response.addText("your mail successfully changed.");

        controller.sendResponse(response);
    }

    /**
     * @Author danial
     * @param phoneNumber
     * set new phonenumber for user
     */
    public void changePhoneNumber(String phoneNumber) {
        Response response = new Response();

        User user = controller.getUser();

        user.setPhoneNumber(phoneNumber);

        response.addText("your phonenumber successfully changed.");

        controller.sendResponse(response);
    }

    /**
     * @Author danial
     * @param password
     * set new password for user
     */
    public void changePassword(String password) {
        User user = controller.getUser();

        user.setPassWord(password);

        Response response = new Response();

        response.addText("your password successfully changed.");

        controller.sendResponse(response);
    }



    /**
     * @Author danial
     * @param  email
     * get email and check duplicate
     */
    public void checkDuplicateEmail(String email){
        Response response = new Response();

       if(clientDataManagement.getEmail(email)!=null){
           response.addText("This email is Duplicate");
           response.setFlag(Flag.NotSuccessful);
       }

       else {
           response.addText("");
           response.setFlag(Flag.Successful);
       }
       controller.sendResponse(response);
    }

    /**
     * @Author danial
     * @param userName
     * get username and check duplicate
     */
    public void checkDuplicateUserName(String userName) {
        Response response = new Response();

        if(clientDataManagement.getUserByUsername(userName) != null){
            response.addText("Username is duplicate.");
            response.setFlag(Flag.NotSuccessful);
        }

        else {
            response.setFlag(Flag.Successful);
        }

        controller.sendResponse(response);
    }


    /** @Author amin
     * @param userName
     * @param password
     * purpose of this function is make a response that let user SignIn or not
     */
    public void SignIn(String userName, String password){
        User user = clientDataManagement.singInUser(userName,password);

        Response response = new Response();

        if(user == null){
            response.addText("Invalid username or password");

            response.setFlag(Flag.NotSuccessful);
        }

        else {
            response.setFlag(Flag.Successful);

            controller.setUserCheckStatus(user);

            controller.setUser(user);

            user.setActive(true);
        }

        controller.sendResponse(response);
    }

    public void showSpecificChannelPermissionToRemove(String username){

        controller.showSpecificChannelPermissionToRemove(username);

    }

    public void showSpecificChannelPermissionToAdd(String username){

        controller.showSpecificChannelPermissionToAdd(username);

    }

    public void addChannelPermissionForSpecificChannel(String userName,String permission){

        controller.addChannelPermissionForSpecificChannel(userName,permission);

    }

    public void removeChannelPermissionForSpecificChannel(String username,String permission){

        controller.removeChannelPermissionForSpecificChannel(username,permission);

    }

    public void enterServer(String index){

        controller.setServer(index);

        controller.setMember();


    }

    public void showServerActions(){
        controller.showServerMenu();

    }

    public void inviteServer(String userName) {

        User user  = clientDataManagement.getUser(userName);

        Response response = new Response();

        if(user==null){

            response.setFlag(Flag.NotSuccessful);

            response.addText("this user doesn't exist");

            response.addText("Try again :");
        }
        else {

            ServerJoinInvite serverJoinInvite = new ServerJoinInvite(controller.getUser(),user,controller.getServer());

            user.addServerJoinInvite(serverJoinInvite);

            response.setFlag(Flag.Successful);

            response.addText("User invited successfully ");

            sendNotification("New server invite","You have new server invite from " +
                    controller.getUser().getUserName() + " for join to " + controller.getServer().getName() + " server.",userName);
        }

        controller.sendResponse(response);

    }

    public void showMembersOfServer(){

        controller.showMembers();

    }

    public void showChannelsOfServer(){

        controller.showChannels();

    }

    public void removeChannel(int index){

        controller.removeChannel(index);

    }


    public void showRoles(){

        controller.showRoles();

    }

    public void deleteRole(int index){

            controller.deleteRole(index);
    }

    /**
     * @Author amin
     * purpose of this function is to show DiscordServerOfUser
     */
    public void showDiscordServerOfUser(){

        controller.getDiscordServerOfUser();
    }

    /**
     * @Author amin
     * purpose of this function is making response that contains friends of user
     */
    public void getFriendsList(){
        controller.getFriendsOfUser();
    }

    public void SignUp(User user) {
        clientDataManagement.addUser(user);

        Response response = new Response();

        response.addText("Registration completed successfully.");

        response.setFlag(Flag.Successful);

        controller.sendResponse(response);
    }




    public void selectChannel(String index){

        controller.showChannelActions();

        controller.setChannel(Integer.parseInt(index));

    }

    public void acceptServerInvite(int index){

            controller.getUser().acceptServerInvite(index);

            Response response  = new Response();

            response.addText("welcome to the"+"  "+controller.getUser().getServerJoinInviteByIndex(index));

            controller.sendResponse(response);

    }

    public void rejectServerInvite(int index){

        controller.getUser().removeServerInviteByIndex(index);

        Response response = new Response();

        response.addText("Rejected Successfully");

    }


    public void sendMessageToChannel(String msg){
        controller.messageToChannel(msg);

        Channel channel = (Channel) controller.getChat();

        for(Subscriber sb : channel.getSubscribers()) {
            ClientController newcc = (ClientController) sb;

            sendNotification("New message","You have new message in " +
                    channel.getName() + " channel.",newcc.getUser().getUserName());
        }
    }

    public void showServerJoinInvites() {

        Response response = new Response();

        for (ServerJoinInvite SI:   controller.getUser().getServerJoinInvites()) {

            response.addText(SI.toString());
        }

        controller.sendResponse(response);

    }

    public void manageServer() {

        controller.showManageServerActions();

    }

    public void addChannel (String channelName) {

        Channel channel = new Channel(channelName);

        controller.getServer().addChannel(channel);

        Response response = new Response();

        response.addText("channel  added successfully ");

        controller.sendResponse(response);


    }

    public void addChannelPermission(String channelPermission,int index){

        controller.addChannelPermissionToRole(channelPermission,index);

    }

    public void showValidChannelPermissionToAdd(int index) {

        controller.showValidChannelPermissionsToAdd(index);

    }

    public void removeChannelPermissionOfRole(String channelPermission, int indexOfSelectedRole){

        controller.removeChannelPermission(channelPermission,indexOfSelectedRole);

    }

    public void removeServerPermissionOfRole(String selectedPermission, int index) {

        controller.removeServerPermission(selectedPermission,index);

    }

    public void changeNameOfServer(String newName){

        controller.changeNameOfServer(newName);

    }

    public void createRole(String roleName){

        controller.addRole(roleName);

    }

    public void changeNameOfRole(int indexOfRole,String newName){

        controller.changeNameOfRole(indexOfRole,newName);

    }


    public void showMembersOfRole(int index){

        controller.showMembersBaseOnTheirRole(index);


    }

    public void removeRoleFromMember(int indexOfRole,String username){

        controller.removeRoleFromMember(username,indexOfRole);

    }

    public void addRoleForMember(int indexOfRole,String username){

        controller.addRoleToMember(username,indexOfRole);

    }

    public void joinChat(){

        controller.joinChat();

    }

    public void getMessage(){

            controller.getMessageOfChannel();

    }

    public void checkForSendMessage(){

        controller.checkForSendMessage();

    }

    public void checkForManageMessage(){

        controller.checkManageMessage();

    }

    public void leaveChat(){

        controller.leaveChat();

    }


}
