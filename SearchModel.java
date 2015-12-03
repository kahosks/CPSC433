import java.util.*;
public class SearchModel {
	//Need to add notAssigned and index array to accommodate the new prob structure
	
	//ArrayList<Class> notAssigned; May not be needed depth could be used
	String[] indexArray;
	
	ArrayList<Class> classesToSchedule;
	private int pen_coursemin =0;
	private int pen_secdiff=0;
	private int pen_notpaired=0;
	private int pen_pref=0;
	
	private int numMondaySlots;
	private int numMondayLabSlots;
	private int numTuesdaySlots;
	private int numTuesdayLabSlots;
	private int numFridaySlots;
	private Slot[] courses;
	private Slot[] labs;
	private Constraint[] constr;
	
	private ArrayList<Parser.PairedCourseClass> pairs;
	CommandParser commandParser;
	
	public SearchModel(ArrayList<Class> classesToSchedule, ArrayList<Parser.PairedCourseClass> pairs) {
		this.pairs = pairs;
		this.classesToSchedule = classesToSchedule;

	}
	SearchModel(ArrayList<Class> labsAndCourses) {
		//this.labsAndClasses = labsAndCourses;
	}
	SearchModel(String[] aIndexArray, CommandParser commPar, Object[] aSlot ) {
	
		indexArray = aIndexArray;
		commandParser = commPar;
		courses = (Slot[]) aSlot[0];
		labs = (Slot[]) aSlot[1];
		
	}
	SearchModel(String[] aIndexArray, CommandParser commPar, Object[] aSlot, Constraint[] aConstr ) {
		
		indexArray = aIndexArray;
		commandParser = commPar;
		courses = (Slot[]) aSlot[0];
		labs = (Slot[]) aSlot[1];
		constr = aConstr;
		
	}
	SearchModel(String[] aIndexArray, int[] slotSizes) {
		
		indexArray = aIndexArray;
		numMondaySlots = slotSizes[0];
		numTuesdaySlots = slotSizes[1];
		numTuesdayLabSlots = slotSizes[2];
		numFridaySlots = slotSizes[3];
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
			//p.setEvalValue(eval(p));
			//eval -> check that it is better or equal to the bound value
			//div
			//return div(p);
			return null;
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
		
		indexToScheduleClassLab = prob[0];
		if (prob[0] >= prob.length) {
			return null;
		}
		String identifier = indexArray[indexToScheduleClassLab];
		//Class classOrLabToSchedule;
		
		
		indexToScheduleClassLab = prob[0];
		boolean isClass;		
			
		if (identifier.contains("TUT") || identifier.contains("LAB")) {
				//I don't have the actual numbers here these are just estimates if anyone has
				//the actual numbers please update the code
			//int numMondaySlots = 11;
			//int numTuesdaySlots = 5;
			numSlots = labs.length;
			isClass = false;
						
		} else {
			
			//This case means we are scheduling a Lab
			//int numMondaySlots = 11;
			//int numTuesdayLabsSlots = 11;
			//int numFridaySlots = 4;

			numSlots = courses.length;
			isClass = true;			
			
		}
		int[][] probArray = new int[numSlots][prob.length];
		//indexArray[indexToScheduleClassLab] = classOrLabToSchedule.getIdentifier();
		int j = 0;
		for (int i = 0; i < numSlots; i++) {
			
			probArray[i] = prob.clone();	//Set the current index to be the old version of prob that has been sent in
			if (isClass) {
				probArray[i][indexToScheduleClassLab] = courses[i].getDayTimeInt();
			} else {
				probArray[i][indexToScheduleClassLab] = labs[i].getDayTimeInt();
			} 
		 
			
			//May want to do a call to hard constraints here so that if it fails hardconstraints we can 
			//handle that somehow
			if(passConstr(probArray[i], constr)) {
				probArray[j] = probArray[i].clone();
				probArray[j][1] = eval(probArray[i]);
				probArray[j][0] +=1;
				j++;
			} else {
				probArray[i][indexToScheduleClassLab] = 0;
			}
		}
		int[][] newprobArray = new int[j][prob.length];
		for (int i = 0; i < j; i++) {
			newprobArray[i] = probArray[i];
			
		}
		return newprobArray;
	}
	
	
	//Wont be just an example of how to run through the constraint set (should probably be in DIV)
	public boolean passConstr(int[] prob, Constraint[] hc){
		boolean noFails = true;
		for(int i = 0; noFails && (i<(hc.length-1)); i++){
			noFails= hc[i].testHard(prob);
			
		}
		return noFails;
	}
		
	//Not exactly sure how to handle what integer we use for a slot
	private int computeSlot(int i, boolean isClass) {
	
		if (isClass) {
			return i+ 1;
		} else {
			return i + 1;
		}
	
	//actually should return a valid value here
	//return 0;
	
	}
	
	
	private int eval(int[] p) {
		//int mod_Minfilled = commandParser.getMinfilled();
		//int mod_Pref = commandParser.getPref();
		//int mod_Pair = commandParser.getPair();
		//int mod_SecDiff = commandParser.getSecdiff();
		
		//below will only be commented so that it be uncommented once the soft constraints 
		//have been integerated
		
		// int minFilled = mod_Minfilled * aClass.getMinFilledConstr(p);
		// int pref = mod_Pref * aClass.getPrefConstr(p);
		// int pair = mod_Par * aClass.getPairConstr(p);
		// int secDiff = mod_SecDiff * aClass.getSecDiffConstr(p);
		
		//int eval = minFilled + pref + pair + secDiff;
		return 0;
	}
	//For testing
	public static void main(String[] args) {
		int[] prob = { 2 ,0, 0,0}; //Monday has 3 slots,  TuesdayLab has 2 slots
		String[] classes = {"", "","CPSC 433 LEC 01", "SENG 311 LEC 01 TUT 01"};
		int numTestMondaySlots = 3;
		int numTestTuesdaySlots = 0;
		int numTestTuesdayLabSlots = 2;
		int numTestFridaySlots = 0;
		
		int [] testSlotSizes = {numTestMondaySlots, numTestTuesdaySlots, numTestTuesdayLabSlots, numTestFridaySlots};
		SearchModel search = new SearchModel(classes, testSlotSizes);
		//int [][] probInstances0 = search.div(prob);
		int [][] probInstances = search.div(prob);
		//~ int [][] probInstances = search.div(probInstances0[0]);
		for (int i = 0; i < probInstances.length; i++) {
			int[] testProb = probInstances[i];
			for (int j = 2; j <testProb.length; j++) {
				if ( testProb[j] == 0) {
					System.out.println("No class at slot" + testProb[j]);
				} else {
					System.out.println("Depth is " + testProb[0]);
					System.out.println(classes[j] + "Is at slot" + testProb[j]);
				}
			}
		}
		
		
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
