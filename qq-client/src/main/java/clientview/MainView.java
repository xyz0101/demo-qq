package clientview;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.jenkin.Const;
import com.jenkin.model.Message;
import com.jenkin.model.User;
import clientToServer.Runchatthread;
import clientToServer.Runchatview;
import common.Conts;
import common.MyButton;

public class MainView extends JFrame {
	int x = 0;
	int y = 0;
	boolean chatback=true;
	Rectangle group = new Rectangle(245, 176, 87, 60);
	Rectangle chat = new Rectangle(137, 176, 87, 60);
	MyButton close = null;
	JScrollPane scrollPane = null;
	JScrollPane scrollPaneGroup = null;
	MyButton min = null;
	MyPaint userpaint = null;
	List<User> list = null;
	User user =null;
	JList contact = new JList();
	JList grouplist = new JList();
	public MainView(User user,List<User> list) {
		this.user=user;
		this.list=list;
		setLocationRelativeTo(null);
		x = 700;
		y = 10;
		setLocation(x, y);
		setSize(350, 737);
		setUndecorated(true);
		//联系人list

		DefaultListModel contactdlm = new DefaultListModel();
		//群聊list

		DefaultListModel groupdlm = new DefaultListModel();

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		Container c = getContentPane();
		c.setLayout(null);

		// 背景图片
		MyPaint p = new MyPaint("back", "");
		p.setLayout(null);
		p.setBounds(0, 0, 350, 737);
		MyMouse m = new MyMouse();
		p.addMouseMotionListener(m);
		p.addMouseListener(m);
		setLayout(null);
		// 当前登录用户头像
		MyPaint myHead = new MyPaint("myHead", user.getUser_head());
		myHead.setLayout(null);
		myHead.setBounds(13, 53, 80, 80);
		myHead.setOpaque(false);
		// 当前登录用户名
		System.out.println(user.getUser_name());
		MyPaint myName = new MyPaint("myName", user.getUser_name());
		myName.setLayout(null);
		myName.setBounds(103, 53, 50, 20);
		myName.setOpaque(false);
		// 当前登录用户个性签名
		JTextField jt = new JTextField(user.getUser_motoo());
		jt.setLayout(null);
		jt.setBounds(103, 83, 150, 20);
		jt.setOpaque(false);
		userpaint = new MyPaint("", "");
		userpaint.setLayout(null);
		userpaint.setBounds(0, 0, 350, 50);
		List<ImageIcon> icon = new ArrayList<ImageIcon>();
		List<JLabel> name = new ArrayList<JLabel>();
		List<ImageIcon> icong = new ArrayList<ImageIcon>();//群聊头像
		List<JLabel> nameg = new ArrayList<JLabel>();//群名
		List<JLabel> sig = new ArrayList<JLabel>();
		List<JLabel> statu = new ArrayList<JLabel>();
		//循环添加用户
		for (int i = 0; i < list.size(); i++) {
			User temp = list.get(i);
			String headName = temp.getUser_head() + ".png";
			System.out.println(headName);
 			// 用户头像
			ImageIcon head = new ImageIcon(Conts.HEAD.get(headName));
			// 用户昵称
			JLabel userName = new JLabel(temp.getUser_name()+"(" + temp.getUser_id() + ")");
			userName.setFont(new Font("宋体", Font.CENTER_BASELINE, 18));
			userName.setBounds(60, 5, 200, 20);
			// 用户个性签名
			JLabel userSig = new JLabel(temp.getUser_motoo());
			userSig.setBounds(60, 28, 200, 18);
			// 用户状态
			String state = "";
			if(temp.getUser_state().equals("0"))state = "离线";
			else state = "在线";
			JLabel userStatu = new JLabel(state);
			userStatu.setBounds(280, 18, 40, 20);
			name.add(userName);
			sig.add(userSig);
			statu.add(userStatu);
			// userpaint.add(userAll);
			icon.add(head);
			//用户添加到listModel
			contactdlm.addElement(temp);
		}
		//更新联系人
		contact.setModel(contactdlm);
		MyCel mycel = new MyCel(icon, name, sig, statu);
		contact.setCellRenderer(mycel);
		contact.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e){
				if(e.getClickCount()==2){   //When double click JList
					JList k=(JList)e.getSource();
					User target = (User) contact.getModel().getElementAt(k.getSelectedIndex());
					if (!Conts.WINDOW_SET.contains(target.getUser_id())) {
						ChatView chatview = new ChatView(getuser(), target);
						Runchatthread runchat = new Runchatthread(chatview);
						runchat.start();
						chatview.newthread(runchat.getchat());
						chatview.enter();
						Conts.WINDOW_SET.add(target.getUser_id());
						String key = user.getUser_id()+"-"+target.getUser_id();
						Queue<Message> messages = Const.MESSAGE_QUEUE.get(key);
						if(messages==null){
							messages = new LinkedBlockingDeque<>();
							Const.MESSAGE_QUEUE.put(key,messages);
						}
					}

				}
			}
		});

		// 所有用户滚动框
		scrollPane = new JScrollPane();
		scrollPane.setViewportView(contact);
		scrollPane.setPreferredSize(new Dimension(200, 100));
		scrollPane.setOpaque(false);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setBounds(1, 224, 349, 440);
		//添加群聊
		{
			// 群头像
			ImageIcon head = new ImageIcon(Conts.HEAD.get(1 + ".png"));
			// 群名字
			JLabel userName = new JLabel("聊天室");
			userName.setFont(new Font("宋体", Font.CENTER_BASELINE, 18));
			userName.setBounds(60, 5, 200, 20);
			nameg.add(userName);
			icong.add(head);
			//群聊添加到listModel
			groupdlm.addElement("");
		}
		//更新群
		grouplist.setModel(groupdlm);
		MyCel groupcel = new MyCel(icong, nameg);
		grouplist.setCellRenderer(groupcel);
		grouplist.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseClicked(e);
				if(e.getClickCount()==2){
					Groupchatview gct=new Groupchatview(getuser(), getlist());
					Runchatthread runchat = new Runchatthread(gct);
					runchat.start();
					gct.newthread(runchat.getchat());
					gct.enter();
				}
			}

		});
		// 所有用户滚动框
		scrollPaneGroup = new JScrollPane();
		scrollPaneGroup.setViewportView(grouplist);
		scrollPaneGroup.setPreferredSize(new Dimension(200, 100));
		scrollPaneGroup.setOpaque(false);
		scrollPaneGroup
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPaneGroup
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPaneGroup.getViewport().setOpaque(false);
		scrollPaneGroup.setBounds(1, 224, 349, 440);
		scrollPaneGroup.setVisible(false);//最初不可见

		MyListener li = new MyListener();
		// 关闭窗口
		close = new MyButton();
		close.setBounds(321, 0, 28, 28);
		close.addActionListener(li);
		// 最小化窗口
		min = new MyButton();
		min.setBounds(288, 0, 28, 28);
		min.addActionListener(li);
		p.addMouseMotionListener(m);
		p.add(scrollPaneGroup);
		p.add(scrollPane);
		p.add(myName);
		p.add(jt);
		p.add(myHead);
		p.add(min);
		p.add(close);
		c.add(p);
		setVisible(true);
	}
	public User getuser(){
		return this.user;
	}
	public List<User> getlist(){
		return this.list;
	}
	//对list元素进行重写
	class MyCel extends JLabel implements ListCellRenderer {
		List<ImageIcon> icons;
		List<JLabel> name;
		List<JLabel> sig;
		List<JLabel> statu;
		public MyCel() {
		};
		public MyCel(List<ImageIcon> icons, List<JLabel> name) {
			this.icons = icons;
			this.name = name;
		};
		public MyCel(List<ImageIcon> icons, List<JLabel> name, List<JLabel> sig, List<JLabel> statu) {
			this.icons = icons;
			this.name = name;
			this.sig = sig;
			this.statu = statu;
		}

		@Override
		public Component getListCellRendererComponent(JList list, Object value,
													  int index, boolean isSelected, boolean cellHasFocus) {
			String name1="";
			String sig1="";
			String statu1="";

			setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));//加入宽度为5的空白边框
			if(name!=null&&name.size()>0)
				name1=name.get(index).getText();

			if(sig!=null&&sig.size()>0)
				sig1=sig.get(index).getText();

			if(statu!=null&&statu.size()>0)
				statu1=statu.get(index).getText();
			if (isSelected) {
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			} else {
				setBackground(new Color(249,244,238));
				setForeground(list.getForeground());
			}
			setText("<html>"+"<span>"+name1+"</span>"+"&nbsp&nbsp&nbsp"
					+ "&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp"
					+ "&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp"
					+ "&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp"
					+ "&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp"
					+ "&nbsp&nbsp&nbsp&nbsp&nbsp"+"<br>  "+sig1+"&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp"
					+ "&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp"
					+ "&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp"+statu1+"</html>");
			if(icons!=null&&icons.size()>0)
				setIcon(icons.get(index));// 设置图片
			setEnabled(list.isEnabled());
			setFont(list.getFont());
			setOpaque(true);
			return this;

		}

	}

	class MyListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			MyButton b = (MyButton) e.getSource();
			if (b == close) {
				System.exit(0);
			} else if (b == min) {
				setExtendedState(JFrame.ICONIFIED);
			}
		}

	}

	class MyPaint extends JPanel {
		String type = "";
		String content = "";
		public MyPaint() {

		}

		public MyPaint(String type, String content) {
			this.type = type;
			if(content==null)this.content="";
			else
				this.content = content;
		}
		@Override
		protected void paintComponent(Graphics g) {
			// TODO Auto-generated method stub
			super.paintComponent(g);
			if (type.equals("myName")) {
				drawMyName(g);
			} else if (type.equals("myHead")) {
				drawMyHead(g);
			}
			if(chatback==false&&type.equals("back")){
				drawGroup(g);
				repaint();
				scrollPaneGroup.setVisible(true);
				scrollPane.setVisible(false);
			}else if(chatback==true&&type.equals("back")){
				drawMain(g);
				scrollPaneGroup.setVisible(false);
				scrollPane.setVisible(true);
				repaint();
			}

		}

		private void drawGroup(Graphics g) {
			BufferedImage main = Conts.IMAGE.get("group.png");
			g.drawImage(main, 0, 0, main.getWidth(), main.getHeight(), this);

		}

		private void drawMyName(Graphics g) {

			String name = this.content;
			Font f = new Font("宋体", Font.BOLD, 15);
			g.setFont(f);
			g.setColor(Color.WHITE);
			g.drawString(name, 0, 16);

		}



		private void drawSig(Graphics g) {
			String sig = this.content;
			Font f = new Font("微软雅黑", 0, 13);
			g.setFont(f);
			g.setColor(Color.gray);
			g.drawString(sig, 10, 16);
		}



		private void drawMyHead(Graphics g) {
			BufferedImage head = Conts.HEAD.get(content+".gif");
			g.drawImage(head, 0, 0, 80, 80, this);

		}


		private void drawMain(Graphics g) {
			BufferedImage main = Conts.CHAT.get("main.png");
			g.drawImage(main, 0, 0, main.getWidth(), main.getHeight(), this);

		}

	}

	class MyMouse extends MouseAdapter {
		int px = 0;
		int py = 0;

		@Override
		public void mouseClicked(MouseEvent e) {
			if(group.contains( e.getPoint())){
				chatback=false;
				repaint();
			}else if(chat.contains( e.getPoint())){
				chatback=true;
				repaint();
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			px = e.getX();
			py = e.getY();
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			setLocation(e.getXOnScreen() - px, e.getYOnScreen() - py);
			// System.out.println(x);
		}

	}



}
