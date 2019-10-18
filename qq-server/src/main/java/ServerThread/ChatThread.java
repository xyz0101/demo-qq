package ServerThread;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import SocketServer.ServerView;
import com.jenkin.model.Message;;
import Common.Groupchatmanager;
import Common.Privatechatmanager;

public class ChatThread extends Thread{
	ServerView view = null;
	Socket socket=null;
	ObjectInputStream in = null;
	public ChatThread(Socket socket,ServerView view) {
		this.socket=socket;
		this.view = view;
	}
	Message msg=null;
	@Override
	public void run() {
		try {
			in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
			while(true){
				Object k = in.readObject();
				if(k!=null)
				{
					msg = (Message) k;
					if(msg.getChat_model().compareTo("group")==0){
						if(!Groupchatmanager.getmanager().check(socket)){
							Groupchatmanager.getmanager().add(socket);
							view.changetext("用户 "+msg.getUser_name()+" 进入群聊");
						}
						if(msg.getText().compareTo("")==0&&msg.getPicarr()==null){
							//do nothing
						}else
							Groupchatmanager.getmanager().transfer(socket, msg);
					}
					else{
						if(msg.getMsg_model().equals("1")){
							Privatechatmanager.getprivatemanager().add(msg.getUser_id()+msg.getSend_to(), socket);
							view.changetext("用户 "+msg.getUser_name()+"与"+msg.getSend_to()+" 私聊");
							System.out.println(Privatechatmanager.getprivatemanager().getsize());
							Privatechatmanager.getprivatemanager().showmap();
						}
						if(!msg.getMsg_model().equals("1"))
							Privatechatmanager.getprivatemanager().transfer(msg.getSend_to()+msg.getUser_id(),msg);
					}

				}

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("断开了一个客户端链接");
			if(msg.getChat_model().compareTo("group")==0)
				Groupchatmanager.getmanager().remove(socket,view,msg.getUser_name());
			Privatechatmanager.getprivatemanager().remove(msg.getUser_id());
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			Groupchatmanager.getmanager().remove(socket,view,msg.getUser_name());
			Privatechatmanager.getprivatemanager().remove(msg.getUser_id());
			e.printStackTrace();
		}
	}


}