package ServerThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import SocketServer.ServerView;
import com.server.SingleChatServer;

public class ChatThreadListener extends Thread{
	ServerView view = null;
	public ChatThreadListener(ServerView view){
		this.view = view;
	}
	@Override
	public void run() {
		//doBioServer(view);
		doNioServer(view);
	}

	private void doNioServer(ServerView view) {
		System.out.println("聊天服务器开启成功");
		new SingleChatServer(view);

	}

	private void doBioServer(ServerView view) {
		ServerSocket chatserver = null;
		try {
			chatserver = new ServerSocket(8888);
			System.out.println("聊天服务器开启成功");
			Socket socket = null;

			while(true){
				socket	= chatserver.accept();
				System.out.println("有个玩意进来了");
				ChatThread cs = new ChatThread(socket,view);
				cs.start();

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
