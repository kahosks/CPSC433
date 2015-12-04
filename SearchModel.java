
import java.util.ArrayList;
import java.util.Arrays;

//import HardConstraints.Constraint;

public class SearchModel {
	//Need to add notAssigned and index array to accommodate the new prob structure
	
	//ArrayList<Class> notAssigned; May not be needed depth could be used
	String[] indexArray;
	
	ArrayList<Class> classesToSchedule;
	private int weight_labmin = 1;
	private int weight_coursemin = 1;
	private int weight_secdiff = 1;
	private int weight_notpaired = 1;
	private int weight_pref = 1;
	
	//Does any of this matter? we know the 
/*	private int numMondaySlots;
	private int numMondayLabSlots;
	private int numTuesdaySlots;
	private int numTuesdayLabSlots;
	private int numFridaySlots;*/
	private Slot[] courses;
	private Slot[] labs;
	private Constraint[] constr;
	
	private CommandParser commandParser;
	private SoftConstraints softConstraints;


	SearchModel(String[] aIndexArray, CommandParser commPar, Object[] aSlot ) {
		indexArray = aIndexArray;
		commandParser = commPar;
		courses = (Slot[]) aSlot[0];
		labs = (Slot[]) aSlot[1];
		getWeights(commPar);
	}
	SearchModel(String[] aIndexArray, CommandParser commPar, Object[] aSlot, Constraint[] aConstr ) {
		
		indexArray = aIndexArray;
		commandParser = commPar;
		courses = (Slot[]) aSlot[0];
		labs = (Slot[]) aSlot[1];
		constr = aConstr;
		getWeights(commPar);
		
	}
	
	SearchModel(String[] aIndexArray, CommandParser commPar, Object[] aSlot, 
	Constraint[] aConstr, SoftConstraints aSoftConstr) {
		this(aIndexArray, commPar, aSlot, aConstr);
		softConstraints = aSoftConstr;
		
		
	}
/*	SearchModel(String[] aIndexArray, int[] slotSizes) {
		
		indexArray = aIndexArray;
		numMondaySlots = slotSizes[0];
		numTuesdaySlots = slotSizes[1];
		numTuesdayLabSlots = slotSizes[2];
		numFridaySlots = slotSizes[3];
	}
*/
	/**
	 * Multiplies weights by penalties and sets those values.
	 */ 
	private void getWeights(CommandParser cp) {
		weight_labmin = cp.getMinlab() * cp.getPenMinlab();
		weight_coursemin= cp.getMincourse() * cp.getPenMincourse();
		weight_pref = cp.getPref();
		weight_notpaired = cp.getPair() * cp.getPenPair();
		weight_secdiff = cp.getSecdiff() * cp.getPenSecdiff();
		
	}
	
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
		prob[0]+=1;
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
	
	/**
	 * Tests an instance of a problem on a set of hard constraints
	 * @param prob: the problem being tested
	 * @param hc: the set of hard constraints being tested
	 * @return boolean true if all constraints passed, false otherwise
	 */
	private boolean passConstr(int[] prob, Constraint[] hc){
		boolean noFails = true;
		for(int i = 0; noFails && (i<hc.length); i++){
			noFails= hc[i].testHard(prob);
/*			if(!noFails){
				System.out.println("Threw away prob"+hc[i]);
			}*/
			
		}
		return noFails;
	}
		
	private int eval(int[] p) {
		//int mod_Minfilled = commandParser.getMinfilled();
		//int mod_Pref = commandParser.getPref();
		//int mod_Pair = commandParser.getPair();
		//int mod_SecDiff = commandParser.getSecdiff();
		
		//below will only be commented so that it be uncommented once the soft constraints 
		//have been integerated
		
		
		 int minFilled = softConstraints.getMinFilled(p);
		 int pref = softConstraints.getPref(p);
		 int pair = softConstraints.getPair(p);
		 int secDiff = softConstraints.getSecDiff(p);
		
		
		int eval = minFilled + pref + pair + secDiff;
		return eval;
		//return minFilled + pref + pair + secDiff;
	}
}
	

