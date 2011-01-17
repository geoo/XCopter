import java.awt.Dimension;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.plot.JPanelFis;


public class ChartPanel extends Thread{

	private JPanelFis chartPanel;
	private FIS fis;
	
	public ChartPanel(FIS fis) {
		super();
		this.fis = fis;
		this.chartPanel = new JPanelFis(fis);
		chartPanel.setSize(new Dimension(550,800));
		chartPanel.setPreferredSize(new Dimension(550,800));
		chartPanel.setMaximumSize(new Dimension(550,800));
		chartPanel.setMinimumSize(new Dimension(550,800));
		start();
	}
	
	public void run() {
		double auftriebTemp = fis.getVariable("auftrieb").getLatestDefuzzifiedValue();
		double schubTemp = fis.getVariable("schub").getLatestDefuzzifiedValue();
		
		while(true) {
			double auftriebAktuell = fis.getVariable("auftrieb").getLatestDefuzzifiedValue();
			double schubAktuell = fis.getVariable("schub").getLatestDefuzzifiedValue();
			
			if(((auftriebTemp - auftriebAktuell) > 1) || ((auftriebTemp - auftriebAktuell) < -1) || ((schubTemp - schubAktuell) > 1) || ((schubTemp - schubAktuell) < -1)) {
				this.chartPanel.repaint();
			}
			
			auftriebTemp = auftriebAktuell;
			schubTemp = schubAktuell;
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public JPanelFis getChartPanel() {
		return this.chartPanel;
	}
	
	public void update() {
		this.chartPanel.repaint();
	}
}
