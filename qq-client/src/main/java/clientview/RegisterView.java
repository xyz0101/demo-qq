package clientview;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import clientToServer.ClientSever;
import com.client.LoginRegisterClient;
import com.jenkin.Const;
import com.jenkin.model.User;
import common.ComboBoxRenderer;
import common.MyButton;
import common.ResourceManager;

public class RegisterView extends JFrame implements ActionListener{
	Backgroundpanel content =new Backgroundpanel();
	JTextField nickname;
	JTextField code;
	JTextField vcode;
	JTextField diploma;
	JTextField phone;
	JTextField Email;
	JTextField location;
	JRadioButton male,female;
	JComboBox year,month,day;
	JTextField blood;
	JComboBox star;
	JComboBox head;
	MyButton cancle;
	MyButton minn;
	MyButton register;
	Integer headstr;
	public  RegisterView(){
		setUndecorated(true);
		setResizable(false);
		setBounds(500, 50, 500, 524);
		content.setLayout(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		content.addMouseMotionListener(new MouseMotionListener() {
			Point l=null;
			@Override
			public void mouseMoved(MouseEvent e) {
				// TODO Auto-generated method stub
				l=new Point(e.getX(),e.getY());
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub
				setLocation(e.getXOnScreen()-l.x,e.getYOnScreen()-l.y);
			}
		});
		{   //关闭和最小化按钮
			cancle =new MyButton();
			cancle.setBounds(460,0,35,28);
			cancle.addActionListener(this);
			content.add(cancle);

			minn =new MyButton();
			minn.setBounds(425,0,35,28);
			minn.addActionListener(this);
			content.add(minn);
		}
		{ //注册按钮
			register =new MyButton();
			register.setBounds(223, 464,181,49);
			register.addActionListener(this);
			content.add(register);
		}
		/**********************************************************************/
		{  //昵称填写栏
			nickname = new JTextField();
			nickname.setBounds(230,75,200,30);
			nickname.setFont(new Font("楷体",2,22));
			content.add(nickname);
		}
		{  //密码输入栏
			code = new JTextField();
			code.setBounds(230,110,200,30);
			code.setFont(new Font("宋体",0,14));
			content.add(code);
		}
		{  //密码确认栏
			vcode = new JTextField();
			vcode.setBounds(230,145,200,30);
			vcode.setFont(new Font("宋体",0,14));
			content.add(vcode);
		}
		{   //性别
			male = new JRadioButton();
			female = new JRadioButton();
			male.setOpaque(false);
			female.setOpaque(false);
			male.setBounds(230,180,60,30);
			female.setBounds(305,180,60,30);
			male.addActionListener(this);
			female.addActionListener(this);
			content.add(male);
			content.add(female);
		}
		{   //生日
			year = new JComboBox();
			for(int i=2017;i>=1900;i--){
				year.addItem(i);
			}
			year.setBounds(230,215,70,30);
			year.setOpaque(false);
			year.addActionListener(this);
			year.setFont(new Font("楷体",0,14));
			month = new JComboBox();
			for(int i=12;i>=1;i--){
				month.addItem(i);
			}
			month.setBounds(310,215,55,30);
			month.setOpaque(false);
			month.setFont(new Font("楷体",0,14));
			month.addActionListener(this);
			day = new JComboBox();
			for(int i=1;i<32;i++){
				day.addItem(i);
			}
			day.setBounds(375,215,55,30);
			day.setOpaque(false);
			day.setFont(new Font("楷体",0,14));
			day.addActionListener(this);
			content.add(year);
			content.add(month);
			content.add(day);
		}
		{   //星座
			star= new JComboBox();
			star.addItem("白羊座(3.21-4.19)");
			star.addItem("金牛座(4.20-5.20)");
			star.addItem("双子座(5.21-6.21)");
			star.addItem("巨蟹座(6.21-7.22)");
			star.addItem("狮子座(7.23-8.22)");
			star.addItem("处女座(8.23-9.22)");
			star.addItem("天秤座(9.23-10.23)");
			star.addItem("天蝎座(10.24-11.22)");
			star.addItem("射手座(11.23-12.21)");
			star.addItem("魔蝎座(12.22-1.19)");
			star.addItem("水瓶座(1.20-2.18)");
			star.addItem("双鱼座(2.21-3.20)");
			star.setFont(new Font("楷体",0,14));
			star.setBounds(230,250,200,30);
			content.add(star);
		}
		{   //学历
			diploma = new JTextField();
			diploma.setBounds(230,285,200,30);
			diploma.setFont(new Font("楷体",0,14));
			content.add(diploma);
		}
		{   //血型
			blood = new JTextField();
			blood.setBounds(230,320,200,30);
			blood.setFont(new Font("楷体",0,14));
			content.add(blood);
		}
		{   //电话
			phone =new JTextField();
			phone.setBounds(230,355,200,30);
			phone.setFont(new Font("楷体",0,14));
			phone.addKeyListener(new KeyListener() {

				@Override
				public void keyTyped(KeyEvent e) {
					// TODO Auto-generated method stub
					if(e.getKeyChar()>=KeyEvent.VK_0&&e.getKeyChar()<=KeyEvent.VK_9){

					}
					else e.consume();
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
			content.add(phone);
		}
		{   //邮箱
			Email = new JTextField();
			Email.setBounds(230,390,200,30);
			Email.setFont(new Font("楷体",0,14));
			content.add(Email);
		}
		{   //地址
			location = new JTextField();
			location.setBounds(230,430,200,30);
			location.setFont(new Font("楷体",0,14));
			content.add(location);
		}
		{   //选择头像
			Map<String,ImageIcon> box = new  LinkedHashMap<String,ImageIcon>();
			for(int i=0;i<11;i++){
				String str= Const.CLIENT_BASE_DIR+"image/Head/"+i+".gif";
				box.put(str, new ImageIcon(str));
			}
			head =new JComboBox(box.keySet().toArray());
			head.setBounds(5, 150, 120, 100);
			head.setOpaque(false);
			ComboBoxRenderer renderner=new ComboBoxRenderer(box);
			head.setMaximumRowCount(3);
			head.setRenderer(renderner);
			content.add(head);
		}
		/*****************************************************************************/

		getContentPane().add(content);
		setVisible(true);
	}
	public static void main(String[] args) {
		//new RegisterView();
	}
	class Backgroundpanel extends JPanel{
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		Backgroundpanel(){

		}

		@Override
		protected void paintComponent(Graphics g) {
			ResourceManager boss =new ResourceManager();
			ImageIcon icon = new ImageIcon();
			icon =  boss.GetImage("registerview");
			g.drawImage(icon.getImage(), 0,0,icon.getIconWidth(),icon.getIconHeight(),this);
		}


	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object k =e.getSource();
		if(k==cancle){
			this.dispose();
			new LoginView();
		}
		if(k==minn){
			setExtendedState(JFrame.ICONIFIED);
		}
		if(k==register){
			User user = new User();
			user.setUser_name(nickname.getText());
			this.year.getSelectedItem();
			Date date =new Date((int)this.year.getSelectedItem()-1900, (int)this.month.getSelectedItem()-1, (int)this.day.getSelectedItem());
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			df.format(date);
			user.setUser_birth(date);
			user.setUser_blood(blood.getText());
			user.setUser_edu(diploma.getText());
			user.setUser_email(Email.getText());
			headstr=this.head.getSelectedIndex();
			user.setUser_head(headstr.toString());
			user.setUser_location(location.getText());
			user.setUser_pass(code.getText());
			user.setUser_phone(phone.getText());
			String sexx=null;
			if(male.isSelected())sexx="男";
			else sexx="女";
			user.setUser_sex(sexx);
			user.setServertype("register");
			String id=nioRegister(user);
//			bioRegister(user);
//			nioRegister(user);
			if(!id.equals("手机号已被占用，请重试"))
			{
				JOptionPane.showMessageDialog(null, "您的账号为"+id+"祝您使用愉快!");
				new LoginView();
			}
			else {

				JOptionPane.showMessageDialog(null, "手机号已被占用，请重试");
				new RegisterView();
			}
			this.dispose();
		}
		if(k==male){
			female.setSelected(false);
		}
		if(k==female){
			male.setSelected(false);
		}
	}

	private String nioRegister(User user) {
		User register = new LoginRegisterClient(null).register(user);
		return register==null?null:register.getUser_id();
	}


	private String bioRegister(User user) {
		ClientSever server =new ClientSever();
		return server.regiser(user);
	}
}
