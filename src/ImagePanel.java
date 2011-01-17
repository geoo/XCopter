import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

class ImagePanel extends JPanel {
 
  	private Image background;					
  	private ArrayList<BeweglichesBild> top;	
	private ArrayList<BeweglichesBild> bottom;
	private ArrayList<BeweglichesBild> middle;
	private BeweglichesBild copter;
	private ArrayList<BeweglichesBild> smoke;
 
  	public ImagePanel(URL img) 
  	{
  		this(new ImageIcon(img).getImage());	
  	}
 
  	public ImagePanel(Image img)
  	{
    	background = img;
    	Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));	
    	setPreferredSize(size);
    	setMinimumSize(size);
    	setMaximumSize(size);
    	setSize(size);
 
    	top = new ArrayList<BeweglichesBild>();
    	middle = new ArrayList<BeweglichesBild>();
    	bottom = new ArrayList<BeweglichesBild>();
 
    	smoke = new ArrayList<BeweglichesBild>();
  	}
 
	
  	public void paintComponent(Graphics g) 
  	{
    	g.drawImage(background, 0, 0, null); 
    	for(BeweglichesBild img : top)
    		g.drawImage(img.getImage(), (int)(img.getX()), (int)(img.getY()), null);
    	for(BeweglichesBild img : middle)
    		g.drawImage(img.getImage(), (int)(img.getX()), (int)(img.getY()), null);
    	for(BeweglichesBild img : bottom)
    		g.drawImage(img.getImage(), (int)(img.getX()), (int)(img.getY()), null);
    	for(BeweglichesBild img : smoke)
    		g.drawImage(img.getImage(), (int)(img.getX()), (int)(img.getY()), null);
    	if(copter != null)
    		g.drawImage(copter.getImage(), (int)(copter.getX()), (int)(copter.getY()), null);
  	}
 
  	public void updateImages(ArrayList<BeweglichesBild> newTop,ArrayList<BeweglichesBild> newMiddle,ArrayList<BeweglichesBild> newBottom,BeweglichesBild newCopter,ArrayList<BeweglichesBild> newSmoke)
  	{
  		top = newTop;
  		copter = newCopter;
  		middle = newMiddle;
  		bottom = newBottom;
  		smoke = newSmoke;
  		repaint();	
  	}
}