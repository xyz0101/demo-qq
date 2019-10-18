package ServerThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

public class MainThreadListener extends Thread{

	public void run(){
		ServerSocket Mainserver = null;
		try {
			Mainserver = new ServerSocket(9999);
			System.out.println("主服务开启成功");

			while(true){
				Socket socket = Mainserver.accept();
				MainThread thread = new MainThread(socket);
				thread.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
