import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.JPanel;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.plot.JPanelFis;

public class Helicopter extends Thread {

	private FIS fis;
	private JPanelFis chartPanel;

	public static void main(String[] args) {

	}

	private JPanel hintergrund = new JPanel();
	private ImagePanel back;

	public static boolean crash;
	public static boolean gestartet;
	public static boolean gespielt;


	public static int distanz;
	public static int maxDistance;

	public final int XPOS;
	public final int NUMRECS;
	public final int RECHOEHE;
	public final int RECBREITE;

	private int moveIncrement;
	private int anzRauch;

	private ArrayList<BeweglichesBild> recsOben;
	private ArrayList<BeweglichesBild> recsUnten;
	private ArrayList<BeweglichesBild> recsMitte;
	private ArrayList<BeweglichesBild> recs;
	private ArrayList<BeweglichesBild> rauch;
	private BeweglichesBild helicopter;

	public Helicopter(FIS fis, JPanelFis chartPanel) {
		super();
		this.fis = fis;
		this.chartPanel = chartPanel;
		NUMRECS = 28;
		RECHOEHE = 73;
		RECBREITE = 29;
		XPOS = 200;
		gespielt = false;
		maxDistance = 0;
		start();
	}

	public void run() {
		initialisieren();
	}

	public JPanel getContainer() {
		return this.hintergrund;
	}

	public void initialisieren() {
		if (!gespielt) {
			hintergrund.setSize(new Dimension(818, 568));
			hintergrund.setVisible(true);

			back = new ImagePanel(this.getClass().getResource("back.JPG"));
			hintergrund.add(back);

		}
		gespielt = true;
		crash = false;
		gestartet = true;

		moveIncrement = 2;
		anzRauch = 15;

		recs = new ArrayList<BeweglichesBild>();
		recsOben = new ArrayList<BeweglichesBild>();
		recsMitte = new ArrayList<BeweglichesBild>();
		recsUnten = new ArrayList<BeweglichesBild>();
		rauch = new ArrayList<BeweglichesBild>();

		helicopter = new BeweglichesBild(this.getClass().getResource(
				"helicopter.png"), XPOS, 270);

		for (int x = 0; x < NUMRECS; x++)
			recsOben.add(new BeweglichesBild(this.getClass().getResource(
					"rec2.JPG"), RECBREITE * x, 30));
		for (int x = 0; x < NUMRECS; x++)
			recsUnten.add(new BeweglichesBild(this.getClass().getResource(
					"rec2.JPG"), RECBREITE * x, 450));

		recsMitte.add(new BeweglichesBild(this.getClass().getResource(
				"rec2.JPG"), 1392, zufallMitte()));
		recsMitte.add(new BeweglichesBild(this.getClass().getResource(
				"rec2.JPG"), 1972, zufallMitte()));

		setzeRechtecke();
	}

	public void setzeRechtecke() {
		long letztes = System.currentTimeMillis();
		long letzterCopter = System.currentTimeMillis();
		long letzterRauch = System.currentTimeMillis();
		int ersteUpdates = 0;
		double letzteDistanz = (double) System.currentTimeMillis();
		while (true) {
			if (!crash
					&& gestartet
					&& (double) System.currentTimeMillis()
							- (double) (2900 / 40) > letzteDistanz) {
				letzteDistanz = System.currentTimeMillis();
				distanz++;
			}

			if (!crash && gestartet
					&& System.currentTimeMillis() - 10 > letzterCopter) {
				letzterCopter = System.currentTimeMillis();
				updateCopter();
				updateMitte();
			}
			if (!crash && gestartet && System.currentTimeMillis() - 100 > letztes) {
				letztes = System.currentTimeMillis();
				updateRechtecke();
			}
			if (!crash && gestartet
					&& System.currentTimeMillis() - 75 > letzterRauch) {
				letzterRauch = System.currentTimeMillis();
				if (ersteUpdates < anzRauch) {
					ersteUpdates++;
					rauch.add(new BeweglichesBild(this.getClass().getResource(
							"smoke.GIF"), 187, helicopter.getY()));
					for (int x = 0; x < ersteUpdates; x++)
						rauch.set(x, new BeweglichesBild(this.getClass()
								.getResource("smoke.GIF"),
								rauch.get(x).getX() - 12, rauch.get(x).getY()));
				} else {
					for (int x = 0; x < anzRauch - 1; x++)
						rauch.get(x).setY(rauch.get(x + 1).getY());
					rauch.set(anzRauch - 1, new BeweglichesBild(this.getClass()
							.getResource("smoke.GIF"), 187, helicopter.getY()));
				}
			}
			back.updateImages(recsOben, recsMitte, recsUnten, helicopter,
					rauch);
		}
	}

	public void updateRechtecke() {
		for (int x = 0; x < (NUMRECS - 1); x++) // move all but the last
												// rectangle 1 spot to the left
		{
			recsOben.set(x,
					new BeweglichesBild(
							this.getClass().getResource("rec2.JPG"), RECBREITE
									* x, recsOben.get(x + 1).getY()));
			recsUnten.set(x,
					new BeweglichesBild(
							this.getClass().getResource("rec2.JPG"), RECBREITE
									* x, recsUnten.get(x + 1).getY()));
		}
		letztesRechteck();
	}

	public void letztesRechteck() {
		if (distanz % 400 == 0)
			moveIncrement++;
		if (recsOben.get(26).getY() < 2)
			runterBewegen();
		else if (recsUnten.get(26).getY() > 463) 
			hochBewegen();
		else 
		{
			if ((int) (Math.random() * 60) == 50)
				zufaelligSetzen();
			else {
				if ((int) (Math.random() * 2) == 1)
					hochBewegen();
				else
					runterBewegen();
			}
		}
	}

	public void zufaelligSetzen() {
		recsOben.get(26).setY(
				recsOben.get(26).getY() + (463 - recsUnten.get(26).getY()));
		recsUnten.get(26).setY(463);
	}

	public void runterBewegen() {
		recsOben.set((NUMRECS - 1), new BeweglichesBild(this.getClass()
				.getResource("rec2.JPG"), RECBREITE * (NUMRECS - 1), recsOben
				.get(26).getY() + moveIncrement));
		recsUnten.set((NUMRECS - 1), new BeweglichesBild(this.getClass()
				.getResource("rec2.JPG"), RECBREITE * (NUMRECS - 1), recsUnten
				.get(26).getY() + moveIncrement));
	}

	public void hochBewegen() {
		recsUnten.set((NUMRECS - 1), new BeweglichesBild(this.getClass()
				.getResource("rec2.JPG"), RECBREITE * (NUMRECS - 1), recsUnten
				.get(26).getY() - moveIncrement));
		recsOben.set((NUMRECS - 1), new BeweglichesBild(this.getClass()
				.getResource("rec2.JPG"), RECBREITE * (NUMRECS - 1), recsOben
				.get(26).getY() - moveIncrement));
	}

	public int zufallMitte() {
		int max = 10000;
		int min = 0;

		for (int x = 0; x < NUMRECS; x++) {
			if (recsOben.get(x).getY() > min)
				min = (int) recsOben.get(x).getY();
			if (recsUnten.get(x).getY() < max)
				max = (int) recsUnten.get(x).getY();
		}
		min += RECHOEHE;
		max -= (RECHOEHE + min);
		return min + (int) (Math.random() * max);
	}

	public void updateMitte() {

		double schub = fis.getVariable("schub").getLatestDefuzzifiedValue();
		
		if (recsMitte.get(0).getX() > -1 * RECBREITE) {
			recsMitte.set(0,
					new BeweglichesBild(
							this.getClass().getResource("rec2.JPG"), recsMitte
									.get(0).getX() - (schub / 20), recsMitte
									.get(0).getY()));
			recsMitte.set(1,
					new BeweglichesBild(
							this.getClass().getResource("rec2.JPG"), recsMitte
									.get(1).getX() - (schub / 20), recsMitte
									.get(1).getY()));
		} else {
			recsMitte.set(0,
					new BeweglichesBild(
							this.getClass().getResource("rec2.JPG"), recsMitte
									.get(1).getX() - (schub / 20), recsMitte
									.get(1).getY()));
			recsMitte.set(1,
					new BeweglichesBild(
							this.getClass().getResource("rec2.JPG"), recsMitte
									.get(0).getX() + 580, zufallMitte()));
		}
	}

	public boolean istGetroffen() {
		for (int x = 3; x <= 7; x++) {

			boolean repaint = false;
			
			// SENSOR OBEN
			double oben = Math.round(helicopter.getY() - RECHOEHE
					- recsOben.get(x).getY());
			if (oben > 100) {
				fis.setVariable("oben", 100);
			} else {
				fis.setVariable("oben", oben);
				repaint = true;
			}

			// SENSOR UNTEN
			double unten = Math.round((recsUnten.get(x).getY())
					- (helicopter.getY() + 48));
			if (unten > 100) {
				fis.setVariable("unten", 100);
			} else {
				fis.setVariable("unten", unten);
				repaint = true;
			}
			fis.evaluate();
			if (repaint) {
				chartPanel.repaint();
			}
			
			if (helicopter.getY() + 48 >= recsUnten.get(x).getY())
				return true;
		}
		for (int y = 3; y <= 7; y++)
			if (helicopter.getY() <= recsOben.get(y).getY() + RECHOEHE)
				return true;
		for (int z = 0; z <= 1; z++)
			if (istInMittlererEntfernung(z))
				return true;
		return false;
	}

	public boolean istInMittlererEntfernung(int num) {
		Rectangle checkMitte = new Rectangle((int) recsMitte.get(num).getX(),
				(int) recsMitte.get(num).getY(), RECBREITE, RECHOEHE);
		Rectangle checkCopter = new Rectangle((int) helicopter.getX(),
				(int) helicopter.getY(), 106, 48);

		return checkMitte.intersects(checkCopter);
	}

	public void obenSensor() {
	}

	public void untenSensor() {
	}

	public void frontSensor() {
		
		// FRONT SENSOR
		if ((recsMitte.get(0).getY()) > (helicopter.getY() - 24)
				&& (helicopter.getY() - 24) > (recsMitte.get(0).getY() - 73)) {
			double front = (recsMitte.get(0).getX() - 302);

			if (front > 100) {
				fis.setVariable("front", 100);
			} else if (front < 0) {
				fis.setVariable("front", 0);
			} else {
				fis.setVariable("front", front);
				chartPanel.repaint();
			}
		} else {
			fis.setVariable("front", 100);
		}
	}

	public void frontUnten() {

		boolean repaint = false;
		// FRONT UNTEN SENSOR
		fis.setVariable("frontUnten", 100);

		// sehr weit
		if ((380 > recsMitte.get(0).getX()) && recsMitte.get(0).getX() > 350) {
			for (int i = 0; i < 74; i++) {
				if ((helicopter.getY() + 128) > (((int)recsMitte.get(0).getY()) + i)
						&& (((int)recsMitte.get(0).getY()) + i) > (helicopter.getY() + 108)) {
					fis.setVariable("frontUnten", 90);
					repaint = true;
				}
			}
		}

		// weit
		if ((360 > recsMitte.get(0).getX()) && recsMitte.get(0).getX() > 330) {
			for (int i = 0; i < 74; i++) {
				if ((helicopter.getY() + 108) > (((int)recsMitte.get(0).getY()) + i)
						&& (((int)recsMitte.get(0).getY()) + i) > (helicopter.getY() + 88)) {
					fis.setVariable("frontUnten", 70);
					repaint = true;
				}
			}
		}
		// mittel
		if ((340 > recsMitte.get(0).getX()) && recsMitte.get(0).getX() > 310) {
			for (int i = 0; i < 74; i++) {
				if ((helicopter.getY() + 88) > (((int)recsMitte.get(0).getY()) + i)
						&& (((int)recsMitte.get(0).getY()) + i) > (helicopter.getY() + 68)) {
					fis.setVariable("frontUnten", 40);
					repaint = true;

				}
			}
		}
		// nah
		if ((320 > recsMitte.get(0).getX()) && recsMitte.get(0).getX() > 300) {
			for (int i = 0; i < 74; i++) {
				if ((helicopter.getY() + 68) > (((int)recsMitte.get(0).getY()) + i)
						&& (((int)recsMitte.get(0).getY()) + i) > (helicopter.getY() + 48)) {
					fis.setVariable("frontUnten", 20);
					repaint = true;
				}
			}
		}
		
		if(repaint) {
			chartPanel.repaint();
		}

	}

	public void frontOben() {

		boolean repaint = false;
		// FRONT OBEN SENSOR
		fis.setVariable("frontOben", 100);

		// sehr weit
		if ((380 > recsMitte.get(0).getX()) && recsMitte.get(0).getX() > 350) {
			for (int i = 0; i < 74; i++) {
				if ((helicopter.getY() - 80) < (((int)recsMitte.get(0).getY()) + i)
						&& (((int)recsMitte.get(0).getY()) + i) < (helicopter.getY() - 60)) {
					fis.setVariable("frontOben", 90);
					repaint = true;

				}
			}
		}

		// weit
		if ((360 > recsMitte.get(0).getX()) && recsMitte.get(0).getX() > 330) {
			for (int i = 0; i < 74; i++) {
				if ((helicopter.getY() - 60) < (((int)recsMitte.get(0).getY()) + i)
						&& (((int)recsMitte.get(0).getY()) + i) < (helicopter.getY() - 40)) {
					fis.setVariable("frontOben", 70);
					repaint = true;
				}
			}
		}
		// mittel
		if ((340 > recsMitte.get(0).getX()) && recsMitte.get(0).getX() > 310) {
			for (int i = 0; i < 74; i++) {
				if ((helicopter.getY() - 40) < (((int)recsMitte.get(0).getY()) + i)
						&& (((int)recsMitte.get(0).getY()) + i) < (helicopter.getY() - 20)) {
					fis.setVariable("frontOben", 40);
					repaint = true;

				}
			}
		}
		// nah
		if ((320 > recsMitte.get(0).getX()) && recsMitte.get(0).getX() > 300) {
			for (int i = 0; i < 74; i++) {
				if ((helicopter.getY() - 20) < (((int)recsMitte.get(0).getY()) + i)
						&& (((int)recsMitte.get(0).getY()) + i) < (helicopter.getY())) {
					fis.setVariable("frontOben", 20);
					repaint = true;
				}
			}
		}

		if(repaint) {
			chartPanel.repaint();
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
		if (istGetroffen())
			initialisieren();
	}

}