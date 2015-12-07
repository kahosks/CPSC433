
/**
 * Class Scheduler where everything will occur.
 * @author CPSC 433 Toshibe
 */
import java.util.ArrayList;
//import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;


public class Scheduler {
	// Scheduler Variables
	private CommandParser cp;
	
	// Parser Variables
	//private String name;
	private ArrayList<Class> labsAndCourses;
	/*private ArrayList<PairedCourseClass> notCompatible;*/
	private ArrayList<PairedCourseClass> pairs;	//hold pairs; see around line 340
	private ArrayList<Preference> preferences;
	/*private ArrayList<ParserClass> unwanted;
	private ArrayList<ParserClass> partassign;*/
	public static int totalCourses = 0;
	private String[] indexArray;
	private Constraint[] constr;
	private SoftConstraints sconstr;
	private SearchModel searchModel;
	
	private Slot[] M;
	private Slot[] MLabs;
	private Slot[] TCourses;	//will have to put labs in Slot, possible in two Slot
	private Slot[] TLabs;
	private Slot[] FLabs;
	
	// Heap Variables
	private PriorityQueue<int[]> pq;
	private int[] bestSolution;
	private int PROBLEM_LENGTH;
	private int BAD_PROBLEM_SCORE = Integer.MAX_VALUE;

	
	/**
	 * Empty constructor.
	 * : Perhaps pass args through constructor, and make start()
	 *  have no arguments.
	 */
	public Scheduler() {
		
	}
	/**
	 * Method that, when called, will run through the whole code.
	 * : Lots of things to be added.
	 * 
	 * @param args	Arguments to be parsed.
	 * @throws SchedulerException	Throws if error occurs in code.
	 */
	public void start(String[] args) throws SchedulerException{
		cp = new CommandParser(args);
		Parser parser = new Parser(cp.getFilename());
		parser.parse();
		initiateParsedValues(parser);
		//intializeConstraints();
		heapIntializer(parser.getInitialProblem());
		makeSchedule();
		if(bestSolution[1] == BAD_PROBLEM_SCORE){
			System.out.println("There was no valid solution found");
		}
		else{
			System.out.println("The best solution is:");
			OutputSchedule out = new OutputSchedule(indexArray, bestSolution);
			out.output();
		}
		
	}
	/**
	 * Method that gets the parsed values from the parser and 
	 * stores them in their respected ArrayList.
	 * @param parser	Parser that parses the input file.
	 */
	private void initiateParsedValues(Parser parser) {
//		name = parser.getName();
		labsAndCourses = parser.getLabsAndCourses();
//		notCompatible = parser.getNC();
		pairs = parser.getPairs();
		preferences = parser.getPreferences();
//		unwanted = parser.getUnwanted();
//		partassign = parser.getPartassign();
		M = (Slot[])parser.getMO();
		MLabs = parser.getMLabs();
		TCourses = parser.getTCourses();
		TLabs = parser.getTLabs();
		FLabs = parser.getFLabs();
		totalCourses = labsAndCourses.size();
		indexArray = parser.getIndexArray();
		initializeConstraints(parser);

	}
	
	/*
	 * Setup the heap (Priority Queue)
	 */
	private void heapIntializer(int[] prob) {
		
		pq = new PriorityQueue<int[]>(new ScheduleComparator());
		pq.add(prob);
		PROBLEM_LENGTH = prob.length;
		bestSolution = new int[PROBLEM_LENGTH];
		bestSolution[1] = BAD_PROBLEM_SCORE;
	}
	/*
	 * Setup the Soft and Hard constraints so that they can be passed to the search model
	 */
	private void initializeConstraints(Parser parser) {
		//  initialize soft constraints
		sconstr = new SoftConstraints(indexArray,prepSlotsForSoftContraints(), preferences, pairs);
		constr = parser.getHardConstraints();
	}
	
	private void makeSchedule() throws SchedulerException{
		
		if (pq.size() == 0) {
			throw new SchedulerException("No starting problem."); //Can't make the scheduler with nothing in the queue
		}
		OutputSchedule out1 = new OutputSchedule(indexArray, pq.peek());
		out1.output();
		
		searchModel = new SearchModel(indexArray, cp,
		 prepSlotArrayForSearchModel(), constr, sconstr);
		
		int[][] newProblems;
		
		//boolean foundBest = false;
		//OutputSchedule out314 = new OutputSchedule(indexArray, bestSolution);
		while (!pq.isEmpty()) {
			if(pq.peek()[1] > bestSolution[1]) {
				pq.remove();
				System.out.println("Threw away from eval");
			}
			else {
				newProblems = searchModel.div(pq.poll());
				if(newProblems != null) {
					//System.out.println(pq.size());
					for(int[] pr: newProblems) {
						/* if the current problem has a depth greater than the length
						 * of the array it is done, if it also has an eval greater than
						 * the best it is the new best
						 */
						// System.out.println("pr is " + pr[0]);
						if (( pr[0] >= PROBLEM_LENGTH) &&   (bestSolution[1] > pr[1])){
							bestSolution = pr.clone();
							
							OutputSchedule out314 = new OutputSchedule(indexArray, bestSolution);
							out314.output();
							System.out.println("\n");	
							//System.out.println(Arrays.toString(pr));
							//out314.setCourseTimes(bestSolution);
							//out314 = new OutputSchedule(indexArray, bestSolution);
							//out314.writeToFile();
						}
						else if ((PROBLEM_LENGTH > pr[0]) && (pr[1] < bestSolution[1])){			
							pq.add(pr);
							//System.out.println(pq.size());
						}
							
					}
					
				}
			}
			
		}
		OutputSchedule out = new OutputSchedule(indexArray, bestSolution);
		out.output();
		
	}
	
	private Object[] prepSlotArrayForSearchModel() {
		Object[] oArray = new Object[2];
		
		
		// ############### use this if the slots given in the input file are the only valid slots ###############
		Slot[] temp = new Slot[M.length + TCourses.length];
		int i;
		
		for (i = 0; i < M.length; i++) {
			temp[i] = M[i];
		}
		for (int j = 0; j < TCourses.length; j++, i++) {
			temp[i] = TCourses[j];
		}
		
		oArray[0] = temp.clone();
		
		temp = new Slot[MLabs.length + TLabs.length + FLabs.length];
		
		for (i = 0; i < MLabs.length; i++) {
			temp[i] = MLabs[i];
		}
		for (int j = 0; j < TLabs.length; j++, i++) {
			temp[i] = TLabs[j];
		}
		for (int j = 0; j < FLabs.length; j++, i++) {
			temp[i] = FLabs[j];
		}
		
		oArray[1] = temp.clone();
		// ############### ############### ############### ############### ############### ###############
		
		
		
		// ############### use this if all the slots are valid slots ###############
		/*Slot[] courseSlots = new Slot[21];
		Slot[] labSlots = new Slot[32];
		
		
		for (int i = 800, j = 0; i < 2100; i+=100, j++) {
			courseSlots[j] = labSlots[j] = new Slot("MO", Integer.toString(i));
			labSlots[j+13] = new Slot("TU", Integer.toString(i));
		}
		// Monday labs and lectures
		courseSlots[0] = labSlots[0] = new Slot("MO", "8:00");
		courseSlots[1] = labSlots[1] = new Slot("MO", "9:00");
		courseSlots[2] = labSlots[2] = new Slot("MO", "10:00");
		courseSlots[3] = labSlots[3] = new Slot("MO", "11:00");
		courseSlots[4] = labSlots[4] = new Slot("MO", "12:00");
		courseSlots[5] = labSlots[5] = new Slot("MO", "13:00");
		courseSlots[6] = labSlots[6] = new Slot("MO", "14:00");
		courseSlots[7] = labSlots[7] = new Slot("MO", "15:00");
		courseSlots[8] = labSlots[8] = new Slot("MO", "16:00");
		courseSlots[9] = labSlots[9] = new Slot("MO", "17:00");
		courseSlots[10] = labSlots[10] = new Slot("MO", "18:00");
		courseSlots[11] = labSlots[11] = new Slot("MO", "19:00");
		courseSlots[12] = labSlots[12] = new Slot("MO", "20:00");
		// Tuesday lectures
		courseSlots[13] = new Slot("TU", "8:00");
		courseSlots[14] = new Slot("TU", "9:30");
		courseSlots[15] = new Slot("TU", "11:00");
		courseSlots[16] = new Slot("TU", "12:30");
		courseSlots[17] = new Slot("TU", "14:00");
		courseSlots[18] = new Slot("TU", "15:30");
		courseSlots[19] = new Slot("TU", "17:00");
		courseSlots[20] = new Slot("TU", "18:30");
		// Tuesday labs
		labSlots[13] = new Slot("TU", "8:00");
		labSlots[14] = new Slot("TU", "9:00");
		labSlots[15] = new Slot("TU", "10:00");
		labSlots[16] = new Slot("TU", "11:00");
		labSlots[17] = new Slot("TU", "12:00");
		labSlots[18] = new Slot("TU", "13:00");
		labSlots[19] = new Slot("TU", "14:00");
		labSlots[20] = new Slot("TU", "15:00");
		labSlots[21] = new Slot("TU", "16:00");
		labSlots[22] = new Slot("TU", "17:00");
		labSlots[23] = new Slot("TU", "18:00");
		labSlots[24] = new Slot("TU", "19:00");
		labSlots[25] = new Slot("TU", "20:00");
		// Friday labs
		labSlots[26] = new Slot("FR", "8:00");
		labSlots[27] = new Slot("FR", "10:00");
		labSlots[28] = new Slot("FR", "12:00");
		labSlots[29] = new Slot("FR", "14:00");
		labSlots[30] = new Slot("FR", "16:00");
		labSlots[31] = new Slot("FR", "18:00");
		
		oArray[0] = courseSlots.clone();
		oArray[1] = labSlots.clone();*/
		
		// ############### ############### ############### ############### ############### ###############
		
		return oArray;
	}
	
	private Slot[] prepSlotsForSoftContraints(){
	
		Slot[] ret = new Slot[M.length + TCourses.length +
		 MLabs.length + TLabs.length + FLabs.length];
//		int i = 0;
		
		for (int j = 0; j < M.length; j++) {
			ret[j] = M[j];
		}
		
		for (int j = 0; j < TCourses.length; j++) {
			ret[j + M.length] = TCourses[j];
		}
		
		for (int j = 0; j < MLabs.length; j++) {
			ret[j+ M.length + TCourses.length] = MLabs[j];
		}
		
		for (int j = 0; j < TLabs.length; j++) {
			ret[j+ M.length + TCourses.length + MLabs.length] = TLabs[j];
		}
		
		for (int j = 0; j < FLabs.length;j++) {
			ret[j + M.length + TCourses.length + MLabs.length  + TLabs.length] = FLabs[j];
		}
	return ret;
	}
	

	public static void main(String[] args) throws SchedulerException{
		Scheduler sch = new Scheduler();
		sch.start(args);
	}
	
	public class ScheduleComparator implements Comparator<int[]> {
		/*	Returns -1 if p1's depth value is greater than p2's.  
		 * 	Returns 1 if p1's depth value is less than p2's.
		 * 	
		 * 	If p1 and p2 have equal Depth values then it will compare their eval values
		 * 	Returns -1 if p1's eval is greater than p2's
		 * 	Returns 1 if p1's eval is less than p2's
		 * 	Returns 0 if p1 and p2 have the same eval
		*/
		public int compare(int[] p1, int[] p2) {
			if (p1[0] > p2[0]) {
				return -1;
			}
			else if (p1[0] < p2[0]) {
				return 1;
			}
			else if ((p1[0] == p2[0]) && ((p1[1] > p2[1]))) {
				return -1;
			}
			else if ((p1[0] == p2[0]) && ((p1[1] < p2[1]))) {
				return 1;
			}
			else {
				return 0;
			}
		}		
	}
	
}
