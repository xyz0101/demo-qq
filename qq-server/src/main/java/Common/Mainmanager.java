package Common;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.jenkin.model.User;

public class Mainmanager {
	public Mainmanager (){}
	private static  final Mainmanager manager=new Mainmanager();
	public static Mainmanager getmainmanager(){
		return manager;
	}
	Map<String, Socket> map = new HashMap<String,Socket>();
	public void transfer(String name,User user){
		Set<String> set = map.keySet();
		Socket socket = null;
		for(String str:set){
			try {
				socket = map.get(str);
				ObjectOutputStream write = null;
				if(str.compareTo(name)!=0){
					write = new ObjectOutputStream(socket.getOutputStream());
					write.writeObject(user);
					write.flush();
				}
				else{
					write = new ObjectOutputStream(socket.getOutputStream());
					User hehe = new User();
					hehe.setUser_id("pushed");
					write.writeObject(hehe);
					write.flush();
				}
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("用户 "+name+" 退出了客户端");
				map.remove(name);
				e.printStackTrace();
			}
		}
	}
	public void remove(String name){
		map.remove(name);
	}

	public void add(String name,Socket socket){
		map.put(name, socket);
	}
}
