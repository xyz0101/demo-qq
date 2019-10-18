package com.jenkin.model;


import java.io.Serializable;
import java.util.Vector;

public class Message implements Serializable{
	private String user_id;
	private String text;
	private String send_to;
	private String chat_model;
	private String user_name;
	private String msg_model;
	private Vector<MyPic> picarr;
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	public String getSend_to() {
		return send_to;
	}
	public void setSend_to(String send_to) {
		this.send_to = send_to;
	}
	public String getChat_model() {
		return chat_model;
	}
	public void setChat_model(String chat_model) {
		this.chat_model = chat_model;
	}
	
	public Vector<MyPic> getPicarr() {
		return picarr;
	}
	public void setPicarr(Vector<MyPic> picarr) {
		this.picarr = picarr;
	}
	
	public String getMsg_model() {
		return msg_model;
	}
	public void setMsg_model(String msg_model) {
		this.msg_model = msg_model;
	}
	public Message(String user_id, String text, String send_to,
			String chat_model, String user_name, Vector<MyPic> picarr) {
		super();
		this.user_id = user_id;
		this.text = text;
		this.send_to = send_to;
		this.chat_model = chat_model;
		this.user_name = user_name;
		this.picarr = picarr;
	}
	public Message (String chat_model){
		this.chat_model = chat_model;
	}
	public Message() {
		
	}
	
	
	
}
