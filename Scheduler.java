/*
 * Class Scheduler.
 */ 
 import java.util.Vector;
 

public class Scheduler {
	Vector<Class> labsAndCourses;
	Vector<Parser.PairedCourseClass> notCompatible;
	Vector<Parser.PairedCourseClass> pairs;	//hold pairs; see around line 340
	Vector<Parser.Preference> preferences;
	Vector<Parser.ParserClass> unwanted;
	Vector<Parser.ParserClass>partassign;
	String filename = "bob.txt";
	
	Slot[] M;
	Slot[] T;	//will have to put labs in Slot, possible in two Slot
	Slot[] F; 
	
	public Scheduler() {

	}
	public void start(String[] args) throws SchedulerException{
		//Now using CommandParser, so follow format of arguments in that class
		//and pass arguments through Scheduler.
		CommandParser cp = new CommandParser(args);
		Parser parser = new Parser(cp.getFilename());
		//Parser parser = new Parser(filename);
		parser.parse();
		labsAndCourses = parser.getLabsAndCourses();
		notCompatible = parser.getNC();
		pairs = parser.getPairs();
		preferences = parser.getPreferences();
		unwanted = parser.getUnwanted();
		partassign = parser.getPartassign();
		M = parser.getMO();
		T = parser.getTU();
		F = parser.getFR();
		printCommands(cp);
		printData();
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
			System.out.println("Labs and courses");
			for (Object c:labsAndCourses.toArray()) {
				Class d = (Class) c;
				System.out.println(d.toString());
			}
			//TODO: Problem with pairs: does not print out full data for labs/tutorials.
			System.out.println("NC");
			for (Object c:notCompatible.toArray()) {
				Parser.PairedCourseClass d = (Parser.PairedCourseClass) c;
				System.out.println(d.toString());
			}
			//TODO: Problem with pairs: does not print out full data for labs/tutorials
			System.out.println("pairs");
			for (Object c:pairs.toArray()) {
				Parser.PairedCourseClass d = (Parser.PairedCourseClass) c;
				System.out.println(d.toString());
			}
			System.out.println("pref");
			for (Object c:preferences.toArray()) {
				Parser.Preference d = (Parser.Preference) c;
				System.out.println(d.toString());
			}
			System.out.println("unwanted");
			for (Object c:unwanted.toArray()) {
				Parser.ParserClass d = (Parser.ParserClass) c;
				System.out.println(d.toString());
			}
			System.out.println("partassign");
			for (Object c:partassign.toArray()) {
				Parser.ParserClass d = (Parser.ParserClass) c;
				System.out.println(d.toString());
			}
			System.out.println("Monday coursemin - coursemax - labmin - labmax:");
			for (int i=0; i<M.length;i++) {
				System.out.println(M[i].getTime() +" - " + M[i].getCoursemin()
						+" - " + M[i].getCoursemax()  +  " - " + M[i].getLabmin()+ " - " + M[i].getLabmax());
			}
			System.out.println("Tuesday coursemin:");
			for (int i=0; i<T.length;i++) {
				System.out.println(T[i].getTime() +" - " + T[i].getCoursemin() 
						+ " - "+ T[i].getCoursemax()  +" - " + T[i].getLabmin() + " - " + T[i].getLabmax());
			}
			System.out.println("Friday coursemin:");
			for (int i=0; i<F.length;i++) {
				System.out.println(F[i].getTime() +" - " + F[i].getCoursemin()
						+ " - " +F[i].getCoursemax()  + " - " + F[i].getLabmin()  + " - " + F[i].getLabmax());
			}
		}
	public static void main(String[] args) throws SchedulerException{
		Scheduler sch = new Scheduler();
		sch.start(args);
	}
}
