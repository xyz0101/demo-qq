package ServerThread;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import Common.Mainmanager;
import com.jenkin.model.User;

public class MainThread extends Thread{
	Socket socket =null;
	public MainThread(Socket socket){
		this.socket=socket;
	}
	ObjectInputStream in = null;
	public void run(){
		try {
			in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
			while(true){
				Object obj = in.readObject();
				User user = (User)obj;
				Mainmanager.getmainmanager().transfer(user.getUser_id(), user);
				Mainmanager.getmainmanager().add(user.getUser_id(), socket);
				System.out.println("用户登录成功");
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
