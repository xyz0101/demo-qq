package clientview;

import com.jenkin.Const;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.text.Position;

public class IconView extends JWindow{
	GridLayout grid = new GridLayout(7,15);
	ChatView holder = null;
	JPanel panel = null;
	JLabel [] icon = new JLabel[105];
	public IconView(ChatView holder){
		this.holder = holder;
		init();
		setAlwaysOnTop(true);
	}
	private void init() {
		this.setPreferredSize(new Dimension(28*15,28*7));
		panel = new JPanel();
		panel.setOpaque(true);
		panel.setLayout(grid);
		for(int i=0; i<105; i++){
			String iconname = Const.CLIENT_BASE_DIR+"image/icon/"+i+".gif";
			icon[i] = new JLabel(new ImageIcon(iconname));
			icon[i].setToolTipText(i+"");
			icon[i].addMouseListener(new MouseAdapter() {

				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub
					super.mouseClicked(e);
					if(e.getButton()==1){  //如果点了左键
						JLabel temp = (JLabel) e.getSource();
						String name = temp.getToolTipText();
						holder.inserticon(name);
						getobj().dispose();
					}
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub
					super.mouseEntered(e);
					((JLabel)e.getSource()).setBorder(BorderFactory.createLineBorder(Color.BLUE));
				}

				@Override
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub
					super.mouseExited(e);
					((JLabel)e.getSource()).setBorder(BorderFactory.createLineBorder(new Color(250,250,250),1));
				}

			});

			panel.add(icon[i]);

		}
		panel.setVisible(true);
		getContentPane().add(panel);
	}
	private void setperfect() {
		Point p = holder.getbutton().getLocationOnScreen();
		setBounds(p.x-getPreferredSize().width/3, p.y-getPreferredSize().height, getPreferredSize().width, getPreferredSize().height);
	}
	private JWindow getobj() {
		return this;
	}
	public void setvis(boolean flag) {
		if(flag){
			setperfect();
		}
		super.setVisible(flag);
	}
}
