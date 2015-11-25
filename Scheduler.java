/**
 * Class Scheduler where everything will occur.
 * @author CPSC 433 Toshibe
 */
 import java.util.ArrayList;
 

public class Scheduler {
	private String name;
	private ArrayList<Class> labsAndCourses;
	private ArrayList<Parser.PairedCourseClass> notCompatible;
	private ArrayList<Parser.PairedCourseClass> pairs;	//hold pairs; see around line 340
	private ArrayList<Parser.Preference> preferences;
	private ArrayList<Parser.ParserClass> unwanted;
	private ArrayList<Parser.ParserClass>partassign;
	//private String filename = "bob.txt";
	public static int totalCourses =0 ;
	
	private Slot[] M;
	private Slot[] TCourses;	//will have to put labs in Slot, possible in two Slot
	private Slot[] TLabs;
	private Slot[] FLabs; 
	
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
		CommandParser cp = new CommandParser(args);
		Parser parser = new Parser(cp.getFilename());
		parser.parse();
		// TODO: put from lines 44-55 into a method so start() method doesn't look
		//longer and uglier than it has to be.
		initiateParsedValues(parser);
		printCommands(cp);
		printData();
	}
	/**
	 * Method that gets the parsed values from the parser and 
	 * stores them in their respected ArrayList.
	 * @param parser	Parser that parses the input file.
	 */
	public void initiateParsedValues(Parser parser) {
		name = parser.getName();
		labsAndCourses = parser.getLabsAndCourses();
		notCompatible = parser.getNC();
		pairs = parser.getPairs();
		preferences = parser.getPreferences();
		unwanted = parser.getUnwanted();
		partassign = parser.getPartassign();
		M = parser.getMO();
		TCourses = parser.getTCourses();
		TLabs = parser.getTLabs();
		FLabs = parser.getFLabs();
		totalCourses = labsAndCourses.size();
	}
	//prints the data.  Can delete, it's just for testing purposes and is
		//super ugly.
	public void printCommands(CommandParser cp) {
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
		}
	public static void main(String[] args) throws SchedulerException{
		Scheduler sch = new Scheduler();
		sch.start(args);
	}
}
