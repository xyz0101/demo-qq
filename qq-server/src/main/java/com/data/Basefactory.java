package com.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Basefactory {
	public static Connection getConnection(){
		String url="jdbc:mysql://119.29.175.198:3306/" +
				"QQClient?useunicode=true&characterEncoding=utf8";
		String driver="com.mysql.jdbc.Driver";
		String user="root";
		String password="950820";       //这个地方不能给你看
		Connection con = null;
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url,user,password);

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return con;
	}
	public static void close(Connection con,ResultSet res,java.sql.Statement st){

		try {
			if(con!=null){
				con.close();
			}
			if(res!=null){
				res.close();
			}
			if(st!=null){
				st.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

}
