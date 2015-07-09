package com.example.wasabi.common;

import java.util.HashMap;
import java.util.Map;

import android.util.Log;

public class Message {
	private static final String TAG = Message.class.getName();
	
	private String idMessage;
	private String sender;
	private String action;
	private String receiver;
	private String text;
	private int profilePictureId;
	
	public Message(String fullString){
		Log.d(TAG, "Nova mensagem:" + fullString);
		String [] tokens = fullString.split("[|]");
		Map<String, String> map = new HashMap<String, String>();
    	for (String token : tokens) {
    		String [] keyValue = token.split("=");
    		map.put(keyValue[0], keyValue[1]);
		}
    	this.idMessage = map.get("id");
    	this.sender = map.get("f");
    	this.receiver = map.get("t");
    	this.action = map.get("a");
    	this.text = map.get("m");
//    	Log.d("Nova Message", this.sender + "-" + this.action + "-" + this.receiver + "-" + this.text);
	}
	
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public int getProfilePictureId() {
		return profilePictureId;
	}

	public void setProfilePictureId(int profilePictureId) {
		this.profilePictureId = profilePictureId;
	}

	public String getIdMessage() {
		return idMessage;
	}

	public void setIdMessage(String idMessage) {
		this.idMessage = idMessage;
	}
}
