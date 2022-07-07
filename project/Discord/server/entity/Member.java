package project.Discord.server.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

public class Member extends User  {

    private HashSet<Role> roles;

    private HashSet<ChannelPermission> channelPermissions;

    private HashSet<ServerPermission> serverPermissions;

    private ArrayList<Channel> channels;

    public Member(String userName, String passWord, String email, String phoneNumber) {
        super(userName, passWord, email, phoneNumber);

        channels = new ArrayList<>();

        roles = new HashSet<>();

        channelPermissions = new HashSet<>();

        serverPermissions = new HashSet<>();
    }


    public boolean checkExistOfRole(Role r){

        for (Role role:roles
             ) {
            if(r.equals(role)){

                return true;

            }
        }

        return false;

    }


    public void setChannels(ArrayList<Channel> channels) {
        this.channels = channels;
    }

    public boolean isServerPermissionExist(ServerPermission serverPermission){

        for (ServerPermission sp:serverPermissions
             ) {
            if(sp==serverPermission)
                return true;
        }
        return false;
    }

    public ArrayList<Channel> getChannels() {
        return channels;
    }

    public Channel getChannelByIndex(int index){

        return channels.get(index-1);

    }

    public void addRole(Role role){

        roles.add(role);

       addChannelPermissions(role.getChannelPermissions());

        serverPermissions.addAll(role.getServerPermissions());

    }

    public void removeRole(Role role){

        roles.remove(role);

        removeChannelPermissions(role.getChannelPermissions());

        removeServerPermissions(role.getServerPermissions());


    }

    public void removeServerPermissions(HashSet<ServerPermission> serverPermissions){

        this.serverPermissions.removeAll(serverPermissions);

    }


    public void addServerPermissions(ServerPermission SP){

        serverPermissions.add(SP);

    }

    public void addChannelPermission(ChannelPermission CP){

        channelPermissions.add(CP);

        for (Channel c:channels
             ) {

            c.addPermission(this,CP);

        }

    }

    public void removeServerPermission(ServerPermission SP){

        serverPermissions.remove(SP);

    }

    public void addChannelPermissions(HashSet<ChannelPermission > CPS){


        channelPermissions.addAll(CPS);

        for(ChannelPermission CP : CPS){

            for (Channel c:channels
            ) {
                c.addPermission(this,CP);
            }

        }

    }

    public void removeChannelPermissions(HashSet<ChannelPermission> CPS){

        channelPermissions.removeAll(CPS);

        for(ChannelPermission CP : CPS){

            for (Channel c:channels
                 ) {
                c.removePermission(this,CP);
            }

        }

    }

    public void removeChannelPermission(ChannelPermission CP){

        channelPermissions.remove(CP);

        for (Channel c:channels
             ) {
                c.removePermission(this,CP);
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(roles, member.roles) && Objects.equals(channelPermissions, member.channelPermissions) && Objects.equals(serverPermissions, member.serverPermissions) && Objects.equals(channels, member.channels);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roles, channelPermissions, serverPermissions, channels);
    }

    public HashSet<ChannelPermission> getChannelPermissions() {

        return channelPermissions;

    }


    public HashSet<ServerPermission> getServerPermissions() {
        return serverPermissions;
    }

    public String getRoles(){

        StringBuilder stringBuilder = new StringBuilder();

        for (Role r:roles
             ) {
            stringBuilder.append(r.getName()).append("   ");
        }

        return stringBuilder.toString();

    }

    public String status(){

        if(isActive()){

            if(getStatus()==null){

                return "Online";

            }
            else {

                return getStatus().toString();
            }
        }

        else {

            return "Offline";
        }
    }

    public String toString(){

        return String.format("%s   %s\nroles : %s",getUserName(),status(),getRoles());

    }
}
