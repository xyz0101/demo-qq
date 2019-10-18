/**
 * 
 */
package common;


import com.jenkin.Const;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * @author lenovo
 *
 */
public class LoadImages {

	public LoadImages() {
		// TODO Auto-generated constructor stub
	}
    
    public Image loadImage(String name) {
    	File file= new File(Const.CLIENT_BASE_DIR+"image/Chat/"+name+".PNG");
    	Image img=null;
    	try {
			img = ImageIO.read(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return img;
	}
    public Image loadheadImage(String name) {
    	File file= new File(Const.CLIENT_BASE_DIR+"image/Head/"+name+".gif");
    	Image img=null;
    	try {
			img = ImageIO.read(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return img;
	}
    public ImageIcon LoadHeadImageIcon(String name){
        
        ImageIcon icon = new ImageIcon(loadheadImage(name));
		return icon;
    }
    public ImageIcon LoadImageIcon(String name){
    
        ImageIcon icon = new ImageIcon(loadImage(name));
		return icon;
    }
    public Image loadEpImage(String name) {
 	File file= new File(Const.CLIENT_BASE_DIR+"image/Chat/12/"+name+".gif");
 	Image img=null;
 	try {
			img = ImageIO.read(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 	return img;
	}
}
 
