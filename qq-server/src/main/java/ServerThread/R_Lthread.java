package ServerThread;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.jenkin.model.User;
import SocketServer.ServerView;

import com.data.Userdao;

public class R_Lthread extends Thread {
	Socket socket = null;
	ServerView view = null;
	public R_Lthread(Socket socket,ServerView view ) {
		this.socket = socket;
		this.view = view;
	}

	//ObjectInputStream in = null;
	//ObjectOutputStream write = null;

	@Override
	public void run() {
		System.out.println("线程启动成功");
		doBioLogin();
//		doNioLogin();

	}

	private void doNioLogin() {

	}

	private void doBioLogin() {
		try {
			ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
			ObjectOutputStream	write = new ObjectOutputStream(socket.getOutputStream());
			Object obj = in.readObject();
			if (obj instanceof User) {
				Is_User((User) obj,in,write);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void Is_User(User user,ObjectInputStream in,ObjectOutputStream write) {
		// TODO Auto-generated method stub
		Userdao u =new Userdao();
		if (user.getServertype().equals("loading")) {

			String id = user.getUser_id();
			String psw = user.getUser_pass();
			System.out.println("账号:" + id + "\n密码:" + psw);
			String sql = "select *from user_main where user_id=? and user_pass=?";
			String sql2 = "select *from user_main";
			String sql3 = "update user_main set user_state = '1' where user_id =  '"+id+"'";
			try {
				user =u.getuser(id, psw, sql);
				List<User> list = null;
				if(user!=null){
					list = new ArrayList<User>();
					u.changestate(sql3);
					list = u.getlist(sql2,user.getUser_id());
					view.changetext("用户 "+user.getUser_name()+" 登录成功");
					view.addonlineuser(user);
				}
				write.writeObject(list);
				write.flush();
				socket.close();
				in.close();
				write.close();
			}  catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		else if (user.getServertype().equals("register")){
			view.changetext("一名新用户正在注册");
			String sql1="select user_id from user_main where user_phone=?";
			User uu = new User();
			if(u.getid(sql1, user.getUser_phone())==0){
				String sql = "insert into user_main (user_name,user_pass,user_sex,user_birth,user_edu,user_blood,user_location,user_phone,user_email,user_head,user_state) values(?,?,?,?,?,?,?,?,?,?,?)";
				u.adduser(sql, user);
				sql="select user_id from user_main where user_phone=?";
				int id=u.getid(sql, user.getUser_phone());
				user.setUser_id(String.valueOf(id));
				try {
					write.writeObject(user);
					write.flush();
					socket.close();
					in.close();
					write.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else {
				uu.setUser_id("手机号已被占用，请重试");
				try {
					write.writeObject(uu);
					write.flush();
					socket.close();
					in.close();
					write.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	}
}
