package project.Discord.client;

import project.Discord.server.clientmanagment.Flag;
import project.Discord.networkPortocol.Response;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.SocketException;
import java.util.ArrayList;

public class ResponseHandler implements Runnable {


    public void setInChat(boolean inChat) {
        this.inChat = inChat;
    }

    private boolean inChat;

    private Flag flag;

    private final ObjectInputStream in;

    public void setResponse(Response response) {
        this.response = response;
        if(response.getFlag() != null){
            flag = response.getFlag();
        }
    }

    private Response response;


    public Response getResponse() {
        return response;
    }

    public Flag getFlag() {
        return flag;
    }

    public void showResponse(){
        for (String text:response.getTexts()) {
            System.out.println(text);
        }
    }

    /**
     * @Author danial
     * @return int
     * get all response texts
     */
    public ArrayList<String> getAllTexts() {
        return response.getTexts();
    }

    /**
     * @return int
     * return response text array size
     * @Author danial
     */

    public void showResponseWithIndex() {

        int index = 1;
        if(response.getTexts().size() != 0 && !response.getTexts().get(0).equals("")) {
            for (String text:response.getTexts()) {
                System.out.println(index + "." + text);
                ++index;
            }
        }
        else {
            System.out.println("List is empty.");
        }

        System.out.println(index + ".Back");
    }


    /**
     * @return int
     * return response text array size
     * @Author danial
     */
    public int responseTextSize() {
        if (response.getTexts().size() != 0 && !response.getTexts().get(0).equals("")) {
            return response.getTexts().size();
        }
        return 0;
    }


    public ResponseHandler(ObjectInputStream in) {
        this.in = in;

    }

    /**
     * @Author danial
     * @return byte[]
     * response Get file
     *
     */
    public byte[] getFile() {
        return response.getFile();
    }


    @Override
    public void run() {

        while (true) {

            try {
                setResponse((Response) in.readObject());

                if(inChat){
                    showResponse();
                }

            } catch (EOFException | SocketException e) {
                break;
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public String getFirstResponse() {

        return response.getTexts().get(0);
    }
}
