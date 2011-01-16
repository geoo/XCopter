import java.awt.Container;

import javax.swing.BoxLayout;
import javax.swing.JApplet;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.plot.JPanelFis;

import org.antlr.runtime.RecognitionException;


public class Init extends JApplet{

	private static final long serialVersionUID = 1L;
	private FIS fis;
	private JPanel boxPanel = new JPanel();
	private Container container = getContentPane();
	private JPanelFis chartPanel;
	
	public void loadFuzzyData() {
		String fcl = "FUNCTION_BLOCK helicopter\n" +
			"\n" +
			"VAR_INPUT\n" +
			"   oben : REAL;\n" +
			"   frontOben : REAL;\n" +
			"	front : REAL;\n" +
			"	frontUnten : REAL;\n" +
			"	unten : REAL;\n" +
			"END_VAR\n" +
			"\n" +
			"VAR_OUTPUT\n" +
			"   auftrieb : REAL;\n" +
			"	schub : REAL;\n" +
			"END_VAR\n" +
			"\n" +
			"FUZZIFY oben\n" +
			"   TERM nah := (-14,0) (-4,1) (15,1) (25,0);\n" +
			"   TERM mittel := (15,0) (25,1) (45,1) (55,0);\n" +
			"   TERM weit := (45,0) (55,1) (75,1) (85,0);\n" +
			"	TERM sehr_weit := (75,0) (85,1) (110,1) (120,0);\n" +
			"END_FUZZIFY\n" +
			"\n" +
			"FUZZIFY frontOben\n" +
			"	TERM nah := (-14,0) (-4,1) (15,1) (25,0);\n" +
			"	TERM mittel := (15,0) (25,1) (45,1) (55,0);\n" +
			"	TERM weit := (45,0) (55,1) (75,1) (85,0);\n" +
			"	TERM sehr_weit := (75,0) (85,1) (110,1) (120,0);\n" +
			"END_FUZZIFY\n" +
			"\n" +
			"FUZZIFY front\n" +
			"   TERM nah := (-14,0) (-4,1) (15,1) (25,0); \n" +
			"   TERM mittel := (15,0) (25,1) (45,1) (55,0);\n" +
			"   TERM weit := (45,0) (55,1) (75,1) (85,0);\n" + 
			"	TERM sehr_weit := (75,0) (85,1) (110,1) (120,0);\n" +
			"END_FUZZIFY\n" +
			"\n" +
			"FUZZIFY frontUnten\n" +
			"   TERM nah := (-14,0) (-4,1) (15,1) (25,0);\n" +
			"   TERM mittel := (15,0) (25,1) (45,1) (55,0);\n" +
			"   TERM weit := (45,0) (55,1) (75,1) (85,0);\n" +  
			"	TERM sehr_weit := (75,0) (85,1) (110,1) (120,0);\n" +
			"END_FUZZIFY\n" +
			"\n" +
			"FUZZIFY unten\n" +
			"   TERM nah := (-14,0) (-4,1) (15,1) (25,0);\n" + 
			"   TERM mittel := (15,0) (25,1) (45,1) (55,0);\n" +
			"   TERM weit := (45,0) (55,1) (75,1) (85,0);\n" +   
			"	TERM sehr_weit := (75,0) (85,1) (110,1) (120,0);\n" +
			"END_FUZZIFY\n" +
			"\n" +
			"DEFUZZIFY auftrieb\n" +
			"   TERM starkerSinkflug := (-12,0) (-5,1) (5,1) (12,0);\n" +
			"	TERM sinkflug := (5,0) (12,1) (22,1) (29,0);\n" +
			"   TERM leichterSinkflug := (22,0) (29,1) (39,1) (45,0);\n" +
			"   TERM hoeheBeibehalten := (39,0) (45,1) (55,1) (61,0);\n" +
			"	TERM leichterAuftrieb := (55,0) (61,1) (71,1) (78,0);\n" +
			"	TERM auftrieb := (71,0) (78,1) (88,1) (95,0);\n" +
			"   TERM starkerAuftrieb := (88,0) (95,1) (105,1) (112,0);\n" +
			"   METHOD : COG;\n" +
			"   DEFAULT := 0;\n" +
			"END_DEFUZZIFY\n" +
			"\n" +
			"DEFUZZIFY schub\n" +
			"   TERM sehr_langsam := (-20,0) (-10,1) (10,1) (20,0);\n" +
			"	TERM langsam := (10,0) (20,1) (45,1) (55,0);\n" +
			"   TERM normal := (45,0) (55,1) (80,1) (90,0);\n" +
			"	TERM schnell := (80,0) (90,1) (110,1) (120,0);\n" +
			"   METHOD : COG;\n" +
			"   DEFAULT := 0;\n" +
			"END_DEFUZZIFY\n" +
			"\n" +
			"RULEBLOCK No1\n" +
			"   AND : MIN;\n" +
			"   ACT : MIN;\n" +
			"   ACCU : MAX;\n" +
			"\n" +
			"   RULE 1 : IF oben IS sehr_weit THEN auftrieb IS hoeheBeibehalten;\n" +
			"   RULE 2 : IF oben IS weit THEN auftrieb IS leichterSinkflug;\n" +
			"   RULE 3 : IF oben IS mittel THEN auftrieb IS sinkflug;\n" +
			"   RULE 4 : IF oben IS nah THEN auftrieb IS starkerSinkflug;\n" +
			"	RULE 5 : IF unten IS sehr_weit THEN auftrieb IS hoeheBeibehalten;\n" +
			"	RULE 6 : IF unten IS weit THEN auftrieb IS leichterAuftrieb;\n" +
			"	RULE 7 : IF unten IS mittel THEN auftrieb IS auftrieb;\n" +
			"	RULE 8 : IF unten IS nah THEN auftrieb IS starkerAuftrieb;\n" +
			"	RULE 9 : IF front IS sehr_weit THEN schub IS schnell;\n" +
			"	RULE 10 : IF front IS weit THEN schub IS normal;\n" +
			"	RULE 11 : IF front IS mittel THEN schub IS langsam;\n" +
			"	RULE 12 : IF front IS nah THEN schub IS sehr_langsam;\n" +
			"	RULE 13 : IF frontOben IS sehr_weit THEN auftrieb IS hoeheBeibehalten;\n" +
			"	RULE 14 : IF frontOben IS weit THEN auftrieb IS leichterSinkflug;\n" +
			"	RULE 15 : IF frontOben IS weit THEN schub IS normal;\n" +
			"	RULE 16 : IF frontOben IS mittel THEN auftrieb IS sinkflug;\n" +
			"	RULE 17 : IF frontOben IS mittel THEN schub IS langsam;\n" +
			"	RULE 18 : IF frontOben IS nah THEN auftrieb IS starkerSinkflug;\n" +
			"	RULE 19 : IF frontOben IS nah THEN schub IS sehr_langsam;\n" +
			"	RULE 20 : IF frontUnten IS sehr_weit THEN auftrieb IS hoeheBeibehalten;\n" +
			"	RULE 21 : IF frontUnten IS weit THEN auftrieb IS leichterAuftrieb;\n" +
			"	RULE 22 : IF frontUnten IS weit THEN schub IS normal;\n" +
			"	RULE 23 : IF frontUnten IS mittel THEN auftrieb IS auftrieb;\n" +
			"	RULE 24 : IF frontUnten IS mittel THEN schub IS langsam;\n" +
			"	RULE 25 : IF frontUnten IS nah THEN auftrieb IS starkerAuftrieb;\n" +
			"	RULE 26 : IF frontUnten IS nah THEN schub IS sehr_langsam;\n" +
			"\n" +
			"END_RULEBLOCK\n" +
			"\n" +
			"END_FUNCTION_BLOCK";
	
		
		try {
			fis = FIS.createFromString(fcl, true);
		} catch (RecognitionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (fis == null) { // Error while loading?
			System.err.println("Can't load file: ''");
		}	
		
		fis.setVariable("oben", 100);
		fis.setVariable("frontOben", 100);
		fis.setVariable("front", 100);
		fis.setVariable("frontUnten", 100);
		fis.setVariable("unten", 100);
	}
	
	public void drawFuzzyCharts() {
		chartPanel = new JPanelFis(fis);
		boxPanel.add(chartPanel);
	}
	
	public void init() {
		setSize(1150, 800);
		
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.LINE_AXIS));
					
					container.add(boxPanel);
					
					loadFuzzyData();
					drawFuzzyCharts();
					HelicopterForm helicopter;
					Thread helicopterThread = new Thread(helicopter = new HelicopterForm(fis, chartPanel));	
					boxPanel.add(helicopter.getContainer());
				}
			});
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public static void main(String[] args) {

	}

}
