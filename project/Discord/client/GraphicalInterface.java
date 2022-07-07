package project.Discord.client;

import project.Discord.networkPortocol.ObjectRequested;
import project.Discord.networkPortocol.Request;
import project.Discord.networkPortocol.RequestType;
import project.Discord.server.Server;
import project.Discord.server.clientmanagment.Flag;
import project.Discord.server.entity.DiscordServer;
import project.Discord.server.entity.PrivateChat;
import project.Discord.server.entity.Status;
import project.Discord.server.entity.User;

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
    /*
    ArrayList<String> serversName = getAllTexts();

        for (int i = 0; i < serversName.size(); i++) {
            request = new Request(RequestType.GET,ObjectRequested.SERVER,"server/photo");

            request.addContent("index", String.valueOf(i));

            sendRequest(request);

            servers.add(new DiscordServer(serversName.get(i),responseHandler.getFile()));
        }
    */

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
    /*
    public ArrayList<User> loadFriends() {
        ArrayList<String> friendsName = new ArrayList<>();

        Request request = new Request(RequestType.GET,ObjectRequested.USER,"user/friends-username");

        sendRequest(request);

        friendsName = getAllTexts();

        ArrayList<String> friendsStatus = new ArrayList<>();

        request = new Request(RequestType.GET,ObjectRequested.USER,"user/friends-status");

        sendRequest(request);

        friendsStatus = getAllTexts();


        ArrayList<byte[]> friendsPhoto = new ArrayList<>();

        for (int i = 0; i < friendsName.size(); i++) {
            request = new Request(RequestType.GET,ObjectRequested.USER,"user/photo");

            request.addContent("index", String.valueOf(i));

            sendRequest(request);

            Flag flag = responseHandler.getFlag();

            if(flag == Flag.Successful) {
                friendsPhoto.add(responseHandler.getFile());
            }
            else {
                friendsPhoto.add(null);
            }
        }

        ArrayList<User> friends = new ArrayList<>();

        for (int i = 0; i < friendsName.size(); i++) {

            User user = new User(friendsName.get(i),"","","");

            switch (friendsStatus.get(i)) {
                case "OFFLINE" -> user.setStatus(Status.OFFLINE);
                case "ONLINE" -> user.setStatus(Status.ONLINE);
                case "IDLE" -> user.setStatus(Status.IDLE);
                case "DO_NOT_DISTURB" -> user.setStatus(Status.DO_NOT_DISTURB);
                case "INVISIBLE" -> user.setStatus(Status.INVISIBLE);
            }

            if(friendsPhoto.get(i) != null) {
                user.setHavePhoto(true);

                user.setUserPhoto(friendsPhoto.get(i));
            }
            else {
                user.setHavePhoto(false);

                user.setUserPhoto(null);
            }

            friends.add(user);
        }

        return friends;
    }*/

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
}
