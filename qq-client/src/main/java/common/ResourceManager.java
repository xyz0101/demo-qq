package common;

import javax.swing.ImageIcon;

public class ResourceManager {
	/*
	 * 图片资源管理器
	 */
	/**
	 * 获得图片
	 * @param imageName
	 * @return
	 */
	public ImageIcon GetImage(String imageName)
	{
		LoadImages image = new LoadImages();
		return image.LoadImageIcon(imageName);
	}
	public ImageIcon getheadimage(String imageName){
		LoadImages image = new LoadImages();
		return image.LoadHeadImageIcon(imageName);
	}
}
