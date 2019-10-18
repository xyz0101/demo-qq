package clientToServer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import com.jenkin.model.User;

public class ClientSever {
	String outcome;
	//ObjectInputStream in =null;
	//Socket client = null;
	//ObjectOutputStream write = null;
	public ClientSever(){
	}
	public void sendmessage(){

	}
	public String regiser(User user){
		String id="";
		try {
			Socket client = new Socket("127.0.0.1",5020);
			System.out.println("客户端连接成功");
			ObjectOutputStream write = new ObjectOutputStream(client.getOutputStream());
			ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(client.getInputStream()));
			write.writeObject(user);
			write.flush();
			User uu = (User)in.readObject();
			id=uu.getUser_id();
			client.close();
			in.close();
			write.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(id);

		return id;

	}
	public List<User> Login(User user) {
		// TODO Auto-generated constructor stub
		Socket client=null;
		try {
			client = new Socket("127.0.0.1",5020);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("客户端连接成功");
		List<User> list = new ArrayList<User>();
		try {
			ObjectOutputStream   write = new ObjectOutputStream(client.getOutputStream());
			ObjectInputStream  in = new ObjectInputStream(new BufferedInputStream(client.getInputStream()));
			write.writeObject(user);
			write.flush();
			list = (List<User>) in.readObject();
			client.close();
			in.close();
			write.close();
		} catch (IOException e) {
			// TODO Auto-generated catch bloc
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
}
