package project.Discord.server.entity;

import java.net.URL;

public class FileMessage extends Message {
    private URL url;

    private int downloadCode;

    public FileMessage(String sender, String content,URL url) {
        super(sender, content);
        this.url = url;
    }

    public URL getUrl() {
        return url;
    }

    public int getDownloadCode() {
        return downloadCode;
    }

    public void setDownloadCode(int downloadCode) {
        this.downloadCode = downloadCode;
    }

    public String toString() {
        return getSender() + ": (" + getDateTime() + ")\nFile message (download code: " + downloadCode + ")\n" + getContent();
    }
}
