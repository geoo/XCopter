import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;

class BeweglichesBild
{
	private Image image;		
	private double x;			
	private double y;		
	
	public BeweglichesBild(Image img, double xPos, double yPos)
	{
		image = img;
		x = xPos;
		y = yPos;
	}
 
	public BeweglichesBild(URL path, double xPos, double yPos)
	{
		this(new ImageIcon(path).getImage(), xPos, yPos);
	}
 
	public void setPosition(double xPos, double yPos)
	{
		x = xPos;
		y = yPos;
	}
 
	public void setImage(URL path)
	{
		image = new ImageIcon(path).getImage();
	}
 
	public void setY(double newY)
	{
		y = newY;
	}
 
	public void setX(double newX)
	{
		x = newX;
	}
 
	public double getX()
	{
		return x;
	}
 
	public double getY()
	{
		return y;
	}
 
	public Image getImage()
	{
		return image;
	}
}