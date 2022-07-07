package project.Discord.client;

import project.Discord.client.clientException.InputFileAddressException;
import project.Discord.client.clientException.InputIndexException;
import project.Discord.server.clientmanagment.Flag;
import project.Discord.networkPortocol.ObjectRequested;
import project.Discord.networkPortocol.Request;
import project.Discord.networkPortocol.RequestType;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.regex.Pattern;
import static project.Discord.client.Client.exit;



public class ConsoleInterface implements UserInterface {

    private final Scanner scanner;

    private final ResponseHandler responseHandler;

    private final ObjectOutputStream objectOutputStream;

    public ConsoleInterface(ObjectOutputStream objectOutputStream, ResponseHandler responseHandler) {
        this.scanner = new Scanner(System.in);
        this.responseHandler = responseHandler;
        this.objectOutputStream = objectOutputStream;
    }

    /**
     * @Author danial
     * @param maxInput
     * @param minInput
     * @return String
     * check index input and if is ok return it
     */
    public String checkInput(int maxInput,int minInput) {
        String input = null;

        while (true) {
            try {
                input = scanner.nextLine();

                if(Integer.parseInt(input) > maxInput || Integer.parseInt(input) < minInput) {
                    throw new InputIndexException();
                }
                else {
                    return input;
                }
            }
            catch(InputIndexException | NumberFormatException ex) {
                System.err.println("Invalid input.");
            }
        }
    }


    public String checkFileAddressInput() {
        while (true) {
            try {
                String fileAddress = scanner.nextLine();

                File file;

                if ((file = new File(fileAddress)).exists()) {
                    String[] fileName = file.getName().split("\\.");
                    if(fileName[1].equals("jpg")) {
                        return fileAddress;
                    }
                    else {
                        throw new InputFileAddressException();
                    }
                }
                else {
                    throw new InputFileAddressException();
                }
            }
            catch (InputFileAddressException ex) {
                System.err.println("Invalid file address.");
            }
        }
    }


    /**
     * @Author danial
     * show login menu
     */
    @Override
    public void loginMenu() {
        System.out.println("1.SingIn\n2.SingUp\n3.Exit");

        String index = checkInput(3,1);

        switch (index) {
            case "1" :
                signInMenu();
                break;
            case "2" :
                singUpMenu();
                break;
            case "3" :
                closeApp();
                exit();
                break;
            default :
                throw new IllegalStateException("Unexpected value: " + index);
        }
        loginMenu();
    }

    /**
     * @Author danial
     * close app method for close socket
     */
    public void closeApp() {
        Request request = new Request(RequestType.UPDATE,ObjectRequested.SERVER,"server/close-app");

        sendRequest(request);
    }


    /**
     * @Author danial
     * show singin and get username and password fields and singing user
     */
    @Override
    public void signInMenu() {
        Flag flag;

        do{
            System.out.println("Enter your username : ");
            String userName = scanner.nextLine();
            userName = userName.toLowerCase();
            System.out.println("Enter your password : ");
            String password = scanner.nextLine();
            Request request = new Request(RequestType.GET, ObjectRequested.USER,"user/singin");

            request.addContent("username",userName);

            request.addContent("password",password);

            sendRequest(request);

            showResponse();

            flag = responseHandler.getFlag();

        } while (flag==Flag.NotSuccessful);

        startMenu();
    }


    /**
     * @Author danial
     * show response
     */
    public void showResponse(){
        responseHandler.showResponse();
    }

    /**
     * @Author danial
     * show response with index
     */
    public void showResponseWithIndex() {
        responseHandler.showResponseWithIndex();
    }

    /**
     * @Author danial
     * @return int
     * return response text array size
     */
    public int responseTextSize() {
        return responseHandler.responseTextSize();
    }


    /**
     * @Author danial
     * show SignUp and get username and password fields and SignUp user
     */
    @Override
    public void singUpMenu() {

        String userName = userNameChecker();

        String password = passwordChecker();

        String email = emailChecker();

        String phoneNumber = phoneChecker();

        Request request = new Request(RequestType.POST,ObjectRequested.USER,"user/signup");

        request.addContent("username",userName);

        request.addContent("password",password);

        request.addContent("email",email);

        request.addContent("phoneNumber",phoneNumber);

        sendRequest(request);

        showResponse();
    }

    /**
     * @Author danial
     * show start menu
     */
    @Override
    public void startMenu() {
        System.out.println("1.Home\n2.Servers\n3.Create a server\n4.User Settings\n5.Notifications\n6.Back");

        String index = checkInput(6,1);

        switch (index) {
            case "1" :
                homeMenu();
                break;
            case "2" :
                serversMenu();
                break;
            case "3" :
                createServerMenu();
                break;
            case "4" :
                userSettings();
                break;
            case "5" :
                showNotifications();
                break;
            case "6" : {
                logOut();
                loginMenu();
            }
        }


        startMenu();
    }

    /**
     * @Author danial
     * close app method for close socket
     */
    public void logOut() {
        Request request = new Request(RequestType.UPDATE,ObjectRequested.SERVER,"server/logOut");

        sendRequest(request);
    }

    /**
     * @Author danial
     * show friends and private chats menu
     */
    @Override
    public void homeMenu() {
        System.out.println("1.Friends\n2.Private Chats\n3.Back");

        String index = checkInput(3,1);

        switch (index) {
            case "1" :
                showFriendsMenu();
                break;
            case "2" :
                privateChatsMenu();
                break;
            case "3" :
                startMenu();
               break;
        }
        homeMenu();
    }

    /**
     * @Author danial
     * show user notifications
     */
    public void showNotifications() {
        Request request = new Request(RequestType.GET,ObjectRequested.USER,"user/notifications");

        sendRequest(request);

        showResponse();

        System.out.println(1 + ".Back");

        String index = checkInput(1,1);

        if(Integer.parseInt(index) == 1) {
            return;
        }
    }

    /**
     * @Author danial
     * private chats menu
     */
    public void privateChatsMenu() {
        System.out.println("1.List\n2.Add new chat\n3.Back");

        String index = checkInput(3,1);

        switch (index) {
            case "1" :
                showUserPrivateChats();
                break;
            case "2" :
                openPrivateMessageWithOtherUser();
                break;
            case "3" :
                homeMenu();
                return;
        }
        privateChatsMenu();
    }

    /**
     * @Author danial
     * show user private chat
     */
    public void showUserPrivateChats() {
        sendRequest(new Request(RequestType.GET,ObjectRequested.USER,"user/privateChats"));

        showResponseWithIndex();

        int size = responseTextSize();

        if(size != 0) {
            System.out.println("Select a private chat: ");
        }

        String index = checkInput(size + 1,1);

        int maxIndex = responseTextSize() + 1;

        if(Integer.parseInt(index) == maxIndex){
            return;
        }

        int temp = Integer.parseInt(index);

        temp -= 1;

        index = String.valueOf(temp);

        joinSelectedprivateChat(index);
    }

    /**
     * @Author danial
     * open a private message with other users
     */
    public void openPrivateMessageWithOtherUser() {
        System.out.println("Type username to open private chat: ");

        String userName = scanner.nextLine();

        Request request = new Request(RequestType.POST,ObjectRequested.USER,"user/open-private-chat");

        request.addContent("username",userName);

        sendRequest(request);

        Flag flag = responseHandler.getFlag();

        if(flag == Flag.Successful) {
            sendMessageToPrivateChat();
        }
        else {
            showResponse();
        }
    }

    /**
     * @Author danial
     * create or join to selected private chat
     */
    public void createPrivateChat(String indexOfFriend) {
        Request request = new Request(RequestType.GET,ObjectRequested.USER,"user/check-exist-create-privateChat");

        request.addContent("index",indexOfFriend);

        sendRequest(request);

        showResponse();

        sendMessageToPrivateChat();
    }

    public void kickMember(){

        System.out.println("please enter userName");

        String userName = scanner.nextLine();

        Request request = new Request(RequestType.DELETE,ObjectRequested.SERVER,"server/kickMember");

        request.addContent("username",userName);

        sendRequest(request);

        sendRequest(request);

    }


    /**
     * @Author danial
     * @param index
     * join to selected private chat
     */
    public void joinSelectedprivateChat(String index) {
        Request request = new Request(RequestType.GET,ObjectRequested.USER,"user/privateChat");

        request.addContent("index", index);

        sendRequest(request);

        showResponse();

        sendMessageToPrivateChat();
    }

    /**
     * @Author danial
     * send request
     */
    public void sendMessageToPrivateChat() {
        responseHandler.setInChat(true);

        Request request;

        System.out.println("Type #exit for exit this chat and type #file for send file message " +
                "and for download file message type #get-file");

        String msgText = "";
        while (!msgText.equals("#exit")) {
            msgText = scanner.nextLine();

            if(!msgText.equals("#exit") && !msgText.equals("") && !msgText.equals("#file") && !msgText.equals("#get-file")) {
                request = new Request(RequestType.POST,ObjectRequested.CHAT,"chat/sendMessage");

                request.addContent("msg",msgText);

                sendRequest(request);

                Flag flag = responseHandler.getFlag();

                if(flag == Flag.NotSuccessful) {
                    break;
                }
            }
            if (msgText.equals("#file")) {
                request = new Request(RequestType.POST,ObjectRequested.CHAT,"chat/sendfilemessage");

                System.out.println("Type your file caption: ");

                String filecaption = scanner.nextLine();

                System.out.println("Send file url: ");

                String fileURL = scanner.nextLine();

                request.addContent("caption",filecaption);

                request.addContent("url",fileURL);

                sendRequest(request);

                System.out.println("File message send.");
            }
            if(msgText.equals("#get-file")) {
                request = new Request(RequestType.GET,ObjectRequested.USER,"user/download-file");

                System.out.println("Type download code: ");

                String downloadCode = scanner.nextLine();

                request.addContent("downloadcode",downloadCode);

                sendRequest(request);

                Flag flag = responseHandler.getFlag();

                if(flag == Flag.Successful) {
                    URL url = null;

                    try {
                        url = new URL(responseHandler.getFirstResponse());
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }

                    System.out.println("Type address to save file: (example: " +
                            "C:\\Users\\user\\Desktop\\save\\test.pdf) ");

                    String fileAddress = scanner.nextLine();

                    Thread thread = new Thread(new Downloader(url,fileAddress));

                    thread.start();
                }
            }
        }

        responseHandler.setInChat(false);

        request = new Request(RequestType.UPDATE,ObjectRequested.CHAT,"chat/exit-user");

        sendRequest(request);

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
     * show user servers
     */
    @Override
    public void showServers(){

        while (true){


            Request request = new Request(RequestType.GET,ObjectRequested.USER,"user/servers");

            sendRequest(request);

            showResponseWithIndex();

            int maxIndex = responseTextSize()+1;

            String index = scanner.nextLine();

            if(Integer.parseInt(index)==maxIndex)
               break;

            Request request1 = new Request(RequestType.GET,ObjectRequested.USER,"user/discord-server");

            request1.addContent("index",index);

            sendRequest(request1);

            showServerActions();


        }

    }

    /**
     * @Author danial
     * show selected server
     */
    public void showServerActions() {

        while (true){

            Request request = new Request(RequestType.GET,ObjectRequested.USER,"user/showServerActions");

            sendRequest(request);

            showResponseWithIndex();

            String row = scanner.nextLine();

            int maxIndex = responseTextSize()+1;

            if(Integer.parseInt(row)==maxIndex){


               break;

            }

            else{

                String selectedOption = responseHandler.getResponse().getTexts().get(Integer.parseInt(row)-1);

                switch (selectedOption){

                    case "InviteServer"->
                            inviteServer();

                    case "ManageServer"->
                            manageServer();



                    case "Members"->
                            showMembers();



                    case "Channels"->
                            showChannelsForJoining();

                }

            }

        }




    }



    public void showChannelsForJoining(){


        while (true){

            Request request = new Request(RequestType.GET,ObjectRequested.SERVER,"server/channels");

            sendRequest(request);

            showResponseWithIndex();

            String index = scanner.nextLine();

            int maxIndex = responseTextSize()+1;

            if(Integer.parseInt(index)==maxIndex){

                break;
            }

            selectChannel(index);

        }


    }

    public void showMembers(){

        Request request = new Request(RequestType.GET,ObjectRequested.SERVER,"server/members");

        sendRequest(request);

        showResponse();

        System.out.println("press any key to Back....");

        scanner.nextLine();

    }


    public void manageServer(){


        while (true){

            Request request = new Request(RequestType.UPDATE,
                    ObjectRequested.SERVER,"server/manageServer");

            sendRequest(request);

            showResponseWithIndex();

            String index = scanner.nextLine();

            int maxIndex =  responseTextSize()+1;

            if(Integer.parseInt(index)==maxIndex){

               break;

            }

            String selectedOption = responseHandler.getResponse().getTexts().get(Integer.parseInt(index)-1);

            switch (selectedOption){

                case "EditServerSettings"->editServerSettings();

                case "ManageRoles"->manageRoles();

                case "ManageChannels"->manageChannels();

            }


        }


    }



    public void editServerSettings(){

        while (true){

            System.out.println("1.changeName\n2.changePhoto\n3.Back");

            String answer = scanner.nextLine();

            switch (answer){

                case "1"->changeNameOfServer();

                case "2"->changePhotoOfServer();

                case "3"->{
                    return;
                }

            }

        }

    }


    public void changeNameOfServer(){

        System.out.println("please enter newName :");

        String newName = scanner.nextLine();

        Request request = new Request(RequestType.UPDATE,ObjectRequested.SERVER,"server/changeName");

        request.addContent("newName",newName);

        sendRequest(request);

        showResponse();

        System.out.println("press any key to continue ...");

        scanner.nextLine();



    }

    public void manageRoles(){

        while (true){

            System.out.println("1.showRoles\n2.addRole\n3.Back");

            String index = checkInput(3,1);

            switch (index){

                case "1"->showRoles();

                case "2"->addRole();

                case "3"-> {
                    return;
                }

            }


        }


    }

    public void addRole(){

        Flag flag;

        do{
            System.out.println("please enter name of Role");

            String nameOfRole= scanner.nextLine();

            Request request = new Request(RequestType.POST,ObjectRequested.SERVER,"server/role/addRole");

            request.addContent("nameOfRole",nameOfRole);

            sendRequest(request);

            showResponse();

            flag = responseHandler.getFlag();

        }while (flag==Flag.NotSuccessful);


    }

    public void showRoles(){

        while (true){

            Request request = new Request(RequestType.GET,ObjectRequested.SERVER,"server/roles");

            sendRequest(request);

            showResponseWithIndex();

            String index = scanner.nextLine();

            int maxIndex = responseTextSize()+1;

            if(Integer.parseInt(index)==maxIndex){

                break;

            }

            selectRole(index);

        }


    }



    public void selectChannel(String index) {

        while (true) {

            Request request = new Request(RequestType.GET, ObjectRequested.CHANNEL, "channel/actions");

            request.addContent("indexOfChannel", index);

            sendRequest(request);

            showResponseWithIndex();

            String indexOfAction = scanner.nextLine();

            int maxIndex = responseTextSize() + 1;

            if (Integer.parseInt(indexOfAction) == maxIndex) {

                return;

            }

            String option = responseHandler.getResponse().getTexts().get(Integer.parseInt(indexOfAction) - 1);

            switch (option) {

                case "Join channel" -> joinChannel();

                case "ManagePermissionsOfMembers" -> selectMember();

            }

        }
    }

    public void selectAction(String username){

        while (true){


            System.out.println("1.addPermission\n2.removePermission\n3.Back");

            String answer = scanner.nextLine();

            switch (answer){


                case "1"->showSpecificValidChannelPermissionToAdd(username);

                case "2"->showSpecificValidChannelPermissionToRemove(username);

                case "3"->{
                    return;
                }

            }


        }



    }


    public void selectMember(){

        String userName;

        Flag flag ;

        do {

            System.out.println("please enter userName :");

            userName = scanner.nextLine();

            Request request = new Request(RequestType.GET,ObjectRequested.USER, "user/username/checkDuplicate");

            request.addContent("username",userName);

            sendRequest(request);

            flag = responseHandler.getFlag();

            if(flag == Flag.Successful){

                System.out.println("this username doesn't exist ");

            }

        }while (flag == Flag.Successful);

        selectAction( userName);


    }


    public void showSpecificValidChannelPermissionToAdd(String  username){

        while (true){

            Request request = new Request(RequestType.GET,ObjectRequested.USER,"user/specificChannelPermissionToAdd");

            request.addContent("username",username);

            sendRequest(request);

            showResponseWithIndex();

            int maxIndex = responseTextSize()+1;

            String answer = checkInput(maxIndex,1);

            if(Integer.parseInt(answer)==maxIndex){

                return;
            }

            String selectedPermission = responseHandler.getResponse().getTexts().get(Integer.parseInt(answer)-1);

            addChannelPermissionForSpecificChannel(username,selectedPermission);

        }



    }

    public void addChannelPermissionForSpecificChannel(String userName,String permission){

        Request request = new Request(RequestType.POST,ObjectRequested.USER,"user/addChannelPermissionForSpecificChannel");

        request.addContent("permission",permission);

        request.addContent("username",userName);

        sendRequest(request);

        showResponse();


    }

    public void showSpecificValidChannelPermissionToRemove(String username){

        while (true){

            Request request = new Request(RequestType.GET,ObjectRequested.USER,
                    "user/specificChannelPermissionToRemove");

            request.addContent("username",username);

            sendRequest(request);

            showResponseWithIndex();

            int maxIndex = responseTextSize()+1;

            String answer = checkInput(maxIndex,1);

            if(Integer.parseInt(answer)==maxIndex){

                return;
            }

            String selectedPermission = responseHandler.getResponse().getTexts().get(Integer.parseInt(answer)-1);

            removeChannelPermissionForSpecificChannel(username,selectedPermission);

        }



    }

    public void removeChannelPermissionForSpecificChannel(String username,String permission){

        Request request = new Request(RequestType.DELETE,ObjectRequested.USER, "user/removeChannelPermissionForSpecificChannel");

        request.addContent("username",username);

        request.addContent("permission",permission);

        sendRequest(request);

        showResponse();

    }

    public void selectRole(String index){

            System.out.println("1.DeleteRole\n2.EditRole\n3.Back");

            String answer  = scanner.nextLine();

            switch (answer){

                case "1"->DeleteRole(index);

                case "2"->editRole(index);

                case "3"->{
                    return;
                }
            }


    }

    public void editRole(String index){

        while (true){

            System.out.println("1.changeName\n2.changePermissions\n3.changeMembersOfRole\n4.Back");

            String answer = scanner.nextLine();

            switch (answer){

                case "1"->changeNameOfRole(index);

                case "2"->showPermissionsOfRole(index);

                case "3"->changeMembersOfRole(index);

                case "4"->{
                    return;
                }


            }


        }


    }

    public void changeMembersOfRole(String index){


        while (true){

            System.out.println("1.addMember\n2.removeMember\n3.Back");

            String answer = scanner.nextLine();

            switch (answer){

                case "1"->selectMember(index);

                case "2"->showMembersOfRole(index);

                case "3"->{
                    return;
                }

            }


        }


    }

    public void showMembersOfRole(String indexOfRole){

        while (true){

            Request request = new Request(RequestType.GET,ObjectRequested.SERVER,
                    "server/role/members");
            request.addContent("indexOfRole",indexOfRole);

            sendRequest(request);

            showResponseWithIndex();
            String index = scanner.nextLine();
            int maxIndex = responseTextSize()+1;
            if(Integer.parseInt(index)==maxIndex)
                break;

            removeMemberFromRole(indexOfRole,index);
        }

    }


    public void removeMemberFromRole(String indexOfRole,String indexOfSelectedMember){
        String selectedMember = responseHandler.getResponse().getTexts().
                get(Integer.parseInt(indexOfSelectedMember)-1);

        Request request = new Request(RequestType.DELETE,ObjectRequested.SERVER,
                "server/role/removeMember");

        request.addContent("selectedMember",selectedMember);

        request.addContent("indexOfRole",indexOfRole);

        sendRequest(request);

        showResponse();


    }

    public void selectMember(String indexOfRole){

        String selectedUser;

        Flag flag ;

        do {


            System.out.println("please enter userName");

             selectedUser = scanner.nextLine();

            Request request = new Request(RequestType.GET,ObjectRequested.USER,
                    "user/username/checkDuplicate");

            request.addContent("username",selectedUser);

            sendRequest(request);

            flag = responseHandler.getFlag();

           if (flag==Flag.Successful)
               System.out.println("this username doesn't exist");



        }while (flag==Flag.Successful);

        addRoleForMember(selectedUser,indexOfRole);

    }

    public void addRoleForMember(String username,String indexOfRole){

        Request request = new Request(RequestType.POST,ObjectRequested.USER,"user/role/addRole");

        request.addContent("username",username);

        request.addContent("indexOfRole",indexOfRole);

        sendRequest(request);

    }

    public void changeNameOfRole(String index){

        Flag flag ;

        do{

            Request request = new Request(RequestType.UPDATE,ObjectRequested.SERVER,"server/role/changeName");

            System.out.println("enter newName :");

            String newName = scanner.nextLine();

            request.addContent("indexOfRole",index);

            request.addContent("newName",newName);

            sendRequest(request);

            showResponse();

            flag = responseHandler.getFlag();

        }while (flag==Flag.NotSuccessful);


    }

    public void showPermissionsOfRole(String index){

        while (true){


            Request request = new Request(RequestType.GET,ObjectRequested.SERVER,"server/role/serverPermissions");

            request.addContent("index",index);

            sendRequest(request);

            System.out.println("ServerPermissions :");

            showResponse();

            Request request1 = new Request(RequestType.GET,ObjectRequested.SERVER,"server/role/channelPermission");

            request1.addContent("index",index);

            sendRequest(request1);

            System.out.println("\n");

            System.out.println("ChannelPermissions : ");

            showResponse();

            System.out.print("\n");

            System.out.println("1.addPermissions\n2.removePermissions\n3.Back");

            String answer = scanner.nextLine();

            switch (answer){

                case "1"->addPermissions(index);

                case "2"->removePermissions(index);

                case "3"->{return;}


            }


        }




    }

    public void addPermissions(String index){

        while (true){

            System.out.println("1.addServerPermissions\n2.addChannelPermissions\n3.Back");

            String answer = scanner.nextLine();

            switch (answer){

                case "1"->showValidServerPermissionsToAdd(index);

                case "2"->showValidChannelPermissionToAdd(index);

                case "3"->{return;}

            }

        }


    }



    public void showValidChannelPermissionToAdd(String index){

        while (true){

            Request request = new Request(RequestType.GET,ObjectRequested.SERVER,"server/Role/validChannelPermission");

            request.addContent("index",index);

            sendRequest(request);

            showResponseWithIndex();

            int maxIndex = responseTextSize()+1;

            String row = scanner.nextLine();

            if(Integer.parseInt(row)==maxIndex)
                break;


            addChannelPermission(row,index);
        }

    }

    public void addChannelPermission(String indexOfSelectedPermissions,String indexOfSelectedRole){


        String selectedPermission= responseHandler.getResponse().getTexts().get(Integer.
                parseInt(indexOfSelectedPermissions)-1);

        Request request = new Request(RequestType.POST,ObjectRequested.SERVER,"server/Role/addChannelPermissionToRole");

        request.addContent("permission",selectedPermission);

        request.addContent("indexOfSelectedRole",indexOfSelectedRole);

        sendRequest(request);

        showResponse();


    }

    public void showValidServerPermissionsToAdd(String index){

        while (true){

            Request request = new Request(RequestType.GET,ObjectRequested.SERVER,"server/Role/validServerPermissions");

            request.addContent("index",index);

            sendRequest(request);

            showResponseWithIndex();

            int maxIndex = responseTextSize()+1;

            String row = scanner.nextLine();

            if(Integer.parseInt(row)==maxIndex)
                break;

            addServerPermissions(row,index);
        }

    }

    public void addServerPermissions(String indexOfSelectedPermissions,String indexOfSelectedRole){

        String selectedPermission= responseHandler.getResponse().getTexts().get(Integer.
                parseInt(indexOfSelectedPermissions)-1);

        Request request = new Request(RequestType.POST,ObjectRequested.SERVER,"server/Role/addServerPermissionToRole");

        request.addContent("permission",selectedPermission);

        request.addContent("indexOfSelectedRole",indexOfSelectedRole);

        sendRequest(request);

        showResponse();

    }

    public void removePermissions(String index){

        while (true){

            System.out.println("1.removeServerPermission\n2.removeChannelPermission\n3.Back");

            String answer = scanner.nextLine();

            switch (answer){


                case "1"->showValidServerPermissionToRemove(index);

                case "2"->showValidChannelPermissionToRemove(index);

                case "3"->{return;}
            }

        }

    }

    public void showValidServerPermissionToRemove(String index){


        while (true){

            Request request = new Request(RequestType.GET,ObjectRequested.SERVER,"server/role/serverPermissions");

            request.addContent("index",index);

            sendRequest(request);

            showResponseWithIndex();

            int maxIndex = responseTextSize()+1;

            String row = scanner.nextLine();

            if(Integer.parseInt(row)==maxIndex)
                break;

            removeServerPermission(row,index);

        }


    }

    public void showValidChannelPermissionToRemove(String index){

        while (true){

            Request request1 = new Request(RequestType.GET,ObjectRequested.SERVER,"server/role/channelPermission");

            request1.addContent("index",index);

            sendRequest(request1);

            showResponseWithIndex();

            int maxIndex = responseTextSize()+1;

            String row = scanner.nextLine();

            if(Integer.parseInt(row)==maxIndex)
                break;

            removeChannelPermission(row,index);

        }

    }

    public void removeChannelPermission(String indexOfPermission,String indexOfRole){

        String selected = responseHandler.getResponse().getTexts().get(Integer.parseInt(indexOfPermission)-1);

        Request request = new Request(RequestType.DELETE,ObjectRequested.SERVER,"server/role/removeChannelPermission");

        request.addContent("selectedPermission",selected);

        request.addContent("index",indexOfRole);
        sendRequest(request);

        showResponse();


    }

    public void removeServerPermission(String indexOfPermission,String indexOfRole){

        String selected  = responseHandler.getResponse().getTexts().get(Integer.parseInt(indexOfPermission)-1);

        Request request = new Request(RequestType.DELETE,ObjectRequested.SERVER,"server/role/removeServerPermission");

        request.addContent("index",indexOfRole);

        request.addContent("selectedPermission",selected);

        sendRequest(request);

        showResponse();

    }

    public void manageChannels(){

        while (true){

            System.out.println("1.addChannel\n2.removeChannel\n3.Back");

            String index = checkInput(3,1);

            switch (index){

                case "1"->addChannel();

                case "2"->{String row =showChannels();

                    if(row.equals(""))
                        continue;

                    removeChannel(row);
                }

                case "3"->{return;}

            }

        }

    }

    public String showChannels(){

        Request request = new Request(RequestType.GET,ObjectRequested.SERVER,"server/channels");

        sendRequest(request);

        showResponseWithIndex();

        String index = scanner.nextLine();

        int maxIndex = responseTextSize()+1;

        if(Integer.parseInt(index)==maxIndex){
            return "";
        }

        return index;

    }

    public void DeleteRole(String index){

        Request request = new Request(RequestType.DELETE,ObjectRequested.SERVER,"server/deleteRole");

        request.addContent("index",index);

        sendRequest(request);

        showResponse();

        System.out.println("PressAnyKeyToBach ...");

        scanner.nextLine();
    }

    public void removeChannel(String index){

        Request request = new Request(RequestType.DELETE,ObjectRequested.CHANNEL,"channel/removeChannel");

        request.addContent("index",index);

        sendRequest(request);

        showResponse();

        System.out.println("PressAnyKeyToBack ...");

        String answer = scanner.nextLine();

    }

    public void addChannel(){

        System.out.println("Enter channel name :");

        String channelName = scanner.nextLine();

        Request request = new Request(RequestType.POST, ObjectRequested.CHANNEL,"channel/addChannel");

        request.addContent("channelName",channelName);

        sendRequest(request);

        showResponse();

        System.out.println("PressAnyKeyToBack ...");

        String answer = scanner.nextLine();

    }


    public void joinChannel(){

        Request request = new Request(RequestType.POST,ObjectRequested.CHANNEL,"channel/joinChannel");

        sendRequest(request);

        loadMessages();

    }

    public void loadMessages(){

        Request request = new Request(RequestType.GET,ObjectRequested.CHANNEL,"channel/getMessages");

        sendRequest(request);

        showResponse();

        int size = responseTextSize();

        startChat(size);
    }


    public void sendMessageToChannel(String msg){

        Request request = new Request(RequestType.POST,ObjectRequested.CHANNEL,"channel/sendMessage");

        request.addContent("message",msg);

        sendRequest(request);
    }


    public void startChat(int size){

        System.out.println("""
                for exit from chat enter #exit
                for getting file enter #get-file
                for sending file enter #file
                for pin enter #pin\s""");


        boolean sendMessage;

        boolean manageMessage;

        responseHandler.setInChat(true);

        loop : while (true){

            Request request = new Request(RequestType.GET,ObjectRequested.USER,"user/checkSendMessage");

            sendRequest(request);

            Flag flag = responseHandler.getFlag();

            if(flag==Flag.Successful)
                sendMessage=true;

            else
                sendMessage = false;

            Request request1 = new Request(RequestType.GET,ObjectRequested.USER,"user/checkManageMessage");

            sendRequest(request1);

            Flag flag1 = responseHandler.getFlag();


            if(flag1==Flag.Successful)
                manageMessage=true;
            else
                manageMessage=false;


            String msg=scanner.nextLine();

            switch (msg){


                case "#exit"->{
                    responseHandler.setInChat(false);
                    break loop;}

                case "#file"->{

                    if(sendMessage){
                        sendFileToChannel();

                    }
                    else
                        System.err.println("you don't have permission to send any message !");

                }

                case "#get-file"->{
                    getFileFromChannel();
                }

                case "#pin"->{

                    if(sendMessage&&manageMessage){
                        pinMessage(size);
                    }
                    else
                        System.err.println("you don't have permission to pin any message !");



                }

                default -> {
                    if(sendMessage)
                        sendMessageToChannel(msg);

                    else
                        System.err.println("you don't have permission to send message !");

                }

            }


        }

        Request request2 = new Request(RequestType.UPDATE,ObjectRequested.CHANNEL,"channel/leaveChat");

        sendRequest(request2);

    }


    /**
     * @Author danial
     * show Pin Messages
     */
    public void showPinMessages() {
        Request request = new Request(RequestType.GET,ObjectRequested.CHANNEL,"channel/pin-messages");

        sendRequest(request);

        showResponse();
    }

    /**
     * @Author danial
     * get file from channel
     */
    public void getFileFromChannel() {
        Request request = new Request(RequestType.GET,ObjectRequested.USER,"user/download-file-from-channel");

        System.out.println("Type download code: ");

        String downloadCode = scanner.nextLine();

        request.addContent("downloadcode",downloadCode);

        sendRequest(request);

        Flag flag = responseHandler.getFlag();

        if(flag == Flag.Successful) {
            URL url = null;

            try {
                url = new URL(responseHandler.getFirstResponse());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            System.out.println("Type address to save file: (example: " +
                    "C:\\Users\\user\\Desktop\\save\\test.pdf) ");

            String fileAddress = scanner.nextLine();

            Thread thread = new Thread(new Downloader(url,fileAddress));

            thread.start();
        }
    }

/**
 * @Author danial
 * send file to channel
 */
public void sendFileToChannel(){
        Request request = new Request(RequestType.POST,ObjectRequested.CHAT,"chat/send-file-message-to-channel");

        System.out.println("Type your file caption: ");

        String filecaption = scanner.nextLine();

        System.out.println("Send file url: ");

        String fileURL = scanner.nextLine();

        request.addContent("caption",filecaption);

        request.addContent("url",fileURL);

        sendRequest(request);

        System.out.println("File message send.");


        }


    /**
     * @Author danial
     * pin message in channel
     */
    public void pinMessage(int size){
        System.out.println("Type message code or download code to pin message: ");

        String index = checkInput(size - 1,0);

        Request request = new Request(RequestType.POST,ObjectRequested.CHAT,"chat/pin-message");

        request.addContent("indexofmessage",index);

        sendRequest(request);

        showResponse();
    }

    /**
     * @Author danial
     * create server menu
     */
    @Override
    public void createServerMenu() {
        System.out.println("Enter server name: ");

        String serverName = scanner.nextLine();

        System.out.println("Enter server photo file address: (path: .jpg) ");

        String fileAddress = scanner.nextLine();

        byte[] serverPhoto = new byte[0];

        try {
            BufferedImage bImage = ImageIO.read(new File(fileAddress));

            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            ImageIO.write(bImage, "jpg", bos );

            serverPhoto = bos.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
        }

        Request request = new Request(RequestType.POST,ObjectRequested.SERVER,"server/create-server");

        request.addContent("servername",serverName);

        request.setFile(serverPhoto);

        sendRequest(request);

        showResponse();

    }



    public void userSettings() {
        System.out.println("1.Edit password\n2.Choose photo\n3.Change mail\n4.Change phone number\n5.Change status\n6.Back");

        String index = checkInput(6,1);

        switch (index) {
            case "1" :
                editPassword();
                break;
            case "2" :
                choosePhotoForProfile();
                break;
            case "3" :
                changeMail();
                break;
            case "4" :
                changePhoneNumber();
                break;
            case "5":
                changeUserStatus();
                break;
            case "6" :
                startMenu();
                return;
        }
        userSettings();

    }

    /**
     * @Author danial
     * change user status
     */
    public void changeUserStatus() {
        System.out.println("Choose your status:\n1.Online\n2.Idle\n3.Do Not Disturb\n4.Invisible");

        String index = checkInput(4,1);

        Request request = new Request(RequestType.UPDATE,ObjectRequested.USER,"user/change-status");

        switch (index) {
            case "1":
                request.addContent("status","ONLINE");
                break;
            case "2":
                request.addContent("status","IDLE");
                break;
            case "3":
                request.addContent("status","DO_NOT_DISTURB");
                break;
            case "4":
                request.addContent("status","INVISIBLE");
                break;
        }

        sendRequest(request);

        showResponse();
    }



    /**
     * @Author danial
     * edit password for user
     */
    public void editPassword() {
        Flag flag;
        do {
            System.out.println("Type old password: ");

            String oldPass = scanner.nextLine();

            Request request = new Request(RequestType.GET, ObjectRequested.USER, "user/check-old-password");

            request.addContent("oldPass",oldPass);

            sendRequest(request);

            showResponse();

            flag = responseHandler.getFlag();

        } while (flag == Flag.NotSuccessful);


        String newPass = passwordChecker();

        Request request = new Request(RequestType.UPDATE,ObjectRequested.USER,"user/change-password");

        request.addContent("password",newPass);

        sendRequest(request);

        showResponse();
    }

    public void changePhotoOfServer(){

        System.out.println("Enter  photo file address: (path: .jpg) ");

        String fileAddress = checkFileAddressInput();

        try {
            BufferedImage bImage = ImageIO.read(new File(fileAddress));

            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            ImageIO.write(bImage, "jpg", bos );

            byte[] serverPhoto  = bos.toByteArray();

            Request request = new Request(RequestType.UPDATE,ObjectRequested.SERVER,"server/changePhoto");

            request.setFile(serverPhoto);

            sendRequest(request);

            showResponse();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * @Author danial
     * choose user profile photo
     */
    public void choosePhotoForProfile() {
        System.out.println("Enter profile photo file address: (path: .jpg) ");

        String fileAddress = checkFileAddressInput();

        byte[] profilePhoto = new byte[0];

        try {
            BufferedImage bImage = ImageIO.read(new File(fileAddress));

            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            ImageIO.write(bImage, "jpg", bos );

            profilePhoto = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Request request = new Request(RequestType.POST,ObjectRequested.USER,"user/profile-photo");

        request.setFile(profilePhoto);

        sendRequest(request);

        showResponse();
    }

    /**
     * @Author danial
     * change user mail
     */
    public void changeMail() {
        String newMail = emailChecker();

        Request request = new Request(RequestType.UPDATE,ObjectRequested.USER,"user/change-mail");

        request.addContent("mail",newMail);

        sendRequest(request);

        showResponse();
    }

    /**
     * @Author danial
     * change user phone number
     */
    public void changePhoneNumber() {
        String newPhoneNumber = phoneChecker();

        Request request = new Request(RequestType.UPDATE,ObjectRequested.USER,"user/change-phonenumber");

        request.addContent("phonenumber",newPhoneNumber);

        sendRequest(request);

        showResponse();
    }

    /**
     * @Author danial
     * show user friend menu
     */
    public void showFriendsMenu() {
        System.out.println("1.Show Friends\n2.Pending friend requests\n3.Add friend\n4.Back");

        String index = checkInput(4,1);

        switch (index) {
            case "1":
                showFriends();
                break;
            case "2":
                pendingFriendRequest();
                break;
            case "3":
                addFriend();
                break;
            case "4":
                homeMenu();
                break;
        }
        showFriendsMenu();
        return;
    }

    /**
     * @Author danial
     * show friend request
     */
    public void addFriend() {
        System.out.println("Type username to send friend request: ");

        String userName = scanner.nextLine();

        Request request = new Request(RequestType.POST,ObjectRequested.USER,"user/send-friend-request");

        request.addContent("username",userName);

        sendRequest(request);

        showResponse();
    }


    public void serversMenu(){

        while (true){

            System.out.println("1.showServers\n2.serverJoinInvites\n3.Back");

            String index = checkInput(3,1);

            switch (index){

                case "1"->showServers();

                case "2"->serverJoinInvites();

            case "3"->startMenu();

            }

        }



    }

    public void inviteServer(){

        Flag flag;
        System.out.println("Please enter a userName to invite to server");

        do{


            String userName = scanner.nextLine();

            Request request = new Request(RequestType.POST,ObjectRequested.SERVER,"server/inviteToServer");

            request.addContent("username",userName);

            sendRequest(request);

            flag = responseHandler.getFlag();

            showResponse();


        }while (flag==Flag.NotSuccessful);



    }


    public void serverJoinInvites(){

        Request request = new Request(RequestType.GET,ObjectRequested.USER,"user/serverJoinInvites");

        sendRequest(request);

        showResponseWithIndex();

        String index = checkInput(responseTextSize()+1,1);

        int maxIndex = responseTextSize()+1;

        if(Integer.parseInt(index)==maxIndex)
            return;

        System.out.println("1.Accept\n2.Reject");

        String userChoose = scanner.nextLine();

        switch (userChoose){

            case "1"->{

                Request request1 = new Request(RequestType.GET,ObjectRequested.USER,"user/acceptServerJoinInvite");

                request1.addContent("index",index);

                sendRequest(request1);

                showResponse();
            }

            case "2"->{

                Request request1= new Request(RequestType.GET,ObjectRequested.USER,"user/rejectServerJoinInvite");

                request1.addContent("index",index);

                sendRequest(request1);

                showResponse();
            }

        }


    }

    /**
     * @Author danial
     * show friend request
     */
    public void pendingFriendRequest() {
        Request request = new Request(RequestType.GET,ObjectRequested.USER,"user/pending-friend-requests");

        sendRequest(request);

        showResponseWithIndex();

        String index = checkInput(responseTextSize()+1,1);

        int maxIndex = responseTextSize() + 1;

        if(Integer.parseInt(index) == maxIndex){
            return;
        }

        System.out.println("1.Accept\n2.Reject\n3.Back");

        String userChoose = scanner.nextLine();

        if(Integer.parseInt(userChoose) == 3) {
            pendingFriendRequest();
            return;
        }

        String userAction = "";

        switch (userChoose) {
            case "1":
                userAction = "accept";
                break;
            case "2":
                userAction = "reject";
                break;
        }

        request = new Request(RequestType.UPDATE,ObjectRequested.USER,"user/update-friend-request");

        request.addContent("index",index);

        request.addContent("action",userAction);

        sendRequest(request);

        showResponse();
    }

    /**
     * @Author danial
     * show friends list
     */
    public void showFriends() {
        sendRequest(new Request(RequestType.GET,ObjectRequested.USER,"user/friends"));

        showResponseWithIndex();

        String index = checkInput(responseTextSize()+1,1);

        int maxIndex = responseTextSize() + 1;

        if(maxIndex == Integer.parseInt(index)){
            return;
        }

        int temp = Integer.parseInt(index);

        temp -= 1;

        index = String.valueOf(temp);

        friendMenu(index);

    }

    /**
     * @Author danial
     * friend actions for user
     */
    public void friendMenu(String indexOfFriend) {
        System.out.println("1.Profile\n2.Message\n3.Remove friend\n4.Block/Unblock\n5.Back");

        String index = checkInput(5,1);

        switch (index) {
            case "1" :
                showFriendProfile(indexOfFriend);
                break;
            case "2":
                createPrivateChat(indexOfFriend);
                break;
            case "3":
                removeFriend(indexOfFriend);
                break;
            case "4":
                blockSelectedFriend(indexOfFriend);
                break;
            case "5":
                showFriendsMenu();
                return;
        }
        showFriendsMenu();
    }

    /**
     * @Author danial
     *  show friend profile
     */
    public void showFriendProfile(String index) {
        Request request = new Request(RequestType.GET,ObjectRequested.USER,"user/friend/profile");

        request.addContent("indexOfFriend",index);

        sendRequest(request);

        if(responseHandler.getFlag() == Flag.NotSuccessful) {
            showResponse();
            return;
        }

        showResponse();
    }

    /**
     * @Author danial
     *  remove friend from friend list
     */
    public void removeFriend(String index) {
        Request request = new Request(RequestType.DELETE,ObjectRequested.USER,"user/delete-friend");

        request.addContent("indexOfFriend",index);

        sendRequest(request);

        showResponse();
    }

    /**
     * @Author danial
     * block or unblock Selected Friend
     */
    public void blockSelectedFriend(String indexOfFriend) {
        Request request = new Request(RequestType.GET,ObjectRequested.USER,"user/check-block-status-friend");

        request.addContent("index", String.valueOf(indexOfFriend));

        sendRequest(request);

        Flag flag = responseHandler.getFlag();

        String userChoose;

        if(flag == Flag.Successful) {
            System.out.println("Are you want to unblock user:\n1.Yes\n2.No");

            userChoose = scanner.nextLine();

            switch (userChoose) {
                case "1":
                    request = new Request(RequestType.UPDATE,ObjectRequested.USER,"user/unblock-friend");

                    request.addContent("index", String.valueOf(indexOfFriend));

                    sendRequest(request);

                    showResponse();
                case "2":
                    friendMenu(indexOfFriend);
                    break;
            }
        }
        else if(flag == Flag.NotSuccessful) {
            System.out.println("Are you want to block user:\n1.Yes\n2.No");

            userChoose = scanner.nextLine();

            switch (userChoose) {
                case "1":
                    request = new Request(RequestType.UPDATE,ObjectRequested.USER,"user/block-friend");

                    request.addContent("index", String.valueOf(indexOfFriend));

                    sendRequest(request);

                    showResponse();

                    break;
                case "2":
                    friendMenu(indexOfFriend);
                    break;
            }
        }
    }

    /**
     * @Author danial
     * @return String
     * check email duplicate and check email string
     */
    public String emailChecker() {
        Flag flag = Flag.NotSuccessful;
        String email;

        do{
            String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                    "[a-zA-Z0-9_+&*-]+)*@" +
                    "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                    "A-Z]{2,7}$";

            Pattern mailPattern = Pattern.compile(emailRegex);
            System.out.println("Enter your email address (example : example@yahoo.com): ");
            email = scanner.nextLine();
            if(!mailPattern.matcher(email).matches()){
                System.err.println("wrong format for email");
                continue;
            }

            Request request = new Request(RequestType.GET,ObjectRequested.USER,"user/email/checkDuplicate");

            request.addContent("email",email);

            sendRequest(request);

            showResponse();

            flag = responseHandler.getFlag();

        }while (flag==Flag.NotSuccessful);


        return email;
    }

    /**
     * @Author danial
     * @return String
     * check phone number string
     */
    public String phoneChecker() {
        String phoneRegex = "(0/91)?[7-9][0-9]{9}";
        Pattern phonePattern = Pattern.compile(phoneRegex);

        System.out.println("Enter your phone number or for skip enter skip : (valid phone number -> 9999999998)");
        String phoneNumber = scanner.nextLine();
        Boolean checkPhone = phonePattern.matcher(phoneNumber).matches();

        while (checkPhone == false && !phoneNumber.equals("skip")) {
            System.err.println("The phone number you entered is incorrect!");
            phoneNumber = scanner.nextLine();
            checkPhone = phonePattern.matcher(phoneNumber).matches();
        }

        if(phoneNumber.equals("skip")) {
            phoneNumber = "";
        }

        return phoneNumber;
    }

    /**
     * @Author danial
     * @return String
     * check password string
     */
    public String passwordChecker() {
        String passwordRegex = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])\\w{8,}";
        Pattern phonePattern = Pattern.compile(passwordRegex);

        System.out.println("Enter your password (example : Df123456): ");
        String password = scanner.nextLine();
        boolean checkpassword = phonePattern.matcher(password).matches();

        while (!checkpassword) {
            System.err.println("The password you entered is incorrect!");
            password = scanner.nextLine();
            checkpassword = phonePattern.matcher(password).matches();
        }

        return password;
    }

    /**
     * @Author danial
     * @return String
     * check username duplicate and check username string
     */
    public String userNameChecker() {

        Flag flag = Flag.NotSuccessful;

        String userName;

        do{
            String userNameRegex = "(?=.*[a-z,A-Z])(?=.*[0-9])\\w{6,}";
            Pattern userNamePattern = Pattern.compile(userNameRegex);

            System.out.println("Enter your userName (Username must be unique and contain at least 6 letters and only English letters and numbers.): ");
             userName = scanner.nextLine();
             userName = userName.toLowerCase();
            if(!userNamePattern.matcher(userName).matches()){
                System.err.println("wrong format for username");
                continue;
            }

            Request request = new Request(RequestType.GET, ObjectRequested.USER,"user/username/checkDuplicate");

            request.addContent("username",userName);

            sendRequest(request);

            flag = responseHandler.getFlag();

            showResponse();


        }while (flag==Flag.NotSuccessful);

        return userName;
    }
}
