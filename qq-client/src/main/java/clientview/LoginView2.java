package clientview;

import com.jenkin.Const;
import common.MyButton;
import common.ResourceManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class LoginView2 extends JFrame implements ActionListener,ItemListener{
	private JPasswordField password;
	private JTextField name;
	private Rectangle close =new Rectangle(399,0,30,28);
	private Rectangle minist=new Rectangle(370,0,28,28);
	private MyButton cancle,minn;
	private MyButton register;
	private MyButton login;
	private MyButton find,gg;
	private JCheckBox rem;
	private String imagename;
	private static String id="",mima="";
	int flag=0;


	LoginView2(){
		BackgroundPanel con = new BackgroundPanel();
		con.setLayout(null);
		setUndecorated(true);
		setResizable(false);
		setBounds(500, 250, 429, 330);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		{     //关闭和最小化按钮
			cancle=new MyButton();
			minn=new MyButton();
			cancle.setBounds(close);
			minn.setBounds(minist);
			cancle.addActionListener(this);
			minn.addActionListener(this);
			con.add(cancle);
			con.add(minn);
		}
		{   //注册按钮
			register =new MyButton();
			register.setBounds(328,195,63,26);
			register.addActionListener(this);
			con.add(register);
		}
		{   //登录按钮
			login = new MyButton();
			login.setBounds(133, 286,195,33);
			login.addActionListener(this);
			con.add(login);
		}
		{   //找回按钮
			find  = new MyButton();
			find.setBounds(328,226,63,26);
			find.addActionListener(this);
			con.add(find);
		}

		{   //记住密码
			rem=new JCheckBox();
			rem.setBounds(129, 257,18,18);
			rem.setSelected(true);
			rem.addActionListener(this);
			con.add(rem);
		}
		{   //对于账号输入框的设计
			name = new JTextField();
			name.setText(id);
			name.setOpaque(true);
			name.setBounds(133,195,194,30);

			name.addKeyListener(new KeyListener() {
				@Override
				public void keyTyped(KeyEvent e) {
					// TODO Auto-generated method stub
					int keychar=e.getKeyChar();
					if(keychar>=KeyEvent.VK_0&&keychar<=KeyEvent.VK_9){
						if(name.getText().length()==0&&keychar==KeyEvent.VK_0)
							e.consume();
					}
					else  e.consume();
				}

				@Override
				public void keyReleased(KeyEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void keyPressed(KeyEvent e) {
					// TODO Auto-generated method stub

				}
			});

			con.add(name);
		}

		{    //密码输入框
			password = new JPasswordField(mima);
			password.setBounds(133,223,194,30);
			password.setOpaque(true);
			con.add(password);
		}
		//添加拖动
		con.addMouseMotionListener(new MouseMotionListener() {
			Point l=null;
			@Override
			public void mouseMoved(MouseEvent e) {
				// TODO Auto-generated method stub
				l = new Point(e.getX(),e.getY());
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub
				setLocation(e.getXOnScreen()-l.x, e.getYOnScreen()-l.y);
			}
		});

		getContentPane().add(con);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
	}
	public static void main(String[] args){
		File file = new File(Const.CLIENT_BASE_DIR+"save/defult");
		FileReader f=null;
		BufferedReader br=null;
		try {
			f = new FileReader(file);
			br=new BufferedReader(f);
			id=br.readLine();
			mima=br.readLine();
			f.close();
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		new LoginView2();
	}




	class BackgroundPanel extends JPanel{

		/**
		 *
		 */
		private static final long serialVersionUID = 1L;
		BackgroundPanel(){
			imagename="login1";
		}
		public void setbk()
		{

		}
		public void paintComponent(Graphics g)
		{
			int x = 0,y = 0;

			//调用资源管理类中的加载图片资源方法，来加载背景图片
			ResourceManager imageResource = new ResourceManager();
			ImageIcon icon = new ImageIcon();
			icon = imageResource.GetImage(imagename);
			//绘制窗口
			g.drawImage(icon.getImage(),x,y,icon.getIconWidth(),icon.getIconHeight(),this);

		}

	}
	@SuppressWarnings("deprecation")
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

		Object k= e.getSource();

		if(k==cancle){
			System.exit(0);
		}
		if(k==minn){
			setExtendedState(JFrame.ICONIFIED);
		}
		if(k==register){
			this.dispose();
			new RegisterView();
		}
		if(k==login){
			if(name.getText().equals("")){
				JOptionPane.showMessageDialog(null, "账号不能为空!");
			}
			else if(password.getText().equals(""))
			{
				JOptionPane.showMessageDialog(null, "密码不能为空!");
			}
			else {

				this.dispose();
				new Loadingview(name.getText(),password.getText()).inspect();
			}
		}
		if(k==find){
			new Findcodeview();
		}
	}
	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		Object b=e.getSource();
		if(b==rem){
			if(rem.isSelected()){
				rem.setSelected(false);
			}
			else rem.setSelected(true);
		}
	}
}
