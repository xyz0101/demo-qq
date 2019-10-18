package com.jenkin.model;

import java.io.Serializable;
import java.sql.Date;


public class User implements Serializable{
	
	private String user_id;
	private String user_name;
	private String user_pass;
	private String user_sex;
	private Date user_birth;
	private String user_edu;
	private String user_blood;
	private String user_location;
	private Date user_date;
	private String user_phone;
	private String user_email;
	private String user_head;
	private String user_state;
	private String Servertype;
	private String user_motoo;
	public User (){
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getUser_pass() {
		return user_pass;
	}
	public void setUser_pass(String user_pass) {
		this.user_pass = user_pass;
	}
	public String getUser_sex() {
		return user_sex;
	}
	public void setUser_sex(String user_sex) {
		this.user_sex = user_sex;
	}
	public Date getUser_birth() {
		return user_birth;
	}
	public void setUser_birth(Date user_birth) {
		this.user_birth = user_birth;
	}
	public String getUser_edu() {
		return user_edu;
	}
	public void setUser_edu(String user_edu) {
		this.user_edu = user_edu;
	}
	public String getUser_blood() {
		return user_blood;
	}
	public void setUser_blood(String user_blood) {
		this.user_blood = user_blood;
	}
	public String getUser_location() {
		return user_location;
	}
	public void setUser_location(String user_location) {
		this.user_location = user_location;
	}
	public Date getUser_date() {
		return user_date;
	}
	public void setUser_date(Date user_date) {
		this.user_date = user_date;
	}
	public String getUser_phone() {
		return user_phone;
	}
	public void setUser_phone(String user_phone) {
		this.user_phone = user_phone;
	}
	public String getUser_email() {
		return user_email;
	}
	public void setUser_email(String user_email) {
		this.user_email = user_email;
	}
	public String getUser_head() {
		return user_head;
	}
	public void setUser_head(String user_head) {
		this.user_head = user_head;
	}
	
	public String getServertype() {
		return Servertype;
	}
	public void setServertype(String servertype) {
		Servertype = servertype;
	}
	public String getUser_state() {
		return user_state;
	}
	public void setUser_state(String user_state) {
		this.user_state = user_state;
	}
	public String getUser_motoo() {
		return user_motoo;
	}
	public void setUser_motoo(String user_motoo) {
		this.user_motoo = user_motoo;
	}
	public User(String user_id, String user_name, String user_head,
			String user_state, String user_motoo) {
		super();
		this.user_id = user_id;
		this.user_name = user_name;
		this.user_head = user_head;
		this.user_state = user_state;
		this.user_motoo = user_motoo;
	}
	public User(String user_id, String user_name, String user_pass,
			String user_sex, Date user_birth, String user_edu,
			String user_blood, String user_location, Date user_date,
			String user_phone, String user_email, String user_head,
			String user_state, String servertype, String user_motoo) {
		super();
		this.user_id = user_id;
		this.user_name = user_name;
		this.user_pass = user_pass;
		this.user_sex = user_sex;
		this.user_birth = user_birth;
		this.user_edu = user_edu;
		this.user_blood = user_blood;
		this.user_location = user_location;
		this.user_date = user_date;
		this.user_phone = user_phone;
		this.user_email = user_email;
		this.user_head = user_head;
		this.user_state = user_state;
		Servertype = servertype;
		this.user_motoo = user_motoo;
	}
	
	

}
