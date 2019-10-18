package com.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Basetest {
	   public static void main(String[] args) {
		String sql="select * from user_main where user_id=10004";
	   Connection con = Basefactory.getConnection();
	   Statement pst;
	   
	try {
		  pst = con.createStatement();
		//pst = con.prepareStatement(sql);
		  //pst.setInt(1,10000);
		  // pst.setString(2, "00001");
		   ResultSet res=pst.executeQuery(sql);  
		   while(res.next()){
		System.out.println(1);
		int i = res.getInt(1);
		String s = res.getString(2);
		System.out.println(s+"00");
		System.out.println(i);
			   //   System.out.println(res.getString("user_name"));
		   }
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	   
	}
	    
	   
}
