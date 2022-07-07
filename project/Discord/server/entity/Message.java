package project.Discord.server.entity;

import java.io.Serializable;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message implements Serializable {

    private String sender;

    private String content;

    private LocalDateTime sendTime;

    private int messageCode;

    public Message(String sender, String content) {
        this.sender = sender;
        this.content = content;
        this.sendTime = LocalDateTime.now();
    }

    public String getDateTime(){

        DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("dd/MM/yy - hh:mm:ss");

        return sendTime.format(customFormatter);

    }

    public String getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getSendTime() {
        return sendTime;
    }

    public int getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(int messageCode) {
        this.messageCode = messageCode;
    }

    @Override
    public String toString(){
        return sender + ": (" + getDateTime() + ")\n" + content;
    }

    public String toStringInChannel(){
        return sender + ": (" + getDateTime() + ")\n(message code: " + messageCode + ")\n" + content;
    }
}
