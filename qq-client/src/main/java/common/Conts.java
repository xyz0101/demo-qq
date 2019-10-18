package common;

import com.client.ClientMessageDealer;
import com.jenkin.Const;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;



public class Conts {
	public static HashMap<String,BufferedImage> CHAT=new HashMap<String,BufferedImage>();
	public static HashMap<String,BufferedImage> HEAD=new HashMap<String,BufferedImage>();
	public static HashMap<String,BufferedImage> IMAGE=new HashMap<String,BufferedImage>();
	public static HashMap<String,File> AUDIOS=new HashMap<String,File>();
	public static final Set<String> WINDOW_SET = new HashSet<>();
	public static final ConcurrentHashMap<String, ClientMessageDealer> WINDOW_MAP = new ConcurrentHashMap<>();

	//静态代码块
	static{
		try {
			//URI uri=Conts.class.getResource("/image/Chat").toURI();
			//System.out.println(uri.toURL().toString());
			File f=new File(Const.CLIENT_BASE_DIR+"image/Chat");
			File[] fs=f.listFiles();
			for (File ft : fs) {
				if(ft.getName().toLowerCase().endsWith(".jpg")||ft.getName().toLowerCase().endsWith(".png")||ft.getName().toLowerCase().endsWith(".gif")){

					CHAT.put(ft.getName(), ImageIO.read(ft));

				}else if(ft.getName().toLowerCase().endsWith(".wav")){
					AUDIOS.put(ft.getName(), ft);
				}
			}
		}  catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	//静态代码块
	static{
		try {
			//URI uri=Conts.class.getResource("/image/Head").toURI();
			//	System.out.println(uri.toURL().toString());
			File f=new File(Const.CLIENT_BASE_DIR+"image/Head");
			File[] fs=f.listFiles();
			for (File ft : fs) {
				if(ft.getName().toLowerCase().endsWith(".jpg")||ft.getName().toLowerCase().endsWith(".png")||ft.getName().toLowerCase().endsWith(".gif")){
					System.out.println(ft.getName());
					HEAD.put(ft.getName(), ImageIO.read(ft));

				}
			}
		}  catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	static{
		try {
			//	URI uri=Conts.class.getResource("/image").toURI();
			//	System.out.println(uri.toURL().toString());
			File f=new File(Const.CLIENT_BASE_DIR+"image");
			File[] fs=f.listFiles();
			for (File ft : fs) {
				if(ft.getName().toLowerCase().endsWith(".jpg")||ft.getName().toLowerCase().endsWith(".png")||ft.getName().toLowerCase().endsWith(".gif")){

					IMAGE.put(ft.getName(), ImageIO.read(ft));

				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


}
