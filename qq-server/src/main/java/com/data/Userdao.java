package com.data;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.jenkin.model.User;

public class Userdao {

	public User getuser(String id, String psw, String sql) {
		User b = null;
		Connection con = Basefactory.getConnection();
		PreparedStatement pst;
		try {
			pst = con.prepareStatement(sql);
			pst.setString(1, id);
			pst.setString(2, psw);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				String user_id = null;
				user_id = rs.getString("user_id");
				String user_name = null;
				user_name = rs.getString("user_name");
			    String user_head = null;
				user_head = rs.getString("user_head");
				String user_state = null;
				user_state = rs.getString("user_state");
				String user_motoo = null;
				user_motoo = rs.getString("user_motoo");
				b = new User(user_id, user_name, user_head,
						 user_state, user_motoo);
				Basefactory.close(con, rs, pst);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return b;

	}
    public void changestate(String sql){
    	Connection con = Basefactory.getConnection();
    	PreparedStatement pst;
    	try {
			pst = con.prepareStatement(sql);
			pst.executeUpdate();
			Basefactory.close(con, null, pst);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
	public boolean adduser(String sql, User user) {
		boolean f = false;
		Connection con = Basefactory.getConnection();
		PreparedStatement pst;
		try {
			pst = con.prepareStatement(sql);
			pst.setString(1, user.getUser_name());
			pst.setString(2, user.getUser_pass());
            pst.setString(3, user.getUser_sex());
            Date date = user.getUser_birth();
            pst.setDate(4,date);
            pst.setString(5, user.getUser_edu());
            pst.setString(6, user.getUser_blood());
            pst.setString(7, user.getUser_location());
            pst.setString(8, user.getUser_phone());
            pst.setString(9, user.getUser_email());
            pst.setString(10, user.getUser_head());
            pst.setString(11, "0");
            if(pst.executeUpdate()>0)f=true;
            Basefactory.close(con, null, pst);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return f;
	}

	public List getlist(String sql,String id) {
		User b = null;
		List<User> list = new ArrayList<User>();
		Connection con = Basefactory.getConnection();
		PreparedStatement pst;
		try {
			pst = con.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				String user_id = null;
				user_id = rs.getString("user_id");
				String user_name = null;
				user_name = rs.getString("user_name");
				String user_head = null;
				user_head = rs.getString("user_head");
				String user_state = null;
				user_state = rs.getString("user_state");
				String user_motoo = null;
				user_motoo = rs.getString("user_motoo");
				b = new User(user_id, user_name, user_head,
						 user_state, user_motoo);
				list.add(b);
			}
			Basefactory.close(con, rs, pst);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return list;
	}
	public int getid(String sql,String pnb){
		int b = 0;
		Connection con = Basefactory.getConnection();
		PreparedStatement pst;
		try {
			pst = con.prepareStatement(sql);
			pst.setString(1, pnb);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				
				b=rs.getInt("user_id");
				Basefactory.close(con, rs, pst);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return b;
	}
}
