import java.util.*;
public class SearchModel {
	ArrayList<Class> classesToSchedule;
	private int pen_coursemin =0;
	private int pen_secdiff=0;
	private int pen_notpaired=0;
	private int pen_pref=0;
	private ArrayList<Parser.PairedCourseClass> pairs;
	
	public SearchModel(ArrayList<Class> classesToSchedule, ArrayList<Parser.PairedCourseClass> pairs) {
		this.pairs = pairs;
		this.classesToSchedule = classesToSchedule;

	}
	SearchModel(ArrayList<Class> labsAndCourses) {
		this.labsAndClasses = labsAndClasses;
	}
	/*Prob[] div(Prob p, int fBound) {
		//loop structure
		
		//Figure out what classes are already in prob
		ArrayList<Prob> divList;
		labsAndClasses.removeAll(p.getclassesScheduled());
		
		Slot[][] slots = prob.getDays();
		
		//Sorry about the weird naming...
		for (Slot[] s_1:slots){
			 for (Slot s_actual:s_1) {
				 copyProb = new Prob(p);
				 
				 //copyProb.add(); Fix this up once Prob has been finalized, use 
				 //s_actual
				 
				 
				 
				 divList.add(copyProb);
			 }
		}
		
		
		
		
		return divList.toArray();
	}
	
	private int eval(CommandParser commPar, Prob p) {
		int eval = 0;
		
		for (Slot[] s_1:slots) {
			for (Slot s_actual:s_1) {
				
			}
		}
		//Check coursmin(s)
		//Check labsmin(s)
		
		//Check proffessor preferences
		
		// 
		
		return 0;	
	}
	
	*/

	public Prob[] checkValidAndDiv(Prob p) {
		HardConstraints hc =  new CourseMax();
		if (hc.checkAllHardConstraints(p)) {
			p.setEvalValue(eval(p));
			//eval -> check that it is better or equal to the bound value
			//div
			return div(p);
		}
		else {
			return null;
		}
	}
	//Check for syntax errors
	public Prob[] div(Prob p) {
		//loop structure
		Prob copyProb;
		//Figure out what classes are already in prob
		ArrayList<Prob> divList = new ArrayList<Prob>();
		classesToSchedule.removeAll(p.getClassesScheduled());
		
		Slot[][] slots = p.getDays();
		
		//Sorry about the weird naming...
		for (Slot[] sl:slots){
			 for (Slot s_actual:sl) {
				 copyProb = new Prob(p);
				 
				 //copyProb.add(); //Fix this up once Prob has been finalized, use 
				 //s_actual
				 divList.add(copyProb);
			 }
		}	
		return (Prob[]) divList.toArray();
	}
	//Should work, but might be syntax errore
	private int eval(Prob p) {
		int eval = 0;
		int penalty =0;
		for (Slot[] sl:p.getDays()) {
			Eval[] evalArr = {new PenCourseMin(sl, pen_coursemin), new PenLabmin(sl, pen_coursemin),
					new PenNotpaired(sl, pen_notpaired, pairs), new PenSecdiff(sl, pen_secdiff),
					 new PenPref(sl, pen_pref)};
			penalty = new PenPref().evaluateAll(evalArr);
		}
		return penalty;

		//Check coursmin(s)
		//Check labsmin(s)
		
	}
}
