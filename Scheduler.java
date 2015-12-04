/**
 * Class Scheduler where everything will occur.
 * @author CPSC 433 Toshibe
 */
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
 

public class Scheduler {
	// Scheduler Variables
	private CommandParser cp;
	
	// Parser Variables
	private String name;
	private ArrayList<Class> labsAndCourses;
	private ArrayList<Parser.PairedCourseClass> notCompatible;
	private ArrayList<Parser.PairedCourseClass> pairs;	//hold pairs; see around line 340
	private ArrayList<Parser.Preference> preferences;
	private ArrayList<Parser.ParserClass> unwanted;
	private ArrayList<Parser.ParserClass> partassign;
	public static int totalCourses = 0;
	private String[] indexArray;
	private Constraint[] constr;
	
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
		intializeConstraints();
		heapIntializer(parser.getInitialProblem());
		makeSchedule();
		
//		printCommands(cp);
//		printData();
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
		constr = parser.getHardConstraints();
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
	private void intializeConstraints() {
		// TODO
	}
	
	private void makeSchedule() throws SchedulerException{
		
		if (pq.size() == 0) {
			throw new SchedulerException("No starting problem."); //Can't make the scheduler with nothing in the queue
		}
		int k = 0;
		OutputSchedule out1 = new OutputSchedule(indexArray, pq.peek());
		out1.output();
		SearchModel searchModel = new SearchModel(indexArray, cp, prepSlotArrayForSearchModel(), constr);
		int[][] newProblems;
		//System.out.println("Div");
		boolean foundBest = false;
		while (!pq.isEmpty() && !foundBest) {
			newProblems = searchModel.div(pq.poll());
			if(newProblems != null) {
				for(int[] pr: newProblems) {
					/* if the current problem has a depth greater than the length
					 * of the array it is done, if it also has an eval greater than
					 * the best it is the new best
					 */
					 System.out.println("pr is " + pr[0]);
					if (( pr[0] >= PROBLEM_LENGTH) &&   (bestSolution[1] > pr[1])){
						bestSolution = pr;	
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
	
		//prints the data.  Can delete, it's just for testing purposes and is
		//super ugly.
/*	public void printCommands(CommandParser cp) {
		System.out.println("File: " + cp.getFilename());
		System.out.println("minfilled: " + cp.getMinfilled());
		System.out.println("Pair: " + cp.getPair());
		System.out.println("Pref: " + cp.getPref());
		System.out.println("Secdiff: " + cp.getSecdiff());
	}
	public void printData() {
		
		System.out.println("Name:");
		System.out.println(name);
		System.out.println("Labs and courses");
		for (Object c:labsAndCourses.toArray()) {
			Class d = (Class) c;
			System.out.println(d);
		}
		//TODO: Problem with pairs: does not print out full data for labs/tutorials.
		System.out.println("NC");
		for (Object c:notCompatible.toArray()) {
			Parser.PairedCourseClass d = (Parser.PairedCourseClass) c;
			System.out.println(d);
		}
		//TODO: Problem with pairs: does not print out full data for labs/tutorials
		System.out.println("pairs");
		for (Object c:pairs.toArray()) {
			Parser.PairedCourseClass d = (Parser.PairedCourseClass) c;
			System.out.println(d);
		}
		System.out.println("pref");
		for (Object c:preferences.toArray()) {
			Parser.Preference d = (Parser.Preference) c;
			System.out.println(d);
		}
		System.out.println("unwanted");
		for (Object c:unwanted.toArray()) {
			Parser.ParserClass d = (Parser.ParserClass) c;
			System.out.println(d);
		}
		System.out.println("partassign");
		for (Object c:partassign.toArray()) {
			Parser.ParserClass d = (Parser.ParserClass) c;
			System.out.println(d);
		}
		System.out.println("Monday coursemin - coursemax - labmin - labmax:");
		for (int i=0; i<M.length;i++) {
			System.out.println(M[i].getTime() +" - " + M[i].getCoursemin()
					+" - " + M[i].getCoursemax()  +  " - " + M[i].getLabmin()+ " - " + M[i].getLabmax());
		}
		System.out.println("Tuesday Courses coursemin - coursemax:");
		for (int i=0; i<TCourses.length;i++) {
			System.out.println(TCourses[i].getTime() +" - " + TCourses[i].getCoursemin() 
					+ " - "+ TCourses[i].getCoursemax());
		}
		System.out.println("Tuesday Labs labmin - labmax:");
		for (int i=0; i<TLabs.length;i++) {
			System.out.println(TLabs[i].getTime()  +" - " + TLabs[i].getLabmin() + " - " + TLabs[i].getLabmax());
		}
		System.out.println("Friday coursemin:");
		for (int i=0; i<FLabs.length;i++) {
			System.out.println(FLabs[i].getTime() +" - " + FLabs[i].getCoursemin()
					+ " - " +FLabs[i].getCoursemax()  + " - " + FLabs[i].getLabmin()  + " - " + FLabs[i].getLabmax());
		}
	}*/
	public static void main(String[] args) throws SchedulerException{
		Scheduler sch = new Scheduler();
		sch.start(args);
	}
	
	public class ScheduleComparator implements Comparator<int[]> {
		/*	Returns 1 if p1's depth value is greater than p2's.  
		 * 	Returns -1 if p1's depth value is less than p2's.
		 * 	
		 * 	If p1 and p2 have equal Depth values then it will compare their eval values
		 * 	Returns 1 if p1's eval is greater than p2's
		 * 	Returns -1 if p1's eval is less than p2's
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
