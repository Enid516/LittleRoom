package com.little.room.model;



/**
 * Created by Enid on 2017/6/8.
 */

public class ChatMessageModel{
  

	private String nickName;
    private String message;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }


    public String getMessage() {
        return message;
    }


    public void setMessage(String message) {
        this.message = message;
    }
}
