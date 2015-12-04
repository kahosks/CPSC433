
/**
 * Class Scheduler where everything will occur.
 * @author CPSC 433 Toshibe
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;


public class Scheduler {
	// Scheduler Variables
	private CommandParser cp;
	
	// Parser Variables
	private String name;
	private ArrayList<Class> labsAndCourses;
	private ArrayList<PairedCourseClass> notCompatible;
	private ArrayList<PairedCourseClass> pairs;	//hold pairs; see around line 340
	private ArrayList<Preference> preferences;
	private ArrayList<ParserClass> unwanted;
	private ArrayList<ParserClass> partassign;
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
	 * TODO: Perhaps pass args through constructor, and make start()
	 *  have no arguments.
	 */
	public Scheduler() {
		
	}
	/**
	 * Method that, when called, will run through the whole code.
	 * TODO: Lots of things to be added.
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
		
	}
	/**
	 * Method that gets the parsed values from the parser and 
	 * stores them in their respected ArrayList.
	 * @param parser	Parser that parses the input file.
	 */
	private void initiateParsedValues(Parser parser) {
		name = parser.getName();
		labsAndCourses = parser.getLabsAndCourses();
		notCompatible = parser.getNC();
		pairs = parser.getPairs();
		preferences = parser.getPreferences();
		unwanted = parser.getUnwanted();
		partassign = parser.getPartassign();
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
		// TODO initialize soft constraints
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
		
		boolean foundBest = false;
		//OutputSchedule out314 = new OutputSchedule(indexArray, bestSolution);
		while (!pq.isEmpty() && !foundBest) {
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
					else if ((PROBLEM_LENGTH > pr[0])){			
						pq.add(pr);
					}
						
				}
				
			}
			
		}
		OutputSchedule out = new OutputSchedule(indexArray, bestSolution);
		out.output();
		
	}
	
	private Object[] prepSlotArrayForSearchModel() {
		Slot[] temp = new Slot[M.length + TCourses.length];
		Object[] oArray = new Object[2];
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
		
		return oArray;
	}
	
	private Slot[] prepSlotsForSoftContraints(){
	
		Slot[] ret = new Slot[M.length + TCourses.length +
		 MLabs.length + TLabs.length + FLabs.length];
		int i = 0;
		
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
