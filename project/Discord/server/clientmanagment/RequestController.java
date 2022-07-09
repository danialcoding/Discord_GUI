package project.Discord.server.clientmanagment;

import project.Discord.server.entity.User;
import project.Discord.networkPortocol.ObjectRequested;
import project.Discord.networkPortocol.Request;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;


public class RequestController implements Runnable {

    private final ClientServices clientServices;

    private Request request;

    public void setRequest(Request r){

        request = r;

    }

    public RequestController(ObjectOutputStream out,Socket socket) {
        clientServices = new ClientServices(new ClientDataManagement(), new ClientController(out,socket));
    }


    public synchronized void Control(Request r) {

        switch (r.getRequestType()) {

            case GET -> GET(r);

            case POST -> POST(r);

            case DELETE -> DELETE(r);

            case UPDATE -> UPDATE(r);

        }

    }

    public synchronized void GET(Request r) {

        String endPoint = r.getEndPoint();

        ObjectRequested objectRequested = r.getObjectRequested();


        if (objectRequested == ObjectRequested.USER) {

            switch (endPoint) {

                case "user/singin" -> {
                    HashMap<String, String> content = r.getContent();

                    String username = content.get("username");

                    String password = content.get("password");

                    clientServices.SignIn(username, password);

                }

                case "user/specificChannelPermissionToAdd"->clientServices.showSpecificChannelPermissionToAdd(r.getContent().get("username"));

                case "user/specificChannelPermissionToRemove"->clientServices.showSpecificChannelPermissionToRemove(r.getContent().get("username"));

                case "user/checkSendMessage"->clientServices.checkForSendMessage();

                case "user/checkManageMessage"->clientServices.checkForManageMessage();

                case "user/username/checkDuplicate" -> clientServices.checkDuplicateUserName(r.getContent().get("username"));

                case "user/email/checkDuplicate" -> clientServices.checkDuplicateEmail(r.getContent().get("email"));

                case "user/friends" -> clientServices.showFriendListWithStatus();

                case "user/friends-username" -> clientServices.getFriendsUserName();

                case "user/friends-status" -> clientServices.getFriendsStatus();

                case "user/friend/profile" -> clientServices.showProfileFriendByIndex(Integer.parseInt(r.getContent().get("indexOfFriend")));

                case "user/servers" -> clientServices.showDiscordServerOfUser();

                //case "user/privateChats" -> clientServices.getUserPrivateChats();

                case "user/privateChat" -> clientServices.joinToPrivateChat(Integer.parseInt(r.getContent().get("index")));

                case "user/pending-friend-requests" -> clientServices.showFriendRequests();

                case "user/check-block-status-friend" -> clientServices.checkFriendwithuserblocklist(Integer.parseInt(r.getContent().get("index")));

                case "user/check-old-password" -> clientServices.checkOldPassword(r.getContent().get("oldPass"));

                case "user/discord-server"->clientServices.enterServer(r.getContent().get("index"));

                case "user/showServerActions"->clientServices.showServerActions();

                case "user/serverJoinInvites"->clientServices.showServerJoinInvites();

                case "user/acceptServerJoinInvite"->
                        clientServices.acceptServerInvite(Integer.parseInt(r.getContent().get("index")));

                case "user/rejectServerJoinInvite"->
                        clientServices.rejectServerInvite(Integer.parseInt(r.getContent().get("index")));

                case "user/check-exist-create-privateChat" -> clientServices.checkExistPrivateChat(Integer.parseInt(r.getContent().get("index")));

                case "user/notifications" -> clientServices.showNotifications();

                case "user/download-file" -> clientServices.downloadFile(Integer.parseInt(r.getContent().get("downloadcode")));

                case "user/download-file-from-channel" -> clientServices.downloadFileFromChannel(Integer.parseInt(r.getContent().get("downloadcode")));

                case "server/photo" -> clientServices.getServerPhoto(Integer.parseInt(r.getContent().get("index")));

                case "user/photo" -> clientServices.getFriendPhoto(Integer.parseInt(r.getContent().get("index")));




                case "user/friend-request-obg" -> clientServices.getFriendRequestObject();

                case "user" -> clientServices.getUserObject();

                case "user/friends-obg" -> clientServices.getUserFriendsObject();

                case "user/servers-obg" -> clientServices.getUserServersObject();

                case "user/private-chat-obg" -> clientServices.getUserPrivateChats();

                case "user/get-selected-user" -> clientServices.getSelectedUser(r.getContent().get("username"));

                case "user/check-exist-create-privateChat-with-userName" -> clientServices.checkExistPrivateChatWithUserName(r.getContent().get("username"));
            }

        } else if (objectRequested == ObjectRequested.SERVER) {
            switch (r.getEndPoint()) {

                case "server/members"->clientServices.showMembersOfServer();

                case "server/channels"->clientServices.showChannelsOfServer();

                case "server/roles"->clientServices.showRoles();

                case "server/role/serverPermissions"->clientServices.showServerPermissionsOfRole(Integer.parseInt(r.getContent().get("index")));

                case "server/role/channelPermission"->clientServices.showChannelPermissionsOfRole(Integer.parseInt(r.getContent().get("index")));

                case "server/Role/validServerPermissions"->clientServices.showValidServerPermissionsToAdd(Integer.parseInt(r.getContent().get("index")));

                case "server/Role/validChannelPermission"->clientServices.showValidChannelPermissionToAdd(Integer.parseInt(r.getContent().get("index")));

                case "server/role/members"->clientServices.showMembersOfRole(Integer.parseInt(r.getContent().get("indexOfRole")));
        }



        } else if (objectRequested == ObjectRequested.CHANNEL) {

            switch (r.getEndPoint()) {
//

                case "channel/actions"-> clientServices.selectChannel(r.getContent().get("indexOfChannel"));

                case "channel/getMessages"-> clientServices.getMessage();

                case "channel/pin-messages" -> clientServices.showPinMessages();


            }
        }
        else if(objectRequested == ObjectRequested.CHAT) {
            switch (r.getEndPoint()) {
                case "chat/get-messages" -> clientServices.getChatMessages();

                case "chat/get-friend-messages" -> clientServices.getFriendMessage();
            }
        }

    }

    public synchronized void POST (Request r){
        String endPoint = r.getEndPoint();

        ObjectRequested objectRequested = r.getObjectRequested();

        if (objectRequested == ObjectRequested.USER) {

            switch (endPoint) {

                case "user/signup" -> {
                    HashMap<String, String> content = r.getContent();

                    User user = new User(content.get("username"),
                            content.get("password"),
                            content.get("email"),
                            content.get("phoneNumber"));
                    clientServices.SignUp(user);
                }

                case "user/addChannelPermissionForSpecificChannel"->clientServices.addChannelPermissionForSpecificChannel(r.getContent().get("username"), r.getContent().get("permission"));

                case "chat/sendMessage" -> clientServices.sendMessageToPrivateChat(r.getContent().get("msg"));

                case "user/send-friend-request" -> clientServices.sendFriendRequest(r.getContent().get("username"));

                case "user/profile-photo" -> clientServices.setUserProfilePhoto(r.getFile());

                case "user/open-private-chat" -> clientServices.openPrivateMessageWithOtherUser(r.getContent().get("username"));

                case "user/role/addRole"->clientServices.addRoleForMember(
                        Integer.parseInt(r.getContent().get("indexOfRole")),
                                r.getContent().get("indexOfRole"));

            }

        }


        else if(objectRequested==ObjectRequested.CHANNEL){

            switch (endPoint){


                case "channel/joinChannel"->clientServices.joinChat();

                case "channel/sendMessage"->clientServices.sendMessageToChannel(r.getContent().get("message"));

                case  "channel/addChannel"->clientServices.addChannel(r.getContent().get("channelName"));

            }
        }

        else if(objectRequested == ObjectRequested.SERVER){

            switch (endPoint){

                case "server/inviteToServer"->clientServices.inviteServer(r.getContent().get("username"));

                case "server/Role/addServerPermissionToRole"->clientServices.addServerPermission(r.getContent().get("permission"),Integer.parseInt(r.getContent().get("indexOfSelectedRole")));

                case "server/Role/addChannelPermissionToRole"->clientServices.addChannelPermission(r.getContent().get("permission"),
                        Integer.parseInt(r.getContent().get("indexOfSelectedRole")));

                case "server/role/addRole"->clientServices.createRole(r.getContent().get("nameOfRole"));

                case "server/create-server" -> clientServices.createNewServer(r.getContent().get("servername"),r.getFile());
            }
        }

        else if(objectRequested == ObjectRequested.CHAT) {
            switch (endPoint) {
                case "chat/sendMessage" -> clientServices.sendMessageToPrivateChat(r.getContent().get("msg"));




                case "chat/sendfilemessage" -> clientServices.sendFileMessageToPrivateChat(r.getContent().get("caption"),r.getContent().get("url"));

                case "chat/pin-message" -> clientServices.pinMessage(Integer.parseInt(r.getContent().get("indexofmessage")));

                case "chat/send-file-message-to-channel" -> clientServices.sendFileMessageToChannel(r.getContent().get("caption"),r.getContent().get("url"));
            }
        }
    }

    public synchronized void UPDATE (Request r){
        String endPoint = r.getEndPoint();

        ObjectRequested objectRequested = r.getObjectRequested();

        if (objectRequested == ObjectRequested.USER) {

            switch (endPoint) {

                case "user/change-password" -> clientServices.changePassword(r.getContent().get("password"));

                case "user/change-mail" -> clientServices.changeMail(r.getContent().get("mail"));

                case "user/change-phonenumber" -> clientServices.changePhoneNumber(r.getContent().get("phonenumber"));

                case "user/change-status" -> clientServices.changeUserStatus(r.getContent().get("status"));

                case "user/update-friend-request" -> clientServices.updateFriendRequestsList(Integer.parseInt(r.getContent().get("index")),r.getContent().get("action"));

                case "user/block-friend" -> clientServices.blockUser(Integer.parseInt(r.getContent().get("index")));

                case "user/unblock-friend" -> clientServices.unblockFriend(Integer.parseInt(r.getContent().get("index")));
            }
        }
        else if(objectRequested == ObjectRequested.CHAT) {

            switch (endPoint) {
                case "chat/exit-user" -> clientServices.exitFromChat();
            }


        }

        else if(objectRequested == ObjectRequested.SERVER) {
            switch (endPoint) {
                case "server/close-app" -> clientServices.closeApp();

                case "server/manageServer"->clientServices.manageServer();

                case "server/role/changeName"->clientServices.changeNameOfRole(Integer.parseInt(r.getContent().get("indexOfRole")),
                        r.getContent().get("newName"));

                case "server/changeName"->clientServices.changeNameOfServer(r.getContent().get("newName"));

                case  "server/changePhoto"->clientServices.setPhotoForServer(r.getFile());
            }
        }


        else if(objectRequested==ObjectRequested.CHANNEL){

            switch (endPoint){

                case "channel/leaveChat"->clientServices.leaveChat();
            }



        }
    }

    public synchronized void DELETE (Request r){

        String endPoint = r.getEndPoint();

        ObjectRequested objectRequested = r.getObjectRequested();

        if (objectRequested == ObjectRequested.USER) {

            switch (endPoint) {

                case "user/delete-friend" -> clientServices.removeFriend(Integer.parseInt(r.getContent().get("indexOfFriend")));

                case "user/removeChannelPermissionForSpecificChannel"->clientServices.removeChannelPermissionForSpecificChannel(r.getContent().get("username"), r.getContent().get("permission"));

            }

        }

        else if(objectRequested==ObjectRequested.SERVER){


            switch (endPoint){


                case "server/deleteRole"->clientServices.deleteRole(Integer.parseInt(r.getContent().get("index")));

                case "server/role/removeChannelPermission"->clientServices.removeChannelPermissionOfRole(r.getContent().get("selectedPermission"),
                        Integer.parseInt(r.getContent().get("index")));


                case "server/role/removeServerPermission"->clientServices.removeServerPermissionOfRole(r.getContent().get("selectedPermission"),
                        Integer.parseInt(r.getContent().get("index")));

                case "server/role/removeMember"->clientServices.removeRoleFromMember(Integer.parseInt(r.getContent().get("indexOfRole"))-1,
                        r.getContent().get("selectedMember"));

                case "server/kickMember"->clientServices.kickMember(r.getContent().get("username"));
            }

        }

        else if(objectRequested == ObjectRequested.CHANNEL){

            switch (endPoint){

                case "channel/removeChannel"->clientServices.removeChannel(Integer.parseInt(r.getContent().get("index")));

            }

        }

    }

    @Override
    public void run() {

        Control(request);

        ClientDataManagement.saveInformation();

    }
}