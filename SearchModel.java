import java.util.*;
public class SearchModel {
	//Need to add notAssigned and index array to accommodate the new prob structure
	ArrayList<Class> notAssigned;
	String[] indexArray;
	
	ArrayList<Class> classesToSchedule;
	private int pen_coursemin =0;
	private int pen_secdiff=0;
	private int pen_notpaired=0;
	private int pen_pref=0;
	private ArrayList<Parser.PairedCourseClass> pairs;
	CommandParser commandParser;
	
	public SearchModel(ArrayList<Class> classesToSchedule, ArrayList<Parser.PairedCourseClass> pairs) {
		this.pairs = pairs;
		this.classesToSchedule = classesToSchedule;

	}
	SearchModel(ArrayList<Class> labsAndCourses) {
		//this.labsAndClasses = labsAndCourses;
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
	/*Old version of div
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
	*/
	
	//New version of Div that uses the new structure of an array instead of a class
	
	/*
	
		@param prob[] an array that encapsulates the information inside a single problem instance
	
	*/
	
	public int[][] div(int[] prob) {
		int numSlots;					//Used to determine how many prob.length arrays to return
		int indexToScheduleClassLab;	//used to find the index in which we want to add a class or a lab
		
		Class classOrLabToSchedule;
		
		int[][] probArray = new int[numSlots][prob.length];
		indexToScheduleClassLab = prob[0];
		boolean isClass;		
			
		if (classOrLabToSchedule.isCourse()) {
				//I don't have the actual numbers here these are just estimates if anyone has
				//the actual numbers please update the code
			int numMondaySlots = 11;
			int numTuesdaySlots = 5; 
			numSlots = numMondaySlots + numTuesdaySlots;
			isClass = true;
						
		} else {
			
			//This case means we are scheduling a Lab
			int numMondaySlots = 11;
			int numTuesdaySlots = 11;
			int numFridaySlots = 4;
			numSlots = numMondaySlots + numTuesdaySlots + numFridaySlots;
			isClass = false;			
			
		}
		indexArray[indexToScheduleClassLab] = classOrLabToSchedule.getIdentifier();
		
		for (int i = 0; i < numSlots; i++) {
			
			probArray[i] = prob;	//Set the current index to be the old version of prob that has been sent in
			probArray[i][indexToScheduleClassLab] = computeSlot(i, isClass);
			
			//May want to do a call to hard constraints here so that if it fails hardconstraints we can 
			//handle that somehow
			probArray[i][1] = eval(probArray[i][indexToScheduleClassLab]);
			
		}
		
		return probArray;
			
			
		}
		
	//Not exactly sure how to handle what integer we use for a slot
	private int computeSlot(int i, boolean isClass) {
	
	//actually should return a valid value here
	return 0;
	
	}
	
	
	private int eval(int[] p) {
		int mod_Minfilled = commandParser.getMinfilled();
		int mod_Pref = commandParser.getPref();
		int mod_Pair = commandParser.getPair();
		int mod_SecDiff = commandParser.getSecDiff();
		
		//below will only be commented so that it be uncommented once the soft constraints 
		//have been integerated
		
		// int minFilled = mod_Minfilled * aClass.getMinFilledConstr(p);
		// int pref = mod_Pref * aClass.getPrefConstr(p);
		// int pair = mod_Par * aClass.getPairConstr(p);
		// int secDiff = mod_SecDiff * aClass.getSecDiffConstr(p);
		
		//int eval = minFilled + pref + pair + secDiff;
	}
	}
	
	//OLD version of eval
	/*
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
}*/
