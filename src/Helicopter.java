import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.JPanel;

import net.sourceforge.jFuzzyLogic.FIS;

public class Helicopter extends Thread {

	// private Data data;
	private FIS fis;
	private ChartPanel chartPanel;

	public static void main(String[] args) {

		/*
		 * FIS fis; String fileName = "fcl.fcl"; fis = FIS.load(fileName, true);
		 * fis.toStringFcl();
		 * 
		 * if (fis == null) { // Error while loading?
		 * System.err.println("Can't load file: '" + fileName + "'"); return; }
		 * fis.chart(); fis.setVariable("oben", 100);
		 * fis.setVariable("frontOben", 100); fis.setVariable("front", 100);
		 * fis.setVariable("frontUnten", 100); fis.setVariable("unten", 100);
		 */
		// HelicopterForm a = new HelicopterForm();

	}

	private JPanel background = new JPanel();
	// private Container container;
	// private JButton button;
	private ImagePanel back;

	public static boolean crashed;
	public static boolean started;
	public static boolean playedOnce;

	public boolean goingUp;

	public static int distance;
	public static int maxDistance;

	public final int XPOS;
	public final int NUMRECS;
	public final int RECHEIGHT;
	public final int RECWIDTH;

	private int moveIncrement;
	private int numSmoke;

	private ArrayList<BeweglichesBild> toprecs;
	private ArrayList<BeweglichesBild> bottomrecs;
	private ArrayList<BeweglichesBild> middlerecs;
	private ArrayList<BeweglichesBild> recs;
	private ArrayList<BeweglichesBild> smoke;
	private BeweglichesBild helicopter;

	public Helicopter(FIS fis, ChartPanel chartPanel) {
		super();
		this.fis = fis;
		this.chartPanel = chartPanel;
		NUMRECS = 28;
		RECHEIGHT = 73;
		RECWIDTH = 29;
		XPOS = 200;
		playedOnce = false;
		maxDistance = 0;
		start();
	}

	public void run() {
		initiate();
	}

	public JPanel getContainer() {
		return this.background;
	}

	public void initiate() {
		if (!playedOnce) {
			background.setSize(new Dimension(818, 568));
			background.setVisible(true);

			back = new ImagePanel(this.getClass().getResource("back.JPG"));
			background.add(back);

		}
		playedOnce = true;
		goingUp = false;
		crashed = false;
		started = true;

		moveIncrement = 2;
		numSmoke = 15;

		recs = new ArrayList<BeweglichesBild>();
		toprecs = new ArrayList<BeweglichesBild>();
		middlerecs = new ArrayList<BeweglichesBild>();
		bottomrecs = new ArrayList<BeweglichesBild>();
		smoke = new ArrayList<BeweglichesBild>();

		helicopter = new BeweglichesBild(this.getClass().getResource(
				"helicopter.png"), XPOS, 270);

		for (int x = 0; x < NUMRECS; x++)
			toprecs.add(new BeweglichesBild(this.getClass().getResource(
					"rec2.JPG"), RECWIDTH * x, 30));
		for (int x = 0; x < NUMRECS; x++)
			bottomrecs.add(new BeweglichesBild(this.getClass().getResource(
					"rec2.JPG"), RECWIDTH * x, 450));

		middlerecs.add(new BeweglichesBild(this.getClass().getResource(
				"rec2.JPG"), 1392, randomMidHeight()));
		middlerecs.add(new BeweglichesBild(this.getClass().getResource(
				"rec2.JPG"), 1972, randomMidHeight()));

		drawRectangles();
	}

	public void drawRectangles() {
		long last = System.currentTimeMillis();
		long lastCopter = System.currentTimeMillis();
		long lastSmoke = System.currentTimeMillis();
		int firstUpdates = 0;
		double lastDistance = (double) System.currentTimeMillis();
		while (true) {
			if (!crashed
					&& started
					&& (double) System.currentTimeMillis()
							- (double) (2900 / 40) > lastDistance) {
				lastDistance = System.currentTimeMillis();
				distance++;
			}

			if (!crashed && started
					&& System.currentTimeMillis() - 10 > lastCopter) {
				lastCopter = System.currentTimeMillis();
				updateCopter();
				updateMiddle();
			}
			if (!crashed && started && System.currentTimeMillis() - 100 > last) {
				last = System.currentTimeMillis();
				updateRecs();
			}
			if (!crashed && started
					&& System.currentTimeMillis() - 75 > lastSmoke) {
				lastSmoke = System.currentTimeMillis();
				if (firstUpdates < numSmoke) {
					firstUpdates++;
					smoke.add(new BeweglichesBild(this.getClass().getResource(
							"smoke.GIF"), 187, helicopter.getY()));
					for (int x = 0; x < firstUpdates; x++)
						smoke.set(x, new BeweglichesBild(this.getClass()
								.getResource("smoke.GIF"),
								smoke.get(x).getX() - 12, smoke.get(x).getY()));
				} else {
					for (int x = 0; x < numSmoke - 1; x++)
						smoke.get(x).setY(smoke.get(x + 1).getY());
					smoke.set(numSmoke - 1, new BeweglichesBild(this.getClass()
							.getResource("smoke.GIF"), 187, helicopter.getY()));
				}
			}
			back.updateImages(toprecs, middlerecs, bottomrecs, helicopter,
					smoke);
		}
	}

	public void updateRecs() {
		for (int x = 0; x < (NUMRECS - 1); x++) // move all but the last
												// rectangle 1 spot to the left
		{
			toprecs.set(x,
					new BeweglichesBild(
							this.getClass().getResource("rec2.JPG"), RECWIDTH
									* x, toprecs.get(x + 1).getY()));
			bottomrecs.set(x,
					new BeweglichesBild(
							this.getClass().getResource("rec2.JPG"), RECWIDTH
									* x, bottomrecs.get(x + 1).getY()));
		}
		lastRec();
	}

	public void lastRec() {
		if (distance % 400 == 0)
			moveIncrement++;
		if (toprecs.get(26).getY() < 2) // if too high, move down
			moveDown();
		else if (bottomrecs.get(26).getY() > 463) // else if too low, move up
			moveUp();
		else // else move randomly
		{
			if ((int) (Math.random() * 60) == 50)
				randomDrop();
			else {
				if ((int) (Math.random() * 2) == 1)
					moveUp();
				else
					moveDown();
			}
		}
	}

	public void randomDrop() {
		toprecs.get(26).setY(
				toprecs.get(26).getY() + (463 - bottomrecs.get(26).getY()));
		bottomrecs.get(26).setY(463);
	}

	public void moveDown() {
		toprecs.set((NUMRECS - 1), new BeweglichesBild(this.getClass()
				.getResource("rec2.JPG"), RECWIDTH * (NUMRECS - 1), toprecs
				.get(26).getY() + moveIncrement));
		bottomrecs.set((NUMRECS - 1), new BeweglichesBild(this.getClass()
				.getResource("rec2.JPG"), RECWIDTH * (NUMRECS - 1), bottomrecs
				.get(26).getY() + moveIncrement));
	}

	public void moveUp() {
		bottomrecs.set((NUMRECS - 1), new BeweglichesBild(this.getClass()
				.getResource("rec2.JPG"), RECWIDTH * (NUMRECS - 1), bottomrecs
				.get(26).getY() - moveIncrement));
		toprecs.set((NUMRECS - 1), new BeweglichesBild(this.getClass()
				.getResource("rec2.JPG"), RECWIDTH * (NUMRECS - 1), toprecs
				.get(26).getY() - moveIncrement));
	}

	public int randomMidHeight() {
		int max = 10000;
		int min = 0;

		for (int x = 0; x < NUMRECS; x++) {
			if (toprecs.get(x).getY() > min)
				min = (int) toprecs.get(x).getY();
			if (bottomrecs.get(x).getY() < max)
				max = (int) bottomrecs.get(x).getY();
		}
		min += RECHEIGHT;
		max -= (RECHEIGHT + min);
		return min + (int) (Math.random() * max);
	}

	// TODO
	// moves the randomly generated middle rectangles
	public void updateMiddle() {

		double schub = fis.getVariable("schub").getLatestDefuzzifiedValue();
		
		if (middlerecs.get(0).getX() > -1 * RECWIDTH) {
			middlerecs.set(0,
					new BeweglichesBild(
							this.getClass().getResource("rec2.JPG"), middlerecs
									.get(0).getX() - (schub / 20), middlerecs
									.get(0).getY()));
			middlerecs.set(1,
					new BeweglichesBild(
							this.getClass().getResource("rec2.JPG"), middlerecs
									.get(1).getX() - (schub / 20), middlerecs
									.get(1).getY()));
		} else {
			middlerecs.set(0,
					new BeweglichesBild(
							this.getClass().getResource("rec2.JPG"), middlerecs
									.get(1).getX() - (schub / 20), middlerecs
									.get(1).getY()));
			middlerecs.set(1,
					new BeweglichesBild(
							this.getClass().getResource("rec2.JPG"), middlerecs
									.get(0).getX() + 580, randomMidHeight()));
		}
	}

	public boolean isHit() {
		for (int x = 3; x <= 7; x++) {

			// SENSOR OBEN
			double oben = Math.round(helicopter.getY() - RECHEIGHT
					- toprecs.get(x).getY());
			if (oben > 100) {
				fis.setVariable("oben", 100);
			} else {
				fis.setVariable("oben", oben);
			}

			// SENSOR UNTEN
			double unten = Math.round((bottomrecs.get(x).getY())
					- (helicopter.getY() + 48));
			if (unten > 100) {
				fis.setVariable("unten", 100);
			} else {
				fis.setVariable("unten", unten);
			}
			fis.evaluate();

			if (helicopter.getY() + 48 >= bottomrecs.get(x).getY())
				return true;
		}
		for (int y = 3; y <= 7; y++)
			if (helicopter.getY() <= toprecs.get(y).getY() + RECHEIGHT)
				return true;
		for (int z = 0; z <= 1; z++)
			if (isInMidRange(z))
				return true;
		return false;
	}

	public boolean isInMidRange(int num) {
		Rectangle middlecheck = new Rectangle((int) middlerecs.get(num).getX(),
				(int) middlerecs.get(num).getY(), RECWIDTH, RECHEIGHT);
		Rectangle coptercheck = new Rectangle((int) helicopter.getX(),
				(int) helicopter.getY(), 106, 48);

		return middlecheck.intersects(coptercheck);
	}

	public void obenSensor() {
	}

	public void untenSensor() {
	}

	public void frontSensor() {
		// FRONT SENSOR
		if ((middlerecs.get(0).getY()) > (helicopter.getY() - 24)
				&& (helicopter.getY() - 24) > (middlerecs.get(0).getY() - 73)) {
			double front = (middlerecs.get(0).getX() - 302);

			if (front > 100) {
				fis.setVariable("front", 100);
			} else if (front < 0) {
				fis.setVariable("front", 0);
			} else {
				fis.setVariable("front", front);
			}
		} else {
			fis.setVariable("front", 100);
		}
	}

	public void frontUnten() {

		// FRONT UNTEN SENSOR
		fis.setVariable("frontUnten", 100);

		// sehr weit
		if ((380 > middlerecs.get(0).getX()) && middlerecs.get(0).getX() > 350) {
			for (int i = 0; i < 74; i++) {
				if ((helicopter.getY() + 128) > (middlerecs.get(0).getY() + i)
						&& (middlerecs.get(0).getY() + i) > (helicopter.getY() + 108)) {
					fis.setVariable("frontUnten", 100);
				}
			}
		}

		// weit
		if ((360 > middlerecs.get(0).getX()) && middlerecs.get(0).getX() > 330) {
			for (int i = 0; i < 74; i++) {
				if ((helicopter.getY() + 108) > (middlerecs.get(0).getY() + i)
						&& (middlerecs.get(0).getY() + i) > (helicopter.getY() + 88)) {
					fis.setVariable("frontUnten", 70);
				}
			}
		}
		// mittel
		if ((340 > middlerecs.get(0).getX()) && middlerecs.get(0).getX() > 310) {
			for (int i = 0; i < 74; i++) {
				if ((helicopter.getY() + 88) > (middlerecs.get(0).getY() + i)
						&& (middlerecs.get(0).getY() + i) > (helicopter.getY() + 68)) {
					fis.setVariable("frontUnten", 40);

				}
			}
		}
		// nah
		if ((320 > middlerecs.get(0).getX()) && middlerecs.get(0).getX() > 300) {
			for (int i = 0; i < 74; i++) {
				if ((helicopter.getY() + 68) > (middlerecs.get(0).getY() + i)
						&& (middlerecs.get(0).getY() + i) > (helicopter.getY() + 48)) {
					fis.setVariable("frontUnten", 20);

				}
			}
		}

	}

	public void frontOben() {

		// FRONT OBEN SENSOR
		fis.setVariable("frontOben", 100);

		// sehr weit
		if ((380 > middlerecs.get(0).getX()) && middlerecs.get(0).getX() > 350) {
			for (int i = 0; i < 74; i++) {
				if ((helicopter.getY() - 80) < (middlerecs.get(0).getY() + i)
						&& (middlerecs.get(0).getY() + i) < (helicopter.getY() - 60)) {
					fis.setVariable("frontOben", 100);
				}
			}
		}

		// weit
		if ((360 > middlerecs.get(0).getX()) && middlerecs.get(0).getX() > 330) {
			for (int i = 0; i < 74; i++) {
				if ((helicopter.getY() - 60) < (middlerecs.get(0).getY() + i)
						&& (middlerecs.get(0).getY() + i) < (helicopter.getY() - 40)) {
					fis.setVariable("frontOben", 70);
				}
			}
		}
		// mittel
		if ((340 > middlerecs.get(0).getX()) && middlerecs.get(0).getX() > 310) {
			for (int i = 0; i < 74; i++) {
				if ((helicopter.getY() - 40) < (middlerecs.get(0).getY() + i)
						&& (middlerecs.get(0).getY() + i) < (helicopter.getY() - 20)) {
					fis.setVariable("frontOben", 40);

				}
			}
		}
		// nah
		if ((320 > middlerecs.get(0).getX()) && middlerecs.get(0).getX() > 300) {
			for (int i = 0; i < 74; i++) {
				if ((helicopter.getY() - 20) < (middlerecs.get(0).getY() + i)
						&& (middlerecs.get(0).getY() + i) < (helicopter.getY())) {
					fis.setVariable("frontOben", 20);

				}
			}
		}

	}

	public void updateCopter() {

		frontSensor();
		frontOben();
		frontUnten();
		fis.evaluate();

		if (fis.getVariable("auftrieb").getLatestDefuzzifiedValue() > 51) {
			helicopter
					.setPosition(XPOS, (double) (helicopter.getY() - (fis
							.getVariable("auftrieb")
							.getLatestDefuzzifiedValue() / 50)));
		} else if (fis.getVariable("auftrieb").getLatestDefuzzifiedValue() < 49) {

			helicopter.setPosition(XPOS,
					(double) (helicopter.getY() + ((fis.getVariable("auftrieb")
							.getLatestDefuzzifiedValue() + 50) / 50)));

		}
		if (isHit())
			initiate();
	}

}