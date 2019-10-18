package clientToServer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.xml.ws.handler.MessageContext.Scope;

import com.client.SingleChatClient;
import com.jenkin.model.Message;;
import clientview.ChatView;
import common.Conts;

public class Chatmanager {
	private SingleChatClient singleChatClient ;
	public Chatmanager(ChatView ct){
		this.chat =ct;
	}
	String line =null;
	ChatView chat = null;
	ObjectOutputStream write =null;
	static public boolean go = true;
	public void send(Message message){
		if(message!=null){
			try {
				write.writeObject(message);
				write.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void startchat(){
//		startBioChat();
		startNioChat();
	}

	private void startNioChat() {
//		Readthread read = new Readthread(null,chat);
//		read.start();

		SingleChatClient singleChatClient = new SingleChatClient(chat);
		String user_id = this.chat.destination.getUser_id();
		Conts.WINDOW_MAP.put(user_id,singleChatClient.getClientMessageDealer());
		setSingleChatClient(singleChatClient);
		System.out.println("启动NIO聊天服务器");
	}

	private void startBioChat() {
		Socket socket = null;
		try {
			socket = new Socket("127.0.0.1",8888);
			System.out.println("连接群聊服务器成功");
			write = new ObjectOutputStream(socket.getOutputStream());
			Readthread.go=true;
			Readthread read = new Readthread(socket,chat);
			read.start();
			while(go){
				Thread.sleep(1000);
			}
			read.go = false;
			socket.close();
			write.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public SingleChatClient getSingleChatClient() {
		return singleChatClient;
	}

	public void setSingleChatClient(SingleChatClient singleChatClient) {
		this.singleChatClient = singleChatClient;
	}
}
