package project.Discord.client;

import project.Discord.networkPortocol.ObjectRequested;
import project.Discord.networkPortocol.Request;
import project.Discord.networkPortocol.RequestType;
import project.Discord.networkPortocol.Response;
import project.Discord.server.Server;
import project.Discord.server.clientmanagment.Flag;
import project.Discord.server.entity.*;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class GraphicalInterface {

    private ResponseHandler responseHandler;

    private ObjectOutputStream objectOutputStream;

    private ArrayList<DiscordServer> servers;

    private ArrayList<User> friends;

    private ArrayList<PrivateChat> privateChats;

    private User user;

    public GraphicalInterface() {
        servers = new ArrayList<>();
        friends = new ArrayList<>();
        privateChats = new ArrayList<>();
    }

    public ArrayList<DiscordServer> getServers() {
        return servers;
    }

    public ArrayList<User> getFriends() {
        return friends;
    }

    public ArrayList<PrivateChat> getPrivateChats() {
        return privateChats;
    }

    public User getUser() {
        return user;
    }

    synchronized public void update() {
        servers = loadServers();

        friends = loadFriends();

        privateChats = loadPrivateChats();

        user = loadUser();
    }

    public GraphicalInterface(ObjectOutputStream objectOutputStream, ResponseHandler responseHandler) {
        this.responseHandler = responseHandler;
        this.objectOutputStream = objectOutputStream;
    }

    /**
     * @Author danial
     * send request
     */
    public void sendRequest(Request request){
        try {
            objectOutputStream.writeObject(request);
            Thread.sleep(300);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Author danial
     * @return String
     * show First Response
     */
    public String showFirstResponse() {
        return responseHandler.getFirstResponse();
    }

    /**
     * @Author danial
     * @return ArrayList<String>
     * get response texts arraylist
     */
    public ArrayList<String> getAllTexts() {
        return responseHandler.getAllTexts();
    }

    /**
     * @Author danial
     * chekc login info
     */
    public Boolean login(String userName,String password) {

        Request request = new Request(RequestType.GET, ObjectRequested.USER,"user/singin");

        request.addContent("username",userName);

        request.addContent("password",password);

        sendRequest(request);

        Flag flag = responseHandler.getFlag();

        if(flag == Flag.Successful) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * @Author danial
     * show SignUp and get username and password fields and SignUp user
     */
    public GraphicInputStatus singUp(String email,String userName,String password) {
        userName = userName.toLowerCase();

        InputStatus usernameStatus = userNameChecker(userName);

        InputStatus emailStatus = emailChecker(email);

        InputStatus passwordStatus = passwordChecker(password);

        if(emailStatus == InputStatus.FormatError) {
            return GraphicInputStatus.EmailFormatError;
        }

        if(usernameStatus == InputStatus.FormatError) {
            return GraphicInputStatus.UserNameFormatError;
        }

        if (passwordStatus == InputStatus.FormatError) {
            return GraphicInputStatus.PasswordFormatError;
        }

        if(emailStatus == InputStatus.Duplicate) {
            return GraphicInputStatus.EmailDuplicate;
        }

        if(usernameStatus == InputStatus.Duplicate) {
            return GraphicInputStatus.UserNameDuplicate;
        }

        Request request = new Request(RequestType.POST,ObjectRequested.USER,"user/signup");

        request.addContent("username",userName);

        request.addContent("password",password);

        request.addContent("email",email);

        request.addContent("phoneNumber","");

        sendRequest(request);

        return GraphicInputStatus.Successful;
    }

    /**
     * @Author danial
     * @return String
     * check email duplicate and check email string
     */
    public InputStatus emailChecker(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern mailPattern = Pattern.compile(emailRegex);

        if(!mailPattern.matcher(email).matches()){
            return InputStatus.FormatError;
        }

        Request request = new Request(RequestType.GET,ObjectRequested.USER,"user/email/checkDuplicate");

        request.addContent("email",email);

        sendRequest(request);

        Flag flag = responseHandler.getFlag();

        if (flag==Flag.NotSuccessful) {
            return InputStatus.Duplicate;
        }

        return InputStatus.Successful;
    }

    /**
     * @Author danial
     * @return String
     * check password string
     */
    public InputStatus passwordChecker(String password) {
        String passwordRegex = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])\\w{8,}";

        Pattern phonePattern = Pattern.compile(passwordRegex);

        boolean checkpassword = phonePattern.matcher(password).matches();

        if(!checkpassword) {
            return InputStatus.FormatError;
        }

        return InputStatus.Successful;
    }

    /**
     * @Author danial
     * @return Boolean
     * check username duplicate and check username string
     */
    public InputStatus userNameChecker(String userName) {
        String userNameRegex = "(?=.*[a-z,A-Z])(?=.*[0-9])\\w{6,}";

        Pattern userNamePattern = Pattern.compile(userNameRegex);

        userName = userName.toLowerCase();

        if(!userNamePattern.matcher(userName).matches()){
            return InputStatus.FormatError;
        }

        Request request = new Request(RequestType.GET, ObjectRequested.USER,"user/username/checkDuplicate");

        request.addContent("username",userName);

        sendRequest(request);

        Flag flag = responseHandler.getFlag();

        if(flag == Flag.NotSuccessful) {
            return InputStatus.Duplicate;
        }

        return InputStatus.Successful;
    }


    /**
     * @Author danial
     * @return ArrayList<DiscordServer>
     * load all user servers
     */
    public ArrayList<DiscordServer> loadServers() {
        ArrayList<DiscordServer> servers = new ArrayList<>();

        Request request = new Request(RequestType.GET,ObjectRequested.USER,"user/servers-obg");

        sendRequest(request);

        servers = responseHandler.getResponse().getServers();

        if (servers == null) {
            return new ArrayList<>();
        }

        return servers;
    }

    /**
     * @Author danial
     * @return ArrayList<DiscordServer>
     * load all user friends
     */
    public ArrayList<User> loadFriends() {
        ArrayList<User> friends = new ArrayList<>();

        Request request = new Request(RequestType.GET,ObjectRequested.USER,"user/friends-obg");

        sendRequest(request);

        friends = responseHandler.getResponse().getUsers();

        if (friends == null) {
            return new ArrayList<>();
        }

        return friends;
    }

    /**
     * @Author danial
     * @return ArrayList<FriendRequest>
     * load all friend request
     */
    public ArrayList<FriendRequest> loadFriendRequest() {
        Request request = new Request(RequestType.GET,ObjectRequested.USER,"user/friend-request-obg");

        sendRequest(request);

        Response response = responseHandler.getResponse();

        ArrayList<FriendRequest> friendRequests = response.getFriendRequests();

        if(response.getFriendRequest() != null && response.getFriendRequest() != friendRequests.get(friendRequests.size()-1)) {
            friendRequests.add(response.getFriendRequest());
            response.setFriendRequest(null);
        }

        if (friendRequests == null) {
            return new ArrayList<>();
        }

        return friendRequests;
    }

    /**
     * @Author danial
     * @return ArrayList<PrivateChat>
     * load all user private chats
     */
    public ArrayList<PrivateChat> loadPrivateChats() {
        ArrayList<PrivateChat> privateChats = new ArrayList<>();

        Request request = new Request(RequestType.GET,ObjectRequested.USER,"user/private-chat-obg");

        sendRequest(request);

        privateChats = responseHandler.getResponse().getPrivateChats();

        if (privateChats == null) {
            return new ArrayList<>();
        }

        return privateChats;
    }

    /**
     * @Author danial
     * @return user
     * load user info
     */
    public User loadUser() {
        Request request = new Request(RequestType.GET,ObjectRequested.USER,"user");

        sendRequest(request);

        User user = responseHandler.getResponse().getUser();

        return user;
    }

    /**
     * @Author danial
     * show friend request
     */
    public GraphicInputStatus addFriend(String userName) {
        userName = userName.toLowerCase();

        InputStatus userNameStatus = userNameChecker(userName);

        if(userNameStatus == InputStatus.FormatError) {
            return GraphicInputStatus.UserNameFormatError;
        }

        Request request = new Request(RequestType.POST,ObjectRequested.USER,"user/send-friend-request");

        request.addContent("username",userName);

        sendRequest(request);

        Flag flag = responseHandler.getFlag();

        if(flag == Flag.NotSuccessful) {
            return GraphicInputStatus.NotSuccessful;
        }
        else {
            return GraphicInputStatus.Successful;
        }
    }

    /**
     * @Author danial
     * show friend request
     */
    public void pendingFriendRequest(String userAction,int index) {

        Request request = new Request(RequestType.UPDATE,ObjectRequested.USER,"user/update-friend-request");

        request.addContent("index", String.valueOf(index));

        request.addContent("action",userAction);

        sendRequest(request);
    }
}
