package project.Discord.server.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Notification implements Serializable {

    private String topic;

    private String text;

    private LocalDateTime sendTime;

    public Notification(String topic,String text) {
        this.topic = topic;
        this.text = text;
        this.sendTime = LocalDateTime.now();
    }

    public String getDateTime(){
        DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("dd/MM/yy - hh:mm:ss");

        return sendTime.format(customFormatter);
    }

    public String toString(){
        return  topic + ": (" + getDateTime() + ")\n" + text;
    }
}
