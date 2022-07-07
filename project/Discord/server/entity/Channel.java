package project.Discord.server.entity;

import project.Discord.server.observerpattern.Subscriber;
import project.Discord.networkPortocol.Response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Channel extends Chat implements Serializable {

    private String name;

    private ArrayList<Message> pinMessages;

    public ArrayList<Message> getSavedMessages() {
        return savedMessages;
    }

    private ArrayList<Message> savedMessages;

    public void saveMessages(){

        savedMessages = getMessages();

    }

    private final HashMap<Member, HashSet<ChannelPermission>> disablePermissions;


    public Channel(String name) {
        this.name = name;
        this.disablePermissions = new HashMap<>();
        this.pinMessages = new ArrayList<>();
    }

    public ArrayList<Message> getPinMessages() {
        return pinMessages;
    }

    public String getName() {
        return name;
    }

    public HashMap<Member, HashSet<ChannelPermission>> getDisablePermissions() {
        return disablePermissions;
    }

    public void addPermission(Member member,ChannelPermission CP) {

        disablePermissions.get(member).remove(CP);
    }


    public HashSet<ChannelPermission> getChannelPermissionsThatAMemberHas(Member m){

        HashSet<ChannelPermission> result = new HashSet<>();

        result.add(ChannelPermission.sendMessage);
        result.add(ChannelPermission.readMessageHistory);
        result.add(ChannelPermission.manageMessage);

        result.removeAll(disablePermissions.get(m));

        return  result;

    }

    public void removePermission(Member m, ChannelPermission CP){

        disablePermissions.get(m).add(CP);

    }


    public boolean checkPermission(Member member,ChannelPermission permission){

        if(disablePermissions.get(member)!=null){

            for (ChannelPermission p: disablePermissions.get(member)
            ) {

                if(p==permission)
                    return false;
            }

        }

        return true;

    }


    @Override
    public String toString(){
        return name;
    }


    public void loadMessages(Member member){

        Response response = new Response();

        if(checkPermission(member,ChannelPermission.readMessageHistory)){

            int indexOfMessage = 0;
            for (Message m: getMessages()) {
                if(m instanceof FileMessage) {
                    FileMessage fm = (FileMessage) m;

                    fm.setDownloadCode(indexOfMessage);

                    response.addText(fm.toString());
                }
                else {
                    m.setMessageCode(indexOfMessage);

                    response.addText(m.toStringInChannel());
                }
                ++indexOfMessage;
            }

        }
        else {

            response.addText("you don't have permission to read previous messages  :(");

        }

        notifySubscriber(member,response);

    }

}




