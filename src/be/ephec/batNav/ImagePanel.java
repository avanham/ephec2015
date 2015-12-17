package be.ephec.batNav;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Serializable;
import javax.swing.JPanel;
public class ImagePanel extends JPanel implements Serializable{
    Image image = null;
    public String batSelected = null;
    public ImagePanel(Image image) {
        this.image = image;
    }
    public ImagePanel() {
    }
    public void setImage(Image image){
        this.image = image;
    }
    public Image getImage(Image image){
        return image;
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g); //paint background
        if (image != null) { //there is a picture: draw it
        	//this.addMouseListener(this);
            //int height = this.getSize().height;
            //int width = this.getSize().width;
            g.drawImage(image, 0, 0, this); //use image size          
            
        }
    }
    public void addImage(Image image){
    	//g.drawImage(image,0,0, 200, 200, this);
    }
}