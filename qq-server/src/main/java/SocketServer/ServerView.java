package SocketServer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;

import com.jenkin.model.User;
import Common.DateTool;
import ServerThread.ChatThreadListener;
import ServerThread.MainThreadListener;
import ServerThread.R_LThreadListener;

public class ServerView extends JFrame {
	private static final ExecutorService executor = new ThreadPoolExecutor(10,100,
			60, TimeUnit.SECONDS,new ArrayBlockingQueue<>(200),new ThreadPoolExecutor.AbortPolicy());

	JTextArea serverdate = null;
	JList mylist = new JList<>();
	JButton begin =null;
	JButton end = null;
	DefaultListModel model = new DefaultListModel();
	User user = null;
	R_LThreadListener r_l  =  null;
	MainThreadListener main = null;
	ChatThreadListener chat = null;
	public ServerView(){

		setBounds(500, 300, 800, 500);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		JPanel content = new JPanel();
		content.setLayout(null);

		JLabel datetitle = new JLabel(" 服务器日志:");
		datetitle.setBounds(0, 0, 610, 30);
		datetitle.setFont(new Font("宋体", Font.BOLD, 20));
		datetitle.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		content.add(datetitle);

		serverdate = new JTextArea();
		serverdate.setBounds(0, 31, 610, 350);
		serverdate.setOpaque(true);
		serverdate.setEditable(false);
		serverdate.setLineWrap(true);        //激活自动换行功能
		serverdate.setWrapStyleWord(true);
		serverdate.setFont(new Font("宋体", ABORT, 15));
		JScrollPane jsp = new JScrollPane(serverdate);
		jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		jsp.setBounds(0, 31, 610, 350);
		content.add(jsp);

		JLabel onlineuser = new JLabel("在线用户列表:");
		onlineuser.setBounds(611, 0, 799, 30);
		onlineuser.setFont(new Font("宋体", Font.BOLD, 20));
		onlineuser.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		content.add(onlineuser);

		mylist.setBounds(611, 31, 799, 350);
		mylist.setModel(model);
		mylist.setCellRenderer(new MyCel());
		JScrollPane jsp1 = new JScrollPane(mylist);
		jsp1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		jsp1.setBounds(611, 31, 799, 350);
		content.add(jsp1);

		begin = new JButton("开启服务");
		begin.setBounds(100, 400, 100, 50);
		begin.addMouseListener(new mylistener());
		content.add(begin);

		end = new JButton("关闭服务");
		end.setBounds(410, 400, 100, 50);
		end.addMouseListener(new mylistener());
		content.add(end);

		getContentPane().add(content);
		setVisible(true);


	}
	public void changetext(String text){
		serverdate.append("--------------------------------------\n"+"处理时间:"+DateTool.f(new Date())+'\n'
				+text+'\n'+"--------------------------------------\n");
		serverdate.paintImmediately(serverdate.getBounds());
		serverdate.setCaretPosition(serverdate.getText().length());
	}
	public void addonlineuser(User user){
		model.addElement(user);
		mylist.setModel(model);
	}
	public void removeonlineuser(User user){
		model.removeElement(user);
		mylist.setModel(model);
	}
	public ServerView getview(){
		return this;
	}
	class mylistener extends MouseAdapter{

		@Override
		public void mousePressed(MouseEvent e) {
			Object k = e.getSource();
			if(k==begin){
				R_LThreadListener r_l  =  new R_LThreadListener(getview());
//				MainThreadListener main = new MainThreadListener();
				ChatThreadListener chat = new ChatThreadListener(getview());
				r_l.start();
//				main.start();
				chat.start();
				changetext("登录注册服务器启动成功");
				//changetext("主服务开启成功");
				changetext("聊天服务器开启成功");
			}
			else if(k == end){
				System.exit(0);
			}
		}
	}
	class MyCel extends JLabel implements ListCellRenderer {

		public MyCel() {
		};

		@Override
		public Component getListCellRendererComponent(JList list, Object value,
													  int index, boolean isSelected, boolean cellHasFocus) {
			User user = (User)value;
			setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));//加入宽度为5的空白边框

			if (isSelected) {
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			} else {
				setBackground(new Color(249,244,238));
				setForeground(list.getForeground());
			}
			setText(user.getUser_name()+" ("+user.getUser_id()+") ");
			setEnabled(list.isEnabled());
			setFont(list.getFont());
			setOpaque(true);
			return this;

		}
	}
	public static void main(String[] args) {
		new ServerView();
	}
}
