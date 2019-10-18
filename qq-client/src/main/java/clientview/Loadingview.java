package clientview;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import clientToServer.Chatmanager;
import clientToServer.ClientSever;
import com.client.LoginRegisterClient;
import com.jenkin.model.User;
import common.MyButton;
import common.ResourceManager;

public class Loadingview extends JFrame implements ActionListener{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	MyButton gg;
	background content =new background();
	String id,psw;
	public Loadingview(String id,String psw) {
		// TODO Auto-generated constructor stub
		this.id=id;
		this.psw=psw;
		setBounds(500,250,380,294);
		setUndecorated(true);
		setResizable(false);
		content.setLayout(null);
		{   //取消按钮
			gg =new MyButton();
			gg.setBounds(114, 252,159,34);
			gg.setContentAreaFilled(false);
			gg.setVisible(true);
			content.add(gg);
			gg.addActionListener(this);
		}
		content.addMouseMotionListener(new MouseMotionListener() {
			Point l=null;
			@Override
			public void mouseMoved(MouseEvent e) {
				l = new Point(e.getX(),e.getY());
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				setLocation(e.getXOnScreen()-l.x, e.getYOnScreen()-l.y);
			}
		});
		getContentPane().add(content) ;
		setVisible(true);
	}
	public void inspect(){
		User user =new User();
		user.setUser_id(id);
		user.setUser_pass(psw);
		user.setServertype("loading");
		// ClientSever verify=;
		try {
			List<User> list = nioLogin(user);
			//bioLogin(user);
			if (list != null) {

				this.dispose();
				for (int i = 0; i < list.size(); i++) {
					User temp = list.get(i);
					if (temp.getUser_id().equals(id)) {
						user = temp;
						list.remove(i);
						break;
					}
				}
				new MainView(user, list);

			}
		}catch (RuntimeException e){
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		}

	}

	private List<User> nioLogin(User user) throws RuntimeException{
		return new LoginRegisterClient(null).login(user);
	}

	private List<User> bioLogin(User user) {
		return new ClientSever().Login(user);
	}

	class background extends JPanel{

		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		@Override
		protected void paintComponent(Graphics g) {
			ResourceManager boss = new ResourceManager();
			ImageIcon image = new ImageIcon();
			image=boss.GetImage("login2");
			g.drawImage(image.getImage(),0,0,image.getIconWidth(),image.getIconHeight(),this);
		}

	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object k = e.getSource();
		if(k==gg){
			this.dispose();
			new LoginView();
		}
	}
}
