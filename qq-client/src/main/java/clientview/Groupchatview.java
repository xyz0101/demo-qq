package clientview;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;

import com.jenkin.model.User;
import common.Conts;

public class Groupchatview extends ChatView{//85
	private static Rectangle bigsize = new Rectangle(200, 100, 954, 638);
	private static Rectangle chatsize = new Rectangle(2, 105, 700, 359);
	private static Rectangle inputsize = new Rectangle(2, 505, 698, 96);
	//Rectangle s = new Rectangle(2, 505, 698, 96);
	private static Rectangle closesize = new Rectangle(844+75, 3, 33, 33);
	private static Rectangle minnsize = new Rectangle(770+75, 3, 33, 33);
	private static Rectangle baishesize = new Rectangle(808+75, 3, 33, 33);
	private static Rectangle sendsize = new Rectangle(592, 599, 109, 29);
	private static Rectangle canclesize = new Rectangle(497, 599, 87, 29);
	private static Rectangle iconsize = new Rectangle(42, 467, 35, 35);
	Rectangle facesize = new Rectangle(10, 5, 54, 54);
	private static String iconname="groupview";
	private User user;
	private List<User> list = null;
	public Groupchatview(User user,List<User> list){
		super(bigsize,chatsize,inputsize,closesize,minnsize,baishesize,sendsize,canclesize,iconsize,iconname);
		this.user=user;
		this.list=list;
		this.chat_model="group";
		list.add(user);
		JScrollPane scrollPane = null;
		JList contact = new JList();
		DefaultListModel contactdlm = new DefaultListModel();
		List<ImageIcon> icon = new ArrayList<ImageIcon>();//群聊头像
		List<JLabel> name = new ArrayList<JLabel>();//群名
		//循环添加用户
		for (int i = 0; i < list.size(); i++) {
			User temp = list.get(i);
			// 用户头像
			ImageIcon head = new ImageIcon(Conts.HEAD.get(temp.getUser_head() + ".png"));
			// 用户昵称
			JLabel userName = new JLabel(temp.getUser_name() +"(" + (temp.getUser_id()) + ")");
			userName.setFont(new Font("宋体", Font.CENTER_BASELINE, 15));
			userName.setBounds(60, 5, 200, 18);

			name.add(userName);
			// userpaint.add(userAll);
			icon.add(head);
			//用户添加到listModel
			contactdlm.addElement("");
		}
		//更新联系人
		contact.setFixedCellHeight(25);
		contact.setModel(contactdlm);
		contact.setCellRenderer(new MyCel(icon, name));
		// 所有用户滚动框
		scrollPane = new JScrollPane();
		scrollPane.setViewportView(contact);
		scrollPane.setPreferredSize(new Dimension(200, 20));
		scrollPane.setOpaque(false);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setBounds(707, 494, 244, 140);
		super.content.add(scrollPane);
		super.myself = this.user;
		super.destination = this.user;
		super.init();

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
					+ "&nbsp&nbsp&nbsp&nbsp&nbsp"+statu1+"<br>  "+sig1+"</html>");
			if(icons!=null&&icons.size()>0)
				setIcon(icons.get(index));// 设置图片
			setEnabled(list.isEnabled());
			setFont(list.getFont());
			setOpaque(true);
			return this;

		}

	}


}
