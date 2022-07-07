package project.Discord.server.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class DiscordServer implements Serializable {

    private ArrayList<Channel> channels;

    private ArrayList<Member> members;

    private String name;

   private transient byte[] serverPhoto;

    public ArrayList<Role> getDefinedRoles() {
        return definedRoles;
    }

    private ArrayList<Role> definedRoles;

    public DiscordServer(String name, byte[] serverPhoto) {
        this.name = name;
        this.serverPhoto = serverPhoto;
        this.havePhoto = true;
        this.channels = new ArrayList<>();
        this.members = new ArrayList<>();
        this.definedRoles = new ArrayList<>();
    }


    public void setName(String name) {
        this.name = name;
    }

    public void addMemberToServer(User user) {

        Member m = new Member(user.getUserName(), user.getPassWord(), user.getEmail(), user.getPhoneNumber());

        m.setChannels(channels);

        members.add(m);



    }


    public ArrayList<Member> getMembersOfRole(Role r){

        ArrayList<Member> result = new ArrayList<>();

        for (Member m:members
             ) {

            if(m.checkExistOfRole(r))
                result.add(m);
        }

        return result;
    }

    public void remove(Member member) {

        members.remove(member);

        member.removeFromServer(this);

        for (Channel c:channels
        ) {
            c.getDisablePermissions().remove(member);
        }

    }

    public void addOwner(User user) {

        Member m = new Member(user.getUserName(), user.getPassWord(), user.getEmail(), user.getPhoneNumber());

        Role owner = new Role("OWNER");

        owner.getServerPermissions().add(ServerPermission.manageServer);

        owner.getServerPermissions().add(ServerPermission.manageChannel);

        owner.getServerPermissions().add(ServerPermission.inviteToServer);

        owner.getServerPermissions().add(ServerPermission.manageRoles);

        owner.getServerPermissions().add(ServerPermission.kickMember);

        owner.getChannelPermissions().add(ChannelPermission.manageMessage);

        owner.getChannelPermissions().add(ChannelPermission.sendMessage);

        owner.getChannelPermissions().add(ChannelPermission.readMessageHistory);

        addRole(owner);

        m.addRole(owner);

        m.setChannels(channels);

        members.add(m);
    }

   private Boolean havePhoto;

   public byte[] getServerPhoto() {
      return serverPhoto;
   }

   public Boolean getHavePhoto() {
      return havePhoto;
   }

   public void setServerPhoto(byte[] serverPhoto) {
      this.serverPhoto = serverPhoto;
   }

   public void setHavePhoto(Boolean havePhoto) {
      this.havePhoto = havePhoto;
   }
    public void addRole(Role r){

        definedRoles.add(r);

    }

    public void removeRole(int index){

        Role r = definedRoles.get(index-1);

        definedRoles.remove(index-1);

        for (Member m:members
             ) {
            m.removeRole(r);
        }

    }

    public void addServerPermission(ServerPermission SP,Role r){

        for (Member m:members
             ) {

           if( m.checkExistOfRole(r)){

                m.addServerPermissions(SP);
            }


        }


    }

    public void addChannelPermission(ChannelPermission CP, Role r){

        for (Member m:members
             ) {

            if( m.checkExistOfRole(r)){

                m.addChannelPermission(CP);
            }


        }


    }

    public void removeServerPermission(ServerPermission SP,Role r){

        for (Member m:members
             ) {

            if(m.checkExistOfRole(r))
            m.removeServerPermission(SP);
        }

    }

    public void removeChannelPermission(ChannelPermission CP,Role r){

        for (Member m:members
             ) {


            if(m.checkExistOfRole(r))
                m.removeChannelPermission(CP);


        }

    }



    public Role getRoleByIndex(int index){

       return definedRoles.get(index-1);
    }

    public DiscordServer(String name) {
        this.name = name;

        members = new ArrayList<>();

        channels = new ArrayList<>();

        definedRoles = new ArrayList<>();

    }

    public ArrayList<Member> getActiveMembers() {


        ArrayList<Member> ActiveMembers = new ArrayList<>();

        for (Member m : members
        ) {
            if (m.isActive())
                ActiveMembers.add(m);

        }
        return ActiveMembers;
    }

    public ArrayList<Member> getInActiveMembers() {

        ArrayList<Member> inActiveMembers = new ArrayList<>();

        for (Member m : members
        ) {
            if (!m.isActive())
                inActiveMembers.add(m);

        }
        return inActiveMembers;
    }

    public Member getMemberByUserName(String userName) {

        for (Member member : members
        ) {
            if (member.getUserName().equals(userName)) {

                return member;

            }
        }
        return null;
    }


    public void addChannel(Channel channel){

        channels.add(channel);

    }

    public void removeChannel(int index){

        channels.remove(index-1);

    }


    @Override
    public String toString() {
        return name;
    }

    public ArrayList<Channel> getChannels() {
        return channels;
    }

    public String getName() {
        return name;
    }

}
