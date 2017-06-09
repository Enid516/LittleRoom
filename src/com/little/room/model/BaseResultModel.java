package com.little.room.model;

public class BaseResultModel {
	private String action;
	private int code;
	private String message;
	
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
