import org.jfree.chart.JFreeChart;

import net.sourceforge.jFuzzyLogic.FIS;


public class FisThread  extends Thread {
	private FIS fis;
	private Thread thread;
	private Data data;
	private JFreeChart test;
	
	public FisThread(Data data){
		
		super();
		
		this.data = data;
		String fileName = "fcl.fcl";
		fis = FIS.load(fileName, true);
		fis.toStringFcl();

		if (fis == null) { // Error while loading?
			System.err.println("Can't load file: '" + fileName + "'");
			return;
		}
		fis.chart();
		fis.setVariable("oben", 100);
		fis.setVariable("frontOben", 100);
		fis.setVariable("front", 100);
		fis.setVariable("frontUnten", 100);
		fis.setVariable("unten", 100);
		// fis.getFunctionBlock("helicopter").getVariable("oben").chart(true);
		test = fis.getVariable("auftrieb").chartDefuzzifier(true);
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
		System.out.println("VALUE: "+fis.getVariable("auftrieb").getLatestDefuzzifiedValue());
		data.setAuftrieb(fis.getVariable("auftrieb").getLatestDefuzzifiedValue());
		//fis.getVariable("auftrieb").chartDefuzzifier(true);
}
		//fis.evaluate();
	//	fis.getVariable("auftrieb").chartDefuzzifier(true);
		
	}
	
	public synchronized void setVariable(String name, double wert){
		fis.setVariable(name, wert);
	}

}
