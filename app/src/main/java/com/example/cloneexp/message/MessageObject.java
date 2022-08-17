package com.example.cloneexp.message;

import java.util.ArrayList;

public class MessageObject {
    String messageId,
            message,
             senderId;
    ArrayList<String> mediaUrlList;

    public MessageObject(String messageId, String message,String sender, ArrayList<String> mediaUrlList) {
        this.messageId = messageId;
        this.message = message;
        this.mediaUrlList=mediaUrlList;
        this.senderId=sender;
    }
    public ArrayList<String> getMediaUrlList(){
        return mediaUrlList;
    }


    public String getMessage() {
        return message;
    }


    public String getSenderId() {
        return senderId;
    }
}
