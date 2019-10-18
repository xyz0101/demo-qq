package com.jenkin.model;


public class Answer {
     private String user_id;
     private String ans_1;
     private String ans_2;
     private String ans_3;
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getAns_1() {
		return ans_1;
	}
	public void setAns_1(String ans_1) {
		this.ans_1 = ans_1;
	}
	public String getAns_2() {
		return ans_2;
	}
	public void setAns_2(String ans_2) {
		this.ans_2 = ans_2;
	}
	public String getAns_3() {
		return ans_3;
	}
	public void setAns_3(String ans_3) {
		this.ans_3 = ans_3;
	}
	public Answer(String user_id, String ans_1, String ans_2, String ans_3) {
		super();
		this.user_id = user_id;
		this.ans_1 = ans_1;
		this.ans_2 = ans_2;
		this.ans_3 = ans_3;
	}
     
}
