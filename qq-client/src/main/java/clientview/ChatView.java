package clientview;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import com.alibaba.fastjson.JSON;

import com.client.ClientMessageDealer;
import com.client.SingleChatClient;
import com.jenkin.Const;
import com.jenkin.model.MyPic;
import common.Conts;
import com.jenkin.model.Message;;
import com.jenkin.model.User;
import clientToServer.Chatmanager;
import clientToServer.Runchatthread;

import common.DateTool;
import common.MyButton;
import common.ResourceManager;

public class ChatView extends JFrame implements MouseListener{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	bk content = new bk();
	private MyButton send = null;
	private MyButton close = null;
	private MyButton minn = null;
	private MyButton cancle = null;
	private MyButton iconselect = null;
	private Chatmanager gc = null;
	private textPane chat = null;
	private textPane input = null;
	private Vector<MyPic> sendarr;
	private MyPic  readpic;
	private Vector<MyPic>  readarr;
	private StyledDocument docchat = null;
	private StyledDocument docinput = null;
	private IconView iconselector = null;
	private Rectangle bigsize = new Rectangle(500, 200, 879, 638);
	private Rectangle chatsize = new Rectangle(2, 105, 700, 359);
	private Rectangle inputsize = new Rectangle(2, 505, 698, 96);
	//Rectangle s = new Rectangle(2, 505, 698, 96);
	private Rectangle closesize = new Rectangle(844, 3, 33, 33);
	private Rectangle minnsize = new Rectangle(770, 3, 33, 33);
	private Rectangle baishesize = new Rectangle(808, 3, 33, 33);
	private Rectangle sendsize = new Rectangle(592, 599, 109, 29);
	private Rectangle canclesize = new Rectangle(497, 599, 87, 29);
	private Rectangle iconsize = new Rectangle(42, 467, 35, 35);
	private Rectangle drawname = new Rectangle(73, 25, 100, 23);
	private Rectangle drawmotoo = new Rectangle(73, 43, 200, 15);
	private Rectangle facesize = new Rectangle(10, 5, 54, 54);
	private String str = "chatview";
	String chat_model = "private";
	public User myself,destination;
	Runchatthread runchat = null;
	public ChatView(Rectangle bigsize,Rectangle chatsize,Rectangle inputsize,Rectangle closesize
			,Rectangle minnsize,Rectangle baishesize,Rectangle sendsize ,Rectangle canclesize ,
					Rectangle iconsize,String iconname) {
		this.bigsize=bigsize;
		this.inputsize=inputsize;
		this.closesize=closesize;
		this.minnsize=minnsize;
		this.baishesize=baishesize;
		this.sendsize=sendsize;
		this.canclesize=canclesize;
		this.iconsize=iconsize;
		this.str = iconname;
	}
	public ChatView(User myself,User destination) {
		this.myself=myself;
		this.destination=destination;
		init();
	}
	 void init(){
		setBounds(bigsize);
		setUndecorated(true);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		content.setLayout(null);
		sendarr = new Vector<MyPic>();
		readarr = new Vector<MyPic>();
		// 添加拖动
		{
			content.addMouseMotionListener(new MouseMotionListener() {
				Point l = null;
				@Override
				public void mouseMoved(MouseEvent e) {
					// TODO Auto-generated method stub
					l = new Point(e.getX(), e.getY());
				}
				@Override
				public void mouseDragged(MouseEvent e) {
					// TODO Auto-generated method stub
					setLocation(e.getXOnScreen() - l.x, e.getYOnScreen() - l.y);
				}
			});
		}
		// 设置聊天框
		{
			chat = new textPane();
			chat.setBounds(chatsize);
			chat.setOpaque(false);
			chat.setEditable(false);
			chat.setFont(new Font("宋体", ABORT, 25));
			chat.setVisible(true);
			chat.addMouseListener(this);
			JScrollPane jsp = new JScrollPane(chat);
			jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

			jsp.setBounds(0, 103, 701, 360);
			jsp.setOpaque(false);
			jsp.getViewport().setOpaque(false);
			docchat = chat.getStyledDocument();
			jsp.setVisible(true);
			content.add(jsp);

		}
		// 设置输入框
		{
			input = new textPane();
			input.setBounds(inputsize);
			input.setOpaque(false);
			input.setFont(new Font("宋体", ABORT, 15));
			input.setVisible(true);
			input.addMouseListener(this);
			docinput = input.getStyledDocument();
			content.add(input);
		}

		// 关闭按钮
		{
			close = new MyButton();
			close.setBounds(closesize);
			close.addMouseListener(this);
			close.setVisible(true);
			content.add(close);
		}
		// 最小化
		{
			minn = new MyButton();
			minn.setBounds(minnsize);
			minn.addMouseListener(this);
			minn.setVisible(true);
			content.add(minn);
		}
		//摆设
		{
			MyButton full = new MyButton();
			full.setBounds(baishesize);
			full.setVisible(true);
			content.add(full);
		}
		// 发送按钮
		{
			send = new MyButton();
			send.setBounds(sendsize);
			send.addMouseListener(this);
			send.setVisible(true);
			content.add(send);

		}
		// 取消按钮
		{
			cancle = new MyButton();
			cancle.setBounds(canclesize);
			cancle.addMouseListener(this);
			cancle.setVisible(true);
			content.add(cancle);
		}
		// 表情框按钮
		{
			iconselect = new MyButton();
			iconselect.setBounds(iconsize);
			iconselect.addMouseListener(this);
			iconselect.setVisible(true);
			content.add(iconselect);
		}
		iconselector = new IconView(this); //弄一个表情框
		content.addMouseListener(this);
		getContentPane().add(content);
		setVisible(true);



		//enter();
	}
	 void newthread(Chatmanager ct){
		gc = ct;  //返回一个跟本聊天框绑定好的聊天服务对象
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	 void enter(){
		Message message =new Message();
		message.setChat_model(chat_model);
		message.setUser_id(myself.getUser_id());
		message.setText("");
		message.setMsg_model("1");
		message.setSend_to(destination.getUser_id());
		message.setUser_name(myself.getUser_name());
		//sendMessageBio(message);
		 sendMessageNio(message);
	}

	private void sendMessageNio(Message message) {
		String msg = JSON.toJSONString(message);
		System.out.println("发送Nio消息=====>"+msg);
		try {
			ClientMessageDealer clientMessageDealer = Conts.WINDOW_MAP.get(destination.getUser_id());
			if (clientMessageDealer!=null) {
				clientMessageDealer.doWrite(msg);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void sendMessageBio(Message message) {
		gc.send(message);
	}

	public void showmessage(Message message) {
		String speakname = message.getUser_name();
		readarr = message.getPicarr() ;
		String textmessage =message.getText();
		processmessage(speakname, textmessage, readarr);
		readarr.clear();
	}
	private void showword(String word){
		/*这些函数对消息进行一系列加工 */
		if(word.compareTo("")!=0)
			try {
				docchat.insertString(docchat.getLength(), word,
						chat.getAttrSet());
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		//如提取图片 和 文本消息什么的
	}
	private void showimage(Vector<MyPic> arr, String text){
		String iconid =null;
		int pos = 0;
		int cot = 0;
		for(int i=0;i<arr.size();i++,cot++){
			iconid = Const.CLIENT_BASE_DIR+"image/icon/"+arr.get(i).getPicid()+".gif";
			pos = docchat.getLength() - text.length()+arr.get(i).getIndex()-cot;
			chat.setCaretPosition(pos);
			chat.insertIcon(new ImageIcon(iconid));
		}
	}
	private void sendmessage(){
		processmessage(myself.getUser_name(),input.getText().replace(" ",""),sendarr);
		Message message =new Message();
		message.setChat_model(chat_model);
		message.setPicarr(sendarr);
		if(input.getText().replace(" ","").equals(""))
			message.setText("");
		else
			message.setText(input.getText().replace(" ",""));
		message.setMsg_model("0");
		message.setUser_id(myself.getUser_id());
		message.setUser_name(myself.getUser_name());
		message.setSend_to(destination.getUser_id());
		sendMessageNio(message);
		sendarr = new Vector<MyPic>();
	}
	private void processmessage(String speakname,String textmessage,Vector<MyPic> arr){
		try {
			docchat.insertString(docchat.getLength(),speakname+" "+DateTool.f(new Date())+"\n",
					chat.getAttrSet());
			showword(textmessage);
			showimage(arr,textmessage);
			docchat.insertString(docchat.getLength(),"\n"+"\n",
					chat.getAttrSet());
			chat.selectAll();
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	 MyButton getbutton(){
		return this.iconselect;
	}
	 void inserticon(String iconid){
		String location = Const.CLIENT_BASE_DIR+"image/icon/"+iconid+".gif";
		input.setCaretPosition(docinput.getLength());
		MyPic sendpic = new MyPic();
		sendpic.setIndex(docinput.getLength());
		sendpic.setPicid(iconid);
		input.insertIcon(new ImageIcon(location));
		sendarr.add(sendpic);
	}

	class textPane extends JTextPane {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;
		SimpleAttributeSet attrSet;// 参数需要

		private SimpleAttributeSet getAttrSet() {
			attrSet = new SimpleAttributeSet();
			StyleConstants.setFontFamily(attrSet, "宋体");
			StyleConstants.setBold(attrSet, false);
			StyleConstants.setItalic(attrSet, false);
			StyleConstants.setFontSize(attrSet, 20);
			StyleConstants.setForeground(attrSet, Color.RED);
			return attrSet;
		}

	}
	class bk extends JPanel {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void paint(Graphics g) {
			// TODO Auto-generated method stub
			super.paint(g);
			drawHead(g,facesize,destination.getUser_head());
		}



		@Override
		protected void paintComponent(Graphics g) {
			drawBack(g,str);
			if(!chat_model.equals("group")){
				drawName(g,destination.getUser_name());
				drawMotoo(g,destination.getUser_motoo());
			}
			else
				drawName(g,"聊天室");

		}



		private void drawName(Graphics g, String name) {
			// 网名显示框
			{
				g.setFont(new Font("宋体", Font.BOLD, 20));
				g.drawString(name, drawname.x, drawname.y);
			}
		}
		private void drawMotoo(Graphics g, String name) {
			// 网名显示框
			{
				g.setFont(new Font("黑体", Font.BOLD, 14));
				g.drawString(name, drawmotoo.x, drawmotoo.y);
			}
		}
		private void drawHead(Graphics g, Rectangle facesize,String facestr) {
			ResourceManager boss = new ResourceManager();
			ImageIcon image2 = new ImageIcon();
			image2 = boss.getheadimage(facestr);
			g.drawImage(image2.getImage(), facesize.x,facesize.y,facesize.width,facesize.height, this);
		}
		private void drawBack(Graphics g,String str) {
			ResourceManager boss = new ResourceManager();
			ImageIcon image = new ImageIcon();
			image = boss.GetImage(str);
			g.drawImage(image.getImage(), 0, 0, image.getIconWidth(),
					image.getIconHeight(), this);

		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		this.iconselector.setvis(false);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getButton()!=0)e.consume();
		Object k =e.getSource();
		if (k == send) {
			if(docinput.getLength()!=0){
				sendmessage();
				this.iconselector.setvis(false);
				sendarr.clear();
				input.setText("");
				input.requestFocus();
			}
		}
		if (k == close || k == cancle) {

			this.dispose();
//			gc.go = false;
			ClientMessageDealer clientMessageDealer = Conts.WINDOW_MAP.get(destination.getUser_id());
			if (clientMessageDealer!=null) {
				clientMessageDealer.stop();
			}
			Conts.WINDOW_SET.remove(destination.getUser_id());
			Conts.WINDOW_MAP.remove(destination.getUser_id());
			System.out.println("退出聊天");
		}
		if (k == minn) {
			setExtendedState(JFrame.ICONIFIED);
			this.iconselector.setvis(false);
		}
		if(k == iconselect){
			this.iconselector.setvis(true);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
