import java.awt.Image;

import javax.swing.ImageIcon;

class MovingImage
{
	private Image image;		//The picture
	private double x;			//X position
	private double y;			//Y position
 
	//Construct a new Moving Image with image, x position, and y position given
	public MovingImage(Image img, double xPos, double yPos)
	{
		image = img;
		x = xPos;
		y = yPos;
	}
 
	//Construct a new Moving Image with image (from file path), x position, and y position given
	public MovingImage(String path, double xPos, double yPos)
	{
		this(new ImageIcon(path).getImage(), xPos, yPos);	
			//easiest way to make an image from a file path in Swing
	}
 
	//They are set methods.  I don't feel like commenting them.
	public void setPosition(double xPos, double yPos)
	{
		x = xPos;
		y = yPos;
	}
 
	public void setImage(String path)
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
 
	//Get methods which I'm also not commenting
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