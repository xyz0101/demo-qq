package clientview;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import common.ResourceManager;

public class Findcodeview extends JFrame{
	private bk content =new bk();
	Findcodeview(){
		setBounds(500, 250,797, 563);
		setUndecorated(true);
		setResizable(false);

		//添加拖动
		content.addMouseMotionListener(new MouseMotionListener() {
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
		getContentPane().add(content);
		setVisible(true);
	}
	class bk extends JPanel{
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;
		String str ="hehe";
		@Override
		protected void paintComponent(Graphics g) {
			ResourceManager boss = new ResourceManager();
			ImageIcon image = new ImageIcon();
			image = boss.GetImage(str);
			g.drawImage(image.getImage(), 0,0,image.getIconWidth(),image.getIconHeight(),this);
		}
	}
}
