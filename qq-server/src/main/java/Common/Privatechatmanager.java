package Common;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import com.jenkin.model.Message;;

public class Privatechatmanager {
    Privatechatmanager(){}
    private static final Privatechatmanager manager = new Privatechatmanager();
    public static Privatechatmanager getprivatemanager(){
        return manager;
    }
    Map<String, Socket> map = new HashMap<String, Socket>();
    public void transfer(String id,Message message){
        Socket socket =map.get(id);

        if(socket!=null){
            try {
                System.out.println(id);
                ObjectOutputStream write = new ObjectOutputStream(socket.getOutputStream());
                write.writeObject(message);
                write.flush();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                System.out.println("断开了一个客户端链接");
                Privatechatmanager.getprivatemanager().remove(id);
                e.printStackTrace();
            }

        }
    }
    public boolean check(String ip){
        if(map.get(ip)!=null){
            return true;
        }
        return false;
    }
    public void add(String id , Socket socket){
        map.put(id, socket);
    }
    public void remove(String id){
        map.remove(id);
    }
    public int getsize(){
        return map.size();
    }
    public void showmap(){
        for(String str:map.keySet()){
            System.out.println(str);
        }
    }
}
