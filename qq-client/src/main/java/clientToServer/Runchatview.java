package clientToServer;

import clientview.ChatView;
import com.jenkin.model.User;

public class Runchatview extends Thread{
       User user_1,user_2;
       public Runchatview(User user1,User user2){
    	   this.user_1=user1;
    	   this.user_2=user2;
       }
       public void run(){
    	   new ChatView(user_1,user_2);
       }
}
