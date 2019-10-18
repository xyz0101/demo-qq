package common;

import java.awt.Component;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class ComboBoxRenderer extends JLabel implements ListCellRenderer{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Map<String, ImageIcon> box =null;
	public ComboBoxRenderer(Map<String, ImageIcon> box) {
		// TODO Auto-generated constructor stub
		this.box=box;
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		// TODO Auto-generated method stub
		String key=(String)value;
		setIcon(box.get(key));
		setFont(list.getFont());
		return this;
	}

}
