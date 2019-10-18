package com.jenkin.model;

import java.io.Serializable;

public class MyPic implements Serializable{
      private String picid;
      private int index;
	public String getPicid() {
		return picid;
	}
	public void setPicid(String picid) {
		this.picid = picid;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public MyPic() {
		
	}
	public MyPic(String picid, int index) {
		super();
		this.picid = picid;
		this.index = index;
	}
      
}
