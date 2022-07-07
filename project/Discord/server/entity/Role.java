package project.Discord.server.entity;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Role implements Serializable {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;
    private HashSet<ServerPermission> serverPermissions;
    private HashSet<ChannelPermission> channelPermissions;

    public Role(String name) {
        this.name = name;
        serverPermissions = new HashSet<>();
        channelPermissions = new HashSet<>();
    }


    public void addChannelPermission(ChannelPermission CP){

        channelPermissions.add(CP);

    }

    public void addServerPermission(ServerPermission SP){

        serverPermissions.add(SP);

    }

    public void removeServerPermission(ServerPermission SP){

        serverPermissions.remove(SP);

    }


    public void removeChannelPermission(ChannelPermission CP){

        channelPermissions.remove(CP);
    }

    public void setChannelPermissions(HashSet<ChannelPermission> channelPermissions) {
        this.channelPermissions = channelPermissions;
    }

    public void setServerPermissions(HashSet<ServerPermission> serverPermissions) {
        this.serverPermissions = serverPermissions;
    }

    @Override
    public String toString(){

        return name;

    }

    public HashSet<ChannelPermission> getChannelPermissions() {
        return channelPermissions;
    }

    public HashSet<ServerPermission> getServerPermissions() {
        return serverPermissions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(name, role.name) && Objects.equals(serverPermissions, role.serverPermissions) && Objects.equals(channelPermissions, role.channelPermissions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, serverPermissions, channelPermissions);
    }

    public HashSet<ServerPermission> getServerPermissionsThatNotExist() {

        HashSet<ServerPermission> result = new HashSet<>();

        result.add(ServerPermission.manageRoles);
        result.add(ServerPermission.inviteToServer);
        result.add(ServerPermission.manageServer);
        result.add(ServerPermission.kickMember);
        result.add(ServerPermission.manageChannel);

        result.removeAll(serverPermissions);

        return result;

    }


    public HashSet<ChannelPermission> getChannelPermissionsThatNotExist() {

        HashSet<ChannelPermission> result = new HashSet<>();

        result.add(ChannelPermission.readMessageHistory);
        result.add(ChannelPermission.sendMessage);
        result.add(ChannelPermission.manageMessage);

        result.removeAll(channelPermissions);

        return result;

    }
}
