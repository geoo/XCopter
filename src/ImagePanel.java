import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

class ImagePanel extends JPanel {
 
  	private Image hintergrund;					
  	private ArrayList<BeweglichesBild> oben;	
	private ArrayList<BeweglichesBild> unten;
	private ArrayList<BeweglichesBild> mitte;
	private BeweglichesBild heli;
	private ArrayList<BeweglichesBild> rauch;
 
  	public ImagePanel(URL img) 
  	{
  		this(new ImageIcon(img).getImage());	
  	}
 
  	public ImagePanel(Image img)
  	{
    	hintergrund = img;
    	Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));	
    	setPreferredSize(size);
    	setMinimumSize(size);
    	setMaximumSize(size);
    	setSize(size);
 
    	oben = new ArrayList<BeweglichesBild>();
    	mitte = new ArrayList<BeweglichesBild>();
    	unten = new ArrayList<BeweglichesBild>();
 
    	rauch = new ArrayList<BeweglichesBild>();
  	}
 
	
  	public void paintComponent(Graphics g) 
  	{
    	g.drawImage(hintergrund, 0, 0, null); 
    	for(BeweglichesBild img : oben)
    		g.drawImage(img.getImage(), (int)(img.getX()), (int)(img.getY()), null);
    	for(BeweglichesBild img : mitte)
    		g.drawImage(img.getImage(), (int)(img.getX()), (int)(img.getY()), null);
    	for(BeweglichesBild img : unten)
    		g.drawImage(img.getImage(), (int)(img.getX()), (int)(img.getY()), null);
    	for(BeweglichesBild img : rauch)
    		g.drawImage(img.getImage(), (int)(img.getX()), (int)(img.getY()), null);
    	if(heli != null)
    		g.drawImage(heli.getImage(), (int)(heli.getX()), (int)(heli.getY()), null);
  	}
 
  	public void updateBilder(ArrayList<BeweglichesBild> newTop,ArrayList<BeweglichesBild> newMiddle,ArrayList<BeweglichesBild> newBottom,BeweglichesBild newCopter,ArrayList<BeweglichesBild> newSmoke)
  	{
  		oben = newTop;
  		heli = newCopter;
  		mitte = newMiddle;
  		unten = newBottom;
  		rauch = newSmoke;
  		repaint();	
  	}
}