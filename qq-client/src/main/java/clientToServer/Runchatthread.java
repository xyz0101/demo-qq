package clientToServer;

import clientview.ChatView;

public class Runchatthread extends Thread{
       Chatmanager gc = null;
       public Runchatthread(ChatView ct){
    	   Chatmanager.go=true;
    	   gc=new Chatmanager(ct);
       }
       public Chatmanager getchat(){
    	   return gc;
       }
       public void run(){
           System.out.println("启动聊天");
    	   gc.startchat();
       }
}
