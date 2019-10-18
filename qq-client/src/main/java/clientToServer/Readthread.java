package clientToServer;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import javax.xml.ws.handler.MessageContext.Scope;

import com.client.SingleChatClient;
import com.jenkin.model.Message;;
import clientview.ChatView;

public class Readthread extends Thread{
       Socket socket =null;
       ChatView chat = null;
       public Readthread(Socket socket,ChatView chat){
    	   this.socket = socket;
    	   this.chat = chat;
       }
       ObjectInputStream in = null;
       static public boolean go = true;
	@Override
	public void run() {
		//doBioChat();
	   doNioChat();
	}

	private void doNioChat() {
		 new SingleChatClient(chat);
	}

	private void doBioChat() {
		try {

			while(go){
				in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
				if(in!=null){
					Object obj = in.readObject();
					chat.showmessage((Message)obj);
				}
				Thread.sleep(1000);
			}
			in.close();
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

}
