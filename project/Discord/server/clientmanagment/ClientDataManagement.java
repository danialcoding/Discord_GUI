package project.Discord.server.clientmanagment;

import project.Discord.server.entity.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ClientDataManagement implements CRUDFunctions {


    private static ArrayList<User> users;

    private static ArrayList<HashMap<Channel,ArrayList<Message>>> messagesOfChannels;


    public ClientDataManagement() {
        if(users==null)
        users = new ArrayList<>();
    }

    /**
     * @Author danial
     * @return  ArrayList<User>
     * return users arraylist
     */
    public ArrayList<User> getUsers() {
        return users;
    }

    public User getUserByUsername(String username){

        for (User user:users) {
            if(user.getUserName().equals(username))
                return user;
        }

        return null;
    }

    public String getEmail(String email){

        for (User user:users) {
            if(user.getEmail().equals(email))
                return user.getUserName();
        }
        return null;
    }


    /**
     * @Author amin
     * @param username
     * @param password
     * @return user
     * The purpose of this function is to match the username and password when logging in to the application
     */
    public User singInUser(String username,String password){

        for (User user:users) {
            if(user.getUserName().equals(username)&&user.getPassWord().equals(password))
                return user;
        }

        return null;
    }

    /**
     * @Author danial
     * @param userName
     * @return User
     * search user and return it
     */
    public User getUser(String userName) {
        for (User user:users) {
            if(user.getUserName().equals(userName))
                return user;
        }
        return null;
    }




    @Override
    public void delete() {

    }

    @Override
    public void update() {

    }

    @Override
    public void read() {

    }

    @Override
    public void crate() {

    }

    /**
     * @Author danial
     * save all users photo
     */
    public static void saveUsersProfilePhoto() {
        int index = 0;
        for (User user : users) {
            if(user.getHavePhoto()) {
                try {
                    String fileAddress = "project/Discord/server/save/users-photo/" + index + ".jpg";

                    byte[] data = user.getUserPhoto();

                    ByteArrayInputStream bis = new ByteArrayInputStream(data);

                    BufferedImage bImage = ImageIO.read(bis);

                    ImageIO.write(bImage, "jpg",new File(fileAddress));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            ++index;
        }
    }

    /**
     * @Author danial
     * save all servers photo
     */
    public static void saveDiscordServerPhoto() {
        int index = 0;
        for (User user : users) {
            for (DiscordServer server : user.getDiscordServers()) {
                if(server.getHavePhoto()) {
                    try {
                        String fileAddress = "project/Discord/server/save/servers-photo/" + index + ".jpg";

                        byte[] data = server.getServerPhoto();

                        ByteArrayInputStream bis = new ByteArrayInputStream(data);

                        BufferedImage bImage = ImageIO.read(bis);

                        ImageIO.write(bImage, "jpg",new File(fileAddress));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                ++index;
            }
        }
    }

    /**
     * @Author danial
     * save all private chats messsages
     */
    public static void savePrivateChatsMessages() {
        int index = 0;
        for (User user : users) {
            for (PrivateChat Pc:user.getChats()) {
                Pc.saveMessages();
            }
            ArrayList<PrivateChat> pChats = user.getChats();
            try(FileOutputStream fileOutputStream = new FileOutputStream("project/Discord/server/save/user-privatechats/" + index);
                ObjectOutputStream out = new ObjectOutputStream(fileOutputStream)) {
                out.writeObject(pChats);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            ++index;
        }
    }

    /**
     * @Author danial
     * save all channel messsages
     */
    public static void saveChannelMessages() {
        int uindex = 0;
        for (User user : users) {
            int sindex = 0;
            for (DiscordServer server : user.getDiscordServers()) {
                int index = 0;
                for (Channel channel : server.getChannels()) {

                    ArrayList<Message> messages = channel.getMessages();

                    try(FileOutputStream fileOutputStream = new FileOutputStream("project/Discord/server/save/channel-messages/" + uindex + sindex + index);
                        ObjectOutputStream out = new ObjectOutputStream(fileOutputStream)) {
                        out.writeObject(messages);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                    ++index;
                }
                ++sindex;
            }
            ++uindex;
        }
    }

    /**
     * @Author danial
     * load all channel messsages
     */
    public static void loadChannelMessages() {

        int uindex = 0;
        for (User user : users) {
            int sindex = 0;
            for (DiscordServer server : user.getDiscordServers()) {
                int index = 0;
                for (Channel channel : server.getChannels()) {

                    try(FileInputStream fileInputStream = new FileInputStream("project/Discord/server/save/channel-messages/" + uindex + sindex + index);
                        ObjectInputStream in = new ObjectInputStream(fileInputStream)) {

                        channel.setMessages((ArrayList<Message>)in.readObject());
                    }
                    catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    ++index;
                }
                ++sindex;
            }
            ++uindex;
        }
    }

    /**
     * @Author danial
     * load all private chats messsages
     */
    public static void loadPrivateChatsMessages() {
        int index = 0;
        for (User user : users) {
            try(FileInputStream fileInputStream = new FileInputStream("project/Discord/server/save/user-privatechats/" + index);
                ObjectInputStream in = new ObjectInputStream(fileInputStream)) {

                user.setChats((ArrayList<PrivateChat>)in.readObject());

            }
            catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            ++index;
        }
    }

    /**
     * @Author danial
     * load all users photo
     */
    public static void loadUsersProfilePhoto() {
        int index = 0;
        for (User user : users) {
            if(user.getHavePhoto()) {
                try {
                    String fileAddress = "project/Discord/server/save/users-photo/" + index + ".jpg";

                    BufferedImage bImage = ImageIO.read(new File(fileAddress));

                    ByteArrayOutputStream bos = new ByteArrayOutputStream();

                    ImageIO.write(bImage, "jpg", bos );

                    byte [] data = bos.toByteArray();

                    user.setUserPhoto(data);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            ++index;
        }
    }

    /**
     * @Author danial
     * load all servers photo
     */
    public static void loadDiscordServerPhoto() {
        int index = 0;
        for (User user : users) {
            for (DiscordServer server : user.getDiscordServers()) {
                if(server.getHavePhoto()) {
                    try {
                        String fileAddress = "project/Discord/server/save/servers-photo/" + index + ".jpg";

                        BufferedImage bImage = ImageIO.read(new File(fileAddress));

                        ByteArrayOutputStream bos = new ByteArrayOutputStream();

                        ImageIO.write(bImage, "jpg", bos );

                        byte [] data = bos.toByteArray();

                        server.setServerPhoto(data);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                ++index;
            }
        }
    }



    public static void  saveInformation(){

        saveUsersProfilePhoto();

        saveDiscordServerPhoto();

        savePrivateChatsMessages();

        saveChannelMessages();

        try(FileOutputStream fileOutputStream = new FileOutputStream("project/Discord/server/save/users");
            ObjectOutputStream out = new ObjectOutputStream(fileOutputStream)) {
            out.writeObject(users);
        }catch (ClassCastException ignored){

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadInformation(){
        try(FileInputStream fileInputStream = new FileInputStream("project/Discord/server/save/users");
        ObjectInputStream in = new ObjectInputStream(fileInputStream)) {

            users = (ArrayList<User>)in.readObject();

        }catch (ClassCastException | EOFException ignored){

        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        if(users!=null){

            loadUsersProfilePhoto();

            loadDiscordServerPhoto();

            loadPrivateChatsMessages();

            loadChannelMessages();
        }

    }

    public void addUser(User user) {
        users.add(user);
    }
}
