package Common;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Vector;

import SocketServer.ServerView;
import com.jenkin.model.Message;;
import com.jenkin.model.User;

public class Groupchatmanager {

	Groupchatmanager() {
	}

	private static final Groupchatmanager manager = new Groupchatmanager();

	public static Groupchatmanager getmanager() {
		return manager;
	}

	Vector<Socket> vector = new Vector<Socket>();

	public void transfer(Socket socket, Message message) {
		for (int i = 0; i < vector.size(); i++) {
			Socket gg = vector.get(i);
			if (!gg.equals(socket)) {
				try {
					ObjectOutputStream write = new ObjectOutputStream(
							gg.getOutputStream());
					write.writeObject(message);
					write.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("断开了一个客户端链接");
					Groupchatmanager.getmanager().remove(gg);
					e.printStackTrace();
				}
			}

		}
	}
	public boolean check(Socket socket){
		for (int i = 0; i < vector.size(); i++){
			Socket gg = vector.get(i);
			if(gg.equals(socket)){
				return true;
			}
		}
		return false;
	}
	public void add(Socket socket) {
		vector.add(socket);
	}
	public void remove(Socket socket){
		vector.remove(socket);
	}
	public void remove(Socket socket,ServerView view,String user) {
		vector.remove(socket);
		view.changetext("用户 "+user+" 退出群聊");
	}

}
