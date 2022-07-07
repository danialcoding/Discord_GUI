package project.Discord.networkPortocol;

import java.io.Serializable;
import java.util.HashMap;

public class Request implements Serializable {

    private final RequestType requestType;

    private final ObjectRequested objectRequested;

    private final String endPoint;

    private final HashMap<String,String> content; //for example "username"="amin" , "password"="1234"

    private byte[] file;


    public Request(RequestType requestType, ObjectRequested objectRequested, String endPoint) {
        this.requestType = requestType;
        this.objectRequested = objectRequested;
        this.endPoint = endPoint;
        content = new HashMap<>();

    }

    public void addContent(String key,String value) {

        content.put(key, value);

    }

    public ObjectRequested getObjectRequested() {
        return objectRequested;
    }

    public HashMap<String, String> getContent() {
        return content;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }
}


