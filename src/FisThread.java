import org.jfree.chart.JFreeChart;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.plot.JPanelFis;


public class FisThread extends Thread {
	private FIS fis;
	private Thread thread;
	private Data data;
	private JPanelFis chartPanel;
	
	public FisThread(Data data, FIS fis, JPanelFis chartPanel){
		super();
		this.chartPanel = chartPanel;
		
		this.data = data;
		this.fis = fis;

		
		fis.setVariable("oben", 100);
		fis.setVariable("frontOben", 100);
		fis.setVariable("front", 100);
		fis.setVariable("frontUnten", 100);
		fis.setVariable("unten", 100);

		start();
	}
	
	public void run() {
		while(true){
		//System.out.println("DATA oben: "+data.getOben());
		fis.setVariable("oben", data.getOben());
		//System.out.println("DATA unten: "+data.getUnten());
		fis.setVariable("unten", data.getUnten());
		//System.out.println("DATA front: "+data.getFront());
		fis.setVariable("front", data.getFront());

		fis.evaluate();
//		System.out.println("VALUE: "+fis.getVariable("auftrieb").getLatestDefuzzifiedValue());
		data.setAuftrieb(fis.getVariable("auftrieb").getLatestDefuzzifiedValue());
		chartPanel.repaint();
		//fis.getVariable("auftrieb").chartDefuzzifier(true);
		}	
	}
	
	public synchronized void setVariable(String name, double wert){
		fis.setVariable(name, wert);
	}

}
