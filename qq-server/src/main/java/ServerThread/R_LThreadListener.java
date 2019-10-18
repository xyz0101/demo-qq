package ServerThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import SocketServer.ServerView;
import com.server.LoginRegisterServer;

public class R_LThreadListener extends Thread{
	ServerView view = null;
	public R_LThreadListener(ServerView view){
		this.view = view;
	}
	@Override
	public void run() {
//		doBioLogin();
		doNioLogin();
	}

	private void doNioLogin() {
		new LoginRegisterServer(view);
	}

	private void doBioLogin() {
		ServerSocket server = null;
		try {
			server = new ServerSocket(5020);
			System.out.println("登录注册服务器启动成功");
			Socket socket;
			try {
				while (true) {
					socket = server.accept();
					R_Lthread thread = new R_Lthread(socket,view);
					thread.start();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}